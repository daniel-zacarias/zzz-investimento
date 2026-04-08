package br.zzz.investimento.application.wallet.create;

import br.zzz.investimento.application.UseCaseTest;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.wallet.WalletGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Objects;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateWalletUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateWalletUseCase useCase;

    @Mock
    private WalletGateway walletGateway;

    @Override
    protected Object[] getMocks() {
        return new Object[]{
                walletGateway
        };
    }

    @Test
    public void givenAValidCommand_whenCallsCreateWallet_thenReturnWalletOutput() {
        // given
        final var expectedUserId = UUID.randomUUID().toString();
        final var expectedName = "Renda Fixa";
        final var aCommand = CreateWalletCommand.with(expectedUserId, expectedName);

        when(walletGateway.create(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.walletId());
        Assertions.assertEquals(expectedUserId, actualOutput.userId());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertNotNull(actualOutput.createdAt());
        Assertions.assertNotNull(actualOutput.updatedAt());
        Assertions.assertNull(actualOutput.deletedAt());

        verify(walletGateway, times(1)).create(argThat(wallet ->
                Objects.equals(wallet.getUserId(), UserID.from(expectedUserId))
                        && Objects.equals(wallet.getName(), expectedName)
                        && Objects.nonNull(wallet.getId())
                        && Objects.nonNull(wallet.getCreatedAt())
                        && Objects.nonNull(wallet.getUpdatedAt())
                        && Objects.isNull(wallet.getDeletedAt())
                        && wallet.getInvestments().isEmpty()
        ));
    }

    @Test
    public void givenAValidCommand_whenCallsCreateWallet_thenUseCorrectUserId() {
        // given
        final var expectedUserId = UUID.randomUUID().toString();
        final var expectedName = "Renda Fixa";
        final var aCommand = CreateWalletCommand.with(expectedUserId, expectedName);

        when(walletGateway.create(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        useCase.execute(aCommand);

        // then
        verify(walletGateway, times(1)).create(argThat(wallet ->
                Objects.equals(wallet.getUserId(), UserID.from(expectedUserId))
                        && Objects.equals(wallet.getName(), expectedName)
        ));
    }
}
