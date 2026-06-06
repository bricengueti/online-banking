package com.onlinebank.accountingService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.onlinebank.accountingService", "com.onlinebank.common"})
public class AccountingServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AccountingServiceApplication.class, args);
	}
}