package br.zzz.investimento.application.wallet.retrieve.list;

import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.pagination.Pagination;
import br.zzz.investimento.domain.wallet.Wallet;
import br.zzz.investimento.domain.wallet.WalletGateway;
import br.zzz.investimento.domain.wallet.WalletSearchQuery;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class DefaultListWalletsByUserIdUseCase extends ListWalletsByUserIdUseCase {

    private final WalletGateway walletGateway;
    private final InvestmentGateway investmentGateway;

    public DefaultListWalletsByUserIdUseCase(final WalletGateway walletGateway, final InvestmentGateway investmentGateway) {
        this.walletGateway = Objects.requireNonNull(walletGateway);
        this.investmentGateway = Objects.requireNonNull(investmentGateway);
    }

    @Override
    public Pagination<ListWalletOutput> execute(final WalletSearchQuery query) {
        Objects.requireNonNull(query);

        final var page = this.walletGateway.findAllByUserId(query);
        if (page == null) {
            return new Pagination<>(query.page(), query.perPage(), 0, List.of());
        }
        if (page.items() == null || page.items().isEmpty()) {
            return new Pagination<>(page.currentPage(), page.perPage(), page.total(), List.of());
        }

        final var walletIds = page.items().stream().map(Wallet::getId).collect(java.util.stream.Collectors.toSet());
        final var totalsByWalletId = this.investmentGateway.sumResultsByWalletIds(walletIds);

        return page.map(aWallet -> {
            final var total = (totalsByWalletId == null)
                    ? BigDecimal.ZERO
                    : totalsByWalletId.getOrDefault(aWallet.getId(), BigDecimal.ZERO);

            return ListWalletOutput.from(aWallet, total.setScale(2, RoundingMode.HALF_UP));
        });
    }
}
