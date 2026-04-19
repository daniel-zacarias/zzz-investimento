package br.zzz.investimento.investmentservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class InvestmentServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(InvestmentServiceMain.class, args);
    }
}
