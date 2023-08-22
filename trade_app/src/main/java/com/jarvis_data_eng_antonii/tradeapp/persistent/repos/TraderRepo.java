package com.jarvis_data_eng_antonii.tradeapp.persistent.repos;

import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.Account;
import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.Trader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TraderRepo extends JpaRepository<Trader, String> {}