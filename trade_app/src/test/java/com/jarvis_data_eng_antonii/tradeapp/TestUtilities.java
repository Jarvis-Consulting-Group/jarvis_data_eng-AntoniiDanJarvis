package com.jarvis_data_eng_antonii.tradeapp;

import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.*;
import org.assertj.core.api.Assertions;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

public class TestUtilities {
    public static final String FAIL_TICKER = "failed-ticker";
    public static final String SUC_TICKER = "MSFT";
    public static Trader getTrader() {
        return new Trader(UUID.randomUUID().toString(), "Joan", "McGreen", Date.valueOf(LocalDate.now()),
                "Georgia", "joMcGreen@gmail.com");
    }

    public static Quote getQuote() {
        return new Quote("MSFT", 320.35, 2, 3, 4, 5);
    }

    public static IEXQuote getIEXQuote() {
        return IEXQuote.builder()
                .avgTotalVolume(31383549)
                .calculationPrice("iexlasttrade")
                .change(-0.83)
                .changePercent(-0.00258)
                .close(null)
                .closeSource("official")
                .closeTime(null)
                .companyName("Microsoft Corporation")
                .currency("USD")
                .delayedPrice(null)
                .delayedPriceTime(null)
                .extendedChange(null)
                .extendedChangePercent(null)
                .extendedPrice(null)
                .extendedPriceTime(null)
                .high(0)
                .highSource(null)
                .highTime(0)
                .iexAskPrice(4)
                .iexAskSize(5)
                .iexBidPrice(2)
                .iexBidSize(3)
                .iexClose(320.35)
                .iexCloseTime(1692215998025L)
                .iexLastUpdated(1692215998025L)
                .iexMarketPercent(0.013623974448498877)
                .iexOpen(320.835)
                .iexOpenTime(1692192600666L)
                .iexRealtimePrice(320.34)
                .iexRealtimeSize(96)
                .iexVolume(267059)
                .lastTradeTime(1692215999997L)
                .latestPrice(320.35)
                .latestSource("IEX Last Trade")
                .latestTime("August 16, 2023")
                .latestUpdate(1692215998025L)
                .latestVolume(0)
                .low(0)
                .lowSource(null)
                .lowTime(0)
                .marketCap(2380124808343L)
                .oddLotDelayedPrice(null)
                .oddLotDelayedPriceTime(null)
                .open(null)
                .openTime(null)
                .openSource("official")
                .peRatio(33.09)
                .previousClose(321.18)
                .previousVolume(16966285)
                .primaryExchange("NASDAQ")
                .symbol(SUC_TICKER)
                .volume(0)
                .week52High(366.01)
                .week52Low(211.39)
                .ytdChange(0.34581272242413447)
                .isUSMarketOpen(true)
                .build();
    }

    public static Account getAccount() {
        return new Account(UUID.randomUUID().toString(), getTrader(), 0);
    }

    public static SecurityOrder getSecurityOrder() {
        return new SecurityOrder(UUID.randomUUID().toString(), getAccount(), "FILED", getQuote(), 1, 10, "medium priority");
    }

    public static void assertAccount(Account actual, Account expected) {
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(expected).isNotNull();
        Assertions.assertThat(actual.getTrader()).isEqualTo(expected.getTrader());
        Assertions.assertThat(actual.getAmount()).isEqualTo(expected.getAmount());
    }
}
