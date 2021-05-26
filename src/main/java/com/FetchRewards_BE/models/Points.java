package com.FetchRewards_BE.models;

import javax.persistence.Entity;


public class Points {

    private int points;
    private String user;
    public Points(){

    }
    public Points(String user ,int points){
        super();
        this.user = user;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
