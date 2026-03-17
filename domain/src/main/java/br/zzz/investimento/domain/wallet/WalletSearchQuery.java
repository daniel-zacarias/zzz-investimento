package br.zzz.investimento.domain.wallet;

import br.zzz.investimento.domain.user.UserID;

public record WalletSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction,
        UserID userId
) {
}
