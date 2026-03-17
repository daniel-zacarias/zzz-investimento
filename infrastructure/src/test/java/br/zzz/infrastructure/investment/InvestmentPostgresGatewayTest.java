package br.zzz.infrastructure.investment;

import br.zzz.infrastructure.investment.persistence.InvestmentJpaEntity;
import br.zzz.infrastructure.investment.persistence.InvestmentRepository;
import br.zzz.investimento.domain.investment.Investment;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.wallet.WalletID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentPostgresGatewayTest {

    @Mock
    private InvestmentRepository investmentRepository;

    @InjectMocks
    private InvestmentPostgresGateway gateway;

    @Test
    void givenAValidInvestment_whenCallsCreate_thenShouldReturnCreatedInvestment() {
        final var expectedAnnualPeriod = 5;
        final var expectedAmount = new BigDecimal("1000.00");
        final var expectedAnnualRate = new BigDecimal("0.10");

        final var investment = Investment.newInvestment(expectedAnnualPeriod, expectedAmount, expectedAnnualRate, WalletID.unique());
        final var entity = InvestmentJpaEntity.from(investment);

        when(investmentRepository.save(any())).thenReturn(entity);

        final var actualInvestment = gateway.create(investment);

        assertNotNull(actualInvestment);
        assertEquals(investment.getId().getValue(), actualInvestment.getId().getValue());
        assertEquals(expectedAnnualPeriod, actualInvestment.getAnnualPeriod());
        assertNotNull(actualInvestment.getAmount());
        assertNotNull(actualInvestment.getAnnualRate());
        assertNotNull(actualInvestment.getResult());
        assertNotNull(actualInvestment.getCreatedAt());
        assertNotNull(actualInvestment.getUpdatedAt());
        assertNull(actualInvestment.getDeletedAt());

        final var captor = ArgumentCaptor.forClass(InvestmentJpaEntity.class);
        verify(investmentRepository, times(1)).save(captor.capture());

        final var capturedEntity = captor.getValue();
        assertEquals(investment.getId().getValue(), capturedEntity.getId());
        assertEquals(expectedAnnualPeriod, capturedEntity.getAnnualPeriod());
        assertEquals(expectedAmount.doubleValue(), capturedEntity.getAmount());
        assertEquals(expectedAnnualRate.doubleValue(), capturedEntity.getAnnualRate());
    }

    @Test
    void givenAValidInvestment_whenCallsCreate_thenShouldPersistInvestmentWithCorrectResult() {
        final var annualPeriod = 2;
        final var amount = new BigDecimal("1000.00");
        final var annualRate = new BigDecimal("0.10");

        // (1 + 0.10)^2 * 1000 = 1210.00
        final var expectedResult = new BigDecimal("1210.00");

        final var investment = Investment.newInvestment(annualPeriod, amount, annualRate, WalletID.unique());
        final var entity = InvestmentJpaEntity.from(investment);

        when(investmentRepository.save(any())).thenReturn(entity);

        final var actualInvestment = gateway.create(investment);

        assertNotNull(actualInvestment);
        assertEquals(0, expectedResult.compareTo(actualInvestment.getResult()));

        verify(investmentRepository, times(1)).save(any(InvestmentJpaEntity.class));
    }

    @Test
    void givenAValidId_whenCallsDeleteById_thenShouldNotThrow() {
        final var anId = InvestmentID.unique();

        assertDoesNotThrow(() -> gateway.deleteById(anId));

        verify(investmentRepository, times(1)).deleteById(anId.getValue());
    }

    @Test
    void givenAValidId_whenCallsFindById_thenShouldReturnEmpty() {
        final var anId = InvestmentID.unique();

        final var actualResult = gateway.findById(anId);

        assertTrue(actualResult.isEmpty());
    }
}
