package br.zzz.investimento.domain.investment;

import br.zzz.investimento.domain.AggregateRoot;
import br.zzz.investimento.domain.exceptions.NotificationException;
import br.zzz.investimento.domain.investment.calculation.InvestmentResultCalculator;
import br.zzz.investimento.domain.validation.ValidationHandler;
import br.zzz.investimento.domain.validation.handler.Notification;
import br.zzz.investimento.domain.wallet.WalletID;

import java.math.BigDecimal;
import java.time.Instant;

public class Investment extends AggregateRoot<InvestmentID> {

    private final Integer annualPeriod;

    private final BigDecimal amount;

    private final BigDecimal monthAmount;

    private final BigDecimal result;

    private final BigDecimal annualRate;

    private final WalletID wallet;

    private final Instant createdAt;

    private final Instant updatedAt;

    private final Instant deletedAt;


    protected Investment(
            final InvestmentID investmentID,
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate,
            final BigDecimal monthAmount,
            final WalletID wallet,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        this(
                investmentID,
                annualPeriod,
                amount,
                annualRate,
                monthAmount,
                calculateResult(annualPeriod, amount, annualRate, monthAmount),
                wallet,
                createdAt,
                updatedAt,
                deletedAt);
    }

    protected Investment(
            final InvestmentID investmentID,
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate,
            final BigDecimal monthAmount,
            final BigDecimal result,
            final WalletID wallet,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        super(investmentID);
        this.annualPeriod = annualPeriod;
        this.amount = amount;
        this.annualRate = annualRate;
        this.monthAmount = (monthAmount == null) ? BigDecimal.ZERO : monthAmount;
        this.result = result;
        this.wallet = wallet;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        selfValidate();

    }

    public static Investment newInvestment(
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate) {
        return newInvestment(annualPeriod, amount, annualRate, BigDecimal.ZERO, WalletID.unique());
    }

    public static Investment newInvestment(
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate,
            final BigDecimal monthAmount) {
        return newInvestment(annualPeriod, amount, annualRate, monthAmount, WalletID.unique());
    }

    public static Investment newInvestment(
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate,
            final WalletID wallet) {
        return newInvestment(annualPeriod, amount, annualRate, BigDecimal.ZERO, wallet);
    }

    public static Investment newInvestment(
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate,
            final BigDecimal monthAmount,
            final WalletID wallet) {
        final var anId = InvestmentID.unique();
        final var now = Instant.now();
        return new Investment(
                anId,
                annualPeriod,
                amount,
                annualRate,
                monthAmount,
                wallet,
                now,
                now,
                null);
    }

    public static Investment from(final Investment investment) {
        return new Investment(
                investment.getId(),
                investment.annualPeriod,
                investment.amount,
                investment.annualRate,
                investment.monthAmount,
                investment.result,
                investment.wallet,
                investment.createdAt,
                investment.updatedAt,
                investment.deletedAt);
    }

    public static Investment with(
            final InvestmentID anId,
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate,
            final BigDecimal monthAmount,
            final WalletID wallet,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Investment(
                anId,
                annualPeriod,
                amount,
                annualRate,
                monthAmount,
                wallet,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public static Investment with(
            final InvestmentID anId,
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate,
            final BigDecimal monthAmount,
            final BigDecimal result,
            final WalletID wallet,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Investment(
                anId,
                annualPeriod,
                amount,
                annualRate,
                monthAmount,
                result,
                wallet,
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

    public BigDecimal getMonthAmount() {
        return monthAmount;
    }

    public BigDecimal getResult() {
        return result;
    }

    public BigDecimal getAnnualRate() {
        return annualRate;
    }

    public WalletID getWallet() {
        return wallet;
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
        return update(annualPeriod, amount, annualRate, this.monthAmount);
    }

    public Investment update(
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate,
            final BigDecimal monthAmount) {
        return new Investment(
                this.getId(),
                annualPeriod,
                amount,
                annualRate,
                monthAmount,
                this.wallet,
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
                this.monthAmount,
                this.wallet,
                this.createdAt,
                Instant.now(),
                Instant.now());
    }

    private static BigDecimal calculateResult(
            final Integer annualPeriod,
            final BigDecimal amount,
            final BigDecimal annualRate,
            final BigDecimal monthAmount) {
        if (annualPeriod == null || annualPeriod <= 0 || amount == null || annualRate == null) {
            return BigDecimal.ZERO;
        }
        return InvestmentResultCalculator.create().calculate(annualPeriod, amount, annualRate, monthAmount);
    }

    private void selfValidate() {
        final var notification = Notification.create();

        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Failed to create an Investment", notification);
        }
    }

}
