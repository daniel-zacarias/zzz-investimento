package br.zzz.investimento.domain.investment;

import br.zzz.investimento.domain.wallet.WalletID;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**
 * Port used by the Wallet use cases to obtain investment totals per wallet.
 * The investment bounded context itself lives in the investment-service.
 */
public interface InvestmentGateway {

    Map<WalletID, BigDecimal> sumResultsByWalletIds(Set<WalletID> walletIds);
}
