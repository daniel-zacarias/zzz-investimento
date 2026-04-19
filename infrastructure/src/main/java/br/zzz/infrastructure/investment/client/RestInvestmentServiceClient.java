package br.zzz.infrastructure.investment.client;

import br.zzz.infrastructure.investment.client.models.WalletInvestmentIdsResponse;
import br.zzz.infrastructure.investment.client.models.WalletTotalsResponse;
import br.zzz.infrastructure.investment.models.CreateInvestmentRequest;
import br.zzz.infrastructure.investment.models.InvestmentResponse;
import br.zzz.infrastructure.investment.models.UpdateInvestmentRequest;
import br.zzz.investimento.application.investment.create.CreateInvestmentOutput;
import br.zzz.investimento.application.investment.update.UpdateInvestmentOutput;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RestInvestmentServiceClient implements InvestmentServiceClient {

    private final RestClient restClient;

    public RestInvestmentServiceClient(
            final RestClient.Builder builder,
            @Value("${investment-service.base-url:http://localhost:8081}") final String baseUrl
    ) {
        this.restClient = builder
                .baseUrl(Objects.requireNonNull(baseUrl))
                .build();
    }

    @Override
    public CreateInvestmentOutput create(final CreateInvestmentRequest request) {
        return restClient.post()
                .uri("/api/investments")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(CreateInvestmentOutput.class);
    }

    @Override
    public InvestmentResponse getById(final String id) {
        return restClient.get()
                .uri("/api/investments/{id}", id)
                .retrieve()
                .body(InvestmentResponse.class);
    }

    @Override
    public UpdateInvestmentOutput update(final String id, final UpdateInvestmentRequest request) {
        return restClient.put()
                .uri("/api/investments/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(UpdateInvestmentOutput.class);
    }

    @Override
    public void deleteById(final String id) {
        restClient.delete()
                .uri("/api/investments/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public Map<String, Set<String>> getInvestmentIdsByWalletIds(final List<String> walletIds) {
        if (walletIds == null || walletIds.isEmpty()) {
            return Map.of();
        }

        final WalletInvestmentIdsResponse response = restClient.get()
                .uri(uriBuilder -> {
                    final var b = uriBuilder.path("/api/internal/wallets/investment-ids");
                    walletIds.stream().filter(Objects::nonNull).distinct().forEach(w -> b.queryParam("walletIds", w));
                    return b.build();
                })
                .retrieve()
                .body(WalletInvestmentIdsResponse.class);

        if (response == null || response.items() == null) {
            return Map.of();
        }

        return response.items();
    }

    @Override
    public Map<String, BigDecimal> getTotalsByWalletIds(final List<String> walletIds) {
        if (walletIds == null || walletIds.isEmpty()) {
            return Map.of();
        }

        final WalletTotalsResponse response = restClient.get()
                .uri(uriBuilder -> {
                    final var b = uriBuilder.path("/api/internal/wallets/totals");
                    walletIds.stream().filter(Objects::nonNull).distinct().forEach(w -> b.queryParam("walletIds", w));
                    return b.build();
                })
                .retrieve()
                .body(WalletTotalsResponse.class);

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
