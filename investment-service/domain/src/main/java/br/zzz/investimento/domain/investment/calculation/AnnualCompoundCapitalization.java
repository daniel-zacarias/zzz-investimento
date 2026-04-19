package br.zzz.investimento.domain.investment.calculation;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AnnualCompoundCapitalization implements CapitalizationStrategy {

    @Override
    public BigDecimal calculateFinalAmount(
            final BigDecimal principal,
            final BigDecimal annualRate,
            final int annualPeriod
    ) {
        final var compound = BigDecimal.ONE.add(annualRate).pow(annualPeriod);
        final var finalAmount = principal.multiply(compound);
        return finalAmount.setScale(2, RoundingMode.HALF_UP);
    }
}
