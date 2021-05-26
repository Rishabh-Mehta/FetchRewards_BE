package com.FetchRewards_BE.repository;

import com.FetchRewards_BE.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;


public interface TransactionRepository extends JpaRepository<Transaction,Long> {



    @Transactional
    @Modifying
    @Query(value = "Select * from Transaction t where t.payer = :payer",nativeQuery = true)
    List<Transaction> findByPayer(@Param("payer") String payer /*,TransactionStatus status*/);

    @Transactional
    @Modifying
    @Query(value = "Select * from Transaction t where t.redeem_request_id = :redeem_request_id",nativeQuery = true)
    List<Transaction> findByRedeem_request_id(@Param("redeem_request_id") UUID redeem_request_id);

    @Transactional
    @Modifying
    @Query(value = "Update transaction t set t.available_points = :available_points ,t.status = :status where t.id = :id",nativeQuery = true)
    void updateTransactionById(@Param("id") long id, @Param("available_points") int available_points,@Param("status") String status);


}
