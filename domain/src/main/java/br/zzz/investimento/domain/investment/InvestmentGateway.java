package br.zzz.investimento.domain.investment;

import java.util.Optional;

public interface InvestmentGateway {

    Investment create(Investment investment);

    void deleteById(InvestmentID id);

    Optional<Investment> findById(InvestmentID id);

    Investment update(Investment investment);

}
