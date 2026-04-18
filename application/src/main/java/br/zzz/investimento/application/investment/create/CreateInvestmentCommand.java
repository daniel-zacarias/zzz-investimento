package br.zzz.investimento.application.investment.create;

import java.math.BigDecimal;

public record CreateInvestmentCommand(
        BigDecimal amount,
        Integer annualPeriod,
        BigDecimal annualRate,
        BigDecimal monthAmount,
        String walletId
) {
    public static CreateInvestmentCommand with(
            final BigDecimal amount,
            final Integer period,
            final BigDecimal annualRate,
            final BigDecimal monthAmount,
            final String walletId
    ) {
        return new CreateInvestmentCommand(amount, period, annualRate, monthAmount, walletId);
    }
}
