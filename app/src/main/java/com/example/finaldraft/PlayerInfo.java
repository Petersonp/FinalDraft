package com.example.finaldraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PlayerInfo extends Activity {
    public static final String RESULT_KEY_MESSAGE = "com.example.a12fixedfirstdraft.PlayerInfo - Return Message";

    String[] info;

    Button btnCreate;
    Button btnCancel;

    EditText txtFirstName;
    EditText txtLastName;
    EditText txtNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerinfo_layout);

        //views
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        txtFirstName = (EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText) findViewById(R.id.txtLastName);
        txtNumber = (EditText) findViewById(R.id.txtNumber);

        //event listeners

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnResult();
            }
        });

        Bundle bundle = getIntent().getExtras();
        info = bundle.getStringArray("info");
        btnCreate.setText("Create");
        setFields();
    }

    protected void setFields(){
        if (Boolean.valueOf(info[0])){
            txtFirstName.setText(info[1]);
            txtLastName.setText(info[2]);
            txtNumber.setText(info[3]);
            btnCreate.setText("Update");
        }
    }

    protected void returnResult(){
        String[] result = new String[4];
        result[0] = txtFirstName.getText().toString();
        result[1] = txtLastName.getText().toString();
        result[2] = txtNumber.getText().toString();
        result[3] = info[4];
        Intent i = new Intent();
        i.putExtra(RESULT_KEY_MESSAGE,result);
        setResult(Activity.RESULT_OK,i);
        finish();
    }

    public static String[] getResultKeyMessage(Intent intent){
        return intent.getStringArrayExtra(RESULT_KEY_MESSAGE);
    }
}
