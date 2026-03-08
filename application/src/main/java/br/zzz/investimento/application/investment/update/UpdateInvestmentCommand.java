package br.zzz.investimento.application.investment.update;

import java.math.BigDecimal;

public record UpdateInvestmentCommand(
        String id,
        BigDecimal amount,
        Integer annualPeriod,
        BigDecimal annualRate
) {
    public static UpdateInvestmentCommand with(String id, BigDecimal amount, Integer annualPeriod, BigDecimal annualRate) {
        return new UpdateInvestmentCommand(id, amount, annualPeriod, annualRate);
    }
}
