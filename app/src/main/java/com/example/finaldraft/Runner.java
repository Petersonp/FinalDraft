package com.example.finaldraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Runner extends StartingWindow {
    public static final String RESULT_KEY_MESSAGE = "com.example.finaldraft.Runner - Return Message";
    public static final int REQUEST_CODE_SAFEOUT = 72;
    DisplayMetrics displayMetrics;

    String[] runner;

    Button btnSteal;
    Button btnOut;
    Button btnBack;

    TextView lblRunner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runner_layout);

        displayMetrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*0.90),(int)(height*0.90));

        Bundle bundle = getIntent().getExtras();

        runner = bundle.getStringArray("Runner");

        btnSteal = (Button) findViewById(R.id.btnSteal);
        btnOut = (Button) findViewById(R.id.btnOut);
        btnBack = (Button) findViewById(R.id.btnBack);
        lblRunner = (TextView) findViewById(R.id.lblRunner);
        lblRunner.setText(runner[0]+" "+runner[1]+" #"+runner[2]+" @"+getBase(runner[3]));

        btnSteal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),SafeOut.class);
                startActivityForResult(i, REQUEST_CODE_SAFEOUT);

            }
        });

        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("Out");
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
            }
        });
    }
    protected String getBase(String base){
        switch (base){
            case ("1"):
                return "FB";
            case ("2"):
                return "SB";
            case ("3"):
                return "TB";
        }
        return "lol";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_SAFEOUT:
                if(resultCode == Activity.RESULT_OK){
                    String message = SafeOut.getResultKeyMessage(data);

                    //System.out.println(message+" Messege at MainActivity --------------------");
                    returnResult(message);

                }
        }
    }

    protected void returnResult(String result){
        String[] message = new String[2];
        message[0] = result;
        message[1] = runner[3];
        Intent intent = new Intent();
        intent.putExtra(RESULT_KEY_MESSAGE,message);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    public static String[] getResultKeyMessage(Intent intent){
        return intent.getStringArrayExtra(RESULT_KEY_MESSAGE);
    }
}
