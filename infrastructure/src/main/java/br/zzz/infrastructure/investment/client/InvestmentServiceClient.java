package br.zzz.infrastructure.investment.client;

import br.zzz.infrastructure.investment.models.CreateInvestmentRequest;
import br.zzz.infrastructure.investment.models.InvestmentResponse;
import br.zzz.infrastructure.investment.models.UpdateInvestmentRequest;
import br.zzz.investimento.application.investment.create.CreateInvestmentOutput;
import br.zzz.investimento.application.investment.update.UpdateInvestmentOutput;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface InvestmentServiceClient {

    CreateInvestmentOutput create(CreateInvestmentRequest request);

    InvestmentResponse getById(String id);

    UpdateInvestmentOutput update(String id, UpdateInvestmentRequest request);

    void deleteById(String id);

    Map<String, Set<String>> getInvestmentIdsByWalletIds(List<String> walletIds);

    Map<String, BigDecimal> getTotalsByWalletIds(List<String> walletIds);
}
