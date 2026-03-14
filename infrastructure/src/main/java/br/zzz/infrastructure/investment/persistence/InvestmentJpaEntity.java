package br.zzz.infrastructure.investment.persistence;

import br.zzz.investimento.domain.investment.Investment;
import br.zzz.investimento.domain.investment.InvestmentID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Entity(name = "Investment")
@Table(name = "investments")
public class InvestmentJpaEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "annual_rate", nullable = false)
    private Double annualRate;

    @Column(name = "result", nullable = false)
    private Double result;

    @Column(name = "annual_period", nullable = false)
    private Integer annualPeriod;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant updatedAt;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP(6)")
    private Instant deletedAt;

    public InvestmentJpaEntity() {
    }

    public InvestmentJpaEntity(
            String id,
            Double amount,
            Double annualRate,
            Double result,
            Integer annualPeriod,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt
    ) {
        this.id = id;
        this.amount = amount;
        this.annualRate = annualRate;
        this.result = result;
        this.annualPeriod = annualPeriod;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }


    public static InvestmentJpaEntity from(Investment entity) {
        return new InvestmentJpaEntity(
                entity.getId().getValue(),
                entity.getAmount().doubleValue(),
                entity.getAnnualRate().doubleValue(),
                entity.getResult().doubleValue(),
                entity.getAnnualPeriod(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

    public Investment toAggregate() {
        return Investment.with(
                InvestmentID.from(getId()),
                getAnnualPeriod(),
                new BigDecimal(getAmount()),
                new BigDecimal(getAnnualRate()),
                getCreatedAt(),
                getUpdatedAt(),
                getDeletedAt()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(Double annualRate) {
        this.annualRate = annualRate;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public Integer getAnnualPeriod() {
        return annualPeriod;
    }

    public void setAnnualPeriod(Integer annualPeriod) {
        this.annualPeriod = annualPeriod;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
