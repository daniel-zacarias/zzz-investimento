package br.zzz.investimento.application.wallet.retrieve.list;

import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.wallet.Wallet;
import br.zzz.investimento.domain.wallet.WalletID;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record ListWalletOutput(
        WalletID id,
        String userId,
        String name,
        Set<InvestmentID> investments,
        BigDecimal totalAmount,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static ListWalletOutput from(final Wallet wallet, final BigDecimal totalAmount) {
        return new ListWalletOutput(
                wallet.getId(),
                wallet.getUserId().getValue(),
                wallet.getName(),
                wallet.getInvestments(),
                totalAmount,
                wallet.getCreatedAt(),
                wallet.getUpdatedAt(),
                wallet.getDeletedAt()
        );
    }
}
