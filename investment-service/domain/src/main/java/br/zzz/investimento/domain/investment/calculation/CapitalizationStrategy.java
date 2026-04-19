package br.zzz.investimento.domain.investment.calculation;

import java.math.BigDecimal;

public interface CapitalizationStrategy {
    BigDecimal calculateFinalAmount(BigDecimal principal, BigDecimal annualRate, int annualPeriod);
}
