package br.zzz.infrastructure.wallet;

import br.zzz.infrastructure.wallet.persistence.WalletJpaEntity;
import br.zzz.infrastructure.wallet.persistence.WalletRepository;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.wallet.Wallet;
import br.zzz.investimento.domain.wallet.WalletGateway;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class WalletPostgresGateway implements WalletGateway {

    private final WalletRepository walletRepository;

    public WalletPostgresGateway(final WalletRepository walletRepository) {
        this.walletRepository = Objects.requireNonNull(walletRepository);
    }

    @Override
    public Optional<Wallet> findWalletByUserId(final UserID userID) {
        Objects.requireNonNull(userID);
        return walletRepository.findByUserId(userID.getValue())
                .map(WalletJpaEntity::toAggregate);
    }
}
