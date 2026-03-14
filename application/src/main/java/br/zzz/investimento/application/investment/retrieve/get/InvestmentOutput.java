package br.zzz.investimento.application.investment.retrieve.get;

import br.zzz.investimento.domain.investment.Investment;

import java.math.BigDecimal;
import java.time.Instant;

public record InvestmentOutput(
        String id,
        Integer annualPeriod,
        BigDecimal amount,
        BigDecimal result,
        BigDecimal annualRate,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static InvestmentOutput from(final Investment investment) {
        return new InvestmentOutput(
                investment.getId().getValue(),
                investment.getAnnualPeriod(),
                investment.getAmount(),
                investment.getResult(),
                investment.getAnnualRate(),
                investment.getCreatedAt(),
                investment.getUpdatedAt(),
                investment.getDeletedAt()
        );
    }
}
