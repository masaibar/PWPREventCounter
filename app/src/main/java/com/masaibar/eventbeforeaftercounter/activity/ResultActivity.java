package com.masaibar.eventbeforeaftercounter.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.masaibar.eventbeforeaftercounter.HighSchool;
import com.masaibar.eventbeforeaftercounter.InputData;
import com.masaibar.eventbeforeaftercounter.R;
import com.masaibar.eventbeforeaftercounter.util.DebugUtil;

public class ResultActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpAd();

        InputData inputData = InputData.read(getApplicationContext());
        if (inputData == null) {
            DebugUtil.log("InputData is null");
            return;
        }

        setResultTexts(inputData);

        String result = String.format("%s %s %s %s %s %s %s", inputData.getHighSchool(),
                inputData.getEvemtCharacter(0), inputData.getEvemtCharacter(1),
                inputData.getEvemtCharacter(2), inputData.getEvemtCharacter(3),
                inputData.getEvemtCharacter(4), inputData.getEvemtCharacter(5));

        DebugUtil.log("result ===========");
        DebugUtil.log(result);

        TextView textView = (TextView) findViewById(R.id.result);
        textView.setText(result);
    }

    private void setResultTexts(InputData inputData) {
        HighSchool hs = inputData.getHighSchool();
        //◯◯高校空きイベント数
        ((TextView) findViewById(R.id.text_high_school_header))
                .setText(getString(R.string.high_school_header, hs.getName()));

        //前◯/後◯
        ((TextView) findViewById(R.id.text_highschool_events))
                .setText(getString(R.string.high_school_events, hs.getBeforeEvents(), hs.getAfterEvents()));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpAd() {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
