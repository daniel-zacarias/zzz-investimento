package br.zzz.investimento.investmentservice.investment.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateInvestmentRequest(
        @JsonProperty("amount") String amount,
        @JsonProperty("annualPeriod") Integer annualPeriod,
        @JsonProperty("annualRate") String annualRate,
        @JsonProperty("monthAmount") String monthAmount,
        @JsonProperty("walletId") String walletId
) {
}
