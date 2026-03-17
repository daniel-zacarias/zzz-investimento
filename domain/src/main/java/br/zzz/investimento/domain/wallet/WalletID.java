package br.zzz.investimento.domain.wallet;

import br.zzz.investimento.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class WalletID extends Identifier {
    private final String value;

    private WalletID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static WalletID from(String value) {
        return new WalletID(value);
    }

    public static WalletID unique() {
        return new WalletID(UUID.randomUUID().toString());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        WalletID that = (WalletID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
