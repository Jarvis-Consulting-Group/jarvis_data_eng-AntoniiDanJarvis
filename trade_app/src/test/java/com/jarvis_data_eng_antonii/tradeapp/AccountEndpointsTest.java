package com.jarvis_data_eng_antonii.tradeapp;

import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.*;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.AccountRepo;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.PositionRepo;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.SecurityOrderRepo;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.TraderRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import java.util.UUID;

@SpringBootTest(classes = TradeAppApplication.class,  webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AccountEndpointsTest {
    private RestTemplate template;
    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private TraderRepo traderRepo;
    @Autowired
    private SecurityOrderRepo orderRepo;
    @Autowired
    private PositionRepo positionRepo;

    @BeforeEach
    public void init() {
        template = new RestTemplate();
        orderRepo.deleteAll();;
        accountRepo.deleteAll();
        traderRepo.deleteAll();
    }

    @Test
    public void shouldCreateAccount() {
        Trader trader = TestUtilities.getTrader();
        Account account = new Account(UUID.randomUUID().toString(), trader, 0);
        Account response = template.postForEntity("http://localhost:8080/account", new HttpEntity<>(trader), Account.class)
                .getBody();
        TestUtilities.assertAccount(response, account);
    }

    @Test
    public void shouldBeAbleToCreateTwoAccountsForOneTrader() {
        Trader trader = TestUtilities.getTrader();

        template.postForEntity("http://localhost:8080/account", new HttpEntity<>(trader), Account.class);
        template.postForEntity("http://localhost:8080/account", new HttpEntity<>(trader), Account.class);

        Assertions.assertThat(traderRepo.findAll()).hasSize(1);
        Assertions.assertThat(accountRepo.findAll()).hasSize(2);
    }

    @Test
    public void shouldRetrieveBadRequestWithAnyNullTraderProperty() {
        Trader trader = TestUtilities.getTrader();
        Assertions.assertThatThrownBy(() -> org.assertj.core.api.Assertions.assertThat(template.postForEntity(
                "http://localhost:8080/account", new HttpEntity<>(
                        new Trader(null, trader.getFirstName(), trader.getLastName(), trader.getDate(),
                                trader.getCountry(), trader.getEmail())), Account.class)))
                .isInstanceOf(HttpClientErrorException.BadRequest.class)
                .hasMessage("400 : \"trader data is invalid\"");

        Assertions.assertThatThrownBy(() -> org.assertj.core.api.Assertions.assertThat(template.postForEntity(
                        "http://localhost:8080/account", new HttpEntity<>(
                                new Trader(trader.getId(), null, trader.getLastName(), trader.getDate(),
                                        trader.getCountry(), trader.getEmail())), Account.class)))
                .isInstanceOf(HttpClientErrorException.BadRequest.class)
                .hasMessage("400 : \"trader data is invalid\"");

        Assertions.assertThatThrownBy(() -> org.assertj.core.api.Assertions.assertThat(template.postForEntity(
                        "http://localhost:8080/account", new HttpEntity<>(
                                new Trader(trader.getId(), trader.getFirstName(), null, trader.getDate(),
                                        trader.getCountry(), trader.getEmail())), Account.class)))
                .isInstanceOf(HttpClientErrorException.BadRequest.class)
                .hasMessage("400 : \"trader data is invalid\"");

        Assertions.assertThatThrownBy(() -> org.assertj.core.api.Assertions.assertThat(template.postForEntity(
                        "http://localhost:8080/account", new HttpEntity<>(
                                new Trader(trader.getId(), trader.getFirstName(), trader.getLastName(), null,
                                        trader.getCountry(), trader.getEmail())), Account.class)))
                .isInstanceOf(HttpClientErrorException.BadRequest.class)
                .hasMessage("400 : \"trader data is invalid\"");

        Assertions.assertThatThrownBy(() -> org.assertj.core.api.Assertions.assertThat(template.postForEntity(
                        "http://localhost:8080/account", new HttpEntity<>(
                                new Trader(trader.getId(), trader.getFirstName(), trader.getLastName(), trader.getDate(),
                                        null, trader.getEmail())), Account.class)))
                .isInstanceOf(HttpClientErrorException.BadRequest.class)
                .hasMessage("400 : \"trader data is invalid\"");

        Assertions.assertThatThrownBy(() -> org.assertj.core.api.Assertions.assertThat(template.postForEntity(
                        "http://localhost:8080/account", new HttpEntity<>(
                                new Trader(trader.getId(), trader.getFirstName(), trader.getLastName(), trader.getDate(),
                                        trader.getCountry(), null)), Account.class)))
                .isInstanceOf(HttpClientErrorException.BadRequest.class)
                .hasMessage("400 : \"trader data is invalid\"");
    }

    @Test
    public void shouldDeleteTrader() {
        SecurityOrder order = TestUtilities.getSecurityOrder();
        orderRepo.save(order);

        template.delete("http://localhost:8080/account/traderId/" + order.getAccount().getTrader().getId());

        Assertions.assertThat(orderRepo.findAll()).hasSize(0);
        Assertions.assertThat(accountRepo.findAll()).hasSize(0);
        Assertions.assertThat(traderRepo.findAll()).hasSize(0);
    }

    @Test
    public void shouldRetrieveBadRequestWithByRemovingTraderWithNonZeroBalance() {
        Account account = new Account(UUID.randomUUID().toString(), TestUtilities.getTrader(), 30);
        SecurityOrder order = new SecurityOrder(UUID.randomUUID().toString(),account, "in progress",
                TestUtilities.getQuote(), 1, 10, "medium priority");
        orderRepo.save(order);
        String exceptionMessage = String.format("400 : \"account %s balance is not valid for removal\"", account.getId());

        Assertions.assertThatThrownBy(() -> org.assertj.core.api.Assertions.assertThat(template.exchange(
                        "http://localhost:8080/account/traderId/" + account.getTrader().getId(), HttpMethod.DELETE, null, Void.class)))
                .isInstanceOf(HttpClientErrorException.BadRequest.class)
                .hasMessage(exceptionMessage);

        Assertions.assertThat(orderRepo.findAll()).hasSize(1);
        Assertions.assertThat(accountRepo.findAll()).hasSize(1);
        Assertions.assertThat(traderRepo.findAll()).hasSize(1);
    }

    @Test
    public void shouldRetrieveBadRequestWithByRemovingTraderWithOpenPositions() {
        SecurityOrder order = new SecurityOrder(UUID.randomUUID().toString(), TestUtilities.getAccount(),
                "OPEN", TestUtilities.getQuote(), 1, 10, "medium priority");
        orderRepo.save(order);

        String exceptionMessage = String.format("400 : \"account %s has open positions\"", order.getAccount().getId());

        Assertions.assertThatThrownBy(() -> org.assertj.core.api.Assertions.assertThat(template.exchange(
                        "http://localhost:8080/account/traderId/" + order.getAccount().getTrader().getId(), HttpMethod.DELETE, null, Void.class)))
                .isInstanceOf(HttpClientErrorException.BadRequest.class)
                .hasMessage(exceptionMessage);

        Assertions.assertThat(orderRepo.findAll()).hasSize(1);
        Assertions.assertThat(accountRepo.findAll()).hasSize(1);
        Assertions.assertThat(traderRepo.findAll()).hasSize(1);
    }

    @Test
    public void shouldWithdraw() {
        Account account = TestUtilities.getAccount();
        accountRepo.saveAndFlush(account);
        double withdrawAmount = 10;
        String url = String.format("http://localhost:8080/account/changeBalance/%s/%f", account.getId(), withdrawAmount);

        template.put(url, null);
        Assertions.assertThat(accountRepo.findById(account.getId()).get().getAmount())
                .isEqualTo(account.getAmount() + withdrawAmount);
    }

    @Test
    public void shouldRetrieveBadRequestWithWrongAccountId() {
        String accountId = "wrongId";
        double withdrawAmount = -10;
        String exceptionMessage = String.format("400 : \"accountId %s is wrong\"", accountId);
        String url = String.format("http://localhost:8080/account/changeBalance/%s/%f", accountId, withdrawAmount);

        Assertions.assertThatThrownBy(() -> org.assertj.core.api.Assertions.assertThat(template.exchange(
                        url, HttpMethod.PUT, null, Void.class)))
                .isInstanceOf(HttpClientErrorException.BadRequest.class)
                .hasMessage(exceptionMessage);
    }

    @Test
    public void shouldRetrieveBadRequestWithWrongWithdraw() {
        Account account = TestUtilities.getAccount();
        accountRepo.save(account);
        double withdrawAmount = 0;
        String url = String.format("http://localhost:8080/account/changeBalance/%s/%f", account.getId(), withdrawAmount);

        Assertions.assertThatThrownBy(() -> org.assertj.core.api.Assertions.assertThat(template.exchange(
                        url, HttpMethod.PUT, null, Void.class)))
                .isInstanceOf(HttpClientErrorException.BadRequest.class)
                .hasMessage("400 : \"change amount is invalid\"");
    }
}