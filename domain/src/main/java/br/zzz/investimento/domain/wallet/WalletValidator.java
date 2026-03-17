package br.zzz.investimento.domain.wallet;

import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.validation.Error;
import br.zzz.investimento.domain.validation.ValidationHandler;
import br.zzz.investimento.domain.validation.Validator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class WalletValidator extends Validator {
    private final Wallet wallet;

    public WalletValidator(final Wallet wallet, final ValidationHandler aHandler) {
        super(aHandler);
        this.wallet = wallet;
    }

    @Override
    public void validate() {
        checkNotNull(wallet.getUserId(), "User ID should not be null");
        checkNotNull(wallet.getInvestments(), "Investment IDs should not be null");
        checkNotNull(wallet.getInitialAmount(), "Initial amount should not be null");
        checkNotNull(wallet.getTotalAmount(), "Total amount should not be null");
        checkGreaterThanZero(wallet.getInitialAmount(), "Initial amount should be greater than zero");
        checkGreaterThanOrEqualZero(wallet.getTotalAmount(), "Total amount should be greater than or equal to zero");
    }

    public static void validate(
            final UserID userId,
            final Set<InvestmentID> investmentIds,
            final BigDecimal initialAmount,
            final BigDecimal totalAmount,
            final ValidationHandler aHandler) {
        checkNotNull(userId, "User ID should not be null", aHandler);
        checkNotNull(investmentIds, "Investment IDs should not be null", aHandler);
        checkNotEmpty(investmentIds, "Investment IDs should not be empty", aHandler);
        checkNotNull(initialAmount, "Initial amount should not be null", aHandler);
        checkNotNull(totalAmount, "Total amount should not be null", aHandler);
        checkGreaterThanZero(initialAmount, "Initial amount should be greater than zero", aHandler);
        checkGreaterThanOrEqualZero(totalAmount, "Total amount should be greater than or equal to zero", aHandler);
    }

    private void checkNotNull(final Object anObject, final String message) {
        if (anObject == null) {
            validationHandler().append(new Error(message));
        }
    }

    private static void checkNotNull(final Object anObject, final String message, final ValidationHandler aHandler) {
        if (anObject == null) {
            aHandler.append(new Error(message));
        }
    }

    private static void checkNotEmpty(final Set<?> aList, final String message, final ValidationHandler aHandler) {
        if (aList == null || aList.isEmpty()) {
            aHandler.append(new Error(message));
        }
    }

    private void checkGreaterThanZero(final BigDecimal value, final String message) {
        if (value != null && value.compareTo(BigDecimal.ZERO) <= 0) {
            validationHandler().append(new Error(message));
        }
    }

    private static void checkGreaterThanZero(final BigDecimal value, final String message, final ValidationHandler aHandler) {
        if (value != null && value.compareTo(BigDecimal.ZERO) <= 0) {
            aHandler.append(new Error(message));
        }
    }

    private void checkGreaterThanOrEqualZero(final BigDecimal value, final String message) {
        if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
            validationHandler().append(new Error(message));
        }
    }

    private static void checkGreaterThanOrEqualZero(final BigDecimal value, final String message, final ValidationHandler aHandler) {
        if (value != null && value.compareTo(BigDecimal.ZERO) < 0) {
            aHandler.append(new Error(message));
        }
    }
}
