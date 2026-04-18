package br.zzz.investimento.application.investment.update;

import br.zzz.investimento.application.UseCaseTest;
import br.zzz.investimento.domain.exceptions.DomainException;
import br.zzz.investimento.domain.exceptions.NotificationException;
import br.zzz.investimento.domain.investment.Investment;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.wallet.WalletID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UpdateInvestmentUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateInvestmentUseCase useCase;

    @Mock
    private InvestmentGateway investmentGateway;

    @Override
    protected Object[] getMocks() {
        return new Object[]{investmentGateway};
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateInvestment_thenReturnInvestmentID() {
        // given
        final var existingInvestment = Investment.newInvestment(5, new BigDecimal("1000.0"), new BigDecimal("0.01"), WalletID.unique());
        final var expectedId = existingInvestment.getId().getValue();
        final var expectedAmount = new BigDecimal("2000.0");
        final var expectedAnnualPeriod = 10;
        final var expectedAnnualRate = new BigDecimal("0.05");
        final var expectedMonthAmount = new BigDecimal("100.0");

        final var aCommand = UpdateInvestmentCommand.with(
                expectedId,
                expectedAmount,
                expectedAnnualPeriod,
                expectedAnnualRate,
                expectedMonthAmount
        );

        when(investmentGateway.findById(InvestmentID.from(expectedId))).thenReturn(Optional.of(existingInvestment));
        when(investmentGateway.update(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId, actualOutput.id());

        verify(investmentGateway, times(1)).findById(InvestmentID.from(expectedId));
        verify(investmentGateway, times(1)).update(argThat(investment ->
                Objects.equals(investment.getId().getValue(), expectedId)
                        && Objects.equals(investment.getAmount(), expectedAmount)
                        && Objects.equals(investment.getAnnualPeriod(), expectedAnnualPeriod)
                        && Objects.equals(investment.getAnnualRate(), expectedAnnualRate)
                        && Objects.equals(investment.getMonthAmount(), expectedMonthAmount)
                        && Objects.nonNull(investment.getUpdatedAt())
        ));
    }

    @Test
    public void givenAnInvalidAnnualPeriod_whenCallsUpdateInvestment_thenReturnNotificationException() {
        // given
        final var existingInvestment = Investment.newInvestment(5, new BigDecimal("1000.0"), new BigDecimal("0.01"), WalletID.unique());
        final var expectedId = existingInvestment.getId().getValue();
        final var expectedAnnualPeriod = 0;

        final var aCommand = UpdateInvestmentCommand.with(
                expectedId,
                new BigDecimal("1000.0"),
                expectedAnnualPeriod,
                new BigDecimal("0.01"),
                BigDecimal.ZERO
        );

        when(investmentGateway.findById(InvestmentID.from(expectedId))).thenReturn(Optional.of(existingInvestment));

        // when
        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(1, actualException.getErrors().size());
        Assertions.assertEquals(
                "Annual period should be greater than zero",
                actualException.getErrors().get(0).message()
        );

        verify(investmentGateway, times(1)).findById(InvestmentID.from(expectedId));
        verify(investmentGateway, times(0)).update(any());
    }

    @Test
    public void givenANonExistentId_whenCallsUpdateInvestment_thenReturnDomainException() {
        // given
        final var expectedId = InvestmentID.unique().getValue();
        final var aCommand = UpdateInvestmentCommand.with(
                expectedId,
                new BigDecimal("1000.0"),
                5,
                new BigDecimal("0.01"),
                BigDecimal.ZERO
        );

        when(investmentGateway.findById(InvestmentID.from(expectedId))).thenReturn(Optional.empty());

        // when
        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(1, actualException.getErrors().size());
        Assertions.assertEquals(
                "Investment with id %s was not found".formatted(expectedId),
                actualException.getErrors().get(0).message()
        );

        verify(investmentGateway, times(1)).findById(InvestmentID.from(expectedId));
        verify(investmentGateway, times(0)).update(any());
    }
}
