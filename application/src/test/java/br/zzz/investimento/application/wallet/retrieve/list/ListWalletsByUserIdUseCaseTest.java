package br.zzz.investimento.application.wallet.retrieve.list;

import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.pagination.Pagination;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.wallet.Wallet;
import br.zzz.investimento.domain.wallet.WalletGateway;
import br.zzz.investimento.domain.wallet.WalletSearchQuery;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ListWalletsByUserIdUseCaseTest {

    @Test
    void givenValidQuery_whenListWalletsByUserId_thenReturnWalletOutputsWithTotalAmount() {
        // given
        final var userId = UUID.randomUUID().toString();
        final var user = UserID.from(userId);
        final var investments = Set.of(InvestmentID.unique(), InvestmentID.unique());

        final var query = new WalletSearchQuery(0, 10, "", "createdAt", "asc", user);

        final var wallet = Wallet.newWallet(user, "Renda Fixa", investments);
        final var expectedTotalAmount = new BigDecimal("2310.00");

        final var walletGateway = mock(WalletGateway.class);
        when(walletGateway.findAllByUserId(any(WalletSearchQuery.class)))
                .thenReturn(new Pagination<>(0, 10, 1, List.of(wallet)));

        final var investmentGateway = mock(InvestmentGateway.class);
        when(investmentGateway.sumResultsByWalletIds(eq(Set.of(wallet.getId()))))
                .thenReturn(Map.of(wallet.getId(), expectedTotalAmount));

        final var useCase = new DefaultListWalletsByUserIdUseCase(walletGateway, investmentGateway);

        // when
        final var result = useCase.execute(query);

        // then
        assertNotNull(result);
        assertEquals(1, result.items().size());

        final var output = result.items().get(0);
        assertEquals(wallet.getId(), output.id());
        assertEquals(userId, output.userId());
        assertEquals(wallet.getName(), output.name());
        assertEquals(investments, output.investments());
        assertEquals(expectedTotalAmount, output.totalAmount());

        verify(walletGateway, times(1)).findAllByUserId(eq(query));
        verify(investmentGateway, times(1)).sumResultsByWalletIds(eq(Set.of(wallet.getId())));
    }

    @Test
    void givenEmptyResult_whenListWalletsByUserId_thenDoNotCallInvestmentGateway() {
        // given
        final var userId = UUID.randomUUID().toString();
        final var query = new WalletSearchQuery(0, 10, "", "createdAt", "asc", UserID.from(userId));

        final var walletGateway = mock(WalletGateway.class);
        when(walletGateway.findAllByUserId(any(WalletSearchQuery.class)))
                .thenReturn(new Pagination<>(0, 10, 0, List.of()));

        final var investmentGateway = mock(InvestmentGateway.class);

        final var useCase = new DefaultListWalletsByUserIdUseCase(walletGateway, investmentGateway);

        // when
        final var result = useCase.execute(query);

        // then
        assertNotNull(result);
        assertTrue(result.items().isEmpty());
        verify(walletGateway, times(1)).findAllByUserId(eq(query));
        verify(investmentGateway, never()).sumResultsByWalletIds(any());
    }

    @Test
    void givenNullQuery_whenExecute_thenThrowNullPointerException() {
        final var useCase = new DefaultListWalletsByUserIdUseCase(mock(WalletGateway.class), mock(InvestmentGateway.class));
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
    }

    @Test
    void givenNullGateway_whenCreateUseCase_thenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DefaultListWalletsByUserIdUseCase(null, mock(InvestmentGateway.class)));
        assertThrows(NullPointerException.class, () -> new DefaultListWalletsByUserIdUseCase(mock(WalletGateway.class), null));
    }
}
