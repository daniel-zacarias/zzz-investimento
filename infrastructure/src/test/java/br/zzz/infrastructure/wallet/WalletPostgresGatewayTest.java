package br.zzz.infrastructure.wallet;

import br.zzz.infrastructure.wallet.persistence.WalletJpaEntity;
import br.zzz.infrastructure.wallet.persistence.WalletRepository;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.wallet.Wallet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WalletPostgresGatewayTest {

    @Mock
    private WalletRepository walletRepository;

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

        final var result = gateway.findAllByUserId(aUserId);

        assertNotNull(result);
        assertEquals(1, result.size());

        final var actual = result.get(0);

        assertEquals(wallet.getId().getValue(), actual.getId().getValue());
        assertEquals(wallet.getUserId().getValue(), actual.getUserId().getValue());
        assertEquals(wallet.getName(), actual.getName());
        assertEquals(wallet.getInvestments().size(), actual.getInvestments().size());

        verify(walletRepository, times(1)).findAllByUserId(aUserId.getValue());
    }

    @Test
    void givenAValidUserId_whenCallsFindAllByUserId_thenShouldReturnEmptyList() {
        final var aUserId = UserID.unique();

        when(walletRepository.findAllByUserId(aUserId.getValue()))
                .thenReturn(List.of());

        final var result = gateway.findAllByUserId(aUserId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(walletRepository, times(1)).findAllByUserId(aUserId.getValue());
    }
}
