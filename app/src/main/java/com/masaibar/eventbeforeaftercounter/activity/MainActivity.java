package com.masaibar.eventbeforeaftercounter.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.masaibar.eventbeforeaftercounter.EventCharacter;
import com.masaibar.eventbeforeaftercounter.HighSchool;
import com.masaibar.eventbeforeaftercounter.InputData;
import com.masaibar.eventbeforeaftercounter.JSONData;
import com.masaibar.eventbeforeaftercounter.R;
import com.masaibar.eventbeforeaftercounter.util.DebugUtil;
import com.masaibar.eventbeforeaftercounter.EventCharacterAdapter;
import com.masaibar.eventbeforeaftercounter.HighSchoolAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener {

    private static final String JSON_URL =
            "https://raw.githubusercontent.com/masaibar/PWPREventCounter/master/json/v0/sample.json";
    private static final int ID_SPINNER_HIGH_SCHOOL = R.id.spinner_high_school;
    private static final int[] ID_SPINNER_EVENT_CHARACTERS = {
            R.id.spinner_character1, R.id.spinner_character2, R.id.spinner_character3,
            R.id.spinner_character4, R.id.spinner_character5, R.id.spinner_character6};

    private Map<Integer, Object> mSpinnerMap = new HashMap<>();
    private Toast mToast;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int parentId = parent.getId();
        mSpinnerMap.put(parentId, getSelectedItem(parentId));
    }

    private Object getSelectedItem(int spinnerId) {
        return ((Spinner) findViewById(spinnerId)).getSelectedItem();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                onNavigationSelected(menuItem);
                return true;
            }
        });

        setUpAd();
        setUpSpinners();

        findViewById(R.id.button_judge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HighSchool highSchool = (HighSchool) mSpinnerMap.get(ID_SPINNER_HIGH_SCHOOL);
                if (highSchool != null) {
                    if (hasDuplicatedCharacters()) {
                        //TODO strings.xml
                        Toast.makeText(MainActivity.this, "duplicated", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //結果暫定表示
                    final TextView textResult = (TextView) findViewById(R.id.text_result);
                    textResult.setText(getString(R.string.result_tmp, highSchool.getName(), highSchool.getBeforeEvents(), highSchool.getAfterEvents()));

                    getInputData().save(getApplicationContext());
                    ResultActivity.start(getApplicationContext());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateDrawerControls();
    }

    private void updateDrawerControls() {
        MenuItem resultMenuItem =
                ((NavigationView) findViewById(R.id.nav_view)).getMenu().findItem(R.id.nav_result);

        resultMenuItem.setEnabled(InputData.hasData(getApplicationContext()));
    }

    private void setUpAd() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    /**
     * スピナーにリスナーと要素(非同期で取得)を設定する
     */
    private void setUpSpinners() {
        Spinner highSchoolSpinner = (Spinner) findViewById(ID_SPINNER_HIGH_SCHOOL);
        highSchoolSpinner.setOnItemSelectedListener(this);

        for (int spinnerId : ID_SPINNER_EVENT_CHARACTERS) {
            ((Spinner) findViewById(spinnerId)).setOnItemSelectedListener(this);
        }

        new GetJSONAsyncTask(
                JSON_URL,
                highSchoolSpinner,
                getEventCharacterSpinnerList()
        ).execute();
    }

    private List<Spinner> getEventCharacterSpinnerList() {
        List<Spinner> eventCharacterSpinnerList = new ArrayList<>();
        for (int id : ID_SPINNER_EVENT_CHARACTERS) {
            eventCharacterSpinnerList.add((Spinner) findViewById(id));
        }
        return eventCharacterSpinnerList;
    }


    /**
     * 重複しているイベキャラがいたらtrueを返す
     *
     * @return true/false
     */
    private boolean hasDuplicatedCharacters() {
        for (Map.Entry<Integer, Object> entryI : mSpinnerMap.entrySet()) {
            if (entryI.getKey() == ID_SPINNER_HIGH_SCHOOL) {
                continue;
            }
            for (Map.Entry<Integer, Object> entryJ : mSpinnerMap.entrySet()) {
                if (entryJ.getKey() == ID_SPINNER_HIGH_SCHOOL ||
                        entryI.getKey() == entryJ.getKey()) {
                    continue;
                }
                if (entryI.getValue().equals(entryJ.getValue())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 現在の入力内容を元にInputDataオブジェクトを返す
     */
    private InputData getInputData() {
        HighSchool highSchool = (HighSchool) mSpinnerMap.get(ID_SPINNER_HIGH_SCHOOL);
        List<EventCharacter> eventCharacterList = getEventCharacterList();
        return new InputData(highSchool, eventCharacterList);
    }

    /**
     * todo getInputdata無くせば不要になる
     */
    private List<EventCharacter> getEventCharacterList() {
        List<EventCharacter> eventCharacterList = new ArrayList<>();
        for (int spinnerId : ID_SPINNER_EVENT_CHARACTERS) {
            eventCharacterList.add(
                    (EventCharacter) ((Spinner) findViewById(spinnerId)).getSelectedItem()
            );
        }
        return eventCharacterList;
    }

    private void setHighSchoolSpinner(Spinner spinner, List<HighSchool> highSchools) {
        HighSchoolAdapter adapter = new HighSchoolAdapter(this, android.R.layout.simple_spinner_item, highSchools);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void setEventCharacterSpinner(Spinner spinner, List<EventCharacter> eventCharacters) {
        EventCharacterAdapter adapter = new EventCharacterAdapter(this, android.R.layout.simple_spinner_item, eventCharacters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    /**
     * 非同期でJSONを取得し、Spinnerに設定するAsyncTask
     */
    private class GetJSONAsyncTask extends AsyncTask<Void, Void, String> {

        private String mUrl;
        private Spinner mSpinnerHighSchool;
        private List<Spinner> mSpinnerEventCharacterList;

        public GetJSONAsyncTask(
                String url,
                Spinner spinnerHighSchool,
                List<Spinner> spinnerEventCharacterList) {
            mUrl = url;
            mSpinnerHighSchool = spinnerHighSchool;
            mSpinnerEventCharacterList = spinnerEventCharacterList;
        }

        @Override
        protected String doInBackground(Void... params) {

            String result = null;

            Request request = new Request.Builder()
                    .url(mUrl)
                    .get()
                    .build();

            OkHttpClient client = new OkHttpClient();

            try {
                Response response = client.newCall(request).execute();
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            super.onPostExecute(jsonString);

            DebugUtil.log("jsonString = %s", jsonString);

            JSONData jsonData = new Gson().fromJson(jsonString, JSONData.class);

            //高校
            setHighSchoolSpinner(mSpinnerHighSchool, jsonData.getHighSchools());

            //イベントキャラクター
            List<EventCharacter> eventCharacters = jsonData.getEventCharacters();

            for (Spinner spinner : mSpinnerEventCharacterList) {
                setEventCharacterSpinner(spinner, eventCharacters);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (mToast == null || !mToast.getView().isShown()) {
                mToast = Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.toast_back_pressed),
                        Toast.LENGTH_SHORT
                );
                mToast.show();
                return;
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onNavigationSelected(MenuItem menuItem) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        Context context = getApplicationContext();
        switch (menuItem.getItemId()) {
            case R.id.nav_result:
                ResultActivity.start(context);
                break;

            case R.id.nav_copyright:
                CopyrightActivity.start(context);
                break;

            default:
                break;
        }
    }
}
