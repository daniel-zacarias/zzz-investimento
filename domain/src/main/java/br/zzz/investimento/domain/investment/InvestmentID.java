package br.zzz.investimento.domain.investment;

import br.zzz.investimento.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class InvestmentID extends Identifier {
    private final String value;

    private InvestmentID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static InvestmentID from(String value) {
        return new InvestmentID(value);
    }

    public static InvestmentID unique() {
        return new InvestmentID(UUID.randomUUID().toString());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InvestmentID that = (InvestmentID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
