package br.zzz.infrastructure.api.controller;

import br.zzz.investimento.domain.wallet.WalletGateway;
import br.zzz.investimento.domain.wallet.WalletID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/internal/wallets")
public class InternalWalletController {

    private final WalletGateway walletGateway;

    public InternalWalletController(final WalletGateway walletGateway) {
        this.walletGateway = Objects.requireNonNull(walletGateway);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Void> exists(@PathVariable("id") final String id) {
        final var exists = walletGateway.existsById(WalletID.from(id));
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
