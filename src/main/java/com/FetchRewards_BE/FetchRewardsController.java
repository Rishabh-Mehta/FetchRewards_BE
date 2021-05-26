package com.FetchRewards_BE;

import com.FetchRewards_BE.models.Payer;
import com.FetchRewards_BE.models.Points;
import com.FetchRewards_BE.models.Transaction;
import com.FetchRewards_BE.service.FetchRewardsService;
import com.FetchRewards_BE.service.FetchRewardsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FetchRewardsController {

    @Autowired
    private FetchRewardsServiceImpl fetchRewardsService;
    @RequestMapping("/")
    public String Welcome(){
        return "Welcome";
    }
    @GetMapping("/payer/balances")
    List<Payer> getAllPlayerBalance(){

        return fetchRewardsService.getAllPlayerBalance();
    }
    @GetMapping("/transactions/log")
    List<Transaction> getAllTransactions(){
        return fetchRewardsService.getAllTransactions();
    }



    @PostMapping(path = "/payer/transaction/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    Transaction RecordTransaction(@RequestBody Transaction transaction){
        return fetchRewardsService.addPoints(transaction);
    }

    @PostMapping(path = "/payer/transaction/reedem", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    void ReedemPoints(@RequestBody Points points){
        fetchRewardsService.Reedem(points);

    }



}
