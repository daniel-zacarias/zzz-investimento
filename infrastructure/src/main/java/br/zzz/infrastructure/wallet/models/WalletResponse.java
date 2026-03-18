package br.zzz.infrastructure.wallet.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record WalletResponse(
        @JsonProperty("id") String id,
        @JsonProperty("userId") String userId,
        @JsonProperty("investments") Set<String> investments,
        @JsonProperty("totalAmount") Double totalAmount,
        @JsonProperty("createdAt") String createdAt,
        @JsonProperty("updatedAt") String updatedAt,
        @JsonProperty("deletedAt") String deletedAt
) {
}
