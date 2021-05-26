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
    public List<Payer> getAllPlayerBalance() {
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
        if(!payerRepository.findByUser(transaction.getUser()).isEmpty()){
            Payer p = payerRepository.findByUser(transaction.getUser()).get(0);
            payerRepository.addPoints(transaction.getAwarded_points(),p.getId());
        }
        else{
            payerRepository.save(new Payer(transaction.getUser(),transaction.getAwarded_points()));
        }
        return transactionRepository.save(transaction);
    }

    @Override
    public void Reedem(Points points) {
        //Build a priority queue sorted with timestamp
        PriorityQueue<Transaction> queue = new PriorityQueue<>((a,b) -> a.getTimeStamp().compareTo(b.getTimeStamp()));
        queue.addAll(transactionRepository.findByUser(points.getUser()));

        UUID reedem_request_id = UUID.randomUUID();
        int pointsToReedem = points.getPoints();
        if(payerRepository.findByUser(points.getUser()).get(0).getPoints() >= pointsToReedem){
            while(!queue.isEmpty() || pointsToReedem !=0){
                Transaction t = queue.peek();
                if( t.getStatus() == "AVAILABLE"){
                    if(t.getAvailable_points()< pointsToReedem){
                        // If Earliest transaction points are less than points to be reedemed
                        pointsToReedem -= t.getAvailable_points();

                        //Mark the transaction processed
                        transactionRepository.updateTransactionById(t.getId(),0,"PROCESSED");

                        //Create a Transaction to show points REEDEMED
                        Transaction Reedem = new Transaction(points.getUser(),"REEDEM_"+t.getProvider(),-t.getAvailable_points());
                        Reedem.setTransaction_reedemed_id(t.getId());
                        Reedem.setReedem_request_id(reedem_request_id);
                        Reedem.setStatus("REEDEM");
                        transactionRepository.save(Reedem);

                        queue.poll();

                    }

                    else{
                        int remainingPoints = t.getAvailable_points() - pointsToReedem;
                        transactionRepository.updateTransactionById(queue.poll().getId(),remainingPoints,"AVAILABLE");
                        pointsToReedem=0;

                        //Create a Transaction to show points REEDEMED
                        Transaction Reedem = new Transaction(points.getUser(),"REEDEM_"+t.getProvider(),-remainingPoints);
                        Reedem.setTransaction_reedemed_id(t.getId());
                        Reedem.setReedem_request_id(reedem_request_id);
                        Reedem.setStatus("REEDEM");
                        transactionRepository.save(Reedem);
                        queue.poll();
                        break;
                    }
                }
                else {
                    //Ignore all transactions whose status does not match
                    queue.poll();

                }

            }

            //  UPDATE Payer points
            Payer p = payerRepository.findByUser(points.getUser()).get(0);
            p.setPoints(p.getPoints()-(points.getPoints()-pointsToReedem));
            payerRepository.save(p);



        }
        else{
            //No transaction Satisfies the points to reedem
            //Create a failed reedem request
            Transaction Reedem = new Transaction(points.getUser(),"REEDEM_POINTS",-points.getPoints());
            Reedem.setStatus("FAILED_INSUFFICIENT_POINTS");
            transactionRepository.save(Reedem);

        }

    }
}
