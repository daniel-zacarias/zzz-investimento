package br.zzz.infrastructure.configuration.usecase;

import br.zzz.investimento.application.wallet.retrieve.get.DefaultFindWalletsByUserIdUseCase;
import br.zzz.investimento.application.wallet.retrieve.get.FindWalletsByUserIdUseCase;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.wallet.WalletGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WalletUseCaseConfig {

    private final WalletGateway walletGateway;
    private final InvestmentGateway investmentGateway;

    public WalletUseCaseConfig(final WalletGateway walletGateway, final InvestmentGateway investmentGateway) {
        this.walletGateway = walletGateway;
        this.investmentGateway = investmentGateway;
    }

    @Bean
    public FindWalletsByUserIdUseCase findWalletsByUserIdUseCase() {
        return new DefaultFindWalletsByUserIdUseCase(walletGateway, investmentGateway);
    }
}
