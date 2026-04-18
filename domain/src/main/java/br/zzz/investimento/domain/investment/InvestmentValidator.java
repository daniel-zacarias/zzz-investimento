package br.zzz.investimento.domain.investment;

import br.zzz.investimento.domain.validation.Error;
import br.zzz.investimento.domain.validation.ValidationHandler;
import br.zzz.investimento.domain.validation.Validator;

import java.math.BigDecimal;

public class InvestmentValidator extends Validator {
    private final Investment investment;

    protected InvestmentValidator(final Investment investment, final ValidationHandler aHandler) {
        super(aHandler);
        this.investment = investment;
    }

    @Override
    public void validate() {
        checkNotNull(investment.getAmount(), "Amount should not be null");
        checkNotNull(investment.getAnnualPeriod(), "Annual period should not be null");
        checkNotNull(investment.getAnnualRate(), "Annual rate should not be null");
        checkNotNull(investment.getMonthAmount(), "Month amount should not be null");
        checkGreaterThanZero(investment.getAnnualPeriod(), "Annual period should be greater than zero");
        checkGreaterOrEqualToZero(investment.getMonthAmount(), "Month amount should be greater than or equal to zero");
    }

    private void checkNotNull(final Object anObject, final String message) {
        if (anObject == null) {
            validationHandler().append(new Error(message));
        }
    }

    private void checkGreaterThanZero(final Integer value, final String message) {
        if (value != null && value <= 0) {
            validationHandler().append(new Error(message));
        }
    }

    private void checkGreaterOrEqualToZero(final BigDecimal value, final String message) {
        if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
            validationHandler().append(new Error(message));
        }
    }
}
