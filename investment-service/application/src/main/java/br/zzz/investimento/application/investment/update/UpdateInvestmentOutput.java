package br.zzz.investimento.application.investment.update;

import br.zzz.investimento.domain.investment.Investment;

public record UpdateInvestmentOutput(String id) {

    public static UpdateInvestmentOutput from(final Investment investment) {
        return new UpdateInvestmentOutput(investment.getId().getValue());
    }
}
