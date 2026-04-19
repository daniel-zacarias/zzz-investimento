package br.zzz.investimento.investmentservice.investment;

import br.zzz.investimento.domain.investment.Investment;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.wallet.WalletID;
import br.zzz.investimento.investmentservice.investment.persistence.InvestmentJpaEntity;
import br.zzz.investimento.investmentservice.investment.persistence.InvestmentRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InvestmentPostgresGateway implements InvestmentGateway {

    private final InvestmentRepository investmentRepository;

    public InvestmentPostgresGateway(final InvestmentRepository investmentRepository) {
        this.investmentRepository = Objects.requireNonNull(investmentRepository);
    }

    @Override
    public Investment create(final Investment investment) {
        return investmentRepository.save(InvestmentJpaEntity.from(investment)).toAggregate();
    }

    @Override
    public void deleteById(final InvestmentID id) {
        investmentRepository.deleteById(id.getValue());
    }

    @Override
    public Optional<Investment> findById(final InvestmentID id) {
        return investmentRepository.findById(id.getValue()).map(InvestmentJpaEntity::toAggregate);
    }

    @Override
    public List<Investment> findAllByWalletId(final WalletID walletId) {
        Objects.requireNonNull(walletId);
        return investmentRepository.findAllByWalletId(walletId.getValue()).stream()
                .map(InvestmentJpaEntity::toAggregate)
                .toList();
    }

    @Override
    public Map<WalletID, BigDecimal> sumResultsByWalletIds(final Set<WalletID> walletIds) {
        if (walletIds == null || walletIds.isEmpty()) {
            return Map.of();
        }

        final var walletIdValues = walletIds.stream()
                .filter(Objects::nonNull)
                .map(WalletID::getValue)
                .distinct()
                .toList();

        if (walletIdValues.isEmpty()) {
            return Map.of();
        }

        return investmentRepository.sumResultByWalletIds(walletIdValues).stream()
                .collect(Collectors.toMap(
                        it -> WalletID.from(it.getWalletId()),
                        it -> BigDecimal.valueOf(it.getTotalResult()).setScale(2, RoundingMode.HALF_UP)
                ));
    }

    @Override
    public Investment update(final Investment investment) {
        final var entity = investmentRepository.findById(investment.getId().getValue())
                .orElseThrow();

        entity.setAmount(investment.getAmount().doubleValue());
        entity.setAnnualRate(investment.getAnnualRate().doubleValue());
        entity.setMonthAmount(investment.getMonthAmount().doubleValue());
        entity.setResult(investment.getResult().doubleValue());
        entity.setAnnualPeriod(investment.getAnnualPeriod());
        entity.setUpdatedAt(investment.getUpdatedAt());

        return investmentRepository.save(entity).toAggregate();
    }
}
