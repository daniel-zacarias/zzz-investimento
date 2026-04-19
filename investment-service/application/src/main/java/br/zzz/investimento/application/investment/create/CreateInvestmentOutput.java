package br.zzz.investimento.application.investment.create;

import br.zzz.investimento.domain.investment.Investment;

public record CreateInvestmentOutput(
        String id
) {
    public static CreateInvestmentOutput from(final String id) {
        return new CreateInvestmentOutput(id);
    }

    public static CreateInvestmentOutput from(final Investment investment) {
        return from(investment.getId().getValue());
    }
}
