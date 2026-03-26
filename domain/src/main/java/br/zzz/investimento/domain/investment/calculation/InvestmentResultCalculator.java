package br.zzz.investimento.domain.investment.calculation;

import java.math.BigDecimal;
import java.util.Objects;

public class InvestmentResultCalculator {

    private final CapitalizationStrategy capitalization;

    private InvestmentResultCalculator(final CapitalizationStrategy capitalization) {
        this.capitalization = Objects.requireNonNull(capitalization);
    }

    public static InvestmentResultCalculator create() {
        return new InvestmentResultCalculator(new AnnualCompoundCapitalization());
    }

    /**
     * Assumes inputs are already validated by the domain.
     */
    public BigDecimal calculate(
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate
    ) {
        return capitalization.calculateFinalAmount(amount, annualRate, annualPeriod);
    }
}
