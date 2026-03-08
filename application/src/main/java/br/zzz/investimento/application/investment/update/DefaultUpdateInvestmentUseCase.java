package br.zzz.investimento.application.investment.update;

import br.zzz.investimento.domain.exceptions.DomainException;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.validation.Error;

import java.util.Objects;

public class DefaultUpdateInvestmentUseCase extends UpdateInvestmentUseCase {

    private final InvestmentGateway gateway;

    public DefaultUpdateInvestmentUseCase(final InvestmentGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public UpdateInvestmentOutput execute(final UpdateInvestmentCommand command) {
        final var id = InvestmentID.from(command.id());

        final var investment = gateway.findById(id)
                .orElseThrow(() -> DomainException.with(new Error("Investment with id %s was not found".formatted(command.id()))));

        final var updatedInvestment = investment.update(command.annualPeriod(), command.amount(), command.annualRate());

        return UpdateInvestmentOutput.from(gateway.update(updatedInvestment));
    }
}
