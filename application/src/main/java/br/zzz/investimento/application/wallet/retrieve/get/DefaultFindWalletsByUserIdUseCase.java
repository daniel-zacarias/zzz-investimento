package br.zzz.investimento.application.wallet.retrieve.get;

import br.zzz.investimento.domain.exceptions.NotFoundException;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.wallet.Wallet;
import br.zzz.investimento.domain.wallet.WalletGateway;

import java.util.Objects;

public class DefaultFindWalletsByUserIdUseCase extends FindWalletsByUserIdUseCase {

    private final WalletGateway walletGateway;

    public DefaultFindWalletsByUserIdUseCase(final WalletGateway walletGateway) {
        this.walletGateway = Objects.requireNonNull(walletGateway);
    }

    @Override
    public WalletOutput execute(final String input) {
        Objects.requireNonNull(input);

        final var aUserId = UserID.from(input);

        final var aWallet = this.walletGateway.findWalletByUserId(aUserId)
                .orElseThrow(() -> NotFoundException.with(Wallet.class, aUserId));

        return WalletOutput.from(aWallet);
    }
}
