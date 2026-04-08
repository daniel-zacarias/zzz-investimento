package br.zzz.investimento.application.wallet.retrieve.get;

import br.zzz.investimento.domain.investment.Investment;
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

        return wallets.stream()
                .map(aWallet -> {
                    final var totalAmount = this.investmentGateway.findAllByWalletId(aWallet.getId()).stream()
                            .map(Investment::getResult)
                            .reduce(BigDecimal.ZERO, BigDecimal::add)
                            .setScale(2, RoundingMode.HALF_UP);

                    return WalletOutput.from(aWallet, totalAmount);
                })
                .toList();
    }
}
