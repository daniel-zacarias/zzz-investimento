package br.zzz.infrastructure.api.controller;

import br.zzz.infrastructure.api.WalletAPI;
import br.zzz.infrastructure.wallet.models.CreateWalletRequest;
import br.zzz.infrastructure.wallet.models.WalletResponse;
import br.zzz.infrastructure.wallet.presenter.WalletPresenter;
import br.zzz.investimento.application.wallet.create.CreateWalletCommand;
import br.zzz.investimento.application.wallet.create.CreateWalletUseCase;
import br.zzz.investimento.application.wallet.retrieve.get.FindWalletsByUserIdUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
public class WalletController implements WalletAPI {

    private final FindWalletsByUserIdUseCase findWalletsByUserIdUseCase;
    private final CreateWalletUseCase createWalletUseCase;

    public WalletController(final FindWalletsByUserIdUseCase findWalletsByUserIdUseCase,
                            final CreateWalletUseCase createWalletUseCase) {
        this.findWalletsByUserIdUseCase = Objects.requireNonNull(findWalletsByUserIdUseCase);
        this.createWalletUseCase = Objects.requireNonNull(createWalletUseCase);
    }

    @Override
    public List<WalletResponse> getByUserId(final String userId) {
        return WalletPresenter.present(findWalletsByUserIdUseCase.execute(userId));
    }

    @Override
    public ResponseEntity<?> create(final CreateWalletRequest request) {
        final var command = CreateWalletCommand.with(request.userId(), request.name());
        final var output = createWalletUseCase.execute(command);
        return ResponseEntity.created(URI.create("/api/wallets/" + output.walletId())).body(output);
    }
}
