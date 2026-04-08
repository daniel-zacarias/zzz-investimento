package br.zzz.infrastructure.wallet.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateWalletRequest(
        @JsonProperty("userId") String userId,
        @JsonProperty("name") String name
) {
}
