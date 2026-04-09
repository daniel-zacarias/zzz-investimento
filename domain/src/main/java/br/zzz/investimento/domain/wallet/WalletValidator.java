package br.zzz.investimento.domain.wallet;

import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.validation.Error;
import br.zzz.investimento.domain.validation.ValidationHandler;
import br.zzz.investimento.domain.validation.Validator;

import java.util.Set;

public class WalletValidator extends Validator {

    private static final int MAX_NAME_LENGTH = 100;

    private final Wallet wallet;

    public WalletValidator(final Wallet wallet, final ValidationHandler aHandler) {
        super(aHandler);
        this.wallet = wallet;
    }

    @Override
    public void validate() {
        checkNotNull(wallet.getUserId(), "User ID should not be null");
        checkNotNull(wallet.getName(), "Wallet name should not be null");
        checkNotBlank(wallet.getName(), "Wallet name should not be blank");
        checkMaxLength(wallet.getName(), MAX_NAME_LENGTH, "Wallet name should not be greater than 100 characters");
        checkNotNull(wallet.getInvestments(), "Investment IDs should not be null");
    }

    public static void validate(
            final UserID userId,
            final String name,
            final Set<InvestmentID> investmentIds,
            final ValidationHandler aHandler) {
        checkNotNull(userId, "User ID should not be null", aHandler);
        checkNotNull(name, "Wallet name should not be null", aHandler);
        checkNotBlank(name, "Wallet name should not be blank", aHandler);
        checkMaxLength(name, MAX_NAME_LENGTH, "Wallet name should not be greater than 100 characters", aHandler);
        checkNotNull(investmentIds, "Investment IDs should not be null", aHandler);
    }

    private void checkNotNull(final Object anObject, final String message) {
        if (anObject == null) {
            validationHandler().append(new Error(message));
        }
    }

    private void checkNotBlank(final String value, final String message) {
        if (value != null && value.isBlank()) {
            validationHandler().append(new Error(message));
        }
    }

    private void checkMaxLength(final String value, final int maxLength, final String message) {
        if (value != null && value.length() > maxLength) {
            validationHandler().append(new Error(message));
        }
    }

    private static void checkNotNull(final Object anObject, final String message, final ValidationHandler aHandler) {
        if (anObject == null) {
            aHandler.append(new Error(message));
        }
    }

    private static void checkNotBlank(final String value, final String message, final ValidationHandler aHandler) {
        if (value != null && value.isBlank()) {
            aHandler.append(new Error(message));
        }
    }

    private static void checkMaxLength(final String value, final int maxLength, final String message, final ValidationHandler aHandler) {
        if (value != null && value.length() > maxLength) {
            aHandler.append(new Error(message));
        }
    }
}
