package com.example.finaldraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.finaldraft.model.Player;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Roster extends StartingWindow {
    public static final int REQUEST_CODE_GETMESSAGE_PLAYERINFO = 3;
    private static final String TAG = "Roster";

    boolean isEdit;

    int count = 0;
    int index_id = 4083;
    int first_id = 4183;
    int last_id = 4283;
    int no_id = 4383;
    int edit_id = 4483;
    int remove_id = 4583;
    int row_id = 4683;

    Realm realm;

    Button btnNewPlayer;
    Button btnBack;
    Button btnTmp;
    TableLayout tblRoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roster_layout);

        realm = getRealm();

        //setting views
        tblRoster = (TableLayout) findViewById(R.id.tblLineUp);
        btnNewPlayer = (Button) findViewById(R.id.btnNewPlayer);
        btnBack = (Button) findViewById(R.id.btnBack);

        // event listeners
        btnNewPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit = false;
                openPlayerInfo(isEdit,null,null,null,null);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });


        TableRow header = new TableRow(getApplicationContext());
        header.setPadding(10,0,0,0);

        loadTable();
    }


    private void loadTable(){
        //Adding all of the players from the database roster to the table
        tblRoster.removeAllViews();
        TableRow header = new TableRow(getApplicationContext());
        header.setPadding(10,0,0,0);
        String[] head = {"   ","First Name","Last Name","#","Edit","Remove"};
        // adding header to table
        addTableRow(head);
        tblRoster.addView(header);
        count = 0;
        RealmResults<Player> players = realm.where(Player.class).findAll();
        for (Player player: players){
            System.out.println(player.getFirstName());
            String[] msg = {String.valueOf(count+1),player.getFirstName(),player.getLastName(),player.getPlayerNumber()};
            int[] ids = {index_id+count, first_id+count, last_id+count, no_id+count, edit_id+count, remove_id+count, row_id+count};
            addTableRow(ids,msg);
            count++;
        }
    }


    private void addTableRow(String[] msg){
        // when receiving just an array of strings
        // for header
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

    private void addTableRow(int[] ids, String[] msg){
        // when receiving array of strings and int
        // for player information
        TableRow tr = new TableRow(getApplicationContext());
        tr.setId(ids[6]);
        tr.setPadding(10,0,0,0);
        // dynamically adding player information in text views
        for (int i =0; i <4;i++){
            TextView lblTmp = new TextView(getApplicationContext());
            lblTmp.setText(msg[i]);
            lblTmp.setId(ids[i]);
            lblTmp.setLayoutParams(new TableRow.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
            lblTmp.setTextSize(20);
            lblTmp.setPadding(70,0,70,0);
            tr.addView(lblTmp);
        }
        // dynamically adding edit button
        Button btnEdit = new Button(getApplicationContext());
        btnEdit.setId(ids[4]);
        btnEdit.setText("Edit");
        btnEdit.setPadding(15,0,15,0);
        btnEdit.setBackgroundResource(R.drawable.yellow_button);
        tr.addView(btnEdit);

        // dynamically adding remove button
        Button btnRemove = new Button(getApplicationContext());
        btnRemove.setId(ids[5]);
        btnRemove.setText("Remove");
        btnRemove.setPadding(15,0,15,0);
        btnRemove.setBackgroundResource(R.drawable.yellow_button);
        tr.addView(btnRemove);

        tblRoster.addView(tr);
        // event listener for remove button
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePlayer(v);
                removePlayerNode(v);
                RosterCount--;
            }
        });
        // event listener for edit button
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEdit(v);
            }
        });

    }

    private void removePlayer(View v){
        int ref = v.getId()-remove_id;
        final RealmResults<Player> results = realm.where(Player.class).findAll();
        Player player = results.get(ref); // receiving ref to pbject based on index
        realm.beginTransaction();
        player.deleteFromRealm(); // removing player object from realm database
        realm.commitTransaction();
        loadTable();
    }
    private void removePlayerNode(View view){
        //removing a player from the roster table
        RosterNode tmp = getRoster();
        int ref = view.getId()-remove_id;
        // getting references to a players text views based on index
        TextView tmpFirst = (TextView) findViewById(ref+first_id);
        TextView tmpLast = (TextView) findViewById(ref+last_id);
        TextView tmpNumber = (TextView) findViewById(ref+no_id);
        if (tmp.index==(ref+1)){  // checking if first obejct in linked list
            if (tmp.next == null){
                setRoster(null);
            }else{
                setRoster(getRoster().next);
                while(tmp.next != null){
                    tmp.next.index --;
                    tmp = tmp.next;
                }
            }
        }else{ // checking for subsequent objects in linked list
            while (tmp.next.index != (ref+1)){
                tmp = tmp.next;
            }
            if (tmp.next.next == null){
                tmp.next = null;
            }else {
                tmp.next = tmp.next.next;
                while (tmp.next != null) {
                    tmp.next.index = (tmp.next.index - 1);
                    tmp = tmp.next;
                }
            }
        }
    }

    private void addPlayerNode(Player player, int index){
        // adding a new player node to the roster
        RosterNode newPlayer = new RosterNode();
        newPlayer.data = player;
        newPlayer.index = index;
        if (getRoster() == null){ // checking if linked list is empty
            setRoster(newPlayer);
        }else{
            RosterNode tmp = getRoster();
            while(tmp.next != null){
                tmp = tmp.next;
            }
            tmp.next = newPlayer;
        }
        printRoster("Add Player Node");
    }

    private void editPlayerNode(Player player, int index){
        // editing a player in the roster
        RosterNode tmp = getRoster();
        if (tmp.index == index){ // checking if first object in linked list is player
            tmp.data = player;
        }else{
            System.out.println("Comparing: "+tmp.next.index+" to: "+index);
            while (tmp.next.index!= index) { // checking subsequent objects in linked list
                tmp = tmp.next;
            }
            tmp.next.data = player;
        }
        printRoster("Edit Player Node");
    }

    protected void openEdit(View v){
        // opening edit window
        isEdit = true;
        int ref = v.getId()-edit_id;
        // finding ref to text views depending on player index
        TextView lblIndex = (TextView) findViewById(index_id+ref);
        TextView lblFirst = (TextView) findViewById(first_id+ref);
        TextView lblLast = (TextView) findViewById(last_id+ref);
        TextView lblNumber = (TextView) findViewById(no_id+ref);
        openPlayerInfo(isEdit,
                lblFirst.getText().toString(),
                lblLast.getText().toString(),
                lblNumber.getText().toString(),
                lblIndex.getText().toString()); //passing data to next activity
    }


    ////////////////RECEIVING DATA FROM OTHER ACTIVITY//////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case (REQUEST_CODE_GETMESSAGE_PLAYERINFO):
                if (resultCode == Activity.RESULT_OK) {
                    if (isEdit) { // if a player information was edited
                        final String[] result = PlayerInfo.getResultKeyMessage(data);
                        int ref = Integer.parseInt(result[3]);
                        final RealmResults<Player> results = realm.where(Player.class).findAll();
                        Player player = results.get(ref-1);
                        realm.beginTransaction();
                        // updating attributes of player object
                        player.setFirstName(result[0]);
                        player.setLastName(result[1]);
                        player.setPlayerNumber(result[2]);
                        realm.commitTransaction();
                        editPlayerNode(player,ref);
                        loadTable();
                    }else{
                        final String[] result = PlayerInfo.getResultKeyMessage(data);
                        // creating new player object based on results
                        Player player = new Player();
                        player.setFirstName(result[0]);
                        player.setLastName(result[1]);
                        player.setPlayerNumber(result[2]);
                        realm.beginTransaction();
                        realm.insert(player);
                        realm.commitTransaction();
                        loadTable();
                        RosterCount++;
                        addPlayerNode(player,RosterCount);
                    }
                }

                break;
        }
    }
    ////////////////RECEIVING DATA FROM OTHER ACTIVITY//////////////

    private void openPlayerInfo(boolean isEdit, String firstName, String lastName, String playerNumber, String index){
        // opening player information activity
        String[] info = {String.valueOf(isEdit),firstName,lastName,playerNumber,index};
        Intent i = new Intent(getApplicationContext(),PlayerInfo.class);
        Bundle b = new Bundle();
        b.putStringArray("info",info);
        i.putExtras(b);
        startActivityForResult(i, REQUEST_CODE_GETMESSAGE_PLAYERINFO);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
