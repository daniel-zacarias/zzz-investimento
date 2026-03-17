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
        Set<InvestmentID> investments,
        BigDecimal initialAmount,
        BigDecimal totalAmount,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static WalletOutput from(final Wallet wallet) {
        return new WalletOutput(
                wallet.getId(),
                wallet.getUserId().getValue(),
                wallet.getInvestments(),
                wallet.getInitialAmount(),
                wallet.getTotalAmount(),
                wallet.getCreatedAt(),
                wallet.getUpdatedAt(),
                wallet.getDeletedAt()
        );
    }
}
