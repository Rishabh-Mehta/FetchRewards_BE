package com.FetchRewards_BE.models;

import org.hibernate.annotations.UpdateTimestamp;

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

    private long transaction_reedemed_id;

    private UUID redeem_request_id;

    private String payer;
    private int awarded_points;
    private int available_points;

    private LocalDateTime timestamp;
    @UpdateTimestamp
    private LocalDateTime updatetimestamp;

    private String status;

    public Transaction() {

    }

    public Transaction(String provider, int points, LocalDateTime timeStamp) {
        super();

        this.payer = provider;
        this.awarded_points = points;
        if(points < 0)
            this.available_points = 0;
        else
            this.available_points = points;
        this.timestamp = timeStamp;

        this.redeem_request_id = null;
    }


    public int getAvailable_points() {
        return available_points;
    }

    public void setAvailable_points(int available_points) {
        this.available_points = available_points;
    }


    public long getId() {
        return id;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
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

    public UUID getRedeem_request_id() {
        return redeem_request_id;
    }

    public void setRedeem_request_id(UUID redeem_request_id) {
        this.redeem_request_id = redeem_request_id;
    }
}
