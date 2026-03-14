package br.zzz.infrastructure.investment;

import br.zzz.infrastructure.investment.persistence.InvestmentJpaEntity;
import br.zzz.infrastructure.investment.persistence.InvestmentRepository;
import br.zzz.investimento.domain.investment.Investment;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.investment.InvestmentID;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class InvestmentPostgresGateway implements InvestmentGateway {

    private final InvestmentRepository investmentRepository;

    public InvestmentPostgresGateway(final InvestmentRepository investmentRepository) {
        this.investmentRepository = Objects.requireNonNull(investmentRepository);
    }


    @Override
    public Investment create(Investment investment) {
        return investmentRepository.save(InvestmentJpaEntity.from(investment)).toAggregate();
    }

    @Override
    public void deleteById(InvestmentID id) {

    }

    @Override
    public Optional<Investment> findById(InvestmentID id) {
        return Optional.empty();
    }

    @Override
    public Investment update(Investment investment) {
        return null;
    }
}
