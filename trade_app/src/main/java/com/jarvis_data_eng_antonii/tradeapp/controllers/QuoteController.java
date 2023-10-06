package com.jarvis_data_eng_antonii.tradeapp.controllers;

import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.IEXQuote;
import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.Quote;
import com.jarvis_data_eng_antonii.tradeapp.services.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("quote")
@RestController
public class QuoteController {
    @Autowired
    private QuoteService quoteService;

    @GetMapping("/iex/ticker/{ticker}")
    public List<IEXQuote> getQuote(@PathVariable String ticker) {
       return quoteService.findIEXQuoteByTicker(ticker);
    }

    @PutMapping("/iexMarketData")
    public void updateQuotes() {
        quoteService.updateMarketData();
    }

    @PutMapping
    public void putQuote(@RequestBody Quote quote) {
        quoteService.saveQuote(quote);
    }

    @PostMapping("/tickerId/{tickerId}")
    public void createQuote(@PathVariable String tickerId) {
        quoteService.createQuote(tickerId);
    }

    @GetMapping("/dailyList")
    public List<Quote> getDailyList() {
        return quoteService.findAllQuotes();
    }
}