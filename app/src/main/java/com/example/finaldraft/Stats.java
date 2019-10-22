package com.example.finaldraft;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.finaldraft.model.Player;

import io.realm.Realm;
import io.realm.RealmResults;

public class Stats extends StartingWindow {
    public static final String BATTING_STATS = "BattingStats";
    public static final String PITCHING_STATS = "PitchingStats";
    public static final String RUNNING_STATS = "RunningStats";

    Button btnPitching;
    Button btnBatting;
    Button btnRunning;
    Button btnBack;

    Realm realm;

    TableLayout tblStats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);
        realm = getRealm();

        //views
        btnPitching = (Button) findViewById(R.id.btnPitching);
        btnBatting = (Button) findViewById(R.id.btnBatting);
        btnRunning = (Button) findViewById(R.id.btnRunning);
        tblStats = (TableLayout) findViewById(R.id.tblLineUp);
        btnBack = (Button) findViewById(R.id.btnBack);

        // event listeners
        btnPitching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTable(PITCHING_STATS);
            }
        });
        btnBatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTable(BATTING_STATS);
            }
        });
        btnRunning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTable(RUNNING_STATS);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateTable(String statsType){
        final RealmResults<Player> results = realm.where(Player.class).findAll();
        tblStats.removeAllViews();
        switch (statsType){
            case (BATTING_STATS):
                String[] BHeaders = {"Batter Name","BA","OBP","OPS","BBK","BB","K"};
                addStatRows(BHeaders);
                for (Player player: results){
                    addStatRows(player.getBattingStats());
                }
                break;
            case (PITCHING_STATS):
                String[] PHeaders = {"Pitcher Name","BABIP","GF","PIT","K","KBB","SP","CP"};
                addStatRows(PHeaders);
                for (Player player: results){
                    addStatRows(player.getPitchingStats());
                }
                break;
            case (RUNNING_STATS):
                String[] RHeaders = {"Runner Name","SP","SA","SS"};
                addStatRows(RHeaders);
                for (Player player: results){
                    addStatRows(player.getRunningStats());
                }
                break;
        }
    }

    private void addStatRows(String[] stats){
        TableRow trTmp = new TableRow(getApplicationContext());
        trTmp.setPadding(10, 0, 0, 0);
        for(int i = 0; i < stats.length;i++){
            TextView lblTmp = new TextView(getApplicationContext());
            lblTmp.setText(stats[i]);
            lblTmp.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
            lblTmp.setTextSize(20);
            lblTmp.setPadding(40,0,55,0);

            trTmp.addView(lblTmp);
        }
        tblStats.addView(trTmp);
    }
}
