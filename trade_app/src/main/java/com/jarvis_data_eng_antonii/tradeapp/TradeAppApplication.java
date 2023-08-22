package com.jarvis_data_eng_antonii.tradeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TradeAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(TradeAppApplication.class, args);
	}
}