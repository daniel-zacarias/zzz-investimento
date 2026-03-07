package br.zzz.investimento.domain.investiment;

import br.zzz.investimento.domain.Identifier;
import br.zzz.investimento.domain.validation.ValidationHandler;

public class InvestimentID extends Identifier {
    private final String value;

    private InvestimentID(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }
}
