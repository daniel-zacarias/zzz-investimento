package br.zzz.investimento.application.investment.retrieve.get;

import br.zzz.investimento.domain.investment.Investment;
import br.zzz.investimento.domain.investment.InvestmentID;

import java.math.BigDecimal;
import java.time.Instant;

public record InvestmentOutput(
        InvestmentID id,
        Integer annualPeriod,
        BigDecimal amount,
        BigDecimal monthAmount,
        BigDecimal result,
        BigDecimal annualRate,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static InvestmentOutput from(final Investment investment) {
        return new InvestmentOutput(
                investment.getId(),
                investment.getAnnualPeriod(),
                investment.getAmount(),
                investment.getMonthAmount(),
                investment.getResult(),
                investment.getAnnualRate(),
                investment.getCreatedAt(),
                investment.getUpdatedAt(),
                investment.getDeletedAt()
        );
    }
}
