package br.zzz.investimento.application.wallet.retrieve.get;

import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.wallet.Wallet;
import br.zzz.investimento.domain.wallet.WalletID;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record WalletOutput(
        WalletID id,
        String userId,
        String name,
        Set<InvestmentID> investments,
        BigDecimal totalAmount,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static WalletOutput from(final Wallet wallet, final BigDecimal totalAmount) {
        return new WalletOutput(
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
