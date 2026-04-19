package br.zzz.investimento.investmentservice.investment.presenter;

import br.zzz.investimento.application.investment.retrieve.get.InvestmentOutput;
import br.zzz.investimento.investmentservice.investment.models.InvestmentResponse;

public interface InvestmentPresenter {
    static InvestmentResponse present(final InvestmentOutput output) {
        return new InvestmentResponse(
                output.id().getValue(),
                output.amount().doubleValue(),
                output.monthAmount().doubleValue(),
                output.annualPeriod(),
                output.annualRate().doubleValue(),
                output.result().doubleValue(),
                output.createdAt().toString());
    }
}
