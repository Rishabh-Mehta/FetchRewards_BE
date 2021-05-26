package com.FetchRewards_BE.service;

import com.FetchRewards_BE.models.Payer;
import com.FetchRewards_BE.models.Points;
import com.FetchRewards_BE.models.Transaction;
import com.FetchRewards_BE.repository.PayerRepository;
import com.FetchRewards_BE.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.PriorityQueue;

@Service
public class FetchRewardsServiceImpl implements FetchRewardsService{


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

        if(!payerRepository.findByUser(transaction.getUser()).isEmpty()){
            Payer p = payerRepository.findByUser(transaction.getUser()).get(0);
            payerRepository.addPoints(transaction.getPoints(),p.getId());
        }
        else{
            payerRepository.save(new Payer(transaction.getUser(),transaction.getPoints()));
        }
        return transactionRepository.save(transaction);
    }

    @Override
    public void Reedem(Points points) {
        PriorityQueue<Transaction> queue = new PriorityQueue<>((a,b) -> a.getTimeStamp().compareTo(b.getTimeStamp()));
        queue.addAll(transactionRepository.findByUser(points.getUser()));

        int pointsToReedem = points.getPoints();
        if(payerRepository.findByUser(points.getUser()).get(0).getPoints() >= pointsToReedem){
            while(!queue.isEmpty() || pointsToReedem !=0){
                Transaction t = queue.peek();
                if( t.getStatus() == "AVAILABLE"){
                    if(t.getPoints()< pointsToReedem){
                        pointsToReedem -= queue.peek().getPoints();
                        transactionRepository.updateTransactionById(t.getId(),t.getPoints(),"PROCESSED");


                        queue.poll();

                    }

                    else{
                        int remainingPoints = queue.peek().getPoints() - pointsToReedem;
                        transactionRepository.updateTransactionById(queue.poll().getId(),remainingPoints,"AVAILABLE");
                        pointsToReedem=0;

                        break;
                    }
                }
                else {

                    queue.poll();

                }

            }
            //Create a Transaction to show points REEDEMED

            Transaction Reedem = new Transaction(points.getUser(),"REEDEM_POINTS",-points.getPoints()+pointsToReedem);
            Reedem.setStatus("REEDEM");
            transactionRepository.save(Reedem);


            //  UPDATE Payer points
            Payer p = payerRepository.findByUser(points.getUser()).get(0);
            p.setPoints(p.getPoints()-(points.getPoints()-pointsToReedem));
            payerRepository.save(p);



        }
        //return payerRepository.ReedemTransactionResult(points.getUser());
    }
}
