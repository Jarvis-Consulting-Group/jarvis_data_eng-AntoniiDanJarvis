package com.jarvis_data_eng_antonii.tradeapp;

import com.jarvis_data_eng_antonii.tradeapp.exceptions.TraderRemovalException;
import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.*;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.*;
import com.jarvis_data_eng_antonii.tradeapp.services.AccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class AccountServiceTest {
    private static final String TRADER_ID = "trader";
    private static final AccountRepo accountRepo = Mockito.mock(AccountRepo.class);
    private static final TraderRepo traderRepo = Mockito.mock(TraderRepo.class);
    private static final SecurityOrderRepo orderRepo = Mockito.mock(SecurityOrderRepo.class);
    private static final PositionRepo positionRepo = Mockito.mock(PositionRepo.class);
    private AccountService accountService;

    @BeforeEach
    public void init() {
        accountService = new AccountService(accountRepo, traderRepo, orderRepo, positionRepo);
        Mockito.reset(accountRepo, traderRepo);
    }
    @Test
    public void shouldCreateAccount() {
        accountService.createAccount(TestUtilities.getTrader());
        Mockito.verify(accountRepo, Mockito.times(1)).saveAndFlush(Mockito.any());
    }

    @Test
    public void shouldThrowExceptionWithNullTrader() {
        Assertions.assertThatThrownBy(() -> accountService.createAccount(null))
                .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> accountService.createAccount(
                new Trader(null, "Joan", "McGreen",
                        Date.valueOf(LocalDate.now()), "Georgia", "joMcGreen@gmail.com")))
                .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> accountService.createAccount(
                        new Trader(UUID.randomUUID().toString(), null, "McGreen",
                                Date.valueOf(LocalDate.now()), "Georgia", "joMcGreen@gmail.com")))
                .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> accountService.createAccount(
                        new Trader(UUID.randomUUID().toString(), "Georgia", null,
                                Date.valueOf(LocalDate.now()), "Georgia", "joMcGreen@gmail.com")))
                .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> accountService.createAccount(
                        new Trader(UUID.randomUUID().toString(), "Georgia", "McGreen",
                                null, "Georgia", "joMcGreen@gmail.com")))
                .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> accountService.createAccount(
                        new Trader(UUID.randomUUID().toString(), "Georgia", "McGreen",
                                Date.valueOf(LocalDate.now()), null, "joMcGreen@gmail.com")))
                .isInstanceOf(IllegalArgumentException.class);

        Assertions.assertThatThrownBy(() -> accountService.createAccount(
                        new Trader(UUID.randomUUID().toString(), "Georgia", "McGreen",
                                Date.valueOf(LocalDate.now()), "Georgia", null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldDeleteTraderById() {
        Account account = TestUtilities.getAccount();
        Mockito.when(accountRepo.findByTraderId(TRADER_ID)).thenReturn(List.of(account));
        accountService.deleteTraderById(TRADER_ID);
        Mockito.verify(orderRepo, Mockito.times(1)).countOpenPositionsByAccountId(account.getId());
        Mockito.verify(orderRepo, Mockito.times(1)).deleteByAccountId(account.getId());
        Mockito.verify(accountRepo, Mockito.times(1)).deleteById(account.getId());
        Mockito.verify(traderRepo, Mockito.times(1)).deleteById(TRADER_ID);
    }

    @Test
    public void shouldThrowExceptionWithNullTraderID() {
        Assertions.assertThatThrownBy(() -> accountService.deleteTraderById(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldNotDeleteTraderIfHasOpenPositions() {
        Account account = TestUtilities.getAccount();
        Mockito.when(accountRepo.findByTraderId(TRADER_ID)).thenReturn(List.of(account));
        Mockito.when(orderRepo.countOpenPositionsByAccountId(account.getId())).thenReturn(1);
        Assertions.assertThatThrownBy(() -> accountService.deleteTraderById(TRADER_ID))
                .isInstanceOf(TraderRemovalException.class);
        Mockito.verify(orderRepo, Mockito.never()).deleteByAccountId(account.getId());
        Mockito.verify(accountRepo, Mockito.never()).deleteById(account.getId());
        Mockito.verify(traderRepo, Mockito.never()).deleteById(TRADER_ID);
    }

    @Test
    public void shouldNotDeleteTraderIfBalanceIsNotZero() {
        Account account = new Account(UUID.randomUUID().toString(), TestUtilities.getTrader(), 10);
        Mockito.when(accountRepo.findByTraderId(TRADER_ID)).thenReturn(List.of(account));
        Assertions.assertThatThrownBy(() -> accountService.deleteTraderById(TRADER_ID))
                .isInstanceOf(TraderRemovalException.class);
        Mockito.verify(orderRepo, Mockito.never()).deleteByAccountId(account.getId());
        Mockito.verify(accountRepo, Mockito.never()).deleteById(account.getId());
        Mockito.verify(traderRepo, Mockito.never()).deleteById(TRADER_ID);
    }

    @Test
    public void shouldChangeBalance() {
        Account account = TestUtilities.getAccount();
        double changeAmount = 100;
        Mockito.when(accountRepo.updateAmount(account.getId(), changeAmount))
                .thenReturn(1);
        accountService.changeBalance(account.getId(), changeAmount);
    }

    @Test
    public void shouldThrowExceptionWithZeroChangeAmount() {
        Assertions.assertThatThrownBy(() -> accountService.changeBalance("id", 0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldThrowExceptionWithNullAccountId() {
        Assertions.assertThatThrownBy(() -> accountService.changeBalance(null, 100))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldThrowExceptionWithMoreThanOneUpdatedRow() {
        Account account = TestUtilities.getAccount();
        double changeAmount = -100;
        Mockito.when(accountRepo.updateAmount(account.getId(), changeAmount))
                .thenReturn(2);

        Assertions.assertThatThrownBy(() -> accountService.changeBalance(account.getId(), changeAmount))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldThrowExceptionWithZeroUpdatedRow() {
        Account account = TestUtilities.getAccount();
        double changeAmount = 100;
        Mockito.when(accountRepo.updateAmount(account.getId(), changeAmount))
                .thenReturn(0);

        Assertions.assertThatThrownBy(() -> accountService.changeBalance(account.getId(), changeAmount))
                .isInstanceOf(IllegalArgumentException.class);
    }
}