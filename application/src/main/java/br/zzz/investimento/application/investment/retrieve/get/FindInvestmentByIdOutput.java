package br.zzz.investimento.application.investment.retrieve.get;

import br.zzz.investimento.domain.investment.Investment;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record FindInvestmentByIdOutput(
        String id,
        Integer annualPeriod,
        BigDecimal amount,
        BigDecimal result,
        BigDecimal annualRate,
        Timestamp createdAt,
        Timestamp updatedAt,
        Timestamp deletedAt
) {
    public static FindInvestmentByIdOutput from(final Investment investment) {
        return new FindInvestmentByIdOutput(
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
