package com.masaibar.eventbeforeaftercounter;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.masaibar.eventbeforeaftercounter.util.DebugUtil;
import com.masaibar.eventbeforeaftercounter.util.StringUtil;

import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String JSON_URL =
            "https://raw.githubusercontent.com/masaibar/PWPREventCounter/master/json/v0/sample.json";

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

        new GetJSONAsyncTask(JSON_URL, this).execute();

        //データ埋めるのは多分非同期だね
        setHighSchoolSpinner();
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

    private void setHighSchoolSpinner() {
        Spinner spinnerHighSchool = (Spinner) findViewById(R.id.spinner_high_school);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item);
        adapter.add("hoge");
        adapter.add("fuga");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHighSchool.setAdapter(adapter);
    }

    private class GetJSONAsyncTask extends AsyncTask<Void, Void, String> {

        private String mUrl;
        private Activity mActivity; //画面更新不要ならいらないかも

        public GetJSONAsyncTask(String url, Activity activity) {
            mUrl = url;
            mActivity = activity;
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpsURLConnection connection;
            String result = "";
            try {
                URL url = new URL(mUrl);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();

                int response = connection.getResponseCode();

                result = StringUtil.fromInputeStream(connection.getInputStream());

                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            DebugUtil.log("result = %s", result);

            Gson gson = new Gson();
            JSONData jsonData = gson.fromJson(result, JSONData.class);

            List<HighSchool> highSchools = jsonData.getHighSchools();

            for(HighSchool highSchool : highSchools) {
                DebugUtil.log("highschool = %s, isLimited = %s, b = %s, a = %s",
                        highSchool.getName(), highSchool.isLimited(), highSchool.getBeforeEvents(), highSchool.getAfterEvents());
            }
        }
    }
}
