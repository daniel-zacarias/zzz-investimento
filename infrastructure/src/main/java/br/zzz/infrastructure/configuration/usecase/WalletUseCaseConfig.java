package br.zzz.infrastructure.configuration.usecase;

import br.zzz.investimento.application.wallet.retrieve.get.DefaultFindWalletsByUserIdUseCase;
import br.zzz.investimento.application.wallet.retrieve.get.FindWalletsByUserIdUseCase;
import br.zzz.investimento.domain.wallet.WalletGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WalletUseCaseConfig {

    private final WalletGateway walletGateway;

    public WalletUseCaseConfig(final WalletGateway walletGateway) {
        this.walletGateway = walletGateway;
    }

    @Bean
    public FindWalletsByUserIdUseCase findWalletsByUserIdUseCase() {
        return new DefaultFindWalletsByUserIdUseCase(walletGateway);
    }
}
