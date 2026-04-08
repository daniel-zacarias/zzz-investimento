package br.zzz.investimento.domain.wallet;

import br.zzz.investimento.domain.user.UserID;

import java.util.List;

public interface WalletGateway {

    Wallet create(Wallet wallet);

    boolean existsById(WalletID id);

    List<Wallet> findAllByUserId(UserID userID);

}

