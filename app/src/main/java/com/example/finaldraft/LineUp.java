package com.example.finaldraft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.finaldraft.model.Player;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class LineUp extends StartingWindow {
    Button btnBack;
    Button btnContinue;
    Button btnAdd;
    Button btnCreate;

    TextView lblError;

    TableLayout tblLineUp;
    int count;
    int order_id = 6033;
    int first_id = 6133;
    int last_id = 6233;
    int number_id = 6333;
    int pos_id = 6433;
    int remove_id = 6533;

    boolean[] spnIndexRef = {false,false,false,false,false,false,false,false,false};
    boolean[] spnPosRef = {false,false,false,false,false,false,false,false,false};



    String[] positions = {"P","C","FB","SB","TB","SS","LF","CF","RF"};


    public static final int REQUEST_CODE_GETMESSAGE_PLAYERINFO = 3;
    public static final int REQUEST_CODE_GETMESSAGE_ADDFROMROSTER = 0;
    Realm realm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lineup_layout);
        realm = getRealm();
        //setting views
        btnBack = (Button) findViewById(R.id.btnBack);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        tblLineUp = (TableLayout) findViewById(R.id.tblLineUp);
        //event listeners
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRoster();
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayerInfo(false,null,null,null,null);
            }
        });

    }

    private void openRoster(){
        Intent i = new Intent(getApplicationContext(),AddFromRoster.class);
        startActivityForResult(i,REQUEST_CODE_GETMESSAGE_ADDFROMROSTER);
    }

    private void openPlayerInfo(boolean isEdit, String firstName, String lastName, String playerNumber, String index){
        String[] info = {String.valueOf(isEdit),firstName,lastName,playerNumber,index};
        Intent i = new Intent(getApplicationContext(),PlayerInfo.class);
        Bundle b = new Bundle();
        b.putStringArray("info",info);
        i.putExtras(b);
        startActivityForResult(i, REQUEST_CODE_GETMESSAGE_PLAYERINFO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case (REQUEST_CODE_GETMESSAGE_PLAYERINFO):
                if (resultCode == Activity.RESULT_OK){
                    final String[] result = PlayerInfo.getResultKeyMessage(data);
                    Player player = new Player();
                    player.setFirstName(result[0]);
                    player.setLastName(result[1]);
                    player.setPlayerNumber(result[2]);
                    realm.beginTransaction();
                    realm.insert(player);
                    realm.commitTransaction();
                    PlayerNode newNode = new PlayerNode();
                    newNode.data = player;
                    newNode.isChecked = true;
                    RosterCount++;
                    newNode.index = RosterCount;
                    updateTable();
                }
                break;
            case (REQUEST_CODE_GETMESSAGE_ADDFROMROSTER):
                if (resultCode == Activity.RESULT_OK){
                    System.out.println(getRoster().isChecked);
                    updateTable();
                }
        }
    }

    private void updateTable(){
        String[] head = {"Order","First Name","Last Name","#","Pos.","Remove"};
        tblLineUp.removeAllViews();
        count = 0;
        addTableRow(head);
        PlayerNode tmp = getRoster();
        if (tmp.isChecked){
            String[] msg = {tmp.data.getFirstName(), tmp.data.getLastName(), tmp.data.getPlayerNumber()};
            int[] ids = {order_id + count, first_id + count, last_id + count, number_id + count, pos_id + count, remove_id + count};
            addTableRow(ids, msg);
            count++;
        }
        while (tmp.next!=null){
            System.out.println("updatetable check");
            if (tmp.next.isChecked) {
                String[] msg = {tmp.next.data.getFirstName(), tmp.next.data.getLastName(), tmp.next.data.getPlayerNumber()};
                int[] ids = {order_id + count, first_id + count, last_id + count, number_id + count, pos_id + count, remove_id + count};
                addTableRow(ids, msg);
                count++;
            }
            tmp = tmp.next;
        }
    }

    private void addTableRow(String[] msg){
        TableRow tr = new TableRow(getApplicationContext());
        tr.setPadding(10, 0, 10, 0);
        for (int i = 0; i < msg.length;i++){
            TextView lblTmp = new TextView(getApplicationContext());
            lblTmp.setText(msg[i]);
            lblTmp.setTextSize(20);
            lblTmp.setPadding(50,0,70,0);
            tr.addView(lblTmp);
        }
        tblLineUp.addView(tr);
    }

    private void addTableRow(int[] ids, String[] msg){
        TableRow tr = new TableRow(getApplicationContext());
        // Order spinner
        final Spinner spnIndex = new Spinner(getApplicationContext());
        spnIndex.setId(ids[0]);
        spnIndex.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        ArrayList<String> indexArray = new ArrayList<String>();
        for (int i = 1; i < 10;i++){
            indexArray.add(String.valueOf(i));
        }
        ArrayAdapter<String> spnIndexAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, indexArray);
        spnIndex.setAdapter(spnIndexAdapter);
        spnIndex.setSelection(count);
        tr.addView(spnIndex);

        // Position spinner
        final Spinner spnPos = new Spinner(getApplicationContext());
        spnPos.setId(ids[4]);
        spnPos.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        ArrayList<String> posArray = new ArrayList<String>();
        for (int i = 0; i < positions.length;i++){
            posArray.add(positions[i]);
        }
        ArrayAdapter<String> spnPosAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, posArray);
        spnPos.setAdapter(spnPosAdapter);
        spnPos.setSelection(count);

        // Player Info
        for (int i =0; i <3;i++){
            TextView lblTmp = new TextView(getApplicationContext());
            lblTmp.setId(ids[i]);
            lblTmp.setText(msg[i]);
            lblTmp.setLayoutParams(new TableRow.LayoutParams(GridLayout.LayoutParams.WRAP_CONTENT, GridLayout.LayoutParams.WRAP_CONTENT));
            lblTmp.setTextSize(20);
            lblTmp.setPadding(40,0,55,0);
            tr.addView(lblTmp);
        }
        tr.addView(spnPos);

        // Remove Button
        Button btnRemove = new Button(getApplicationContext());
        btnRemove.setId(ids[5]);
        btnRemove.setText("Remove");
        btnRemove.setPadding(20,0,20,0);
        tr.addView(btnRemove);

        // Add to table layout
        tblLineUp.addView(tr);

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePlayerNode(v);
                updateTable();
            }
        });

        spnIndex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int ref = parent.getId()-order_id;
                PlayerNode tmp = StartingWindow.getRoster();
                for (int i = 0; i <ref ; i ++){
                    tmp = tmp.next;
                }
                System.out.println(spnIndex.getItemAtPosition(position).toString()+ " IS THE ORDER FOR: "+ tmp.data.getFirstName());
                tmp.order = Integer.valueOf(spnIndex.getItemAtPosition(position).toString());

                //switching
                PlayerNode tmp2 = StartingWindow.getRoster();
                updateSpnIndexRef();
                int index = 0;
                if (tmp2.order == Integer.parseInt(spnIndex.getItemAtPosition(position).toString()) &&
                        !(tmp.data.getFirstName().equals(tmp2.data.getFirstName()) && tmp.data.getLastName().equals(tmp2.data.getLastName()) && tmp.data.getPlayerNumber().equals(tmp2.data.getPlayerNumber()))){
                    Spinner spnTmp = (Spinner) findViewById(order_id+index);

                    for (int i = 0; i<spnIndexRef.length;i++){
                        if (!spnIndexRef[i]){
                            spnTmp.setSelection(i);
                            tmp2.order = Integer.parseInt(spnTmp.getItemAtPosition(i).toString());
                            break;
                        }
                    }
                }else{
                    while (tmp2.next != null) {
                        index++;
                        if (tmp2.next.order == Integer.parseInt(spnIndex.getItemAtPosition(position).toString()) &&
                                !(tmp.data.getFirstName().equals(tmp2.next.data.getFirstName()) && tmp.data.getLastName().equals(tmp2.next.data.getLastName()) && tmp.data.getPlayerNumber().equals(tmp2.next.data.getPlayerNumber()))){
                            Spinner spnTmp = (Spinner) findViewById(order_id+index);
                            for (int i = 0; i<spnIndexRef.length;i++){
                                if (!spnIndexRef[i]){
                                    spnTmp.setSelection(i);
                                    tmp2.next.order = Integer.parseInt(spnTmp.getItemAtPosition(i).toString());
                                    break;
                                }
                            }
                            break;
                        }
                        tmp2 = tmp2.next;
                    }
                    // tis unique index
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        spnPos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int ref = parent.getId()-pos_id;
                PlayerNode tmp = StartingWindow.getRoster();
                for (int i = 0; i <ref ; i ++){
                    tmp = tmp.next;

                }
                System.out.println(spnPos.getItemAtPosition(position).toString()+ " IS THE POSITION FOR: "+ tmp.data.getFirstName());
                tmp.position = spnPos.getItemAtPosition(position).toString();
                System.out.println(tmp.position);
                PlayerNode tmp2 = getRoster();
                updateSpnPosRef();

                int index = 0;
                if (tmp2.position.equals(spnPos.getItemAtPosition(position).toString()) &&
                        !(tmp.data.getFirstName().equals(tmp2.data.getFirstName()) && tmp.data.getLastName().equals(tmp2.data.getLastName()) && tmp.data.getPlayerNumber().equals(tmp2.data.getPlayerNumber()))){
                    Spinner spnTmp = (Spinner) findViewById(pos_id+index);

                    for (int i = 0; i<spnPosRef.length;i++){
                        if (!spnPosRef[i]){
                            spnTmp.setSelection(i);
                            tmp2.position = spnTmp.getItemAtPosition(i).toString();
                            break;
                        }
                    }
                }else{
                    while (tmp2.next != null) {
                        index++;
                        if (tmp2.next.position != null) {
                            if (tmp2.next.position.equals(spnPos.getItemAtPosition(position).toString()) &&
                                    !(tmp.data.getFirstName().equals(tmp2.next.data.getFirstName()) && tmp.data.getLastName().equals(tmp2.next.data.getLastName()) && tmp.data.getPlayerNumber().equals(tmp2.next.data.getPlayerNumber()))) {
                                Spinner spnTmp = (Spinner) findViewById(pos_id + index);
                                for (int i = 0; i < spnPosRef.length; i++) {
                                    if (!spnPosRef[i]) {
                                        spnTmp.setSelection(i);
                                        tmp2.next.position = spnTmp.getItemAtPosition(i).toString();
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        tmp2 = tmp2.next;
                    }
                    // tis unique index
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

    }

    private void updateSpnIndexRef(){
        //true = full
        for (int i = 0; i < 9; i++){
            spnIndexRef[i] = false;
        }
        PlayerNode tmp = getRoster();
        if (tmp.isChecked) {
            spnIndexRef[tmp.order-1] = true;
        }
        while (tmp.next!=null){
            if (tmp.next.isChecked){
                if (tmp.next.order >0) {
                    System.out.println((tmp.next.order - 1));
                    spnIndexRef[tmp.next.order - 1] = true;
                }
            }
            tmp = tmp.next;
        }
        System.out.println("AFTER: "+totString(spnIndexRef));
    }

    private void updateSpnPosRef(){
        //true = full
        for (int i = 0; i < 9; i++){
            spnPosRef[i] = false;
        }
        PlayerNode tmp = getRoster();
        for (int i = 0; i < positions.length;i++){
            if (tmp.isChecked && tmp.position.equals(positions[i])){
                spnPosRef[i] = true;
            }else{
                while (tmp.next != null){
                    if (tmp.next.position != null) {
                        if (tmp.next.isChecked && tmp.next.position.equals(positions[i])) {
                            spnPosRef[i] = true;
                            break;
                        }

                    }
                    tmp = tmp.next;
                }
            }
        }
        System.out.println("AFTER: "+totString(spnPosRef));
    }

    //Nodes
    private void removePlayerNode(View view){
        int ref = view.getId()-remove_id;
        PlayerNode tmp = getStarter();
    }

    private String totString(boolean[] array){
        String s="[";
        for (int i =0;i<array.length;i++){
            s+=(String.valueOf(array[i])+" , ");
        }
        s+="]";
        return s;
    }


}
