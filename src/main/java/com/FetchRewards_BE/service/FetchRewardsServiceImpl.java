package com.FetchRewards_BE.service;

import com.FetchRewards_BE.models.Payer;
import com.FetchRewards_BE.models.Points;
import com.FetchRewards_BE.models.Transaction;
import com.FetchRewards_BE.repository.PayerRepository;
import com.FetchRewards_BE.repository.TransactionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.PriorityQueue;
import java.util.UUID;

@Service
public class FetchRewardsServiceImpl implements FetchRewardsService{

    private static final Logger logger = LoggerFactory.getLogger(FetchRewardsServiceImpl.class);
    @Autowired
    PayerRepository payerRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public List<Payer> getAllPayerBalance() {
        return payerRepository.findAll();
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction addPoints(Transaction transaction) {
        transaction.setStatus("AVAILABLE");
        transaction.setAvailable_points(transaction.getAwarded_points());
        if(!payerRepository.findByPayer(transaction.getPayer()).isEmpty()){
            Payer p = payerRepository.findByPayer(transaction.getPayer()).get(0);
            payerRepository.addPoints(transaction.getAwarded_points(),p.getId());
        }
        else{
            payerRepository.save(new Payer(transaction.getPayer(),transaction.getAwarded_points()));
        }
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> Reedem(Points points) {
        //Build a priority queue sorted with timestamp
        PriorityQueue<Transaction> queue = new PriorityQueue<>((a,b) -> a.getTimestamp().compareTo(b.getTimestamp()));
        queue.addAll(transactionRepository.findAll());

        UUID redeem_request_id = UUID.randomUUID();
        int pointsToRedeem = points.getPoints();
        if(payerRepository.findAvailableBalance() >= pointsToRedeem){
            while(!queue.isEmpty() || pointsToRedeem !=0){
                Transaction t = queue.peek();

                if( t.getStatus() == "AVAILABLE"){
                    if(t.getAvailable_points()<= pointsToRedeem){
                        // If Earliest transaction points are less than points to be reedemed
                        pointsToRedeem -= t.getAvailable_points();

                        //Mark the transaction processed
                        transactionRepository.updateTransactionById(t.getId(),0,"PROCESSED");

                        //Create a Transaction to show points REDEEMED
                        Transaction Redeem = new Transaction(t.getPayer(),-t.getAvailable_points(), LocalDateTime.now());
                        Redeem.setTransaction_reedemed_id(t.getId());
                        Redeem.setRedeem_request_id(redeem_request_id);
                        Redeem.setStatus("REDEEM");
                        transactionRepository.save(Redeem);

                        //  UPDATE Payer points
                        Payer p = payerRepository.findByPayer(t.getPayer()).get(0);

                        p.setPoints(p.getPoints()-t.getAvailable_points());

                        payerRepository.save(p);

                        queue.poll();

                    }

                    else{

                        int remainingPoints = t.getAvailable_points() - pointsToRedeem;
                        transactionRepository.updateTransactionById(queue.poll().getId(),remainingPoints,"AVAILABLE");


                        //Create a Transaction to show points REDEEMED
                        Transaction Redeem = new Transaction(t.getPayer(),-pointsToRedeem, LocalDateTime.now());
                        Redeem.setTransaction_reedemed_id(t.getId());
                        Redeem.setRedeem_request_id(redeem_request_id);
                        Redeem.setStatus("REDEEM");
                        transactionRepository.save(Redeem);
                        queue.poll();

                        //  UPDATE Payer points
                        Payer p = payerRepository.findByPayer(t.getPayer()).get(0);

                        p.setPoints(p.getPoints()-pointsToRedeem);

                        payerRepository.save(p);
                        pointsToRedeem=0;
                        break;
                    }
                }
                else {
                    //Ignore all transactions whose status does not match
                    queue.poll();

                }

            }





        }
        else{
            //No transaction Satisfies the points to reedem
            //Create a failed reedem request
            Transaction Redeem = new Transaction("FAILED_INSUFFICIENT_POINTS",-points.getPoints(), LocalDateTime.now());
            Redeem.setRedeem_request_id(redeem_request_id);
            Redeem.setStatus("FAIL");
            transactionRepository.save(Redeem);

        }
        return transactionRepository.findByRedeem_request_id(redeem_request_id);
    }
}
