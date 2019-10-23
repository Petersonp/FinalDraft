package com.example.finaldraft;

import com.example.finaldraft.model.Player;

public class Game {
    PlayerNode[] LineUp;
    RosterNode[] roster;
    Player[] players;

    String[][] stats;

    int atBat;
    int oatBat;
    int batter;
    int fbRunner;
    int sbRunner;
    int tbRunner;

    int strikes;
    int outs;
    int balls;
    int inning;

    Game next;
    Game back;

    boolean isHome;

}
