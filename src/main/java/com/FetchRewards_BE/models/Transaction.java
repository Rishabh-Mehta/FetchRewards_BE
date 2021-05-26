package com.FetchRewards_BE.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.id.uuid.*;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Transaction")
@SequenceGenerator(name = "TRANSACTION_SEQ")
public class Transaction {

    @Id
    @GeneratedValue(generator = "TRANSACTION_SEQ")
    private long id;

    private long transaction_reedemed_id ;

    private UUID reedem_request_id;
    private String user;
    private String provider;
    private int awarded_points;
    private int available_points;
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
        this.available_points = points;
        this.awarded_points = points;

        this.reedem_request_id = null;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getAvailable_points() {
        return available_points;
    }

    public void setAvailable_points(int available_points) {
        this.available_points = available_points;
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

    public int getAwarded_points() {
        return awarded_points;
    }

    public void setAwarded_points(int awarded_points) {
        this.awarded_points = awarded_points;
    }



    public long getTransaction_reedemed_id() {
        return transaction_reedemed_id;
    }

    public void setTransaction_reedemed_id(long transaction_reedemed_id) {
        this.transaction_reedemed_id = transaction_reedemed_id;
    }

    public UUID getReedem_request_id() {
        return reedem_request_id;
    }

    public void setReedem_request_id(UUID reedem_request_id) {
        this.reedem_request_id = reedem_request_id;
    }
}
