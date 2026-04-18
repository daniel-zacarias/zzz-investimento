package br.zzz.investimento.application.investment.update;

import br.zzz.investimento.domain.exceptions.NotFoundException;
import br.zzz.investimento.domain.investment.Investment;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.investment.InvestmentID;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUpdateInvestmentUseCase extends UpdateInvestmentUseCase {

    private final InvestmentGateway gateway;

    public DefaultUpdateInvestmentUseCase(final InvestmentGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public UpdateInvestmentOutput execute(final UpdateInvestmentCommand command) {
        final var id = InvestmentID.from(command.id());

        final var investment = gateway.findById(id)
                .orElseThrow(notFound(id));

        final var updatedInvestment = investment.update(
                command.annualPeriod(),
                command.amount(),
                command.annualRate(),
                command.monthAmount()
        );

        return UpdateInvestmentOutput.from(gateway.update(updatedInvestment));
    }

    private Supplier<NotFoundException> notFound(final InvestmentID id) {
        return () -> NotFoundException.with(Investment.class, id);
    }
}
