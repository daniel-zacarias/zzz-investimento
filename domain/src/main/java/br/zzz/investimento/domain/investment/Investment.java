package br.zzz.investimento.domain.investment;

import br.zzz.investimento.domain.AggregateRoot;
import br.zzz.investimento.domain.exceptions.NotificationException;
import br.zzz.investimento.domain.validation.ValidationHandler;
import br.zzz.investimento.domain.validation.handler.Notification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

public class Investment extends AggregateRoot<InvestmentID> {

    private final Integer annualPeriod;

    private final BigDecimal amount;

    private final BigDecimal result;

    private final BigDecimal annualRate;

    private final Instant createdAt;

    private final Instant updatedAt;

    private final Instant deletedAt;

    protected Investment(
            final InvestmentID investmentID,
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        super(investmentID);
        this.annualPeriod = annualPeriod;
        this.amount = amount;
        this.annualRate = annualRate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;

        selfValidate();
        this.result = this.calculate();
    }

    public static Investment newInvestment(
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate) {
        final var anId = InvestmentID.unique();
        final var now = Instant.now();
        return new Investment(
                anId,
                annualPeriod,
                amount,
                annualRate,
                now,
                now,
                null);
    }

    public static Investment from(Investment investment) {
        return new Investment(
                investment.getId(),
                investment.annualPeriod,
                investment.amount,
                investment.annualRate,
                investment.createdAt,
                investment.updatedAt,
                investment.deletedAt);
    }

    public static Investment with(
            final InvestmentID anId,
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Investment(
                anId,
                annualPeriod,
                amount,
                annualRate,
                createdAt,
                updatedAt,
                deletedAt);
    }

    @Override
    public void validate(ValidationHandler handler) {
        new InvestmentValidator(this, handler).validate();
    }

    public Integer getAnnualPeriod() {
        return annualPeriod;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getResult() {
        return result;
    }

    public BigDecimal getAnnualRate() {
        return annualRate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public Investment update(
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate) {
        return new Investment(
                this.getId(),
                annualPeriod,
                amount,
                annualRate,
                this.createdAt,
                Instant.now(),
                this.deletedAt);
    }

    public Investment delete() {
        return new Investment(
                this.getId(),
                this.annualPeriod,
                this.amount,
                this.annualRate,
                this.createdAt,
                Instant.now(),
                Instant.now());
    }

    private BigDecimal calculate() {
        BigDecimal compound = BigDecimal.ONE.add(annualRate).pow(annualPeriod);
        BigDecimal finalAmount = amount.multiply(compound);
        return finalAmount.setScale(2, RoundingMode.HALF_UP);
    }

    private void selfValidate() {
        final var notification = Notification.create();

        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Failed to create an Investment", notification);
        }
    }

}
