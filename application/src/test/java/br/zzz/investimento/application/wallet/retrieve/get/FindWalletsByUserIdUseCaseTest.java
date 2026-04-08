package br.zzz.investimento.application.wallet.retrieve.get;

import br.zzz.investimento.domain.investment.Investment;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.wallet.Wallet;
import br.zzz.investimento.domain.wallet.WalletGateway;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FindWalletsByUserIdUseCaseTest {

    @Test
    void givenValidCommand_whenFindWalletsByUserId_thenReturnWalletOutputs() {
        // given
        final var userId = UUID.randomUUID().toString();
        final var investments = Set.of(InvestmentID.unique(), InvestmentID.unique());

        final var wallet = Wallet.newWallet(
                UserID.from(userId),
                "Renda Fixa",
                investments
        );

        final var inv1 = Investment.newInvestment(1, new BigDecimal("1000.00"), new BigDecimal("0.10"), wallet.getId()); // 1100.00
        final var inv2 = Investment.newInvestment(2, new BigDecimal("1000.00"), new BigDecimal("0.10"), wallet.getId()); // 1210.00
        final var expectedTotalAmount = new BigDecimal("2310.00");

        final var gatewayMock = mock(WalletGateway.class);
        when(gatewayMock.findAllByUserId(any(UserID.class)))
                .thenReturn(List.of(wallet));

        final var investmentGatewayMock = mock(InvestmentGateway.class);
        when(investmentGatewayMock.findAllByWalletId(eq(wallet.getId())))
                .thenReturn(List.of(inv1, inv2));

        final var useCase = new DefaultFindWalletsByUserIdUseCase(gatewayMock, investmentGatewayMock);

        // when
        final var outputs = useCase.execute(userId);

        // then
        assertNotNull(outputs);
        assertEquals(1, outputs.size());

        final var output = outputs.get(0);
        assertEquals(wallet.getId(), output.id());
        assertEquals(userId, output.userId());
        assertEquals(wallet.getName(), output.name());
        assertEquals(investments, output.investments());
        assertEquals(expectedTotalAmount, output.totalAmount());
        verify(gatewayMock, times(1)).findAllByUserId(any(UserID.class));
        verify(investmentGatewayMock, times(1)).findAllByWalletId(eq(wallet.getId()));
    }

    @Test
    void givenInvalidUserId_whenFindWalletsByUserId_thenReturnEmptyList() {
        // given
        final var userId = UUID.randomUUID().toString();

        final var gatewayMock = mock(WalletGateway.class);
        when(gatewayMock.findAllByUserId(any(UserID.class)))
                .thenReturn(List.of());

        final var investmentGatewayMock = mock(InvestmentGateway.class);

        final var useCase = new DefaultFindWalletsByUserIdUseCase(gatewayMock, investmentGatewayMock);

        // when
        final var outputs = useCase.execute(userId);

        // then
        assertNotNull(outputs);
        assertTrue(outputs.isEmpty());
        verify(gatewayMock, times(1)).findAllByUserId(any(UserID.class));
        verify(investmentGatewayMock, never()).findAllByWalletId(any());
    }

    @Test
    void givenNullCommand_whenFindWalletsByUserId_thenThrowNullPointerException() {
        // given
        final var gatewayMock = mock(WalletGateway.class);
        final var investmentGatewayMock = mock(InvestmentGateway.class);
        final var useCase = new DefaultFindWalletsByUserIdUseCase(gatewayMock, investmentGatewayMock);

        // when & then
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(gatewayMock, never()).findAllByUserId(any());
    }

    @Test
    void givenNullGateway_whenCreateUseCase_thenThrowNullPointerException() {
        // when & then
        assertThrows(NullPointerException.class, () -> new DefaultFindWalletsByUserIdUseCase(null, mock(InvestmentGateway.class)));
        assertThrows(NullPointerException.class, () -> new DefaultFindWalletsByUserIdUseCase(mock(WalletGateway.class), null));
    }

    @Test
    void givenValidCommand_whenFindWalletsByUserId_thenUseCorrectUserId() {
        // given
        final var userId = UUID.randomUUID().toString();
        final var investments = Set.of(InvestmentID.unique());
        final var wallet = Wallet.newWallet(
                UserID.from(userId),
                "Renda Fixa",
                investments
        );

        final var gatewayMock = mock(WalletGateway.class);
        when(gatewayMock.findAllByUserId(any(UserID.class)))
                .thenReturn(List.of(wallet));

        final var investmentGatewayMock = mock(InvestmentGateway.class);
        when(investmentGatewayMock.findAllByWalletId(any())).thenReturn(List.of());

        final var useCase = new DefaultFindWalletsByUserIdUseCase(gatewayMock, investmentGatewayMock);

        // when
        useCase.execute(userId);

        // then
        verify(gatewayMock, times(1)).findAllByUserId(eq(UserID.from(userId)));
    }
}
