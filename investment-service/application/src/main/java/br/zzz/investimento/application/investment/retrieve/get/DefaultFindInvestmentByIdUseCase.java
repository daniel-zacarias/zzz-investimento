package br.zzz.investimento.application.investment.retrieve.get;

import br.zzz.investimento.domain.exceptions.NotFoundException;
import br.zzz.investimento.domain.investment.Investment;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.investment.InvestmentID;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultFindInvestmentByIdUseCase extends FindInvestmentByIdUseCase {

    private final InvestmentGateway gateway;

    public DefaultFindInvestmentByIdUseCase(final InvestmentGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public InvestmentOutput execute(final String id) {
        final var investmentID = InvestmentID.from(id);

        return gateway.findById(investmentID)
                .map(InvestmentOutput::from)
                .orElseThrow(notFound(investmentID));
    }

    private Supplier<NotFoundException> notFound(InvestmentID id) {
        return () -> NotFoundException.with(Investment.class, id);
    }
}
