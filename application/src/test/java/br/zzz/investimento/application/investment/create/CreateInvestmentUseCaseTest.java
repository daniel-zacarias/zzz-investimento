package br.zzz.investimento.application.investment.create;

import br.zzz.investimento.application.UseCaseTest;
import br.zzz.investimento.domain.exceptions.NotificationException;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.wallet.WalletGateway;
import br.zzz.investimento.domain.wallet.WalletID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateInvestmentUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateInvestmentUseCase useCase;

    @Mock
    private InvestmentGateway investmentGateway;

    @Mock
    private WalletGateway walletGateway;

    @Override
    protected Object[] getMocks() {
        return new Object[]{
                investmentGateway,
                walletGateway
        };
    }

    @Test
    public void givenAValidCommand_whenCallsCreateInvestment_thenReturnInvestmentID() {
        // given
        final var expectedAmount = new BigDecimal("1000.0");
        final var expectedAnnualPeriod = 5;
        final var expectedAnnualRate = new BigDecimal("0.01");
        final var expectedWalletID = WalletID.unique().getValue();

        final var aCommand = CreateInvestmentCommand.with(expectedAmount, expectedAnnualPeriod, expectedAnnualRate, expectedWalletID);

        // when
        when(walletGateway.existsById(any())).thenReturn(true);
        when(investmentGateway.create(any())).thenAnswer(invocation -> invocation.getArgument(0));

        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(walletGateway, times(1)).existsById(WalletID.from(expectedWalletID));
        verify(investmentGateway, times(1)).create(argThat(investment ->
                Objects.equals(investment.getAmount(), expectedAmount)
                        && Objects.equals(investment.getAnnualPeriod(), expectedAnnualPeriod)
                        && Objects.equals(investment.getAnnualRate(), expectedAnnualRate)
                        && Objects.equals(investment.getWallet().getValue(), expectedWalletID)
                        && Objects.nonNull(investment.getCreatedAt())
                        && Objects.nonNull(investment.getUpdatedAt())
                        && Objects.isNull(investment.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithNonExistingWallet_whenCallsCreateInvestment_thenReturnNotificationException() {
        // given
        final var expectedAmount = new BigDecimal("1000.0");
        final var expectedAnnualPeriod = 5;
        final var expectedAnnualRate = new BigDecimal("0.01");
        final var expectedWalletID = WalletID.unique().getValue();

        final var aCommand = CreateInvestmentCommand.with(
                expectedAmount,
                expectedAnnualPeriod,
                expectedAnnualRate,
                expectedWalletID);

        // when
        when(walletGateway.existsById(any())).thenReturn(false);

        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(1, actualException.getErrors().size());
        Assertions.assertEquals(
                "Wallet with ID %s was not found".formatted(expectedWalletID),
                actualException.getErrors().get(0).message()
        );

        verify(walletGateway, times(1)).existsById(eq(WalletID.from(expectedWalletID)));
        verify(investmentGateway, times(0)).create(any());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreateInvestment_thenReturnNotificationException() {
        // given
        final var expectedAmount = new BigDecimal("1000.0");
        final var expectedAnnualPeriod = 0;
        final var expectedAnnualRate = new BigDecimal("0.01");
        final var expectedWalletID = WalletID.unique().getValue();

        final var aCommand = CreateInvestmentCommand.with(expectedAmount, expectedAnnualPeriod, expectedAnnualRate, expectedWalletID);

        // when
        when(walletGateway.existsById(any())).thenReturn(true);

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
        verify(investmentGateway, times(0)).create(any());
    }
}
