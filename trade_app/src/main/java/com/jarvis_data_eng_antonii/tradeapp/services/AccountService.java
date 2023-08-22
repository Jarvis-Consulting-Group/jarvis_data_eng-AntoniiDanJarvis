package com.jarvis_data_eng_antonii.tradeapp.services;

import com.jarvis_data_eng_antonii.tradeapp.exceptions.ExceptionHandlerService;
import com.jarvis_data_eng_antonii.tradeapp.exceptions.TraderRemovalException;
import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.Account;
import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.Trader;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.AccountRepo;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.PositionRepo;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.SecurityOrderRepo;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.TraderRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class AccountService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerService.class);
    private AccountRepo accountRepo;
    private TraderRepo traderRepo;
    private SecurityOrderRepo orderRepo;
    private PositionRepo positionRepo;

    public Account createAccount(Trader trader) {
        validateTrader(trader);
        String accountId = UUID.randomUUID().toString();
        LOGGER.info(String.format("creating new account with next id %s", accountId));
        return accountRepo.saveAndFlush(new Account(accountId, trader, 0));
    }

    @Transactional
    public void deleteTraderById(String traderId) {
        if(traderId == null || traderId.isEmpty()){
            throw new IllegalArgumentException("traderId is invalid");
        }
        List<Account> accounts = accountRepo.findByTraderId(traderId);
        accounts.forEach(this::preRemovalValidation);
        accounts.forEach(account -> {
            LOGGER.info(String.format("removing order for %s account", account.getId()));
            orderRepo.deleteByAccountId(account.getId());
            LOGGER.info(String.format("removing account %s", account.getId()));
            accountRepo.deleteById(account.getId());
        });
        LOGGER.info(String.format("removing trader %s", traderId));
        traderRepo.deleteById(traderId);

        orderRepo.flush();
        accountRepo.flush();
        traderRepo.flush();
    }

    public void changeBalance(String accountId, double amount) {
        if(accountId == null || accountId.isEmpty()) {
            throw new IllegalArgumentException(String.format("accountId %s is invalid", accountId));
        }

        if(amount == 0) {
            throw new IllegalArgumentException("change amount is invalid");
        }

        LOGGER.info(String.format("change balance for %s account", accountId));
        if(accountRepo.updateAmount(accountId, amount) != 1) {
            throw new IllegalArgumentException(String.format("accountId %s is wrong", accountId));
        }
    }

    private void validateTrader(Trader trader) {
        if(trader == null ||
                trader.getId() == null || trader.getId().isEmpty() ||
                trader.getFirstName() == null || trader.getFirstName().isEmpty() ||
                trader.getLastName() == null || trader.getLastName().isEmpty() ||
                trader.getCountry() == null || trader.getCountry().isEmpty() ||
                trader.getEmail() == null || trader.getEmail().isEmpty() ||
                trader.getDate() == null) {
            throw new IllegalArgumentException("trader data is invalid");
        }
    }

    private void preRemovalValidation(Account account) {
        if(account.getAmount() != 0) {
            throw new TraderRemovalException(String.format("account %s balance is not valid for removal", account.getId()));
        }
        if(orderRepo.countOpenPositionsByAccountId(account.getId()) != 0) {
            throw new TraderRemovalException(String.format("account %s has open positions", account.getId()));
        }
    }
}
