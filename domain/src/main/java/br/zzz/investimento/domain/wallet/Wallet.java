package br.zzz.investimento.domain.wallet;

import br.zzz.investimento.domain.AggregateRoot;
import br.zzz.investimento.domain.exceptions.NotificationException;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.validation.ValidationHandler;
import br.zzz.investimento.domain.validation.handler.Notification;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Wallet extends AggregateRoot<WalletID> {

    private final UserID userId;

    private final Set<InvestmentID> investments;

    private final BigDecimal initialAmount;

    private final BigDecimal totalAmount;

    private final Instant createdAt;

    private final Instant updatedAt;

    private final Instant deletedAt;

    protected Wallet(
            final WalletID walletID,
            final UserID userId,
            final Set<InvestmentID> investments,
            final BigDecimal initialAmount,
            final BigDecimal totalAmount,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        super(walletID);
        this.userId = userId;
        this.investments = investments;
        this.initialAmount = initialAmount;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;

        selfValidate();
    }

    public static Wallet newWallet(
            final UserID userId,
            final Set<InvestmentID> investments,
            final BigDecimal initialAmount,
            final BigDecimal totalAmount) {
        var aValidation = Notification.create();
        WalletValidator.validate(userId, investments, initialAmount, totalAmount, aValidation);
        if (aValidation.hasError()) {
            throw new NotificationException("Wallet validation failed", aValidation);
        }
        return new Wallet(
                WalletID.unique(),
                userId,
                investments,
                initialAmount,
                totalAmount,
                Instant.now(),
                Instant.now(),
                null);
    }

    public static Wallet with(
            final WalletID walletID,
            final UserID userId,
            final Set<InvestmentID> investmentIds,
            final BigDecimal initialAmount,
            final BigDecimal totalAmount,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Wallet(
                walletID,
                userId,
                investmentIds,
                initialAmount,
                totalAmount,
                createdAt,
                updatedAt,
                deletedAt);
    }

    public Wallet delete() {
        return Wallet.with(
                this.id,
                this.userId,
                this.investments,
                this.initialAmount,
                this.totalAmount,
                this.createdAt,
                Instant.now(),
                Instant.now());
    }

    @Override
    public void validate(ValidationHandler handler) {
        new WalletValidator(this, handler).validate();
    }

    private void selfValidate() {
        final var notification = Notification.create();

        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("Failed to create a Wallet", notification);
        }
    }

    public WalletID getId() {
        return id;
    }

    public UserID getUserId() {
        return userId;
    }

    public Set<InvestmentID> getInvestments() {
        return Collections.unmodifiableSet(investments);
    }

    public BigDecimal getInitialAmount() {
        return initialAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return Objects.equals(id, wallet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
