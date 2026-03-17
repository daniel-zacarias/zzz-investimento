package br.zzz.investimento.application.investment.retrieve.get;

import br.zzz.investimento.application.UseCaseTest;
import br.zzz.investimento.domain.exceptions.DomainException;
import br.zzz.investimento.domain.investment.Investment;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.wallet.WalletID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class FindInvestmentByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultFindInvestmentByIdUseCase useCase;

    @Mock
    private InvestmentGateway investmentGateway;

    @Override
    protected Object[] getMocks() {
        return new Object[]{investmentGateway};
    }

    @Test
    public void givenAValidId_whenCallsFindInvestmentById_thenReturnInvestment() {
        // given
        final var expectedAnnualPeriod = 5;
        final var expectedAmount = new BigDecimal("1000.0");
        final var expectedAnnualRate = new BigDecimal("0.01");
        final var walletID = WalletID.unique();

        final var existingInvestment = Investment.newInvestment(expectedAnnualPeriod, expectedAmount, expectedAnnualRate, walletID);
        final var expectedId = existingInvestment.getId().getValue();

        when(investmentGateway.findById(InvestmentID.from(expectedId))).thenReturn(Optional.of(existingInvestment));

        // when
        final var actualOutput = useCase.execute(expectedId);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId, actualOutput.id().getValue());
        Assertions.assertEquals(expectedAmount, actualOutput.amount());
        Assertions.assertEquals(expectedAnnualPeriod, actualOutput.annualPeriod());
        Assertions.assertEquals(expectedAnnualRate, actualOutput.annualRate());
        Assertions.assertNotNull(actualOutput.result());
        Assertions.assertNotNull(actualOutput.createdAt());
        Assertions.assertNotNull(actualOutput.updatedAt());
        Assertions.assertNull(actualOutput.deletedAt());

        verify(investmentGateway, times(1)).findById(InvestmentID.from(expectedId));
    }

    @Test
    public void givenANonExistentId_whenCallsFindInvestmentById_thenReturnDomainException() {
        // given
        final var expectedId = InvestmentID.unique().getValue();

        when(investmentGateway.findById(InvestmentID.from(expectedId))).thenReturn(Optional.empty());

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> useCase.execute(expectedId)
        );

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(1, actualException.getErrors().size());
        Assertions.assertEquals(
                "Investment with id %s was not found".formatted(expectedId),
                actualException.getErrors().get(0).message()
        );

        verify(investmentGateway, times(1)).findById(InvestmentID.from(expectedId));
    }
}
