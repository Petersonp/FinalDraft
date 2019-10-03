package com.example.finaldraft.model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Player extends RealmObject {
    //Player Info
    private String firstName;
    private String lastName;
    private String playerNumber;
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getPlayerNumber() {
        return playerNumber;
    }
    public void setPlayerNumber(String playerNumber) {
        this.playerNumber = playerNumber;
    }
    //Player Info Methods

    //Batting Stats
    private int hits = 0;
    private int AB = 0;
    private int BB = 0;
    private int HBP = 0;
    private int K = 0;
    private int TB = 0;
    private int SF = 0;
    public void addHits() {
        setHits(hits + 1);
    }
    public String getHits() { return String.valueOf(hits); }
    public void setHits(int hits) {
        this.hits = hits;
    }
    public void addAB() {
        setAB(AB + 1);
    }
    public String getAB() { return String.valueOf(AB); }
    public void setAB(int AB) { this.AB = AB; }
    public void addBB() {
        setBB(BB + 1);
    }
    public String getBB() { return String.valueOf(BB); }
    public void setBB(int BB) { this.BB = BB; }
    public void addHBP() {
        setHBP(HBP + 1);
    }
    public String getHBP() { return String.valueOf(HBP); }
    public void setHBP(int HBP) { this.HBP = HBP; }
    public void addK() {
        setK(K + 1);
    }
    public String getK() { return String.valueOf(K); }
    public void setK(int K) { this.K = K; }
    public void addTB() {
        setTB(TB + 1);
    }
    public String getTB() { return String.valueOf(TB); }
    public void setTB(int TB) { this.TB = TB; }
    public void addSF() {
        setSF(SF + 1);
    }
    public String getSF() { return String.valueOf(SF); }
    public void setSF(int SF) { this.SF = SF; }
    public String getBA() {
        if (AB == 0){
            return "--";
        }
        return String.valueOf((double)(hits/AB));
    }

    public String getOBP() {
        if((AB+BB+ HBP +SF) == 0){
            return "--";
        }
        return String.valueOf((double)((hits+BB+ HBP)/(AB+BB+ HBP +SF)));
    }

    public String getBBK() {
        if (K == 0){
            return "--";
        }
        return String.valueOf((double)(BB/ K));
    }

    public String getOPS() {
        if (AB == 0){
            return "--";
        }
        else if ((AB+BB+ HBP +SF)+(TB /AB)==0){
            return "--";
        }
        return String.valueOf((double)(((hits+BB+ HBP)/(AB+BB+ HBP +SF))+(HBP /AB)));
    }
    //Batting Stats Methods

    //Pitching Stats
    private int BH=0;
    private int BHR=0;
    private int BAB=0;
    private int BK=0;
    private int BSF=0;
    private int GB=0;
    private int PIT=0;
    private int FB=0;
    private int BBP=0;
    private int S=0;
    private int C=0;
    public String getBH() {
        return String.valueOf(BH);
    }
    public void addBH() {
        setBH(BH + 1);
    }
    public void setBH(int BH) {
        this.BH = BH;
    }
    public String getBHR() {
        return String.valueOf(BHR);
    }
    public void addBHR() {
        setBHR(BHR + 1);
    }
    public void setBHR(int BHR) {
        this.BHR = BHR;
    }
    public String getBAB() {
        return String.valueOf(BAB);
    }
    public void addBAB() {
        setBAB(BAB + 1);
    }
    public void setBAB(int BAB) {
        this.BAB = BAB;
    }
    public String getBK() {
        return String.valueOf(BK);
    }
    public void addBK() {
        setBK(BK + 1);
    }
    public void setBK(int BK) {
        this.BK = BK;
    }
    public String getBSF() {
        return String.valueOf(BSF);
    }
    public void addBSF() {
        setBSF(BSF + 1);
    }
    public void setBSF(int BSF) {
        this.BSF = BSF;
    }
    public String getGB() {
        return String.valueOf(GB);
    }
    public void addGB() {
        setGB(GB + 1);
    }
    public void setGB(int GB) {
        this.GB = GB;
    }
    public String getPIT() {
        return String.valueOf(PIT);
    }
    public void addPIT() {
        setPIT(PIT + 1);
    }
    public void setPIT(int PIT) {
        this.PIT = PIT;
    }
    public String getFB() {
        return String.valueOf(FB);
    }
    public void addFB() {
        setFB(FB + 1);
    }
    public void setFB(int FB) {
        this.FB = FB;
    }
    public String getBBP() {
        return String.valueOf(BBP);
    }
    public void addBBP() {
        setBBP(BBP + 1);
    }
    public void setBBP(int BBP) {
        this.BBP = BBP;
    }
    public String getS() {
        return String.valueOf(S);
    }
    public void addS() {
        setS(S + 1);
    }
    public void setS(int s) {
        S = s;
    }
    public String getC() {
        return String.valueOf(C);
    }
    public void addC() {
        setC(C + 1);
    }
    public void setC(int c) {
        C = c;
    }
    public String getBABIP() {
        if ((BAB - BK - BHR + BSF) == 0){
            return "--";
        }
        return String.valueOf((double)((BH - BHR)/(BAB - BK - BHR + BSF)));
    }

    public String getGF() {
        if (FB == 0){
            return "--";
        }
        return String.valueOf((double)(GB / FB));
    }

    public String getKBB() {
        if (BB == 0){
            return "--";
        }
        return String.valueOf((double)(BK / BB));
    }

    public String getSP() {
        if (PIT == 0){
            return "--";
        }
        System.out.println("PIT IS: "+PIT+" S IS: "+S);
        System.out.println("S/PIT IS: "+((double)(S/PIT)));
        return String.valueOf((double)S/PIT);
    }

    public String getCP() {
        if (PIT == 0){
            return "--";
        }
        return String.valueOf((double)(C /PIT));
    }
    //Pitching Stats Methods

    //Running Stats
    private int SA = 0;
    private int SS = 0;
    public String getSS(){
        return String.valueOf(SS);
    }
    public void addSS(){
        setSS(SS + 1);
    }
    public void setSS(int SS) { this.SS = SS; }
    public String getSA(){
        return String.valueOf(SA);
    }
    public void addSA(){
        setSA(SA + 1);
    }
    public void setSA(int SA) { this.SA = SA; }
    public String getRSP(){
        if (SA == 0){
            return "--";
        }
        return String.valueOf((double)(SA/SS));
    }

    public String[] getPitchingStats(){
        return (new String[] {getFirstName(),getBA(),getOBP(),getBBK(),getOPS()});
    }
    public String[] getBattingStats(){
        return (new String[] {getFirstName(),getBABIP(),getGF(),getKBB(),getCP()});
    }
    public String[] getRunningStats() {
        return (new String[]{getFirstName(), getRSP(), getSA(), getSS()});
    }


}
