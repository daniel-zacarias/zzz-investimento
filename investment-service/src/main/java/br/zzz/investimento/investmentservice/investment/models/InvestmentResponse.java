package br.zzz.investimento.investmentservice.investment.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record InvestmentResponse(
        @JsonProperty("id") String id,
        @JsonProperty("amount") Double amount,
        @JsonProperty("monthAmount") Double monthAmount,
        @JsonProperty("annualPeriod") Integer annualPeriod,
        @JsonProperty("annualRate") Double annualRate,
        @JsonProperty("result") Double result,
        @JsonProperty("createdAt") String createdAt
) {
}
