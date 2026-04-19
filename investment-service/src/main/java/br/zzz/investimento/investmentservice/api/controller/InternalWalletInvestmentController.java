package br.zzz.investimento.investmentservice.api.controller;

import br.zzz.investimento.investmentservice.investment.persistence.InvestmentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/internal/wallets")
public class InternalWalletInvestmentController {

    private final InvestmentRepository investmentRepository;

    public InternalWalletInvestmentController(final InvestmentRepository investmentRepository) {
        this.investmentRepository = Objects.requireNonNull(investmentRepository);
    }

    @GetMapping("/investment-ids")
    public WalletInvestmentIdsResponse investmentIdsByWalletIds(@RequestParam List<String> walletIds) {
        if (walletIds == null || walletIds.isEmpty()) {
            return new WalletInvestmentIdsResponse(Map.of());
        }

        final Map<String, Set<String>> items = investmentRepository.findInvestmentIdsByWalletIds(walletIds).stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(
                        InvestmentRepository.WalletInvestmentIdProjection::getWalletId,
                        Collectors.mapping(InvestmentRepository.WalletInvestmentIdProjection::getInvestmentId, Collectors.toSet())
                ));

        return new WalletInvestmentIdsResponse(items);
    }

    @GetMapping("/totals")
    public WalletTotalsResponse totalsByWalletIds(@RequestParam List<String> walletIds) {
        if (walletIds == null || walletIds.isEmpty()) {
            return new WalletTotalsResponse(Map.of());
        }

        final Map<String, String> totals = investmentRepository.sumResultByWalletIds(walletIds).stream()
                .collect(Collectors.toMap(
                        InvestmentRepository.WalletTotalResultProjection::getWalletId,
                        it -> BigDecimal.valueOf(it.getTotalResult()).setScale(2, RoundingMode.HALF_UP).toPlainString()
                ));

        return new WalletTotalsResponse(totals);
    }

    public record WalletInvestmentIdsResponse(Map<String, Set<String>> items) {
    }

    public record WalletTotalsResponse(Map<String, String> totals) {
    }
}
