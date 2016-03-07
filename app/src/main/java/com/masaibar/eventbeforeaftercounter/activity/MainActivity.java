package com.masaibar.eventbeforeaftercounter.activity;

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
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener {

    private static final String JSON_URL =
            "https://raw.githubusercontent.com/masaibar/PWPREventCounter/master/json/v0/sample.json";

    private HighSchool mHighSchool;
    private List<EventCharacter> mEventCharacterList = new ArrayList<>();

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int spinnerId = parent.getId();
        switch (spinnerId) {
            case R.id.spinner_high_school:
                mHighSchool = getSelectedHighSchool(spinnerId);
                break;

            case R.id.spinner_character1:
                mEventCharacterList.set(0, getSelectedEventCharacter(spinnerId));
                ((TextView) findViewById(R.id.text_role_1)).setText(mEventCharacterList.get(0).getRole());
                break;

            case R.id.spinner_character2:
                mEventCharacterList.set(1, getSelectedEventCharacter(spinnerId));
                ((TextView) findViewById(R.id.text_role_2)).setText(mEventCharacterList.get(1).getRole());
                break;

            case R.id.spinner_character3:
                mEventCharacterList.set(2, getSelectedEventCharacter(spinnerId));
                ((TextView) findViewById(R.id.text_role_3)).setText(mEventCharacterList.get(2).getRole());
                break;

            case R.id.spinner_character4:
                mEventCharacterList.set(3, getSelectedEventCharacter(spinnerId));
                ((TextView) findViewById(R.id.text_role_4)).setText(mEventCharacterList.get(3).getRole());
                break;

            case R.id.spinner_character5:
                mEventCharacterList.set(4, getSelectedEventCharacter(spinnerId));
                ((TextView) findViewById(R.id.text_role_5)).setText(mEventCharacterList.get(4).getRole());
                break;

            case R.id.spinner_character6:
                mEventCharacterList.set(5, getSelectedEventCharacter(spinnerId));
                ((TextView) findViewById(R.id.text_role_6)).setText(mEventCharacterList.get(5).getRole());
                break;

            default:
                break;
        }
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
        navigationView.setNavigationItemSelectedListener(this);

        setUpAd();

        Spinner highSchoolSpinner = (Spinner) findViewById(R.id.spinner_high_school);
        highSchoolSpinner.setOnItemSelectedListener(this);

        List<Spinner> eventCharacterSpinners = getEventCharacterSpinners();
        for (Spinner spinner : eventCharacterSpinners) {
            spinner.setOnItemSelectedListener(this);
            mEventCharacterList.add(null);
        }

        new GetJSONAsyncTask(
                JSON_URL,
                highSchoolSpinner,
                eventCharacterSpinners
        ).execute();

        findViewById(R.id.button_judge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHighSchool != null) {
                    if (hasDuplicatedCharacters()) {
                        //TODO strings.xml
                        Toast.makeText(MainActivity.this, "duplicated", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //結果暫定表示
                    final TextView textResult = (TextView) findViewById(R.id.text_result);
                    textResult.setText(getString(R.string.result_tmp, mHighSchool.getName(), mHighSchool.getBeforeEvents(), mHighSchool.getAfterEvents()));

                    ResultActivity.start(getApplicationContext(), getInputData());
                }
            }
        });
    }

    private List<Spinner> getEventCharacterSpinners() {
        return new ArrayList<Spinner>() {
            {
                add((Spinner) findViewById(R.id.spinner_character1));
                add((Spinner) findViewById(R.id.spinner_character2));
                add((Spinner) findViewById(R.id.spinner_character3));
                add((Spinner) findViewById(R.id.spinner_character4));
                add((Spinner) findViewById(R.id.spinner_character5));
                add((Spinner) findViewById(R.id.spinner_character6));
            }};
    }

    private void setUpAd() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    /**
     * 渡したidのSpinnerで選択されているEventCharacterを返す
     */
    private EventCharacter getSelectedEventCharacter(int id) {
        return (EventCharacter) getSelectedItem(id);
    }

    /**
     * 渡したidのSpinnerで選択されているHighSchoolを返す
     */
    private HighSchool getSelectedHighSchool(int id) {
        return (HighSchool) getSelectedItem(id);
    }

    private Object getSelectedItem(int id) {
        return ((Spinner) findViewById(id)).getSelectedItem();
    }

    /**
     * 重複しているイベキャラがいたらtrueを返す
     *
     * @return true/false
     */
    private boolean hasDuplicatedCharacters() {
        for (int i = 0; i < mEventCharacterList.size(); i++) {
            for (int j = 0; j < mEventCharacterList.size(); j++) {
                if (i == j) {
                    continue;
                }
                if (mEventCharacterList.get(i).equals(mEventCharacterList.get(j))) {
                    return true;
                }
            }
        }

        return false;
    }

    private InputData getInputData() {
        return new InputData(mHighSchool, mEventCharacterList);
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

            for (Spinner spinner: mSpinnerEventCharacterList) {
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
