package br.zzz.infrastructure.wallet.presenter;

import br.zzz.infrastructure.wallet.models.WalletResponse;
import br.zzz.investimento.application.wallet.retrieve.get.WalletOutput;
import br.zzz.investimento.application.wallet.retrieve.list.ListWalletOutput;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.pagination.Pagination;

import java.util.List;
import java.util.stream.Collectors;

public interface WalletPresenter {

    static WalletResponse present(final WalletOutput output) {
        return new WalletResponse(
                output.id().getValue(),
                output.userId(),
                output.name(),
                output.investments().stream().map(InvestmentID::getValue).collect(Collectors.toSet()),
                output.totalAmount().doubleValue(),
                output.createdAt() != null ? output.createdAt().toString() : null,
                output.updatedAt() != null ? output.updatedAt().toString() : null,
                output.deletedAt() != null ? output.deletedAt().toString() : null
        );
    }

    static WalletResponse present(final ListWalletOutput output) {
        return new WalletResponse(
                output.id().getValue(),
                output.userId(),
                output.name(),
                output.investments().stream().map(InvestmentID::getValue).collect(Collectors.toSet()),
                output.totalAmount().doubleValue(),
                output.createdAt() != null ? output.createdAt().toString() : null,
                output.updatedAt() != null ? output.updatedAt().toString() : null,
                output.deletedAt() != null ? output.deletedAt().toString() : null
        );
    }

    static List<WalletResponse> present(final List<WalletOutput> outputs) {
        return outputs.stream().map(WalletPresenter::present).toList();
    }

    static Pagination<WalletResponse> present(final Pagination<ListWalletOutput> page) {
        return page.map(WalletPresenter::present);
    }
}
