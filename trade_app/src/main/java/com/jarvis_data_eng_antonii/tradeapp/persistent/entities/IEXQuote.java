package com.jarvis_data_eng_antonii.tradeapp.persistent.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IEXQuote {
    long avgTotalVolume;
    String calculationPrice;
    double change;
    double changePercent;
    Object close;
    String closeSource;
    Object closeTime;
    String companyName;
    String currency;
    Object delayedPrice;
    Object delayedPriceTime;
    Object extendedChange;
    Object extendedChangePercent;
    Object extendedPrice;
    Object extendedPriceTime;
    double high;
    String highSource;
    long highTime;
    long iexAskPrice;
    long iexAskSize;
    double iexBidPrice;
    long iexBidSize;
    double iexClose;
    long iexCloseTime;
    long iexLastUpdated;
    double iexMarketPercent;
    double iexOpen;
    long iexOpenTime;
    double iexRealtimePrice;
    long iexRealtimeSize;
    long iexVolume;
    long lastTradeTime;
    double latestPrice;
    String latestSource;
    String latestTime;
    long latestUpdate;
    long latestVolume;
    double low;
    String lowSource;
    long lowTime;
    long marketCap;
    Object oddLotDelayedPrice;
    Object oddLotDelayedPriceTime;
    Object open;
    Object openTime;
    String openSource;
    double peRatio;
    double previousClose;
    long previousVolume;
    String primaryExchange;
    String symbol;
    long volume;
    double week52High;
    double week52Low;
    double ytdChange;
    @JsonProperty("isUSMarketOpen")
    boolean isUSMarketOpen;
}