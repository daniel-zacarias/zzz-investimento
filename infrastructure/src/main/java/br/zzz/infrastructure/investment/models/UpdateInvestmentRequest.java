package br.zzz.infrastructure.investment.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateInvestmentRequest(
        @JsonProperty("amount") String amount,
        @JsonProperty("annualPeriod") Integer annualPeriod,
        @JsonProperty("annualRate") String annualRate) {

}
