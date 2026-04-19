package br.zzz.investimento.investmentservice.configuration.usecase;

import br.zzz.investimento.application.investment.create.CreateInvestmentUseCase;
import br.zzz.investimento.application.investment.create.DefaultCreateInvestmentUseCase;
import br.zzz.investimento.application.investment.delete.DefaultDeleteInvestmentByIdUseCase;
import br.zzz.investimento.application.investment.delete.DeleteInvestmentByIdUseCase;
import br.zzz.investimento.application.investment.retrieve.get.DefaultFindInvestmentByIdUseCase;
import br.zzz.investimento.application.investment.retrieve.get.FindInvestmentByIdUseCase;
import br.zzz.investimento.application.investment.update.DefaultUpdateInvestmentUseCase;
import br.zzz.investimento.application.investment.update.UpdateInvestmentUseCase;
import br.zzz.investimento.domain.investment.InvestmentGateway;
import br.zzz.investimento.domain.wallet.WalletGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class InvestmentUseCaseConfig {

    private final InvestmentGateway investmentGateway;
    private final WalletGateway walletGateway;

    public InvestmentUseCaseConfig(final InvestmentGateway investmentGateway,
                                   final WalletGateway walletGateway) {
        this.investmentGateway = Objects.requireNonNull(investmentGateway);
        this.walletGateway = Objects.requireNonNull(walletGateway);
    }

    @Bean
    public CreateInvestmentUseCase createInvestmentUseCase() {
        return new DefaultCreateInvestmentUseCase(investmentGateway, walletGateway);
    }

    @Bean
    public DeleteInvestmentByIdUseCase deleteInvestmentByIdUseCase() {
        return new DefaultDeleteInvestmentByIdUseCase(investmentGateway);
    }

    @Bean
    public FindInvestmentByIdUseCase findInvestmentByIdUseCase() {
        return new DefaultFindInvestmentByIdUseCase(investmentGateway);
    }

    @Bean
    public UpdateInvestmentUseCase updateInvestmentUseCase() {
        return new DefaultUpdateInvestmentUseCase(investmentGateway);
    }
}
