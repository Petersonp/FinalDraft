package com.example.finaldraft;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.finaldraft.model.Player;

import org.w3c.dom.Text;

public class GameActivity extends StartingWindow {
    public static final int REQUEST_CODE_GETMESSAGE_PITCH = 6;
    public static final int REQUEST_CODE_SUBPLAYER = 19;

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

    boolean isHome;

    int strikes;
    int balls;
    int inning;
    int outs;
    int points =0;
    int opoints =0;

    private BatterNode batter = new BatterNode();
    private BatterNode fbRunner = new BatterNode();
    private BatterNode sbRunner = new BatterNode();
    private BatterNode tbRunner = new BatterNode();
    private BatterNode hbRunner = new BatterNode();

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

    private PlayerNode opponent;
    private PlayerNode atBat;
    private PlayerNode oatBat;
    public PlayerNode getOpponent() {
        return opponent;
    }

    Game front;
    Game base;

    public void setOpponent(PlayerNode opponent) {
        this.opponent = opponent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameactivity_layout);
        printRoster("Start of gameactivity");
        boolean[] result = getIntent().getExtras().getBooleanArray("start");
        isHome = result[0];


        //views
        btnPitcher = (Button) findViewById(R.id.btnPitcher);positionButtons[0] = btnPitcher;
        btnCatcher = (Button) findViewById(R.id.btnCatcher);positionButtons[1] = btnCatcher;
        btnFirstBase = (Button) findViewById(R.id.btnFirstBase);positionButtons[2] = btnFirstBase;
        btnSecondBase = (Button) findViewById(R.id.btnSecondBase);positionButtons[3] = btnSecondBase;
        btnThirdBase = (Button) findViewById(R.id.btnThirdBase);positionButtons[4] = btnThirdBase;
        btnShortStop = (Button) findViewById(R.id.btnShortStop);positionButtons[5] = btnShortStop;
        btnLeftField = (Button) findViewById(R.id.btnLeftField);positionButtons[6] = btnLeftField;
        btnCenterField = (Button) findViewById(R.id.btnCenterField);positionButtons[7] = btnCenterField;
        btnRightField = (Button) findViewById(R.id.btnRightField);positionButtons[8] = btnRightField;

        btnPitch = (Button) findViewById(R.id.btnPitch);
        btnUndo = (Button) findViewById(R.id.btnUndo);
        btnStats = (Button) findViewById(R.id.btnStats);
        btnViewTeam = (Button) findViewById(R.id.btnViewTeam);

        lblGameInfo = (TextView) findViewById(R.id.lblGameInfo);
        lblAtBat = (TextView) findViewById(R.id.lblAtBat);
        lblScores = (TextView) findViewById(R.id.lblScores);

        btnFirstRunner = (Button) findViewById(R.id.btnFirstRunner);
        btnSecondRunner = (Button) findViewById(R.id.btnSecondRunner);
        btnThirdRunner = (Button) findViewById(R.id.btnThirdRunner);
        btnBatter = (Button) findViewById(R.id.btnBatter);
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
                //printLineUp();
                Intent i = new Intent(getApplicationContext(),Pitch.class);
                startActivityForResult(i,REQUEST_CODE_GETMESSAGE_PITCH);
            }
        });
        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Stats.class);
                startActivity(i);
            }
        });
        btnViewTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Roster.class);
                startActivity(i);
            }
        });

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
            public void onClick(View v) {
                substituteActivity(secondBase);
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
            public void onClick(View v) {
                substituteActivity(rightField);
            }
        });

        btnCenterField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(centerField);
            }
        });

        btnLeftField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                substituteActivity(leftField);
            }
        });


        //start
        createOpponent();
        atBat = getStarter();
        oatBat = getOpponent();
        updateGame();
        updateGameInfo();
        printRoster("GameActivity");
        printLineUp("GameActivity");
        push();


    }

    private void push(){
        Game game = newGame();
        if(front == null){
            front = game;
            base = game;
        }
        else{
            if(isEmpty()){
                base.next = game;
                game.back = base;
            }
            game.back = front;
            front.next = game;
            front = game;
        }

    }

    private void pop(){
        Game g;
        if (!isEmpty()){
            g = front;
            front = front.back;
        }else{
            g = front;
        }
        setGame(g);
    }

    private void setGame(Game g){
        //Lineup
        PlayerNode tmp = g.LineUp[0];
        for (int i =0; i< 9;i++){
            if (i == 0){
                tmp = g.LineUp[i];
                //tmp.data = g.Players[i];
                setStarter(tmp);
                tmp = getStarter();
            }else{
                PlayerNode newPlayer = g.LineUp[i];
                //newPlayer.data = g.Players[i];
                tmp.next = newPlayer;
                tmp = tmp.next;
            }
        }
        System.out.println("THIS IS IN SETGAME: "+getStarter().data.getFirstName());

        //Roster
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


        //Batters/RUnners
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

        //Game info
        strikes = g.strikes;
        balls = g.balls;
        inning = g.inning;
        outs = g.outs;

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
            System.out.println(newPlayer.getPIT()+" "+j);
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
        PlayerNode tmp = getOpponent();
        int count = 0;
        while (!(tmp.data.getFirstName().equals(playerNode.data.getFirstName())&&tmp.data.getLastName().equals(playerNode.data.getLastName())&&tmp.data.getPlayerNumber().equals(playerNode.data.getPlayerNumber()))){
            count++;
            tmp = tmp.next;
        }
        return count;
    }

    private void substituteActivity(PlayerNode player){
        Intent i = new Intent(getApplicationContext(),SubPlayer.class);
        Bundle bundle = new Bundle();
        bundle.putStringArray("Position",new String[]{player.data.getFirstName(),player.data.getLastName(),player.position});
        i.putExtras(bundle);
        startActivityForResult(i, REQUEST_CODE_SUBPLAYER);

    }


    private void updateGame(){
        if (isHome){
            loadFieldingPosition(getOpponent());
            loadBattingPosition(atBat);
        }else{
            loadFieldingPosition(getStarter());
            loadBattingPosition(oatBat);
        }
    }

    private void loadFieldingPosition(PlayerNode tmpHead){
        PlayerNode tmp = tmpHead;
        for (int i = 0; i < positions.length;i++){
            if(tmp.position!=null) {
                if (tmp.position.equals(positions[i])) {
                    positionNodes[i] = tmp;
                    break;
                }
            }else{
                System.out.println(tmp.data.getFirstName()+" "+tmp.data.getLastName()+" has no position");
            }
        }
        while (tmp.next != null){
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
        for (int i = 0; i < positionButtons.length;i++){
            positionButtons[i].setText(positionNodes[i].data.getFirstName().toString()+" "+positionNodes[i].data.getLastName().charAt(0));
        }
    }

    protected void updateNodes(PlayerNode[] tmppositionNodes){
        pitcher = tmppositionNodes[0];
        //System.out.println(pitcher.data.getFirstName()+" "+pitcher.data.getLastName()+" pitcher");
        catcher = tmppositionNodes[1];
        //System.out.println(catcher.data.getFirstName()+" "+catcher.data.getLastName()+" catcher");
        firstBase = tmppositionNodes[2];
        //System.out.println(firstBase.data.getFirstName()+" "+firstBase.data.getLastName()+" firstBase");
        secondBase = tmppositionNodes[3];
        //System.out.println(secondBase.data.getFirstName()+" "+secondBase.data.getLastName()+" secondBase");
        thirdBase = tmppositionNodes[4];
        //System.out.println(thirdBase.data.getFirstName()+" "+thirdBase.data.getLastName()+" thirdBase");
        shortStop = tmppositionNodes[5];
        //System.out.println(shortStop.data.getFirstName()+" "+shortStop.data.getLastName()+" shortStop");
        leftField = tmppositionNodes[6];
        //System.out.println(leftField.data.getFirstName()+" "+leftField.data.getLastName()+" leftField");
        centerField = tmppositionNodes[7];
        //System.out.println(centerField.data.getFirstName()+" "+centerField.data.getLastName()+" centerField");
        rightField = tmppositionNodes[8];
        //System.out.println(rightField.data.getFirstName()+" "+rightField.data.getLastName()+" rightField");
    }

    private void loadBattingPosition(PlayerNode tmp){
        batter.data = tmp;
        //batter.data.addAB();
        batter.btn.setText(batter.data.data.getFirstName()+" "+batter.data.data.getLastName().charAt(0));
    }

    protected void updateGameInfo(){
        lblGameInfo.setText("I:"+getInning(inning)+" B:"+balls+" S:"+strikes+" O:"+outs);
        lblAtBat.setText("At Bat: "+batter.data.data.getFirstName()+" "+batter.data.data.getLastName());
        lblScores.setText("Bearcats: "+points+" Opponent: "+opoints);

    }

    protected void checkGameInfo(){
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
            System.out.println("Added K to batter");
            getRealm().commitTransaction();
            outs++;
            shiftBatters();
        }
        if (outs == 3){
            inning++;
            outs = 0;
            strikes = 0;
            balls = 0;
            shiftBatters();
            //swapSides();
        }

    }

    private String getInning(int inning){
        String s = "";
        if (inning%2 == 1){
            s+= "^";
        }else{
            s+= "v";
        }
        s+= String.valueOf(Math.round(inning/2));
        return s;
    }

    private void createOpponent(){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case (REQUEST_CODE_GETMESSAGE_PITCH):
                if(resultCode == Activity.RESULT_OK) {
                    push();
                    String[] result = Pitch.getResultKeyMessage(data);
                    System.out.println(result[0] + " IS RESULT[0]");
                    System.out.println(result[1] + " IS RESULT[1]");
                    System.out.println(pitcher.data.getFirstName() + " " + pitcher.data.getLastName() + " IS THE PITCHER ----------------------");
                    getRealm().beginTransaction();
                    pitcher.data.addPIT();
                    getRealm().commitTransaction();
                    switch (result[1]) {
                        case ("Ground Ball"):
                            System.out.println("GROUND BALL IS THE RESULT");
                            getRealm().beginTransaction();
                            pitcher.data.addC();
                            pitcher.data.addGB();
                            getRealm().commitTransaction();
                            break;
                        case ("Line Drive"):
                            System.out.println("LINE DRIVE IS THE RESULT");
                            getRealm().beginTransaction();
                            pitcher.data.addC();
                            getRealm().commitTransaction();
                            break;
                        case ("Pop Fly"):
                            System.out.println("POP FLY IS THE RESULT");
                            getRealm().beginTransaction();
                            pitcher.data.addFB();
                            pitcher.data.addC();
                            pitcher.data.addBSF();
                            batter.data.data.addSF();
                            getRealm().commitTransaction();
                            break;
                        case ("Bunt"):
                            System.out.println("BUNT IS THE RESULT");
                            getRealm().beginTransaction();
                            pitcher.data.addC();
                            getRealm().commitTransaction();
                            break;
                    }
                    switch (result[0]) {
                        case ("Out"):
                            System.out.println("OUT IS THE RESULT");
                            getRealm().beginTransaction();
                            pitcher.data.addS();
                            getRealm().commitTransaction();
                            outs++;
                            break;
                        case ("Single"):
                            System.out.println("SINGLE IS THE RESULT");
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
                            System.out.println("DOUBLE IS THE RESULT");
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
                            System.out.println("TRIPLE IS THE RESULT");
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
                            System.out.println("IN THE PARK HOME RUN IS THE RESULT");
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
                            System.out.println("HOME RUN IS THE RESULT");
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
                            System.out.println("STRIKE IS THE RESULT");
                            getRealm().beginTransaction();
                            pitcher.data.addS();
                            getRealm().commitTransaction();
                            strikes++;
                            break;
                        case ("Ball"):
                            System.out.println("BALL IS THE RESULT");
                            balls++;
                            break;
                        case ("Hit By Pitch"):
                            System.out.println("HIT BY PITCH IS THE RESULT");
                            getRealm().beginTransaction();
                            batter.data.data.addBB();
                            pitcher.data.addBB();
                            batter.data.data.addHBP();
                            getRealm().commitTransaction();
                            balls = 4;
                        case ("Catcher Interference"):
                            System.out.println("CATCHER INTERFERENCE IS THE RESULT");
                            getRealm().beginTransaction();
                            batter.data.data.addBB();
                            pitcher.data.addBB();
                            getRealm().commitTransaction();
                            balls = 4;
                        case ("Balk"):
                            System.out.println("BALK IS THE RESULT");
                            getRealm().beginTransaction();
                            batter.data.data.addBB();
                            pitcher.data.addBB();
                            getRealm().commitTransaction();
                            balls = 4;
                        case ("Intentional Walk"):
                            System.out.println("INTENTIONAL WALK IS THE RESULT");
                            getRealm().beginTransaction();
                            batter.data.data.addBB();
                            pitcher.data.addBB();
                            getRealm().commitTransaction();
                            balls = 4;
                        case ("Foul Ball"):
                            System.out.println("FOUL BALL IS THE RESULT");
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
                if (resultCode == Activity.RESULT_OK){
                    push();
                    String[] result1 = SubPlayer.getResultKeyMessage(data);
                    printRoster("PRE SWITCH");
                    printLineUp("PRE SWITCH");
                    switchPlayer(result1[0],result1[1],result1[2],result1[3],result1[4],result1[5],result1[6]);
                    printRoster("POST SWITCH");
                    printLineUp("POST SWITCH");
                    loadFieldingPosition(getStarter());
                }
                break;

        }
    }

    private void switchPlayer(String fn, String ln, String no, String ofn, String oln, String ono,String isRoster){
        System.out.println(fn+" "+ln+" "+no+" "+ofn+" "+oln+" "+ono+" "+isRoster);
        switch (isRoster){
            case ("true"):
                RosterNode rosterNode = findRosterNode(ofn,oln);
                RosterNode switchedRosterNode = findRosterNode(fn,ln);
                PlayerNode playerNode = findPlayerNode(fn,ln);
                rosterNode.isChecked = true;
                switchedRosterNode.isChecked = false;
                playerNode.data = rosterNode.data;
                break;
            case ("false"):
                PlayerNode playerNode1 = findPlayerNode(fn,ln);
                PlayerNode playerNode2 = findPlayerNode(ofn,oln);
                Player tmp = playerNode1.data;
                playerNode1.data = playerNode2.data;
                playerNode2.data = tmp;
                break;
        }
        PlayerNode tmp = getStarter();
    }

    private RosterNode findRosterNode(String fn, String ln){
        RosterNode tmp = getRoster();
        while (!(tmp.data.getFirstName().equals(fn)&&tmp.data.getLastName().equals(ln))){
            tmp = tmp.next;
        }
        return tmp;
    }

    private PlayerNode findPlayerNode(String fn, String ln){
        PlayerNode tmp = getStarter();
        while (!(tmp.data.getFirstName().equals(fn)&&tmp.data.getLastName().equals(ln))){
            tmp = tmp.next;
        }
        return tmp;
    }

    private void loadBattingButtons(){
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
        //printBatters();

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
                //System.out.println(tbRunner.data.data.getFirstName() + " Third Base Before");
                tbRunner.data = sbRunner.data;
                System.out.println(tbRunner.data.data.getFirstName() + " Third Base After");
                tbRunner.btn.setText(tbRunner.data.data.getFirstName() + " " + tbRunner.data.data.getLastName().charAt(0));
                tbRunner.btn.setBackgroundColor(Color.GREEN);
                sbRunner.data = null;
                sbRunner.btn.setText("--");
            } else {
                System.out.println("Third Base else");
                tbRunner.data = null;
                tbRunner.btn.setText("--");

            }
            if (fbRunner.data != null) {
                //System.out.println(sbRunner.data.data.getFirstName() + " Second Base Before");
                sbRunner.data = fbRunner.data;
                System.out.println(sbRunner.data.data.getFirstName() + " Second Base After");
                sbRunner.btn.setText(sbRunner.data.data.getFirstName() + " " + sbRunner.data.data.getLastName().charAt(0));
                sbRunner.btn.setBackgroundColor(Color.GREEN);
                fbRunner.data = null;
                fbRunner.btn.setText("--");
            } else {
                System.out.println("Second Base else");
                sbRunner.data = null;
                sbRunner.btn.setText("--");
            }
            if (i<1) {
                fbRunner.data = batter.data;
                fbRunner.btn.setText(fbRunner.data.data.getFirstName() + " " + fbRunner.data.data.getLastName().charAt(0));
                fbRunner.btn.setBackgroundColor(Color.GREEN);
            }
        }
        shiftBatters();

    }

    private void shiftBatters(){
        System.out.println("Shiftting batters");
        batter.data = batter.data.next;
        System.out.println("Batter is: "+batter.data.data.getFirstName());
        atBat = batter.data;
        getRealm().beginTransaction();
        batter.data.data.addAB();
        pitcher.data.addBAB();
        getRealm().commitTransaction();
        batter.btn.setText(batter.data.data.getFirstName()+" "+batter.data.data.getLastName().charAt(0));

    }
}
