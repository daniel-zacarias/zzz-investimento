package br.zzz.infrastructure.investment;

import br.zzz.infrastructure.investment.client.InvestmentServiceClient;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.wallet.WalletID;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InvestmentServiceGateway implements InvestmentGateway {

    private final InvestmentServiceClient investmentServiceClient;

    public InvestmentServiceGateway(final InvestmentServiceClient investmentServiceClient) {
        this.investmentServiceClient = Objects.requireNonNull(investmentServiceClient);
    }

    @Override
    public Map<WalletID, BigDecimal> sumResultsByWalletIds(final Set<WalletID> walletIds) {
        if (walletIds == null || walletIds.isEmpty()) {
            return Map.of();
        }

        final var walletIdValues = walletIds.stream()
                .filter(Objects::nonNull)
                .map(WalletID::getValue)
                .distinct()
                .toList();

        final var totals = investmentServiceClient.getTotalsByWalletIds(walletIdValues);
        if (totals == null || totals.isEmpty()) {
            return Map.of();
        }

        return totals.entrySet().stream()
                .filter(e -> e.getKey() != null)
                .collect(Collectors.toMap(
                        e -> WalletID.from(e.getKey()),
                        e -> {
                            final var v = e.getValue();
                            if (v == null) {
                                return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                            }
                            return v.setScale(2, RoundingMode.HALF_UP);
                        }
                ));
    }
}
