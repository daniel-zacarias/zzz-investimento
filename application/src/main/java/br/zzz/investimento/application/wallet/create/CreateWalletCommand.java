package br.zzz.investimento.application.wallet.create;

public record CreateWalletCommand(
        String userId,
        String name
) {
    public static CreateWalletCommand with(final String userId, final String name) {
        return new CreateWalletCommand(userId, name);
    }
}
