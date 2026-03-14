package br.zzz.infrastructure.investment.presenter;

import br.zzz.infrastructure.investment.models.InvestmentResponse;
import br.zzz.investimento.application.investment.retrieve.get.InvestmentOutput;

public interface InvestmentPresenter {
    static InvestmentResponse present(final InvestmentOutput output) {
        return new InvestmentResponse(
                output.id(),
                output.amount().doubleValue(),
                output.annualPeriod(),
                output.annualRate().doubleValue(),
                output.result().doubleValue(),
                output.createdAt().toString());
    }
}
