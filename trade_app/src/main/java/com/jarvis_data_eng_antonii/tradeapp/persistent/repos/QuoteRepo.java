package com.jarvis_data_eng_antonii.tradeapp.persistent.repos;

import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepo extends JpaRepository<Quote, String> {}