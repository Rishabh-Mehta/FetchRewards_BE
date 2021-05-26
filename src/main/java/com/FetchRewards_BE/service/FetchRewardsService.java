package com.FetchRewards_BE.service;

import com.FetchRewards_BE.models.Payer;
import com.FetchRewards_BE.models.Points;
import com.FetchRewards_BE.models.Transaction;

import java.util.List;

public interface FetchRewardsService {

    List<Payer> getAllPayerBalance();

    List<Transaction> getAllTransactions();

    Transaction addPoints(Transaction transaction);


    List<Transaction> Reedem(Points points);
}
