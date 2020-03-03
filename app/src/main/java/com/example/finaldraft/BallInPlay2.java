package com.example.finaldraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BallInPlay2 extends StartingWindow {
    // constant
    public static final String RESULT_KEY_MESSAGE_BALLINPLAY2 = "com.example.a12fixedfirstdraft.BallInPlay2 - Return Message";
    DisplayMetrics displayMetrics;
    // instance variables
    Button btnBack;
    TextView lblBallInPlay2;
    Button btnOut;
    Button btnSingle;
    Button btnDouble;
    Button btnTriple;
    Button btnInParkHomeRun;
    Button btnHomeRun;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ballinplay2_layout);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("Title");

        // changing dimensions of pop up window
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*0.8),(int)(height*0.8));

        //views
        lblBallInPlay2 = (TextView) findViewById(R.id.lblBallInPlay2);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnOut = (Button) findViewById(R.id.btnSub);
        btnSingle = (Button) findViewById(R.id.btnSingle);
        btnDouble = (Button) findViewById(R.id.btnDouble);
        btnTriple = (Button) findViewById(R.id.btnStealOut);
        btnInParkHomeRun = (Button) findViewById(R.id.btnInParkHomeRun);
        btnHomeRun = (Button) findViewById(R.id.btnHomeRun);

        lblBallInPlay2.setText(title);

        // event listeners
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),BallInPlay.class));
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("Out");

            }
        });

        btnSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("Single");

            }
        });

        btnDouble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("Double");

            }
        });

        btnTriple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("Triple");

            }
        });

        btnInParkHomeRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { returnResult("In-The-Park Home Run");

            }
        });

        btnHomeRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult("Home Run");
            }
        });
    }

    public void returnResult(String result){
        // returning input of user
        String[] message = new String[3];
        message[1] = title;
        message[0] = result;
        Intent i = new Intent();
        i.putExtra(RESULT_KEY_MESSAGE_BALLINPLAY2,message);
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    public static String[] getResultKeyMessage(Intent intent){
        return intent.getStringArrayExtra(RESULT_KEY_MESSAGE_BALLINPLAY2);
    }
}
