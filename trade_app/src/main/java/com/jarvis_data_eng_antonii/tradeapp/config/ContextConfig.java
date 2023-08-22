package com.jarvis_data_eng_antonii.tradeapp.config;

import com.jarvis_data_eng_antonii.tradeapp.IEXClient;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.*;
import com.jarvis_data_eng_antonii.tradeapp.services.AccountService;
import com.jarvis_data_eng_antonii.tradeapp.services.QuoteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextConfig {
    @Bean
    public QuoteService getQuoteService(@Value("${iex.api.token}") String token, IEXClient client, QuoteRepo repo) {
        return new QuoteService(token, client, repo);
    }

    @Bean
    public AccountService getAccountService(AccountRepo accountRepo, TraderRepo traderRepo,
                                            SecurityOrderRepo orderRepo, PositionRepo positionRepo) {
        return new AccountService(accountRepo, traderRepo, orderRepo, positionRepo);
    }
}