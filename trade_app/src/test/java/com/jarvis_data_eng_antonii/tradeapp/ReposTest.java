package com.jarvis_data_eng_antonii.tradeapp;

import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.Account;
import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.SecurityOrder;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.AccountRepo;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.SecurityOrderRepo;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.TraderRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.UUID;

@SpringBootTest(classes = TradeAppApplication.class,  webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ReposTest {
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private SecurityOrderRepo orderRepo;
    @Autowired
    private TraderRepo traderRepo;

    @BeforeEach
    public void init() {
        orderRepo.deleteAll();
        accountRepo.deleteAll();
        traderRepo.deleteAll();
    }

    @Test
    public void shouldFindTraderById() {
        Account account = TestUtilities.getAccount();
        accountRepo.save(account);
        TestUtilities.assertAccount(accountRepo.findByTraderId(account.getTrader().getId()).get(0), account);
    }

    @Test
    public void deleteOrderByAccountId() {
        SecurityOrder order = TestUtilities.getSecurityOrder();
        orderRepo.save(order);
        orderRepo.deleteByAccountId(order.getAccount().getId());
        Assertions.assertThat(orderRepo.count()).isEqualTo(0);
    }

    @Test
    public void countOpenPositionsByAccountId() {
        Account account = TestUtilities.getAccount();
        SecurityOrder order = new SecurityOrder(UUID.randomUUID().toString(), account, "OPEN",
                TestUtilities.getQuote(), 1, 10, "medium priority");
        orderRepo.save(order);

        Assertions.assertThat(orderRepo.countOpenPositionsByAccountId(account.getId())).isEqualTo(1);
    }

    @Test
    public void updateAmount() {
        double withdraw = 10;
        Account account = TestUtilities.getAccount();
        accountRepo.save(account);

        accountRepo.updateAmount(account.getId(), withdraw);

        Assertions.assertThat(accountRepo.findById(account.getId()).get().getAmount())
                .isEqualTo(account.getAmount() + withdraw);
    }
}
