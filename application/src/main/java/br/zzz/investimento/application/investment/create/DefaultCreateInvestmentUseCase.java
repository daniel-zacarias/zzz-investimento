package br.zzz.investimento.application.investment.create;

import br.zzz.investimento.domain.exceptions.NotificationException;
import br.zzz.investimento.domain.investment.Investment;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.validation.Error;
import br.zzz.investimento.domain.validation.handler.Notification;
import br.zzz.investimento.domain.wallet.WalletGateway;
import br.zzz.investimento.domain.wallet.WalletID;

import java.util.Objects;

public class DefaultCreateInvestmentUseCase extends  CreateInvestmentUseCase {

    private final InvestmentGateway gateway;

    private final WalletGateway walletGateway;

    public DefaultCreateInvestmentUseCase(final InvestmentGateway gateway, final WalletGateway walletGateway) {
        this.gateway = Objects.requireNonNull(gateway);
        this.walletGateway = Objects.requireNonNull(walletGateway);
    }


    @Override
    public CreateInvestmentOutput execute(final CreateInvestmentCommand command) {
        final var walletID = WalletID.from(command.walletId());
        validateWallet(walletID);
        final var investment = Investment.newInvestment(
                command.annualPeriod(),
                command.amount(),
                command.annualRate(),
                command.monthAmount(),
                walletID
        );
        return CreateInvestmentOutput.from(gateway.create(investment));
    }

    private void validateWallet(final WalletID walletID) {
        final var notification = Notification.create();
        if (!this.walletGateway.existsById(walletID)) {
            notification.append(new Error("Wallet with ID %s was not found".formatted(walletID.getValue())));
        }
        if (notification.hasError()) {
            throw new NotificationException("Failed to create investment", notification);
        }
    }
}
