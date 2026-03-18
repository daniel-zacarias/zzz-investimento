package br.zzz.infrastructure.wallet.persistence;

import br.zzz.infrastructure.investment.persistence.InvestmentJpaEntity;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.wallet.Wallet;
import br.zzz.investimento.domain.wallet.WalletID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@SQLDelete(sql = "UPDATE wallets SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Entity(name = "Wallet")
@Table(name = "wallets")
public class WalletJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY)
    private Set<InvestmentJpaEntity> investments;


    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant updatedAt;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP(6)")
    private Instant deletedAt;

    public WalletJpaEntity() {
    }

    public WalletJpaEntity(
            final String id,
            final String userId,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static WalletJpaEntity from(final Wallet entity) {
        final var walletJpa = new WalletJpaEntity(
                entity.getId().getValue(),
                entity.getUserId().getValue(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );

        final var investments = entity.getInvestments().stream()
                .map(InvestmentID::getValue)
                .map(investmentId -> {
                    final var inv = new InvestmentJpaEntity();
                    inv.setId(investmentId);
                    inv.setWalletId(entity.getId().getValue());
                    return inv;
                })
                .collect(Collectors.toSet());

        walletJpa.setInvestments(investments);
        return walletJpa;
    }

    public Wallet toAggregate() {
        final var investmentIdSet = (investments == null)
                ? Collections.<InvestmentID>emptySet()
                : investments.stream()
                .map(InvestmentJpaEntity::getId)
                .map(InvestmentID::from)
                .collect(Collectors.toSet());

        return Wallet.with(
                WalletID.from(getId()),
                UserID.from(getUserId()),
                investmentIdSet,
                getCreatedAt(),
                getUpdatedAt(),
                getDeletedAt()
        );
    }


    private static Set<InvestmentID> deserializeInvestmentIds(final String raw) {
        if (raw == null || raw.isBlank()) {
            return Collections.emptySet();
        }
        return Arrays.stream(raw.split(","))
                .filter(s -> !s.isBlank())
                .map(String::trim)
                .map(InvestmentID::from)
                .collect(Collectors.toSet());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<InvestmentJpaEntity> getInvestments() {
        return investments;
    }

    public void setInvestments(final Set<InvestmentJpaEntity> investments) {
        this.investments = investments;
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
