package br.zzz.infrastructure.api.controller;

import br.zzz.infrastructure.api.WalletAPI;
import br.zzz.infrastructure.wallet.presenter.WalletPresenter;
import br.zzz.investimento.application.wallet.retrieve.get.FindWalletsByUserIdUseCase;
import br.zzz.infrastructure.wallet.models.WalletResponse;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class WalletController implements WalletAPI {

    private final FindWalletsByUserIdUseCase findWalletsByUserIdUseCase;

    public WalletController(final FindWalletsByUserIdUseCase findWalletsByUserIdUseCase) {
        this.findWalletsByUserIdUseCase = Objects.requireNonNull(findWalletsByUserIdUseCase);
    }

    @Override
    public WalletResponse getByUserId(final String userId) {
        return WalletPresenter.present(findWalletsByUserIdUseCase.execute(userId));
    }
}
