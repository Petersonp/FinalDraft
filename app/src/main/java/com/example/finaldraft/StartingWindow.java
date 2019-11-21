package com.example.finaldraft;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.finaldraft.model.Player;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class StartingWindow extends AppCompatActivity {
    public static final int REQUEST_CODE_GETMESSAGE_LINEUP = 0;
    public static final int REQUEST_CODE_GETMESSAGE_ROSTER = 1;
    public static final int REQUEST_CODE_GETMESSAGE_STATS = 2;
    public static final int REQUEST_CODE_GETMESSAGE_GAMEACTIVITY = 20;

    //starts at 1
    public int RosterCount = 0;

    Button btnNewGame;
    Button btnRoster;
    Button btnStats;

    static boolean first = true;

    private static Realm realm1;

    public static Realm getRealm() {
        return realm1;
    }

    public static void setRealm(Realm realm1) {
        StartingWindow.realm1 = realm1;
    }

    private static PlayerNode starter;

    public static PlayerNode getStarter() {
        return starter;
    }

    public static void setStarter(PlayerNode starter) {
        StartingWindow.starter = starter;
    }

    private static RosterNode roster;
    public static RosterNode getRoster() {
        return roster;
    }

    public static void setRoster(RosterNode roster) {
        StartingWindow.roster = roster;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startingwindow_layout);
        Realm.init(this);
        setRealm(Realm.getDefaultInstance());

        /*RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        setRealm(Realm.getInstance(config));*/







        //views
        btnNewGame = (Button) findViewById(R.id.btnNewGame);
        btnRoster = (Button) findViewById(R.id.btnRoster);
        btnStats = (Button) findViewById(R.id.btnStats);


        // event listeners
        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(LineUp.class, REQUEST_CODE_GETMESSAGE_LINEUP);
            }
        });

        btnRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(Roster.class, REQUEST_CODE_GETMESSAGE_ROSTER);
            }
        });

        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivity(Stats.class,REQUEST_CODE_GETMESSAGE_STATS);
            }
        });
        if (first == true) {
            createRoster();
            first = false;
        }
        // Realm database
        /*
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        */


    }

    public static void printLineUp(String location){
        PlayerNode tmp = getStarter();
        if (tmp != null) {
            System.out.println("<-----Player-----> Order: " + tmp.order+" @"+location);
            System.out.println("First Name: " + tmp.data.getFirstName());
            System.out.println("Last Name: " + tmp.data.getLastName());
            System.out.println("Player Number: " + tmp.data.getPlayerNumber());
            System.out.println("<---------------->");
            while (tmp.next != null) {
                System.out.println("<-----Player-----> Order: " + tmp.next.order+" @"+location);
                System.out.println("First Name: " + tmp.next.data.getFirstName());
                System.out.println("Last Name: " + tmp.next.data.getLastName());
                System.out.println("Player Number: " + tmp.next.data.getPlayerNumber());
                System.out.println("<---------------->");
                tmp = tmp.next;
            }
        }else{
            System.out.println("Roster is Null");
        }
    }
    public static void printRoster(String location){
        RosterNode tmp = getRoster();
        if (tmp != null) {
            System.out.println("<-----Player-----> Index: " + tmp.index+" @"+location);
            System.out.println("First Name: " + tmp.data.getFirstName());
            System.out.println("Last Name: " + tmp.data.getLastName());
            System.out.println("Player Number: " + tmp.data.getPlayerNumber());
            System.out.println("IsChecked: "+tmp.isChecked);
            System.out.println("<---------------->");
            while (tmp.next != null) {
                System.out.println("<-----Player-----> Index: " + tmp.next.index+" @"+location);
                System.out.println("First Name: " + tmp.next.data.getFirstName());
                System.out.println("Last Name: " + tmp.next.data.getLastName());
                System.out.println("Player Number: " + tmp.next.data.getPlayerNumber());
                System.out.println("IsChecked: "+tmp.next.isChecked);
                System.out.println("<---------------->");
                tmp = tmp.next;
            }
        }else{
            System.out.println("Roster is Null");
        }
    }

    private void openActivity(Class cls, int REQUEST_CODE){
        Intent i = new Intent(getApplicationContext(),cls);
        startActivityForResult(i,REQUEST_CODE);
    }

    public void createRoster(){
        boolean first = true;
        final RealmResults<Player> results = getRealm().where(Player.class).findAll();
        RosterNode tmp = new RosterNode();
        for (Player player : results) {
            RosterNode newNode = new RosterNode();
            newNode.data = player;
            RosterCount++;
            newNode.index = RosterCount;
            if (first) {
                setRoster(newNode);
                first = false;
            } else {
                tmp.next = newNode;
            }
            tmp = newNode;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case (REQUEST_CODE_GETMESSAGE_LINEUP):
                if (resultCode == Activity.RESULT_OK) {
                    Intent i = new Intent(getApplicationContext(), GameActivity.class);
                    Bundle b = new Bundle();
                    b.putBooleanArray("start", LineUp.getResultKeyMessage(data));
                    i.putExtras(b);
                    startActivity(i);
                }
                break;
        }
    }
}



