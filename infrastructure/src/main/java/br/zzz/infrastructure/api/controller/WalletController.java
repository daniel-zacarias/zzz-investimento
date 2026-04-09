package br.zzz.infrastructure.api.controller;

import br.zzz.infrastructure.api.WalletAPI;
import br.zzz.infrastructure.wallet.models.CreateWalletRequest;
import br.zzz.infrastructure.wallet.models.WalletResponse;
import br.zzz.infrastructure.wallet.presenter.WalletPresenter;
import br.zzz.investimento.application.wallet.create.CreateWalletCommand;
import br.zzz.investimento.application.wallet.create.CreateWalletUseCase;
import br.zzz.investimento.application.wallet.retrieve.list.ListWalletsByUserIdUseCase;
import br.zzz.investimento.domain.pagination.Pagination;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.wallet.WalletSearchQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class WalletController implements WalletAPI {

    private final ListWalletsByUserIdUseCase listWalletsByUserIdUseCase;
    private final CreateWalletUseCase createWalletUseCase;

    public WalletController(final ListWalletsByUserIdUseCase listWalletsByUserIdUseCase,
                            final CreateWalletUseCase createWalletUseCase) {
        this.listWalletsByUserIdUseCase = Objects.requireNonNull(listWalletsByUserIdUseCase);
        this.createWalletUseCase = Objects.requireNonNull(createWalletUseCase);
    }

    @Override
    public Pagination<WalletResponse> listByUserId(
            final String userId,
            final int page,
            final int perPage,
            final String terms,
            final String sort,
            final String direction
    ) {
        final var query = new WalletSearchQuery(
                page,
                perPage,
                terms,
                sort,
                direction,
                UserID.from(userId)
        );

        final var outputPage = this.listWalletsByUserIdUseCase.execute(query);
        return WalletPresenter.present(outputPage);
    }

    @Override
    public ResponseEntity<?> create(final CreateWalletRequest request) {
        final var command = CreateWalletCommand.with(request.userId(), request.name());
        final var output = createWalletUseCase.execute(command);
        return ResponseEntity.created(URI.create("/api/wallets/" + output.walletId())).body(output);
    }
}
