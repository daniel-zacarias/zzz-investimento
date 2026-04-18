package br.zzz.investimento.domain.investment.calculation;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

public class InvestmentResultCalculator {

    private static final MathContext MC = new MathContext(18, RoundingMode.HALF_UP);

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
        return calculate(annualPeriod, amount, annualRate, BigDecimal.ZERO);
    }

    /**
     * Monthly contribution (ordinary annuity, end of month) using effective monthly rate derived from annualRate:
     * r = (1+annualRate)^(1/12) - 1
     */
    public BigDecimal calculate(
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate,
            final BigDecimal monthAmount
    ) {
        final var mAmount = (monthAmount == null) ? BigDecimal.ZERO : monthAmount;

        if (mAmount.compareTo(BigDecimal.ZERO) == 0) {
            return capitalization.calculateFinalAmount(amount, annualRate, annualPeriod);
        }

        final var compoundAnnual = BigDecimal.ONE.add(annualRate).pow(annualPeriod);
        final var pvFuture = amount.multiply(compoundAnnual, MC);

        final var months = annualPeriod * 12;
        final var monthlyRateDouble = Math.pow(BigDecimal.ONE.add(annualRate).doubleValue(), 1.0 / 12.0) - 1.0;

        if (monthlyRateDouble == 0.0d) {
            return pvFuture.add(mAmount.multiply(BigDecimal.valueOf(months), MC))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        final var monthlyRate = BigDecimal.valueOf(monthlyRateDouble);
        final var annuityFactor = compoundAnnual.subtract(BigDecimal.ONE, MC)
                .divide(monthlyRate, MC);

        return pvFuture.add(mAmount.multiply(annuityFactor, MC))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
