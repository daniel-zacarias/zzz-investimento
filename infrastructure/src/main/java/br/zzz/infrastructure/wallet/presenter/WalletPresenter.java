package br.zzz.infrastructure.wallet.presenter;

import br.zzz.infrastructure.wallet.models.WalletResponse;
import br.zzz.investimento.application.wallet.retrieve.get.WalletOutput;
import br.zzz.investimento.domain.investment.InvestmentID;

import java.util.stream.Collectors;

public interface WalletPresenter {

    static WalletResponse present(final WalletOutput output) {
        return new WalletResponse(
                output.id().getValue(),
                output.userId(),
                output.investments().stream().map(InvestmentID::getValue).collect(Collectors.toSet()),
                output.totalAmount().doubleValue(),
                output.createdAt() != null ? output.createdAt().toString() : null,
                output.updatedAt() != null ? output.updatedAt().toString() : null,
                output.deletedAt() != null ? output.deletedAt().toString() : null
        );
    }
}
