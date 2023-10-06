package com.jarvis_data_eng_antonii.tradeapp;

import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.IEXQuote;
import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.Quote;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.QuoteRepo;
import com.jarvis_data_eng_antonii.tradeapp.services.QuoteService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import org.springframework.dao.DataRetrievalFailureException;

public class QuoteServiceTest {
    private static final String TICKER = TestUtilities.SUC_TICKER;
    private static final String TOKEN = "token";
    private static final IEXClient client = Mockito.mock(IEXClient.class);
    private static final QuoteRepo quoteRepo = Mockito.mock(QuoteRepo.class);
    private QuoteService quoteService;

    @BeforeEach
    public void init() {
        quoteService = new QuoteService(TOKEN, client, quoteRepo);
        Mockito.reset(client, quoteRepo);
        Mockito.when(client.getQuote(TOKEN, TICKER)).then(invocationOnMock -> List.of(TestUtilities.getIEXQuote()));
        Mockito.when(quoteRepo.findAll()).then(invocationOnMock -> List.of(TestUtilities.getQuote()));
    }
    @Test
    public void shouldFindQuote() {
        Assertions.assertThat(quoteService.findIEXQuoteByTicker(TICKER)).containsExactly(TestUtilities.getIEXQuote());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhileLookingForIEXQuote() {
        Assertions.assertThatThrownBy(() -> quoteService.findIEXQuoteByTicker(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldThrowDataRetrievalFailureExceptionWhileLookingForIEXQuote() {
        Assertions.assertThatThrownBy(() -> quoteService.findIEXQuoteByTicker("wrongTicket"))
                .isInstanceOf(DataRetrievalFailureException.class);
    }

    @Test
    public void shouldCallIEXClientByLookingForQuotes() {
        quoteService.findIEXQuoteByTicker(TICKER);
        Mockito.verify(client, Mockito.times(1)).getQuote(TOKEN, TICKER);
    }

    @Test
    public void shouldUpdateQuoteFromDB() {
        quoteService.updateMarketData();
        Mockito.verify(quoteRepo, Mockito.times(1)).findAll();
        Mockito.verify(client, Mockito.times(1)).getQuote(TOKEN, TICKER);
        Mockito.verify(quoteRepo, Mockito.times(1)).save(TestUtilities.getQuote());
    }

    @Test
    public void shouldUpdateQuoteFromDBWithClosedUSMarketProperty() {
        Mockito.when(client.getQuote(TOKEN, TICKER)).then(invocationOnMock -> List.of(getIEXQuoteWithClosedMarket()));
        Mockito.when(quoteRepo.findAll()).then(invocationOnMock -> List.of(getQuoteWithClosedMarket()));
        quoteService.updateMarketData();
        Mockito.verify(quoteRepo, Mockito.times(1)).findAll();
        Mockito.verify(client, Mockito.times(1)).getQuote(TOKEN, TICKER);
        Mockito.verify(quoteRepo, Mockito.times(1)).save(getQuoteWithClosedMarket());
    }

    @Test
    public void shouldNotSaveQuotesWhileUpdatingData() {
        Mockito.when(quoteRepo.findAll()).then(invocationOnMock -> List.of());
        quoteService.updateMarketData();
        Mockito.verify(client, Mockito.never()).getQuote(Mockito.any());
        Mockito.verify(quoteRepo, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void shouldSaveQuote() {
        Quote quote = TestUtilities.getQuote();
        quoteService.saveQuote(quote);
        Mockito.verify(quoteRepo, Mockito.times(1)).saveAndFlush(quote);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWithNullQuote() {
        Assertions.assertThatThrownBy(() -> quoteService.saveQuote(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldCreateNewQuote() {
        quoteService.createQuote(TICKER);
        Mockito.verify(quoteRepo, Mockito.times(1)).save(TestUtilities.getQuote());
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionWhileCreatingNewQuote() {
        Assertions.assertThatThrownBy(() -> quoteService.createQuote(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldThrowDataRetrievalFailureExceptionWhileCreatingNewQuote() {
        Assertions.assertThatThrownBy(() -> quoteService.createQuote("wrongTicket"))
                .isInstanceOf(DataRetrievalFailureException.class);
    }

    @Test
    public void shouldFindAllQuotes() {
        Assertions.assertThat(quoteService.findAllQuotes()).containsExactly(TestUtilities.getQuote());
    }

    @Test
    public void shouldCallQuoteRepoByGettingAllQuotes() {
        quoteService.findAllQuotes();
        Mockito.verify(quoteRepo, Mockito.times(1)).findAll();
    }

    private IEXQuote getIEXQuoteWithClosedMarket() {
        return IEXQuote.builder()
                .avgTotalVolume(32288665)
                .calculationPrice("tops")
                .change(0.53)
                .changePercent(0.00164)
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
                .iexAskPrice(345)
                .iexAskSize(108)
                .iexBidPrice(322.72)
                .iexBidSize(100)
                .iexClose(322.78)
                .iexCloseTime(1691695787011L)
                .iexLastUpdated(1691695800312L)
                .iexMarketPercent( 0.023434766289809773)
                .iexOpen(326.1)
                .iexOpenTime(1691674200668L)
                .iexRealtimePrice(322.76)
                .iexRealtimeSize(100)
                .iexVolume(354018)
                .lastTradeTime(1691695800312L)
                .latestPrice(322.76)
                .latestSource("IEX real time price")
                .latestTime("3:30:00 PM")
                .latestUpdate(1691695800312L)
                .latestVolume(0)
                .low(0)
                .lowSource(null)
                .lowTime(0)
                .marketCap(2398030538913L)
                .oddLotDelayedPrice(null)
                .oddLotDelayedPriceTime(null)
                .open(null)
                .openTime(null)
                .openSource("official")
                .peRatio(33.34)
                .previousClose(322.23)
                .previousVolume(22373268)
                .primaryExchange("NASDAQ")
                .symbol(TICKER)
                .volume(0)
                .week52High(366.78)
                .week52Low(211.37)
                .ytdChange(0.35453402938890727)
                .isUSMarketOpen(false)
                .build();
    }

    private Quote getQuoteWithClosedMarket() {
        return new Quote(TICKER, 0.0, 322.72, 100L, 345L, 108L);
    }
}