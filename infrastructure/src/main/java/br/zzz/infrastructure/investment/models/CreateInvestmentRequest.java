package br.zzz.infrastructure.investment.models;

import org.springframework.data.web.JsonPath;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateInvestmentRequest(
                @JsonProperty("amount") String amount,
                @JsonProperty("annualPeriod") Integer annualPeriod,
                @JsonProperty("annualRate") String annualRate) {

}
