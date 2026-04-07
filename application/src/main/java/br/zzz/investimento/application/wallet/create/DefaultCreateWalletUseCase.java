package br.zzz.investimento.application.wallet.create;

import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.wallet.Wallet;
import br.zzz.investimento.domain.wallet.WalletGateway;

import java.util.Objects;

public class DefaultCreateWalletUseCase extends CreateWalletUseCase {

    private final WalletGateway walletGateway;

    public DefaultCreateWalletUseCase(final WalletGateway walletGateway) {
        this.walletGateway = Objects.requireNonNull(walletGateway);
    }

    @Override
    public CreateWalletOutput execute(CreateWalletCommand input) {
        final var userId = UserID.from(input.userId());
        final var wallet = Wallet.newWallet(userId);
        return CreateWalletOutput.from(walletGateway.create(wallet));
    }
}
