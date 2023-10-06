package com.jarvis_data_eng_antonii.tradeapp.persistent.repos;

import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.SecurityOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface SecurityOrderRepo extends JpaRepository<SecurityOrder, String> {
    @Transactional
    void deleteByAccountId(String accountId);
    @Query(value = "SELECT COUNT(*) FROM security_order o WHERE o.account_id = ?1 AND o.status = 'OPEN'", nativeQuery = true)
    int countOpenPositionsByAccountId(String accountId);
}
