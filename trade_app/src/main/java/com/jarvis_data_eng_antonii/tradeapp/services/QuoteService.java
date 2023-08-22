package com.jarvis_data_eng_antonii.tradeapp.services;

import com.jarvis_data_eng_antonii.tradeapp.IEXClient;
import com.jarvis_data_eng_antonii.tradeapp.exceptions.ExceptionHandlerService;
import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.IEXQuote;
import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.Quote;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.QuoteRepo;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Value
public class QuoteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerService.class);
    String token;
    IEXClient iexClient;
    QuoteRepo repo;

    public List<IEXQuote> findIEXQuoteByTicker(String... ticker) {
        if(ticker == null || ticker.length == 0) {
            LOGGER.error("ticker value is invalid");
            throw new IllegalArgumentException("ticker value is invalid");
        }

        List<IEXQuote> quotes = iexClient.getQuote(token, ticker);
        if(quotes.isEmpty() || !quotes.stream().allMatch(Objects::nonNull)) {
            throw new DataRetrievalFailureException("failure during retrieval");
        }

        LOGGER.info(String.format("returned %s quotes", quotes.size()));
        return quotes;
    }

    public void updateMarketData() {
        repo.findAll().stream()
                .map(Quote::getTicker)
                .map(this::findIEXQuoteByTicker)
                .flatMap(Collection::stream)
                .map(this::convertIEXQuote)
                .forEach(repo::save);
        repo.flush();
    }

    public void saveQuote(Quote quote) {
        if(quote == null) {
            LOGGER.error("quote is null");
            throw new IllegalArgumentException("quote is null");
        }

        LOGGER.info(String.format("Persisting %s quote", quote));
        repo.saveAndFlush(quote);
    }

    public void createQuote(String ticker) {
        if(ticker == null || ticker.isEmpty()) {
            LOGGER.error("ticker value is invalid");
            throw new IllegalArgumentException("ticker value is invalid");
        }
        findIEXQuoteByTicker(ticker).stream()
                .map(this::convertIEXQuote)
                .forEach(repo::save);
        repo.flush();
    }

    public List<Quote> findAllQuotes() {
        return repo.findAll();
    }

    private Quote convertIEXQuote(IEXQuote quote) {
        double latestPrice = quote.getLatestPrice();
        if(!quote.isUSMarketOpen()){
            latestPrice = 0;
        }
        return new Quote(quote.getSymbol(), latestPrice, quote.getIexBidPrice(), quote.getIexBidSize(),
                quote.getIexAskPrice(), quote.getIexAskSize());
    }
}