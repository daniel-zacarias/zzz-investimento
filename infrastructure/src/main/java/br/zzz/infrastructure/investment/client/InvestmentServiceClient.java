package br.zzz.infrastructure.investment.client;

import br.zzz.infrastructure.investment.client.models.WalletInvestmentIdsResponse;
import br.zzz.infrastructure.investment.client.models.WalletTotalsResponse;
import br.zzz.infrastructure.investment.models.CreateInvestmentRequest;
import br.zzz.infrastructure.investment.models.CreateInvestmentResult;
import br.zzz.infrastructure.investment.models.InvestmentResponse;
import br.zzz.infrastructure.investment.models.UpdateInvestmentRequest;
import br.zzz.infrastructure.investment.models.UpdateInvestmentResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@FeignClient(name = "investment-service", url = "${investment-service.base-url}")
public interface InvestmentServiceClient {

    @PostMapping("/api/investments")
    CreateInvestmentResult create(@RequestBody CreateInvestmentRequest request);

    @GetMapping("/api/investments/{id}")
    InvestmentResponse getById(@PathVariable("id") String id);

    @PutMapping("/api/investments/{id}")
    UpdateInvestmentResult update(
            @PathVariable("id") String id,
            @RequestBody UpdateInvestmentRequest request
    );

    @DeleteMapping("/api/investments/{id}")
    void deleteById(@PathVariable("id") String id);

    @GetMapping("/api/internal/wallets/investment-ids")
    WalletInvestmentIdsResponse getInvestmentIds(@RequestParam("walletIds") List<String> walletIds);

    @GetMapping("/api/internal/wallets/totals")
    WalletTotalsResponse getTotals(@RequestParam("walletIds") List<String> walletIds);

    default Map<String, Set<String>> getInvestmentIdsByWalletIds(final List<String> walletIds) {
        if (walletIds == null || walletIds.isEmpty()) {
            return Map.of();
        }

        final var response = getInvestmentIds(walletIds.stream().filter(Objects::nonNull).distinct().toList());
        if (response == null || response.items() == null) {
            return Map.of();
        }

        return response.items();
    }

    default Map<String, BigDecimal> getTotalsByWalletIds(final List<String> walletIds) {
        if (walletIds == null || walletIds.isEmpty()) {
            return Map.of();
        }

        final var response = getTotals(walletIds.stream().filter(Objects::nonNull).distinct().toList());
        if (response == null || response.totals() == null) {
            return Map.of();
        }

        return response.totals().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> {
                            final var raw = e.getValue();
                            if (raw == null || raw.isBlank()) {
                                return BigDecimal.ZERO;
                            }
                            return new BigDecimal(raw).setScale(2, RoundingMode.HALF_UP);
                        }
                ));
    }
}
