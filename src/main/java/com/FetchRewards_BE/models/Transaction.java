package com.FetchRewards_BE.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Transaction")
@SequenceGenerator(name = "TRANSACTION_SEQ")
public class Transaction {

    @Id
    @GeneratedValue(generator = "TRANSACTION_SEQ")
    private long id;
    private String user;
    private String provider;
    private int points;
    @CreationTimestamp
    private LocalDateTime timeStamp;
    @UpdateTimestamp
    private LocalDateTime updatetimestamp;

    private String status;

    public Transaction(){

    }
    public Transaction(String user,String provider, int points ){
        super();
        this.user = user;
        this.provider = provider;
        this.points = points;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }


    public long getId(){
        return  id;
    }



    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
