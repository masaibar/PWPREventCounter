package com.masaibar.eventbeforeaftercounter.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.masaibar.eventbeforeaftercounter.InputData;
import com.masaibar.eventbeforeaftercounter.R;
import com.masaibar.eventbeforeaftercounter.util.DebugUtil;

public class ResultActivity extends AppCompatActivity {

    private static final String INPUT_DATA = "inputData";

    public static void start(Context context, InputData inputData) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(INPUT_DATA, inputData);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent == null) {
            DebugUtil.log("Intent is null");
            return;
        }

        InputData inputData = (InputData) getIntent().getSerializableExtra(INPUT_DATA);
        if (inputData == null) {
            DebugUtil.log("InputData is null");
            return;
        }

        String result = String.format("%s %s %s %s %s %s %s", inputData.getHighSchool(),
                inputData.getEvemtCharacter(0), inputData.getEvemtCharacter(1),
                inputData.getEvemtCharacter(2), inputData.getEvemtCharacter(3),
                inputData.getEvemtCharacter(4), inputData.getEvemtCharacter(5));

        DebugUtil.log("result ===========");
        DebugUtil.log(result);

        TextView textView = (TextView) findViewById(R.id.result);
        textView.setText(result);
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
}
