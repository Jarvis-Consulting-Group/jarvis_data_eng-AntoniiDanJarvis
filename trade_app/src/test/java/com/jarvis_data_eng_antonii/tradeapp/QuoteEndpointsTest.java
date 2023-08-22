package com.jarvis_data_eng_antonii.tradeapp;

import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.IEXQuote;
import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.Quote;
import com.jarvis_data_eng_antonii.tradeapp.persistent.repos.QuoteRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@SpringBootTest(classes = TradeAppApplication.class,  webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@WireMockTest(httpPort = 8070)
public class QuoteEndpointsTest {
    private RestTemplate template;
    @Autowired
    private QuoteRepo repo;
    @Value("${iex.api.token}")
    private String token;

    @BeforeEach
    public void init() {
        template = new RestTemplate();
        repo.deleteAll();
        HttpHeader contentTypeHeader = HttpHeader.httpHeader("Content-Type", "application/json; charset=utf-8");
        HttpHeader transferEncodingHeader = HttpHeader.httpHeader("Transfer-Encoding", "chunked");
        stubFor(get(String.format("/v1/data/CORE/QUOTE/%s?token=%s", TestUtilities.FAIL_TICKER, token))
                .willReturn(aResponse()
                        .withHeaders(new HttpHeaders(contentTypeHeader, transferEncodingHeader))
                        .withBodyFile("fail_resp.json")));
        stubFor(get(String.format("/v1/data/CORE/QUOTE/%s?token=%s", TestUtilities.SUC_TICKER, token))
                .willReturn(aResponse()
                        .withHeaders(new HttpHeaders(contentTypeHeader, transferEncodingHeader))
                        .withBodyFile("success_resp.json")));
    }

    @Test
    public void shouldGetQuote() {
        ParameterizedTypeReference<List<IEXQuote>> typeReference = new ParameterizedTypeReference<>(){};
        List<IEXQuote> quotes = template.exchange(
                "http://localhost:8080/quote/iex/ticker/" + TestUtilities.SUC_TICKER, HttpMethod.GET, HttpEntity.EMPTY, typeReference)
                .getBody();
        Assertions.assertThat(quotes).containsExactly(TestUtilities.getIEXQuote());
    }

    @Test
    public void shouldRetrieveBadRequestWithWrongParams() {
        ParameterizedTypeReference<List<IEXQuote>> typeReference = new ParameterizedTypeReference<>(){};
        Assertions.assertThatThrownBy(() -> Assertions.assertThat(template.exchange(
                        "http://localhost:8080/quote/iex/ticker/" + TestUtilities.FAIL_TICKER,
                        HttpMethod.GET, HttpEntity.EMPTY, typeReference)))
                .isInstanceOf(HttpClientErrorException.BadRequest.class)
                .hasMessage("400 : \"failure during retrieval\"");
    }

    @Test
    public void shouldRefreshData() {
        Quote initialQuote = TestUtilities.getQuote();
        Quote updatedQuote = new Quote(TestUtilities.SUC_TICKER, 320.35, 2, 3, 4, 5);
        repo.save(initialQuote);

        template.put("http://localhost:8080/quote/iexMarketData", null);
        Assertions.assertThat(repo.findById(TestUtilities.SUC_TICKER).get()).isEqualTo(updatedQuote);
    }

    @Test
    public void shouldRetrieveBadRequestWithWrongParamsWithIncorrectQuoteSavedInRepo() {
        Quote quote = new Quote(TestUtilities.FAIL_TICKER, 0, 0, 0, 0, 0);
        repo.save(quote);

        Assertions.assertThatThrownBy(() -> Assertions.assertThat(template.exchange(
                        "http://localhost:8080/quote/iexMarketData",
                        HttpMethod.PUT, HttpEntity.EMPTY, Void.class)))
                .isInstanceOf(HttpClientErrorException.BadRequest.class)
                .hasMessage("400 : \"failure during retrieval\"");
    }

    @Test
    public void shouldPutQuote() {
        Quote quote = TestUtilities.getQuote();
        template.put("http://localhost:8080/quote", quote);
        Assertions.assertThat(repo.findById(TestUtilities.SUC_TICKER).get()).isEqualTo(quote);
    }

    @Test
    public void shouldCreateQuote() {
        Quote quote = new Quote(TestUtilities.SUC_TICKER, 320.35, 2, 3, 4, 5);
        template.postForEntity("http://localhost:8080/quote//tickerId/" + TestUtilities.SUC_TICKER, null, Void.class);
        Assertions.assertThat(repo.findById(TestUtilities.SUC_TICKER).get()).isEqualTo(quote);
    }

    @Test
    public void shouldRetrieveBadRequestWithWongTicker() {
        Assertions.assertThatThrownBy(() -> Assertions.assertThat(template.postForEntity(
                "http://localhost:8080/quote//tickerId/" + TestUtilities.FAIL_TICKER, null, Void.class))
                .isInstanceOf(HttpClientErrorException.BadRequest.class))
                .hasMessage("400 : \"failure during retrieval\"");
    }

    @Test
    public void shouldGetDailyList() {
        Quote quote = TestUtilities.getQuote();
        repo.save(quote);
        ParameterizedTypeReference<List<Quote>> typeReference = new ParameterizedTypeReference<>(){};
        List<Quote> quotes = template.exchange(
                        "http://localhost:8080/quote/dailyList/", HttpMethod.GET, HttpEntity.EMPTY, typeReference)
                .getBody();

        Assertions.assertThat(quotes).containsExactly(quote);
    }

    @Test
    public void shouldEmptyGetDailyList() {
        ParameterizedTypeReference<List<Quote>> typeReference = new ParameterizedTypeReference<>(){};
        List<Quote> quotes = template.exchange(
                        "http://localhost:8080/quote/dailyList/", HttpMethod.GET, HttpEntity.EMPTY, typeReference)
                .getBody();
        Assertions.assertThat(quotes).isEmpty();
    }
}