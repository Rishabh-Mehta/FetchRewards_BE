package com.FetchRewards_BE.models;

import javax.persistence.*;

@Entity
@Table(name="Payer")
@SequenceGenerator(name = "PAYER_SEQ")
public class Payer {
    @Id
    @GeneratedValue(generator="PAYER_SEQ")
    private long id;
    @Column(unique = true)
    private String payer;
    private int points;



    public Payer(){

    }
    public Payer(String user,int points){
        super();
        this.points = points;
        this.payer =user;

    }
    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }


    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
    public long getId(){
        return id;
    }


}
