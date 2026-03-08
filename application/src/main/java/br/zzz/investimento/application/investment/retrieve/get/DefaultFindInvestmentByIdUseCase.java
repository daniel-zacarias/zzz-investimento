package br.zzz.investimento.application.investment.retrieve.get;

import br.zzz.investimento.domain.exceptions.DomainException;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.validation.Error;

import java.util.Objects;

public class DefaultFindInvestmentByIdUseCase extends FindInvestmentByIdUseCase {

    private final InvestmentGateway gateway;

    public DefaultFindInvestmentByIdUseCase(final InvestmentGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public FindInvestmentByIdOutput execute(final String id) {
        final var investmentID = InvestmentID.from(id);

        return gateway.findById(investmentID)
                .map(FindInvestmentByIdOutput::from)
                .orElseThrow(() -> DomainException.with(new Error("Investment with id %s was not found".formatted(id))));
    }
}
