package br.zzz.investimento.application.investment.delete;

import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.investment.InvestmentID;

import java.util.Objects;

public class DefaultDeleteInvestmentByIdUseCase extends DeleteInvestmentByIdUseCase {

    private final InvestmentGateway gateway;

    public DefaultDeleteInvestmentByIdUseCase(final InvestmentGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(final String id) {
        gateway.deleteById(InvestmentID.from(id));
    }
}
