package br.zzz.infrastructure.wallet;

import br.zzz.infrastructure.investment.client.InvestmentServiceClient;
import br.zzz.infrastructure.wallet.persistence.WalletJpaEntity;
import br.zzz.infrastructure.wallet.persistence.WalletRepository;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.pagination.Pagination;
import br.zzz.investimento.domain.wallet.Wallet;
import br.zzz.investimento.domain.wallet.WalletGateway;
import br.zzz.investimento.domain.wallet.WalletID;
import br.zzz.investimento.domain.wallet.WalletSearchQuery;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WalletPostgresGateway implements WalletGateway {

    private final WalletRepository walletRepository;
    private final InvestmentServiceClient investmentServiceClient;

    public WalletPostgresGateway(final WalletRepository walletRepository,
                                 final InvestmentServiceClient investmentServiceClient) {
        this.walletRepository = Objects.requireNonNull(walletRepository);
        this.investmentServiceClient = Objects.requireNonNull(investmentServiceClient);
    }

    @Override
    @Transactional
    public Wallet create(final Wallet wallet) {
        return save(wallet);
    }

    @Override
    public boolean existsById(final WalletID id) {
        return walletRepository.existsById(id.getValue());
    }

    @Override
    public Pagination<Wallet> findAllByUserId(final WalletSearchQuery query) {
        Objects.requireNonNull(query);
        final var aUserId = Objects.requireNonNull(query.userId());

        var stream = walletRepository.findAllByUserId(aUserId.getValue()).stream()
                .map(WalletJpaEntity::toAggregate);

        final var terms = query.terms();
        if (terms != null && !terms.isBlank()) {
            final var normalized = terms.trim().toLowerCase();
            stream = stream.filter(w -> w.getName() != null && w.getName().toLowerCase().contains(normalized));
        }

        final var sorted = stream.sorted(buildComparator(query)).toList();
        final var total = sorted.size();

        final var page = Math.max(0, query.page());
        final var perPage = query.perPage() <= 0 ? 10 : query.perPage();

        final long from = (long) page * (long) perPage;
        if (from >= total) {
            return new Pagination<>(page, perPage, total, List.of());
        }

        final var to = (int) Math.min(from + perPage, total);
        final var items = sorted.subList((int) from, to);

        if (items.isEmpty()) {
            return new Pagination<>(page, perPage, total, items);
        }

        final var walletIds = items.stream()
                .map(it -> it.getId().getValue())
                .distinct()
                .toList();

        final Map<String, Set<InvestmentID>> investmentIdsByWalletId = investmentServiceClient
                .getInvestmentIdsByWalletIds(walletIds)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().stream().map(InvestmentID::from).collect(Collectors.toSet())
                ));

        final var enrichedItems = items.stream()
                .map(w -> Wallet.with(
                        w.getId(),
                        w.getUserId(),
                        w.getName(),
                        investmentIdsByWalletId.getOrDefault(w.getId().getValue(), Set.of()),
                        w.getCreatedAt(),
                        w.getUpdatedAt(),
                        w.getDeletedAt()
                ))
                .toList();

        return new Pagination<>(page, perPage, total, enrichedItems);
    }

    private static Comparator<Wallet> buildComparator(final WalletSearchQuery query) {
        final var sort = query.sort();

        Comparator<Wallet> comparator = switch (sort == null ? "" : sort) {
            case "createdAt" -> Comparator.comparing(Wallet::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
            case "updatedAt" -> Comparator.comparing(Wallet::getUpdatedAt, Comparator.nullsLast(Comparator.naturalOrder()));
            case "name" -> Comparator.comparing(Wallet::getName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));
            default -> Comparator.comparing(
                    w -> w.getId() != null ? w.getId().getValue() : null,
                    Comparator.nullsLast(String::compareTo)
            );
        };

        final var direction = query.direction();
        if (direction != null && direction.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    private Wallet save(final Wallet wallet) {
        final var saved = walletRepository.save(WalletJpaEntity.from(wallet));
        final var investmentIds = wallet.getInvestments() == null ? Set.<InvestmentID>of() : wallet.getInvestments();
        return saved.toAggregate(investmentIds);
    }
}
