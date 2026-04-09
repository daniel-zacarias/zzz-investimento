package br.zzz.investimento.domain.wallet;

import br.zzz.investimento.domain.pagination.Pagination;
import br.zzz.investimento.domain.user.UserID;

import java.util.List;
import java.util.Objects;

public interface WalletGateway {

    Wallet create(Wallet wallet);

    boolean existsById(WalletID id);

    Pagination<Wallet> findAllByUserId(WalletSearchQuery query);

    default List<Wallet> findAllByUserId(final UserID userId) {
        Objects.requireNonNull(userId);
        final var page = findAllByUserId(new WalletSearchQuery(0, Integer.MAX_VALUE, null, null, null, userId));
        return page == null || page.items() == null ? List.of() : page.items();
    }

}
