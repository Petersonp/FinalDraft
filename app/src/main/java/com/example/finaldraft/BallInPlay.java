package com.example.finaldraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class BallInPlay extends StartingWindow {
    public static final int REQUEST_CODE_GETMESSAGE_BALLINPLAY2 = 2;
    public static final String RESULT_KEY_MESSAGE = "com.example.a12fixedfirstdraft.BallInPlay - Return Message";
    DisplayMetrics displayMetrics;
    Button btnBack;
    Button btnGroundBall;
    Button btnBunt;
    Button btnLineDrive;
    Button btnPopFly;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ballinplay_layout);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        getWindow().setLayout((int)(width*0.8),(int)(height*0.8));

        //views
        btnBack = (Button) findViewById(R.id.btnBack);
        btnGroundBall = (Button) findViewById(R.id.btnGroundBall);
        btnBunt = (Button) findViewById(R.id.btnBunt);
        btnLineDrive = (Button) findViewById(R.id.btnLineDrive);
        btnPopFly = (Button) findViewById(R.id.btnPopFly);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Pitch.class));
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        btnGroundBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPopUp(btnGroundBall.getText().toString());

            }
        });

        btnBunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPopUp(btnBunt.getText().toString());

            }
        });

        btnLineDrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPopUp(btnLineDrive.getText().toString());

            }
        });

        btnPopFly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextPopUp(btnPopFly.getText().toString());

            }
        });
    }


    private void nextPopUp(String title){
        Intent i = new Intent(getApplicationContext(),BallInPlay2.class);
        Bundle bundle = new Bundle();
        bundle.putString("Title",title);
        i.putExtras(bundle);
        startActivityForResult(i,REQUEST_CODE_GETMESSAGE_BALLINPLAY2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("OnActivityResult at BallInPlay --------------------");
        System.out.println("RequestCode at BallInPlay is: "+requestCode+" --------------------");
        switch (requestCode){

            case REQUEST_CODE_GETMESSAGE_BALLINPLAY2:
                System.out.println("Checking case at BallInPlay --------------------");
                if (resultCode == Activity.RESULT_OK){
                    System.out.println("REQUEST_CODE_GETMESSAGE at BallInPlay --------------------");
                    String[] message = BallInPlay2.getResultKeyMessage(data);
                    System.out.println(message+" Message at BallInPlay --------------------");
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_KEY_MESSAGE,message);
                    setResult(Activity.RESULT_OK,intent);
                    System.out.println("Prefinish --------------------");
                    finish();
                }else{
                    System.out.println("Else in BallInPlay");
                }
        }
    }

    public static String[] getResultKeyMessage(Intent intent){
        return intent.getStringArrayExtra(RESULT_KEY_MESSAGE);
    }
}
