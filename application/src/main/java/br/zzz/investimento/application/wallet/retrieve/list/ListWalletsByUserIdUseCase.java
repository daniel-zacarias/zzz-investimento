package br.zzz.investimento.application.wallet.retrieve.list;

import br.zzz.investimento.application.UseCase;
import br.zzz.investimento.domain.pagination.Pagination;
import br.zzz.investimento.domain.wallet.WalletSearchQuery;

public abstract class ListWalletsByUserIdUseCase extends UseCase<WalletSearchQuery, Pagination<ListWalletOutput>> {
}
