package com.example.finaldraft;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finaldraft.model.Player;

import org.w3c.dom.Text;

public class GameActivity extends StartingWindow {
    public static final int REQUEST_CODE_GETMESSAGE_PITCH = 6; //Opening Pitch Activity
    public static final int REQUEST_CODE_SUBPLAYER = 19;  //Opening SubPlayer Activity
    public static final int REQUEST_CODE_RUNNER = 99;  //Opening Runner Activity

    // Fielding Buttons
    Button btnPitcher;
    Button btnCatcher;
    Button btnFirstBase;
    Button btnSecondBase;
    Button btnThirdBase;
    Button btnShortStop;
    Button btnLeftField;
    Button btnCenterField;
    Button btnRightField;

    // Running Buttons
    Button btnFirstRunner;
    Button btnSecondRunner;
    Button btnThirdRunner;
    Button btnBatter;

    //Other Buttons
    Button btnUndo;
    Button btnStats;
    Button btnViewTeam;
    Button btnPitch;

    //Text Views
    TextView lblGameInfo;
    TextView lblAtBat;
    TextView lblScores;

    //Whether home or away
    boolean isHome;

    //Game Information
    int strikes;
    int balls;
    int inning;
    int outs;
    int points =0;
    int opoints =0;

    //Batter and Runner Nodes
    private BatterNode batter = new BatterNode();
    private BatterNode fbRunner = new BatterNode();
    private BatterNode sbRunner = new BatterNode();
    private BatterNode tbRunner = new BatterNode();
    private BatterNode hbRunner = new BatterNode();

   //Fielder Nodes
    private PlayerNode pitcher;
    private PlayerNode catcher;
    private PlayerNode firstBase;
    private PlayerNode secondBase;
    private PlayerNode thirdBase;
    private PlayerNode shortStop;
    private PlayerNode leftField;
    private PlayerNode centerField;
    private PlayerNode rightField;


    String[] positions = {"P","C","FB","SB","TB","SS","LF","CF","RF"};
    PlayerNode[] positionNodes = {pitcher,catcher,firstBase,secondBase,thirdBase,shortStop,leftField,centerField,rightField};
    Button[] positionButtons = new Button[9];


    private PlayerNode opponent; //Head of linked list of opponent nodes

    //Pointers for knowing which batter is batting and on deck
    static private PlayerNode atBat;
    static private PlayerNode oatBat;

    //Pointers for Game stack data structure
    Game front;
    Game base;

    //Encapsulated fields for opponent linked list
    public PlayerNode getOpponent() {
        return opponent;
    }
    public void setOpponent(PlayerNode opponent) {
        this.opponent = opponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameactivity_layout);

        //Get result from starting window
        boolean[] result = getIntent().getExtras().getBooleanArray("start");
        isHome = result[0]; //true = home, false = away


        //fielder button views
        btnPitcher = (Button) findViewById(R.id.btnPitcher);positionButtons[0] = btnPitcher;
        btnCatcher = (Button) findViewById(R.id.btnCatcher);positionButtons[1] = btnCatcher;
        btnFirstBase = (Button) findViewById(R.id.btnFirstBase);positionButtons[2] = btnFirstBase;
        btnSecondBase = (Button) findViewById(R.id.btnSecondBase);positionButtons[3] = btnSecondBase;
        btnThirdBase = (Button) findViewById(R.id.btnThirdBase);positionButtons[4] = btnThirdBase;
        btnShortStop = (Button) findViewById(R.id.btnShortStop);positionButtons[5] = btnShortStop;
        btnLeftField = (Button) findViewById(R.id.btnLeftField);positionButtons[6] = btnLeftField;
        btnCenterField = (Button) findViewById(R.id.btnCenterField);positionButtons[7] = btnCenterField;
        btnRightField = (Button) findViewById(R.id.btnRightField);positionButtons[8] = btnRightField;

        //UT button views
        btnPitch = (Button) findViewById(R.id.btnPitch);
        btnUndo = (Button) findViewById(R.id.btnUndo);
        btnStats = (Button) findViewById(R.id.btnStats);
        btnViewTeam = (Button) findViewById(R.id.btnViewTeam);

        //game information views
        lblGameInfo = (TextView) findViewById(R.id.lblGameInfo);
        lblAtBat = (TextView) findViewById(R.id.lblAtBat);
        lblScores = (TextView) findViewById(R.id.lblScores);

        //batter/ runner button views
        btnFirstRunner = (Button) findViewById(R.id.btnFirstRunner);
        btnSecondRunner = (Button) findViewById(R.id.btnSecondRunner);
        btnThirdRunner = (Button) findViewById(R.id.btnThirdRunner);
        btnBatter = (Button) findViewById(R.id.btnBatter);

        //Setting instance variables of batterNodes
        batter.next = fbRunner;
        batter.order = 0;
        batter.btn = btnBatter;
        fbRunner.next = sbRunner;
        fbRunner.order = 1;
        fbRunner.btn = btnFirstRunner;
        sbRunner.next = tbRunner;
        sbRunner.order = 2;
        sbRunner.btn = btnSecondRunner;
        tbRunner.next = hbRunner;
        tbRunner.order = 3;
        tbRunner.btn = btnThirdRunner;


        //event listeners
        btnPitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open pitch window with the intent of receiving data
                Intent i = new Intent(getApplicationContext(),Pitch.class);
                startActivityForResult(i,REQUEST_CODE_GETMESSAGE_PITCH);
            }
        });
        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop(); //pop game object from stack data structure

                //display message on screen
                Toast toast=Toast.makeText(getApplicationContext(),"Undone",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                TextView textView = (TextView) toast.getView().findViewById(android.R.id.message);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(20);
                toast.show();
            }
        });
        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open statistics window
                Intent i = new Intent(getApplicationContext(),Stats.class);
                startActivity(i);
            }
        });
        btnViewTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open Roster window
                Intent i = new Intent(getApplicationContext(),Roster.class);
                startActivity(i);
            }
        });

        //event listener for clicking on fielder buttons
        btnPitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(pitcher);
            }
        });
        btnCatcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(catcher);
            }
        });
        btnFirstBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(firstBase);
            }
        });
        btnSecondBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { substituteActivity(secondBase);
            }
        });
        btnThirdBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(thirdBase);
            }
        });
        btnShortStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(shortStop);
            }
        });
        btnRightField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { substituteActivity(rightField);
            }
        });
        btnCenterField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { substituteActivity(centerField);
            }
        });
        btnLeftField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(leftField);
            }
        });

        //event listener for clicking on runner buttons
        btnFirstRunner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fbRunner.data != null && sbRunner.data == null) {
                    openRunner(fbRunner);
                }
            }
        });
        btnSecondRunner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sbRunner.data != null && tbRunner.data == null) {
                    openRunner(sbRunner);
                }
            }
        });
        btnThirdRunner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tbRunner.data != null) {
                    openRunner(tbRunner);
                }
                updateGameInfo();
            }
        });

        //Starting the game
        createOpponent(); //create opponent linked list
        updateGame(); //start team or opponent as batting depending on isHome
        updateGameInfo(); //Updating game info labels
        push(); //Add new game object to stack data structure
        loadBattingButtons(); //changing text on batter/ runner buttons to
                              //he name of the player object they point at


    }

    protected void openRunner(BatterNode runner){
        //opening runner activity with the intent of receiving data
        Intent i = new Intent(getApplicationContext(), Runner.class);

        //Passing the fn, ln, and pn of the runner to next activity
        Bundle bundle = new Bundle();
        String[] stuff = new String[4];
        stuff[0] = runner.data.data.getFirstName();
        stuff[1] = runner.data.data.getLastName();
        stuff[2] = runner.data.data.getPlayerNumber();
        stuff[3] = String.valueOf(runner.order);
        bundle.putStringArray("Runner",stuff);
        i.putExtras(bundle);

        startActivityForResult(i, REQUEST_CODE_RUNNER);
    }

    /////////////////////STACK DATA STRUCTURE START///////////////////////

    private void push(){ //adding a new game object on top of the stack
        Game game = newGame();
        if(front == null){ //if the stack is empty
            front = game;
            base = game;
        }
        else{   //if the stack has objects already
            if(isEmpty()){
                base.next = game;
                game.back = base;
            }
            game.back = front;
            front.next = game;
            front = game;
        }

    }

    private void pop(){ //returning the game object on top of the stack
        Game g; //new pointer
        if (!isEmpty()){ //if there are still game objects under g
            g = front;
            front = front.back;
        }else{  //if g is the bottom of the stack
            g = front;
        }
        setGame(g); //setting g as the current game on the players screen
    }

    private void setGame(Game g){
        //Setting g.lineup attribute to current lineup
        PlayerNode tmp = g.LineUp[0];
        for (int i =0; i< 9;i++){
            if (i == 0){
                tmp = g.LineUp[i];
                setStarter(tmp);
                tmp = getStarter();
            }else{
                PlayerNode newPlayer = g.LineUp[i];
                tmp.next = newPlayer;
                tmp = tmp.next;
            }
        }
        //Setting g.roster attribute to current roster
        RosterNode rosterTmp = g.roster[0];
        for (int i =0; i<g.roster.length;i++){
            if (i==0){
                rosterTmp = g.roster[i];
                setRoster(rosterTmp);
                rosterTmp = getRoster();
                rosterTmp.data = g.players[i];
            }else{
                RosterNode newRoster = g.roster[i];
                rosterTmp.next = newRoster;
                newRoster.data = g.players[i];
                rosterTmp = rosterTmp.next;
            }
        }
        //setting stats of temporary players in game object to current stats
        RosterNode tmpRoster= getRoster();
        for (int i =0; i<g.stats.length;i++){
            Player tmpPlayer = tmpRoster.data;
            getRealm().beginTransaction();
            tmpPlayer.setHits(Integer.parseInt(g.stats[i][0]));
            tmpPlayer.setAB(Integer.parseInt(g.stats[i][1]));
            tmpPlayer.setBB(Integer.parseInt(g.stats[i][2]));
            tmpPlayer.setHBP(Integer.parseInt(g.stats[i][3]));
            tmpPlayer.setK(Integer.parseInt(g.stats[i][4]));
            tmpPlayer.setTB(Integer.parseInt(g.stats[i][5]));
            tmpPlayer.setSF(Integer.parseInt(g.stats[i][6]));
            tmpPlayer.setBH(Integer.parseInt(g.stats[i][7]));
            tmpPlayer.setBHR(Integer.parseInt(g.stats[i][8]));
            tmpPlayer.setBAB(Integer.parseInt(g.stats[i][9]));
            tmpPlayer.setBK(Integer.parseInt(g.stats[i][10]));
            tmpPlayer.setBSF(Integer.parseInt(g.stats[i][11]));
            tmpPlayer.setGB(Integer.parseInt(g.stats[i][12]));
            tmpPlayer.setPIT(Integer.parseInt(g.stats[i][13]));
            tmpPlayer.setFB(Integer.parseInt(g.stats[i][14]));
            tmpPlayer.setBBP(Integer.parseInt(g.stats[i][15]));
            tmpPlayer.setS(Integer.parseInt(g.stats[i][16]));
            tmpPlayer.setC(Integer.parseInt(g.stats[i][17]));
            tmpPlayer.setSS(Integer.parseInt(g.stats[i][18]));
            tmpPlayer.setSA(Integer.parseInt(g.stats[i][19]));
            getRealm().commitTransaction();
            tmpRoster = tmpRoster.next;
        }
        //Batters/Runners
        isHome = g.isHome;
        PlayerNode tmp1;
        if (isHome){
            tmp1 = getStarter();
            loadFieldingPosition(getOpponent());
        }else{
            tmp1 = getOpponent();
            loadFieldingPosition(getStarter());
        }
        //atbat
        PlayerNode atBatTmp = getStarter();
        for (int i =0; i<g.atBat;i++){
            atBatTmp = atBatTmp.next;
        }
        atBat = atBatTmp;
        //oatbat
        PlayerNode oatBatTmp = getOpponent();
        for (int i =0; i<g.oatBat;i++){
            oatBatTmp = oatBatTmp.next;
        }
        oatBat = oatBatTmp;
        //batter
        PlayerNode batterTmp = tmp1;
        for (int i =0; i<g.batter;i++){
            batterTmp =batterTmp.next;
        }
        batter.data = batterTmp;
        //fbrunner
        if (g.fbRunner!=10) {
            PlayerNode fbRunnerTmp = tmp1;
            for (int i = 0; i < g.fbRunner; i++) {
                fbRunnerTmp = fbRunnerTmp.next;
            }
            fbRunner.data = fbRunnerTmp;
        }else{
            fbRunner.data = null;
        }
        //sbrunner
        if (g.sbRunner != 10) {
            PlayerNode sbRunnerTmp = tmp1;
            for (int i = 0; i < g.sbRunner; i++) {
                sbRunnerTmp = sbRunnerTmp.next;
            }
            sbRunner.data = sbRunnerTmp;
        }else{
            sbRunner.data = null;
        }
        //tbrunner
        if (g.tbRunner != 10) {
            PlayerNode tbRunnerTmp = tmp1;
            for (int i = 0; i < g.tbRunner; i++) {
                tbRunnerTmp = tbRunnerTmp.next;
            }
            tbRunner.data = tbRunnerTmp;
        }else{
            tbRunner.data = null;
        }

        //Setting game instance variables to current game information
        strikes = g.strikes;
        balls = g.balls;
        inning = g.inning;
        outs = g.outs;
        points = g.points;
        opoints = g.opoints;

        updateGameInfo();
        loadBattingButtons();




    }

    protected boolean isEmpty(){
        return (base.next == null);
    }

    private int getRosterLength(){
        int count = 1;
        RosterNode tmp = getRoster();
        while (tmp.next != null){
            count++;
            tmp = tmp.next;
        }
        return count;
    }

    private Game newGame(){
        Game game = new Game();
        PlayerNode[] lineup = new PlayerNode[9];
        PlayerNode tmp = getStarter();
        for (int i = 0; i<9;i++){
            lineup[i] = tmp;
            tmp = tmp.next;
        }
        game.LineUp = lineup;
        RosterNode[] rosterNodes = new RosterNode[getRosterLength()];
        Player[] players = new Player[getRosterLength()];
        RosterNode tmp1 = getRoster();
        for (int j = 0; j<rosterNodes.length;j++){
            rosterNodes[j] = tmp1;
            Player newPlayer = tmp1.data;
            players[j] = newPlayer;
            tmp1 = tmp1.next;
        }
        game.roster = rosterNodes;
        game.players = players;

        //stats
        RosterNode tmpRoster = getRoster();
        String[][] stats = new String[getRosterLength()][20];
        for (int i = 0; i< getRosterLength();i++){
            stats[i] = tmpRoster.data.getRawStats();
            tmpRoster =tmpRoster.next;

        }
        game.stats = stats;
        //game.Players = players;
        game.strikes = strikes;
        game.outs = outs;
        game.inning = inning;
        game.balls = balls;
        game.isHome = isHome;
        game.points = points;
        game.opoints = opoints;

        game.atBat = getInt(atBat);
        game.oatBat = getoInt(oatBat);
        game.batter = getInt(batter.data);
        if (fbRunner.data != null) {
            game.fbRunner = getInt(fbRunner.data);
        }else{
            game.fbRunner = 10;
        }
        if (sbRunner.data != null) {
            game.sbRunner = getInt(sbRunner.data);
        }else{
            game.sbRunner = 10;
        }
        if (tbRunner.data != null) {
            game.tbRunner = getInt(tbRunner.data);
        }else{
            game.tbRunner = 10;
        }
        return game;
    }

    private int getInt(PlayerNode playerNode){
        //Getting the index of the passed plaernode
        System.out.println("Running GetInt");
        PlayerNode tmp;
        if (playerNode.data.getFirstName().equals("Opponent")){
            tmp = getOpponent();
        }else {
            tmp = getStarter();
        }
        int count = 0;
        while (!(tmp.data.getFirstName().equals(playerNode.data.getFirstName())&&tmp.data.getLastName().equals(playerNode.data.getLastName())&&tmp.data.getPlayerNumber().equals(playerNode.data.getPlayerNumber()))){
            System.out.println("Comparing: "+tmp.data.getFirstName()+" "+tmp.data.getLastName()+" #"+tmp.data.getPlayerNumber()+" To: "+playerNode.data.getFirstName()+"po "+playerNode.data.getLastName()+" #"+playerNode.data.getPlayerNumber());
            count++;
            tmp = tmp.next;
        }
        return count;
    }

    private int getoInt(PlayerNode playerNode){
        //getting the index of a player on deck
        PlayerNode tmp = getOpponent();
        int count = 0;
        while (!(tmp.data.getFirstName().equals(playerNode.data.getFirstName())&&tmp.data.getLastName().equals(playerNode.data.getLastName())&&tmp.data.getPlayerNumber().equals(playerNode.data.getPlayerNumber()))){
            count++;
            tmp = tmp.next;
        }
        return count;
    }

    /////////////////////STACK DATA STRUCTURE END///////////////////////

    private void substituteActivity(PlayerNode player){
        if (!player.data.getFirstName().equals("Opponent")) {
            //Opening window to sub players between positions
            Intent i = new Intent(getApplicationContext(), SubPlayer.class);
            Bundle bundle = new Bundle();
            bundle.putStringArray("Position", new String[]{player.data.getFirstName(), player.data.getLastName(), player.position});
            i.putExtras(bundle);
            startActivityForResult(i, REQUEST_CODE_SUBPLAYER);
        }
    }


    private void updateGame(){
        // For setting your team or opponent team to fielding or batting
        if (isHome){
            loadFieldingPosition(getOpponent());
            loadBattingPosition(atBat);
        }else{
            loadFieldingPosition(getStarter());
            loadBattingPosition(oatBat);
        }
    }

    private void loadFieldingPosition(PlayerNode tmpHead){
        // for loading players into their respective fielding positions
        PlayerNode tmp = tmpHead;
        for (int i = 0; i < positions.length;i++){ //For checking the first playerNode in linked list
            if(tmp.position!=null) {
                if (tmp.position.equals(positions[i])) {
                    positionNodes[i] = tmp;
                    break;
                }
            }else{
            }
        }
        while (tmp.next != null){ //For checking subsequent playerNodes in linked list
            for (int i = 0; i < positions.length;i++){
                if(tmp.next.position!=null) {
                    if (tmp.next.position.equals(positions[i])) {
                        positionNodes[i] = tmp.next;
                    }
                }
            }
            tmp = tmp.next;
        }
        updateNodes(positionNodes);
        for (int i = 0; i < positionButtons.length;i++){ //updating the text of the buttons
            positionButtons[i].setText(positionNodes[i].data.getFirstName().toString()+" "+positionNodes[i].data.getLastName().charAt(0));
        }
    }

    protected void updateNodes(PlayerNode[] tmppositionNodes){
        // setting fielding nodes to players
        pitcher = tmppositionNodes[0];
        catcher = tmppositionNodes[1];
        firstBase = tmppositionNodes[2];
        secondBase = tmppositionNodes[3];
        thirdBase = tmppositionNodes[4];
        shortStop = tmppositionNodes[5];
        leftField = tmppositionNodes[6];
        centerField = tmppositionNodes[7];
        rightField = tmppositionNodes[8];
    }

    private void loadBattingPosition(PlayerNode tmp){
        // setting batting node to a player
        batter.data = tmp;
        getRealm().beginTransaction();
        batter.data.data.addAB();
        getRealm().commitTransaction();
        batter.btn.setText(batter.data.data.getFirstName()+" "+batter.data.data.getLastName().charAt(0));
    }

    protected void updateGameInfo(){
        // for displaying the game information
        lblGameInfo.setText("I:"+getInning(inning)+" B:"+balls+" S:"+strikes+" O:"+outs);
        lblAtBat.setText("At Bat: "+batter.data.data.getFirstName()+" "+batter.data.data.getLastName());
        lblScores.setText("Bearcats: "+points+" Opponent: "+opoints);

    }

    protected void checkGameInfo(){
        // checking events regarding game information
        boolean first = true;
        if (balls == 4){
            balls = 0;
            strikes = 0;
            getRealm().beginTransaction();
            batter.data.data.addBB();
            pitcher.data.addBB();
            getRealm().commitTransaction();
            moveBatter(1);
        }
        if (strikes == 3){
            strikes = 0;
            balls = 0;
            getRealm().beginTransaction();
            pitcher.data.addBK();
            batter.data.data.addK();
            getRealm().commitTransaction();
            outs++;
            shiftBatters();
            first = false;
        }
        if (outs == 3){
            inning++;
            outs = 0;
            strikes = 0;
            balls = 0;
            if(first) {
                shiftBatters();
            }
            swapSides();
        }

    }

    private void swapSides(){
        // swapping sides
        if (isHome){
            isHome = false;
        }else{
            isHome = true;
        }
        updateGame();
    }

    private String getInning(int inning){
        // converting integer inning to be displayed as relevant text
        String s = "";
        if (inning%2 == 1){
            s+= "^";
        }else{
            s+= "v";
        }
        s+= String.valueOf(Math.round((inning+2)/2));
        return s;
    }

    private void createOpponent(){

        // creating a linked list of temporary opponents
        for (int i = 0; i <9;i++){
            PlayerNode newPlayer = new PlayerNode();
            Player player = new Player();
            player.setFirstName("Opponent");
            player.setLastName(String.valueOf(i+1));
            player.setPlayerNumber(String.valueOf(i+1));
            newPlayer.data = player;
            newPlayer.order = i+1;
            newPlayer.position = positions[i];
            if (getOpponent() == null){
                setOpponent(newPlayer);
            }else{
                PlayerNode tmp = getOpponent();
                while (tmp.next != null){
                    tmp = tmp.next;
                }
                tmp.next = newPlayer;
            }
        }
    }

    /////////////////////RECEIVING DATA FROM ANOTHER ACTIVITY///////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("ATBAT IS: "+atBat.data.getFirstName());
        System.out.println("OATBAT IS: "+oatBat.data.getFirstName()+" "+oatBat.data.getLastName());
        switch (requestCode){
            case (REQUEST_CODE_GETMESSAGE_PITCH):
                // returning from the pitch activity
                if(resultCode == Activity.RESULT_OK) {
                    push();
                    String[] result = Pitch.getResultKeyMessage(data); //receiving data from the activity
                    getRealm().beginTransaction();
                    pitcher.data.addPIT();
                    getRealm().commitTransaction();
                    // switch case for updating statistics and game events based on input
                    switch (result[1]) {
                        case ("Ground Ball"):
                            getRealm().beginTransaction();
                            pitcher.data.addC();
                            pitcher.data.addGB();
                            getRealm().commitTransaction();
                            break;
                        case ("Line Drive"):
                            getRealm().beginTransaction();
                            pitcher.data.addC();
                            getRealm().commitTransaction();
                            break;
                        case ("Pop Fly"):
                            getRealm().beginTransaction();
                            pitcher.data.addFB();
                            pitcher.data.addC();
                            pitcher.data.addBSF();
                            batter.data.data.addSF();
                            getRealm().commitTransaction();
                            break;
                        case ("Bunt"):
                            getRealm().beginTransaction();
                            pitcher.data.addC();
                            getRealm().commitTransaction();
                            break;
                    }
                    switch (result[0]) {
                        case ("Out"):
                            getRealm().beginTransaction();
                            pitcher.data.addS();
                            getRealm().commitTransaction();
                            outs++;
                            break;
                        case ("Single"):
                            strikes = 0;
                            balls = 0;
                            getRealm().beginTransaction();
                            batter.data.data.addHits();
                            batter.data.data.addTB(1);
                            pitcher.data.addBHR();
                            pitcher.data.addBH();
                            pitcher.data.addS();
                            getRealm().commitTransaction();
                            moveBatter(1);
                            break;
                        case ("Double"):
                            strikes = 0;
                            balls = 0;
                            getRealm().beginTransaction();
                            batter.data.data.addHits();
                            batter.data.data.addTB(2);
                            pitcher.data.addBHR();
                            pitcher.data.addBH();
                            pitcher.data.addS();
                            getRealm().commitTransaction();
                            moveBatter(2);
                            break;
                        case ("Triple"):
                            strikes = 0;
                            balls = 0;
                            getRealm().beginTransaction();
                            batter.data.data.addHits();
                            batter.data.data.addTB(3);
                            pitcher.data.addBHR();
                            pitcher.data.addBH();
                            pitcher.data.addS();
                            getRealm().commitTransaction();
                            moveBatter(3);
                            break;
                        case ("In-The-Park Home Run"):
                            strikes = 0;
                            balls = 0;
                            getRealm().beginTransaction();
                            batter.data.data.addHits();
                            batter.data.data.addTB(4);
                            pitcher.data.addBHR();
                            pitcher.data.addBH();
                            pitcher.data.addS();
                            getRealm().commitTransaction();
                            moveBatter(4);
                            break;
                        case ("Home Run"):
                            strikes = 0;
                            balls = 0;
                            getRealm().beginTransaction();
                            batter.data.data.addHits();
                            batter.data.data.addTB(4);
                            pitcher.data.addBHR();
                            pitcher.data.addBH();
                            pitcher.data.addS();
                            getRealm().commitTransaction();
                            moveBatter(4);
                        case ("Strike"):
                            getRealm().beginTransaction();
                            pitcher.data.addS();
                            getRealm().commitTransaction();
                            strikes++;
                            break;
                        case ("Ball"):
                            balls++;
                            break;
                        case ("Hit By Pitch"):
                            getRealm().beginTransaction();
                            batter.data.data.addBB();
                            pitcher.data.addBB();
                            batter.data.data.addHBP();
                            getRealm().commitTransaction();
                            balls = 4;
                        case ("Catcher Interference"):
                            getRealm().beginTransaction();
                            batter.data.data.addBB();
                            pitcher.data.addBB();
                            getRealm().commitTransaction();
                            balls = 4;
                        case ("Balk"):
                            getRealm().beginTransaction();
                            batter.data.data.addBB();
                            pitcher.data.addBB();
                            getRealm().commitTransaction();
                            balls = 4;
                        case ("Intentional Walk"):
                            getRealm().beginTransaction();
                            batter.data.data.addBB();
                            pitcher.data.addBB();
                            getRealm().commitTransaction();
                            balls = 4;
                        case ("Foul Ball"):
                            getRealm().beginTransaction();
                            pitcher.data.addC();
                            getRealm().commitTransaction();
                            if (strikes < 2) {
                                strikes++;
                            }
                            break;

                    }
                    checkGameInfo();
                    updateGameInfo();
                }
                break;
            case (REQUEST_CODE_SUBPLAYER):
                //  returning from the sub player activity
                if (resultCode == Activity.RESULT_OK){
                    push();
                    String[] result1 = SubPlayer.getResultKeyMessage(data);
                    switchPlayer(result1[0],result1[1],result1[2],result1[3],result1[4],result1[5],result1[6]);
                    loadFieldingPosition(getStarter());
                }
                break;
            case (REQUEST_CODE_RUNNER):
                // returning from runner activity
                if (resultCode == Activity.RESULT_OK){
                    push();
                    String[] result = Runner.getResultKeyMessage(data);
                    BatterNode tmp = new BatterNode();
                    // switch case for which position runner is
                    switch (result[1]){
                        case ("1"):
                            tmp = fbRunner;
                            break;
                        case ("2"):
                            tmp = sbRunner;
                            break;
                        case ("3"):
                            tmp = tbRunner;
                            break;
                    }
                    // switch case for result of the runner
                    switch (result[0]){
                        case ("Out"):
                            outs++;
                            tmp.data = null;
                            tmp.btn.setText("--");
                            break;
                        case ("Safe"):
                            getRealm().beginTransaction();
                            tmp.data.data.addSS();
                            tmp.data.data.addSA();
                            getRealm().commitTransaction();
                            if (!(tmp == tbRunner)) {
                                tmp.next.data = tmp.data;
                                tmp.data = null;
                                tmp.btn.setText("--");


                            }else{
                                if (isHome) {
                                    points++;
                                } else {
                                    opoints++;
                                }
                            }
                            tmp.btn.setText("--");
                            break;
                        case ("SOut"): // stole but out
                            getRealm().beginTransaction();
                            tmp.data.data.addSA();
                            getRealm().commitTransaction();
                            tmp.data = null;
                            tmp.btn.setText("--");
                            break;
                    }
                    updateGameInfo();
                    loadBattingButtons();
                    checkGameInfo();

                }


        }
    }

    /////////////////////RECEIVING DATA FROM ANOTHER ACTIVITY///////////////////

    private void switchPlayer(String fn, String ln, String no, String ofn, String oln, String ono,String isRoster){
        // true = from roster
        // false = from another fielding position
        // fn ln no = information of player to be swapped in
        // ofn oln ono = information of player to be swapped out
        switch (isRoster){
            case ("true"):
                //finding players based on their name
                RosterNode rosterNode = findRosterNode(ofn,oln);
                RosterNode switchedRosterNode = findRosterNode(fn,ln);
                PlayerNode playerNode = findPlayerNode(fn,ln);
                // switching players
                rosterNode.isChecked = true;
                switchedRosterNode.isChecked = false;
                playerNode.data = rosterNode.data;
                break;
            case ("false"):
                //finding players based on their name
                PlayerNode playerNode1 = findPlayerNode(fn,ln);
                PlayerNode playerNode2 = findPlayerNode(ofn,oln);
                // switching players
                Player tmp = playerNode1.data;
                playerNode1.data = playerNode2.data;
                playerNode2.data = tmp;
                break;
        }
        PlayerNode tmp = getStarter();
    }

    private RosterNode findRosterNode(String fn, String ln){
        // method for returning roster node based on name
        RosterNode tmp = getRoster();
        while (!(tmp.data.getFirstName().equals(fn)&&tmp.data.getLastName().equals(ln))){
            tmp = tmp.next;
        }
        return tmp;
    }

    private PlayerNode findPlayerNode(String fn, String ln){
        // method for returning player object based on name
        PlayerNode tmp = getStarter();
        while (!(tmp.data.getFirstName().equals(fn)&&tmp.data.getLastName().equals(ln))){
            tmp = tmp.next;
        }
        return tmp;
    }

    private void loadBattingButtons(){
        // setting the text of a button based on the player object it references
        batter.btn.setText(batter.data.data.getFirstName()+" "+batter.data.data.getLastName().charAt(0));
        if (fbRunner.data != null){
            fbRunner.btn.setText(fbRunner.data.data.getFirstName()+" "+fbRunner.data.data.getLastName().charAt(0));
        }else{
            fbRunner.btn.setText("--");
        }
        if (sbRunner.data != null){
            sbRunner.btn.setText(sbRunner.data.data.getFirstName()+" "+sbRunner.data.data.getLastName().charAt(0));
        }else{
            sbRunner.btn.setText("--");
        }
        if (tbRunner.data != null){
            tbRunner.btn.setText(tbRunner.data.data.getFirstName()+" "+tbRunner.data.data.getLastName().charAt(0));
        }else{
            tbRunner.btn.setText("--");
        }
    }


    protected void moveBatter(int n){
        //For advancing runners
        for (int i =0; i< n;i++) {
            if (tbRunner.data != null) {
                if (!(tbRunner.data.data.getFirstName().equals("Opponent"))) {
                    points++;
                } else {
                    opoints++;
                }
                //add points
                tbRunner.data = null;
                tbRunner.btn.setText("--");

            }
            if (sbRunner.data != null) {
                tbRunner.data = sbRunner.data;
                tbRunner.btn.setText(tbRunner.data.data.getFirstName() + " " + tbRunner.data.data.getLastName().charAt(0));
                tbRunner.btn.setBackgroundColor(Color.GREEN);
                sbRunner.data = null;
                sbRunner.btn.setText("--");
            } else {
                tbRunner.data = null;
                tbRunner.btn.setText("--");

            }
            if (fbRunner.data != null) {
                sbRunner.data = fbRunner.data;
                sbRunner.btn.setText(sbRunner.data.data.getFirstName() + " " + sbRunner.data.data.getLastName().charAt(0));
                sbRunner.btn.setBackgroundColor(Color.GREEN);
                fbRunner.data = null;
                fbRunner.btn.setText("--");
            } else {
                sbRunner.data = null;
                sbRunner.btn.setText("--");
            }
            if (i<1) { //changing the color of the button
                fbRunner.data = batter.data;
                fbRunner.btn.setText(fbRunner.data.data.getFirstName() + " " + fbRunner.data.data.getLastName().charAt(0));
                fbRunner.btn.setBackgroundColor(Color.GREEN);
            }
        }
        shiftBatters();

    }

    private void shiftBatters(){
        // changing batter to the player on deck
        if (batter.data.next != null) {
            batter.data = batter.data.next;
        }else{
            if (batter.data.data.getFirstName().equals("Opponent")){
                batter.data = getOpponent();
            }else{
                batter.data = getStarter();
            }
        }
        if (batter.data.data.getFirstName().equals("Opponent")){
            oatBat = batter.data;
        }else {
            atBat = batter.data;
        }
        getRealm().beginTransaction();
        batter.data.data.addAB();
        pitcher.data.addBAB();
        getRealm().commitTransaction();
        batter.btn.setText(batter.data.data.getFirstName()+" "+batter.data.data.getLastName().charAt(0));

    }
}
