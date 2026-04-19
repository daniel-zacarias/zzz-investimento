package br.zzz.investimento.application.investment.update;

import java.math.BigDecimal;

public record UpdateInvestmentCommand(
        String id,
        BigDecimal amount,
        Integer annualPeriod,
        BigDecimal annualRate,
        BigDecimal monthAmount
) {
    public static UpdateInvestmentCommand with(
            final String id,
            final BigDecimal amount,
            final Integer annualPeriod,
            final BigDecimal annualRate,
            final BigDecimal monthAmount
    ) {
        return new UpdateInvestmentCommand(id, amount, annualPeriod, annualRate, monthAmount);
    }
}
