package br.zzz.investimento.application.investment.create;

import java.math.BigDecimal;

public record CreateInvestmentCommand(
        BigDecimal amount,
        Integer annualPeriod,
        BigDecimal annualRate
) {
    public static CreateInvestmentCommand with(BigDecimal amount, Integer period, BigDecimal annualRate) {
        return new CreateInvestmentCommand(amount, period, annualRate);
    }
}
