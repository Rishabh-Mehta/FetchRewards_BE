package com.FetchRewards_BE.repository;
import com.FetchRewards_BE.models.Payer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface PayerRepository extends JpaRepository<Payer,Long> {


 List<Payer> findByUser(String user);



 @Query(value = "Select * from  Payer p  where p.user = :user and status != AVAILABLE " , nativeQuery = true)
 List<Payer> ReedemTransactionResult(@Param("user") String user);

 @Transactional
 @Modifying
 @Query(value = "UPDATE Payer p set p.points = p.points + :points where p.id = :id" , nativeQuery = true)
 void addPoints(@Param("points") int points, @Param("id") long id);

 @Transactional
 @Modifying
 @Query(value = "UPDATE Payer p set p.points = :points where p.id = :id" , nativeQuery = true)
 List<Payer> removePoints(@Param("points") int points, @Param("id") long id);
}
