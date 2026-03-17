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

import java.math.BigDecimal;
import java.util.Optional;
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
    void givenAValidUserId_whenCallsFindWalletByUserId_thenShouldReturnWallet() {
        final var aUserId = UserID.unique();
        final var inv1 = InvestmentID.unique();
        final var inv2 = InvestmentID.unique();

        final var wallet = Wallet.newWallet(
                aUserId,
                Set.of(inv1, inv2),
                new BigDecimal("1000.00"),
                new BigDecimal("1500.00")
        );

        when(walletRepository.findByUserId(aUserId.getValue()))
                .thenReturn(Optional.of(WalletJpaEntity.from(wallet)));

        final var result = gateway.findWalletByUserId(aUserId);

        assertTrue(result.isPresent());
        final var actual = result.get();

        assertEquals(wallet.getId().getValue(), actual.getId().getValue());
        assertEquals(wallet.getUserId().getValue(), actual.getUserId().getValue());
        assertEquals(wallet.getInvestments().size(), actual.getInvestments().size());
        assertEquals(0, wallet.getInitialAmount().compareTo(actual.getInitialAmount()));
        assertEquals(0, wallet.getTotalAmount().compareTo(actual.getTotalAmount()));

        verify(walletRepository, times(1)).findByUserId(aUserId.getValue());
    }

    @Test
    void givenAValidUserId_whenCallsFindWalletByUserId_thenShouldReturnEmpty() {
        final var aUserId = UserID.unique();

        when(walletRepository.findByUserId(aUserId.getValue()))
                .thenReturn(Optional.empty());

        final var result = gateway.findWalletByUserId(aUserId);

        assertTrue(result.isEmpty());

        verify(walletRepository, times(1)).findByUserId(aUserId.getValue());
    }
}
