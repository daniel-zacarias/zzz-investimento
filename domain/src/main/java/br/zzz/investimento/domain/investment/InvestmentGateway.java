package br.zzz.investimento.domain.investment;

import br.zzz.investimento.domain.wallet.WalletID;

import java.util.List;
import java.util.Optional;

public interface InvestmentGateway {

    Investment create(Investment investment);

    void deleteById(InvestmentID id);

    Optional<Investment> findById(InvestmentID id);

    List<Investment> findAllByWalletId(WalletID walletId);

    Investment update(Investment investment);

}
