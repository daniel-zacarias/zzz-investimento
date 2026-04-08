package br.zzz.investimento.application.wallet.create;

import br.zzz.investimento.domain.wallet.Wallet;

import java.time.Instant;

public record CreateWalletOutput(
        String walletId,
        String userId,
        String name,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static CreateWalletOutput from(final Wallet wallet) {
        return new CreateWalletOutput(
                wallet.getId().getValue(),
                wallet.getUserId().getValue(),
                wallet.getName(),
                wallet.getCreatedAt(),
                wallet.getUpdatedAt(),
                wallet.getDeletedAt()
        );
    }
}
