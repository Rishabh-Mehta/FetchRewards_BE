package com.FetchRewards_BE.models;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.PriorityQueue;

@Entity
@Table(name="Payer")
public class Payer {
    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true)
    private String user;
    private int points;


    public Payer(){

    }
    public Payer(String user,int points){
        super();
        this.points = points;
        this.user=user;
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
