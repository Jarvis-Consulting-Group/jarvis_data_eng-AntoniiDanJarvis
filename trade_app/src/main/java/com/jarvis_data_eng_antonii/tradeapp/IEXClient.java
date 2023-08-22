package com.jarvis_data_eng_antonii.tradeapp;

import com.jarvis_data_eng_antonii.tradeapp.persistent.entities.IEXQuote;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(name = "IEXClient", url = "${iex.quote.url}" )
public interface IEXClient {
    @GetMapping("/v1/data/CORE/QUOTE/{quote}?token={token}")
    List<IEXQuote> getQuote(@PathVariable String token, @PathVariable String... quote);
}