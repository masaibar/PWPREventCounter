package com.masaibar.eventbeforeaftercounter.activity;

import android.app.Activity;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.masaibar.eventbeforeaftercounter.EventCharacter;
import com.masaibar.eventbeforeaftercounter.HighSchool;
import com.masaibar.eventbeforeaftercounter.JSONData;
import com.masaibar.eventbeforeaftercounter.R;
import com.masaibar.eventbeforeaftercounter.util.DebugUtil;
import com.masaibar.eventbeforeaftercounter.util.EventCharacterAdapter;
import com.masaibar.eventbeforeaftercounter.util.HighSchoolAdapter;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String JSON_URL =
            "https://raw.githubusercontent.com/masaibar/PWPREventCounter/master/json/v0/sample.json";

    @Override
    public void onClick(View v) {
        Context context = getApplicationContext();
        HighSchool highSchool = (HighSchool) getSelectedItem(R.id.spinner_high_school);

        EventCharacter character1 = (EventCharacter) getSelectedItem(R.id.spinner_character1);
        EventCharacter character2 = (EventCharacter) getSelectedItem(R.id.spinner_character2);
        EventCharacter character3 = (EventCharacter) getSelectedItem(R.id.spinner_character3);
        EventCharacter character4 = (EventCharacter) getSelectedItem(R.id.spinner_character4);
        EventCharacter character5 = (EventCharacter) getSelectedItem(R.id.spinner_character5);
        EventCharacter character6 = (EventCharacter) getSelectedItem(R.id.spinner_character6);

        switch (v.getId()) {
            case R.id.button_open_wiki_1:
                character1.openWiki(context);
                break;

            case R.id.button_open_wiki_2:
                character2.openWiki(context);
                break;

            case R.id.button_open_wiki_3:
                character3.openWiki(context);
                break;

            case R.id.button_open_wiki_4:
                character4.openWiki(context);
                break;

            case R.id.button_open_wiki_5:
                character5.openWiki(context);
                break;

            case R.id.button_open_wiki_6:
                character6.openWiki(context);
                break;

            case R.id.button_judge:
                if (highSchool != null) {
                    if (hasDuplicatedCharacters()) {
                        //TODO strings.xml
                        Toast.makeText(MainActivity.this, "duplicated", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    //結果暫定表示
                    final TextView textResult = (TextView) findViewById(R.id.text_result);
                    textResult.setText(getString(R.string.result_tmp, highSchool.getName(), highSchool.getBeforeEvents(), highSchool.getAfterEvents()));

                    ResultActivity.start(getApplicationContext());
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpAd();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        new GetJSONAsyncTask(JSON_URL, this).execute();

        findViewById(R.id.button_judge).setOnClickListener(this);
        findViewById(R.id.button_open_wiki_1).setOnClickListener(this);
        findViewById(R.id.button_open_wiki_2).setOnClickListener(this);
        findViewById(R.id.button_open_wiki_3).setOnClickListener(this);
        findViewById(R.id.button_open_wiki_4).setOnClickListener(this);
        findViewById(R.id.button_open_wiki_5).setOnClickListener(this);
        findViewById(R.id.button_open_wiki_6).setOnClickListener(this);
    }

    private void setUpAd() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    /**
     * 渡したidのSpinnerで選択されているObjectを返す、呼び出し元でキャストして使う
     */
    private Object getSelectedItem(int id) {
        return ((Spinner) findViewById(id)).getSelectedItem();
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

    private boolean hasDuplicatedCharacters() {

//        return true;
        return false;
    }

    private class GetJSONAsyncTask extends AsyncTask<Void, Void, String> {

        private String mUrl;
        private Spinner mSpinnerHighSchool;
        private Spinner mSpinnerCharacter1;
        private Spinner mSpinnerCharacter2;
        private Spinner mSpinnerCharacter3;
        private Spinner mSpinnerCharacter4;
        private Spinner mSpinnerCharacter5;
        private Spinner mSpinnerCharacter6;

        public GetJSONAsyncTask(String url, Activity activity) {
            mUrl = url;
            mSpinnerHighSchool = (Spinner) activity.findViewById(R.id.spinner_high_school);
            mSpinnerCharacter1 = (Spinner) activity.findViewById(R.id.spinner_character1);
            mSpinnerCharacter2 = (Spinner) activity.findViewById(R.id.spinner_character2);
            mSpinnerCharacter3 = (Spinner) activity.findViewById(R.id.spinner_character3);
            mSpinnerCharacter4 = (Spinner) activity.findViewById(R.id.spinner_character4);
            mSpinnerCharacter5 = (Spinner) activity.findViewById(R.id.spinner_character5);
            mSpinnerCharacter6 = (Spinner) activity.findViewById(R.id.spinner_character6);
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

            Gson gson = new Gson();
            JSONData jsonData = gson.fromJson(jsonString, JSONData.class);

            //高校
            setHighSchoolSpinner(mSpinnerHighSchool, jsonData.getHighSchools());

            //イベントキャラクター
            List<EventCharacter> eventCharacters = jsonData.getEventCharacters();
            setEventCharacterSpinner(mSpinnerCharacter1, eventCharacters);
            setEventCharacterSpinner(mSpinnerCharacter2, eventCharacters);
            setEventCharacterSpinner(mSpinnerCharacter3, eventCharacters);
            setEventCharacterSpinner(mSpinnerCharacter4, eventCharacters);
            setEventCharacterSpinner(mSpinnerCharacter5, eventCharacters);
            setEventCharacterSpinner(mSpinnerCharacter6, eventCharacters);
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
