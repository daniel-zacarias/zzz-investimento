package br.zzz.investimento.domain.wallet;

import br.zzz.investimento.domain.user.UserID;

import java.util.Optional;

public interface WalletGateway {

    boolean existsById(WalletID id);

    Optional<Wallet> findWalletByUserId(UserID userID);

}

