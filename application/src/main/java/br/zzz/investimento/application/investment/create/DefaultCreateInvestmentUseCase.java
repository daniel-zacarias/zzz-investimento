package br.zzz.investimento.application.investment.create;

import br.zzz.investimento.domain.investment.Investment;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.wallet.WalletID;

import java.util.Objects;

public class DefaultCreateInvestmentUseCase extends  CreateInvestmentUseCase {

    private final InvestmentGateway gateway;

    public DefaultCreateInvestmentUseCase(final InvestmentGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }


    @Override
    public CreateInvestmentOutput execute(final CreateInvestmentCommand command) {
        final var walletID = WalletID.from(command.walletId());
        final var investment = Investment.newInvestment(
                command.annualPeriod(),
                command.amount(),
                command.annualRate(),
                walletID
        );
        return CreateInvestmentOutput.from(gateway.create(investment));
    }
}
