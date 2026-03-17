package br.zzz.investimento.application.investment.create;

import java.math.BigDecimal;

public record CreateInvestmentCommand(
        BigDecimal amount,
        Integer annualPeriod,
        BigDecimal annualRate,
        String walletId
) {
    public static CreateInvestmentCommand with(BigDecimal amount, Integer period, BigDecimal annualRate, String walletId) {
        return new CreateInvestmentCommand(amount, period, annualRate, walletId);
    }
}
