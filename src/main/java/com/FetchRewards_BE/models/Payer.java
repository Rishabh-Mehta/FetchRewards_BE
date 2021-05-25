package com.FetchRewards_BE.models;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Payer {
    @Id
    @GeneratedValue
    private long id;
    private int points;

    public Payer(){

    }
    public Payer(int points){
        super();
        this.points = points;

    }

}
