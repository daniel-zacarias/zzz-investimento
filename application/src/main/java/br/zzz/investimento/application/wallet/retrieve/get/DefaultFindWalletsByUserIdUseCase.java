package br.zzz.investimento.application.wallet.retrieve.get;

import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.wallet.WalletGateway;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class DefaultFindWalletsByUserIdUseCase extends FindWalletsByUserIdUseCase {

    private final WalletGateway walletGateway;
    private final InvestmentGateway investmentGateway;

    public DefaultFindWalletsByUserIdUseCase(final WalletGateway walletGateway, final InvestmentGateway investmentGateway) {
        this.walletGateway = Objects.requireNonNull(walletGateway);
        this.investmentGateway = Objects.requireNonNull(investmentGateway);
    }

    @Override
    public List<WalletOutput> execute(final String input) {
        Objects.requireNonNull(input);

        final var aUserId = UserID.from(input);

        final var wallets = this.walletGateway.findAllByUserId(aUserId);
        if (wallets == null || wallets.isEmpty()) {
            return List.of();
        }

        final var walletIds = wallets.stream()
                .map(w -> w.getId())
                .filter(Objects::nonNull)
                .collect(java.util.stream.Collectors.toSet());

        final var totalsByWalletId = investmentGateway.sumResultsByWalletIds(walletIds);

        return wallets.stream()
                .map(aWallet -> {
                    final var totalAmount = totalsByWalletId
                            .getOrDefault(aWallet.getId(), BigDecimal.ZERO)
                            .setScale(2, RoundingMode.HALF_UP);

                    return WalletOutput.from(aWallet, totalAmount);
                })
                .toList();
    }
}
