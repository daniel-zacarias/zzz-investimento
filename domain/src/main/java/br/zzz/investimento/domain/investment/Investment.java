package br.zzz.investimento.domain.investment;

import br.zzz.investimento.domain.AggregateRoot;
import br.zzz.investimento.domain.exceptions.NotificationException;
import br.zzz.investimento.domain.validation.ValidationHandler;
import br.zzz.investimento.domain.validation.handler.Notification;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;

public class Investment extends AggregateRoot<InvestmentID> {

    private final Integer annualPeriod;

    private final BigDecimal amount;

    private final BigDecimal result;

    private final BigDecimal annualRate;

    private final Timestamp createdAt;

    private final Timestamp updatedAt;

    private final Timestamp deletedAt;

    protected Investment(
            final InvestmentID investmentID,
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate,
            final Timestamp createdAt,
            final Timestamp updatedAt,
            final Timestamp deletedAt) {
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
        final var now = new Timestamp(System.currentTimeMillis());
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public Timestamp getDeletedAt() {
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
                new Timestamp(System.currentTimeMillis()),
                this.deletedAt);
    }

    public Investment delete() {
        return new Investment(
                this.getId(),
                this.annualPeriod,
                this.amount,
                this.annualRate,
                this.createdAt,
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis()));
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
