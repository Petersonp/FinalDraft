package com.example.finaldraft;

import com.example.finaldraft.model.Player;

public class RosterNode {
    Player data;  // player object
    RosterNode next; // next node
    int index; // index in the linked list
    boolean isChecked; // whether or not they were selected for lineup
    int order; // batting order
    String position; // fielding position
}
