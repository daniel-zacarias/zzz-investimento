package br.zzz.investimento.application.investment.delete;

import br.zzz.investimento.application.UseCaseTest;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.investment.InvestmentID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

public class DeleteInvestmentByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteInvestmentByIdUseCase useCase;

    @Mock
    private InvestmentGateway investmentGateway;

    @Override
    protected Object[] getMocks() {
        return new Object[]{investmentGateway};
    }

    @Test
    public void givenAValidId_whenCallsDeleteInvestmentById_thenBeOk() {
        // given
        final var expectedId = InvestmentID.unique().getValue();

        doNothing().when(investmentGateway).deleteById(InvestmentID.from(expectedId));

        // when / then
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId));

        verify(investmentGateway, times(1)).deleteById(InvestmentID.from(expectedId));
    }

    @Test
    public void givenANonExistentId_whenCallsDeleteInvestmentById_thenBeOk() {
        // given
        final var expectedId = InvestmentID.unique().getValue();

        doNothing().when(investmentGateway).deleteById(InvestmentID.from(expectedId));

        // when / then
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId));

        verify(investmentGateway, times(1)).deleteById(InvestmentID.from(expectedId));
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_thenReturnException() {
        // given
        final var expectedId = InvestmentID.unique().getValue();

        doThrow(new IllegalStateException("Gateway error"))
                .when(investmentGateway).deleteById(InvestmentID.from(expectedId));

        // when / then
        Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId)
        );

        verify(investmentGateway, times(1)).deleteById(InvestmentID.from(expectedId));
    }
}
