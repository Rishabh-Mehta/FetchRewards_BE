package com.FetchRewards_BE.repository;

import com.FetchRewards_BE.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction,Long> {


    @Transactional
    @Modifying
    @Query(value = "Select * from Transaction t where t.user = :user",nativeQuery = true)
    List<Transaction> findByUser(String user /*,TransactionStatus status*/);

    @Transactional
    @Modifying
    @Query(value = "Update transaction t set t.available_points = :available_points ,t.status = :status where t.id = :id",nativeQuery = true)
    void updateTransactionById(@Param("id") long id, @Param("available_points") int available_points,@Param("status") String status);
}
