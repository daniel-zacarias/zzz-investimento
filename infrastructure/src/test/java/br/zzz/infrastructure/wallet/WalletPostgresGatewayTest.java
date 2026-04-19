package br.zzz.infrastructure.wallet;

import br.zzz.infrastructure.investment.client.InvestmentServiceClient;
import br.zzz.infrastructure.wallet.persistence.WalletJpaEntity;
import br.zzz.infrastructure.wallet.persistence.WalletRepository;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.wallet.Wallet;
import br.zzz.investimento.domain.wallet.WalletSearchQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletPostgresGatewayTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private InvestmentServiceClient investmentServiceClient;

    @InjectMocks
    private WalletPostgresGateway gateway;

    @Test
    void givenAValidUserId_whenCallsFindAllByUserId_thenShouldReturnWallets() {
        final var aUserId = UserID.unique();
        final var inv1 = InvestmentID.unique();
        final var inv2 = InvestmentID.unique();

        final var wallet = Wallet.newWallet(
                aUserId,
                "Renda Fixa",
                Set.of(inv1, inv2)
        );

        when(walletRepository.findAllByUserId(aUserId.getValue()))
                .thenReturn(List.of(WalletJpaEntity.from(wallet)));

        when(investmentServiceClient.getInvestmentIdsByWalletIds(anyList()))
                .thenReturn(Map.of(wallet.getId().getValue(), Set.of(inv1.getValue(), inv2.getValue())));

        final var query = new WalletSearchQuery(0, 10, null, null, null, aUserId);
        final var result = gateway.findAllByUserId(query);

        assertNotNull(result);
        assertEquals(0, result.currentPage());
        assertEquals(10, result.perPage());
        assertEquals(1, result.total());
        assertEquals(1, result.items().size());

        final var actual = result.items().get(0);

        assertEquals(wallet.getId().getValue(), actual.getId().getValue());
        assertEquals(wallet.getUserId().getValue(), actual.getUserId().getValue());
        assertEquals(wallet.getName(), actual.getName());
        assertEquals(wallet.getInvestments().size(), actual.getInvestments().size());

        verify(walletRepository, times(1)).findAllByUserId(aUserId.getValue());
        verify(investmentServiceClient, times(1)).getInvestmentIdsByWalletIds(anyList());
    }

    @Test
    void givenAValidUserId_whenCallsFindAllByUserId_thenShouldReturnEmptyList() {
        final var aUserId = UserID.unique();

        when(walletRepository.findAllByUserId(aUserId.getValue()))
                .thenReturn(List.of());

        final var query = new WalletSearchQuery(0, 10, null, null, null, aUserId);
        final var result = gateway.findAllByUserId(query);

        assertNotNull(result);
        assertTrue(result.items().isEmpty());
        assertEquals(0, result.total());

        verify(walletRepository, times(1)).findAllByUserId(aUserId.getValue());
        verify(investmentServiceClient, times(0)).getInvestmentIdsByWalletIds(anyList());
    }
}
