package br.zzz.investimento.application.wallet.create;

public record CreateWalletCommand(
        String userId
) {
    public static CreateWalletCommand with(String userId) {
        return new CreateWalletCommand(userId);
    }
}
