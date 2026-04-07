package br.zzz.infrastructure.wallet;

import br.zzz.infrastructure.wallet.persistence.WalletJpaEntity;
import br.zzz.infrastructure.wallet.persistence.WalletRepository;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.wallet.Wallet;
import br.zzz.investimento.domain.wallet.WalletGateway;
import br.zzz.investimento.domain.wallet.WalletID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Component
public class WalletPostgresGateway implements WalletGateway {

    private final WalletRepository walletRepository;

    public WalletPostgresGateway(final WalletRepository walletRepository) {
        this.walletRepository = Objects.requireNonNull(walletRepository);
    }

    @Override
    @Transactional
    public Wallet create(Wallet wallet) {
        return save(wallet);
    }

    @Override
    public boolean existsById(WalletID id) {
        return walletRepository.existsById(id.getValue());
    }

    @Override
    public Optional<Wallet> findWalletByUserId(final UserID userID) {
        return walletRepository.findByUserId(userID.getValue())
                .map(WalletJpaEntity::toAggregate);
    }

    private Wallet save(final Wallet wallet) {
        return walletRepository.save(WalletJpaEntity.from(wallet)).toAggregate();
    }
}
