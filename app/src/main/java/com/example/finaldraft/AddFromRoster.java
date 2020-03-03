package com.example.finaldraft;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finaldraft.model.Player;

import org.w3c.dom.Text;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.internal.Table;

public class AddFromRoster extends Activity {
    Button btnBack;
    Button btnAddPlayer;
    TableLayout tblRoster;

    Realm realm;

    //Ids for the table rows
    int count = 0;
    int index_id = 4083;
    int first_id = 4183;
    int last_id = 4283;
    int number_id = 4383;
    int select_id = 4483;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfromroster_layout);
        realm = StartingWindow.getRealm();
        //setting views
        btnBack = (Button) findViewById(R.id.btnBack);
        btnAddPlayer = (Button) findViewById(R.id.btnAddPlayer);
        tblRoster = (TableLayout) findViewById(R.id.tblRoster);

        //event listeners
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // closing window
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });
        btnAddPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ninePlayers()) {
                    setResult(Activity.RESULT_OK);
                    finish();
                }else{
                    // error message
                    Toast toast=Toast.makeText(getApplicationContext(),"Players in Line Up Exceeds 9",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.getView().setBackgroundResource(R.drawable.error_message);

                    TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(20);
                    toast.show();
                }
            }
        });
        count = 0;
        loadTable();

    }

    private boolean ninePlayers(){
        int count = 0;
        RosterNode tmp = StartingWindow.getRoster();
        if (tmp.isChecked){
            count++;
        }
        while (tmp.next != null){
            if (tmp.next.isChecked){
                count++;
            }
            tmp = tmp.next;
        }
        return count <= 9;
    }

    private void loadTable(){
        // adding table rows of players from the roster
        tblRoster.removeAllViews();
        count=0;
        RosterNode tmp = StartingWindow.getRoster();
        String[] header = {"No.","First Name","Last Name","#","Select"};
        addTableRow(header);
        final RealmResults<Player> results = realm.where(Player.class).findAll();
        for (Player player: results){
            System.out.println("TMP is: "+tmp.data.getFirstName());
            if (tmp.isChecked == false) {
                // only displaying the players that were not selected yet
                int[] ids = {index_id+count,first_id + count, last_id + count, number_id + count, select_id + count};
                String[] msg = {String.valueOf(tmp.index),tmp.data.getFirstName(), tmp.data.getLastName(), tmp.data.getPlayerNumber()};
                addTableRow(ids,msg);
                count++;
            }
            tmp = tmp.next;
        }
    }
    //Polymorphism
    private void addTableRow(String[] msg){
        // adding a table row if only given an array of strings
        // for headers
        TableRow tr = new TableRow(getApplicationContext());
        tr.setPadding(10, 0, 10, 0);
        for (int i = 0; i < msg.length;i++){
            TextView lblTmp = new TextView(getApplicationContext());
            lblTmp.setText(msg[i]);
            lblTmp.setTextSize(20);
            lblTmp.setPadding(50,0,70,0);
            tr.addView(lblTmp);
        }
        tblRoster.addView(tr);
    }


    private void addTableRow(int[] ids, String[] msg) {
        // adding a table row if given array of strings and ints
        // for adding players
        TableRow tr = new TableRow(getApplicationContext());
        tr.setPadding(10, 0, 10, 0);
        for (int i = 0; i < 4; i++) { // dynamically adding text views
            TextView lblTmp = new TextView(getApplicationContext());
            lblTmp.setId(ids[i]);
            lblTmp.setText(msg[i]);
            lblTmp.setLayoutParams(new TableRow.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
            lblTmp.setTextSize(20);
            lblTmp.setPadding(50, 0, 70, 0);
            tr.addView(lblTmp);
        }

        // dynamically adding a check box
        CheckBox cbTmp = new CheckBox(getApplicationContext());
        cbTmp.setId(ids[4]);
        cbTmp.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        cbTmp.setPadding(0, 0, 0, 0);
        tr.addView(cbTmp);

        tblRoster.addView(tr);

        // event listner for check box
        cbTmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                int ref = v.getId() - select_id; // calculating the index number of a player based
                                                 // on what checkbox was selected
                if (checked) {
                    addPlayer(ref,true);
                }else{
                    addPlayer(ref,false);
                }
            }
        });


    }

    protected void addPlayer(int ref, boolean isChecked){
        // for changing the isChecked boolean of a player based on their index
        RosterNode tmp = StartingWindow.getRoster();
        // locating reference of the text views based on index
        TextView lblFirst = (TextView) findViewById(first_id+ref);
        TextView lblLast = (TextView) findViewById(last_id+ref);
        TextView lblNo = (TextView) findViewById(number_id+ref);
        // searching for player object based on information
        while (!(tmp.data.getFirstName().equals(lblFirst.getText().toString()) && tmp.data.getLastName().equals(lblLast.getText().toString()) && tmp.data.getPlayerNumber().equals(lblNo.getText().toString()))) {
            tmp = tmp.next;
        }
        tmp.isChecked = isChecked;

    }
}
