package com.jarvis_data_eng_antonii.tradeapp.controllers;

import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.Account;
import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.Trader;
import com.jarvis_data_eng_antonii.tradeapp.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("account")
@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping
    public Account createTrader(@RequestBody Trader trader) {
        return accountService.createAccount(trader);
    }

    @DeleteMapping("traderId/{traderId}")
    public void deleteTrader(@PathVariable String traderId) {
        accountService.deleteTraderById(traderId);
    }

    @PutMapping("changeBalance/{accountId}/{amount}")
    public void withdraw(@PathVariable String accountId, @PathVariable Double amount) {
        accountService.changeBalance(accountId, amount);
    }
}