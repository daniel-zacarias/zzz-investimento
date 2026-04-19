package br.zzz.investimento.domain.investment;

import br.zzz.investimento.domain.wallet.WalletID;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public interface InvestmentGateway {

    Investment create(Investment investment);

    void deleteById(InvestmentID id);

    Optional<Investment> findById(InvestmentID id);

    List<Investment> findAllByWalletId(WalletID walletId);

    default Map<WalletID, BigDecimal> sumResultsByWalletIds(final Set<WalletID> walletIds) {
        if (walletIds == null || walletIds.isEmpty()) {
            return Map.of();
        }

        return walletIds.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        wId -> wId,
                        wId -> {
                            final var investments = findAllByWalletId(wId);
                            if (investments == null || investments.isEmpty()) {
                                return BigDecimal.ZERO;
                            }
                            return investments.stream()
                                    .map(Investment::getResult)
                                    .filter(Objects::nonNull)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                        }
                ));
    }

    Investment update(Investment investment);

}
