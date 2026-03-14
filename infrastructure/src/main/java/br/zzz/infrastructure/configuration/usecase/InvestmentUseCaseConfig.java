package br.zzz.infrastructure.configuration.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.zzz.investimento.application.investment.create.CreateInvestmentUseCase;
import br.zzz.investimento.application.investment.create.DefaultCreateInvestmentUseCase;
import br.zzz.investimento.application.investment.delete.DefaultDeleteInvestmentByIdUseCase;
import br.zzz.investimento.application.investment.delete.DeleteInvestmentByIdUseCase;
import br.zzz.investimento.application.investment.retrieve.get.DefaultFindInvestmentByIdUseCase;
import br.zzz.investimento.application.investment.retrieve.get.FindInvestmentByIdUseCase;
import br.zzz.investimento.application.investment.update.DefaultUpdateInvestmentUseCase;
import br.zzz.investimento.application.investment.update.UpdateInvestmentUseCase;
import br.zzz.investimento.domain.investment.InvestmentGateway;

@Configuration
public class InvestmentUseCaseConfig {
    private final InvestmentGateway investmentGateway;

    public InvestmentUseCaseConfig(InvestmentGateway investmentGateway) {
        this.investmentGateway = investmentGateway;
    }

    @Bean
    public CreateInvestmentUseCase createInvestmentUseCase() {
        return new DefaultCreateInvestmentUseCase(investmentGateway);
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
