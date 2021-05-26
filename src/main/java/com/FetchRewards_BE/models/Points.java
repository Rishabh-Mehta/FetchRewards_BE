package com.FetchRewards_BE.models;


public class Points {

    private int points;
    private String payer;
    public Points(){

    }
    public Points(String user ,int points){
        super();
        this.payer = user;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }
}
