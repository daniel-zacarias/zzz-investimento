package br.zzz.investimento.domain.wallet;

import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.validation.Error;
import br.zzz.investimento.domain.validation.ValidationHandler;
import br.zzz.investimento.domain.validation.Validator;

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
    }

    public static void validate(
            final UserID userId,
            final Set<InvestmentID> investmentIds,
            final ValidationHandler aHandler) {
        checkNotNull(userId, "User ID should not be null", aHandler);
        checkNotNull(investmentIds, "Investment IDs should not be null", aHandler);
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


}
