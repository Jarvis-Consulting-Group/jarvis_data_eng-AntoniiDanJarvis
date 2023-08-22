package com.jarvis_data_eng_antonii.tradeapp.persistent.repos;

import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AccountRepo extends JpaRepository<Account, String> {
    List<Account> findByTraderId(String traderId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE account SET amount = ((select amount from account WHERE id = ?1) + ?2) WHERE id = ?1", nativeQuery = true)
    int updateAmount(String id, double amount);
}