package br.zzz.infrastructure.investment.client.models;

import java.util.Map;
import java.util.Set;

public record WalletInvestmentIdsResponse(Map<String, Set<String>> items) {
}
