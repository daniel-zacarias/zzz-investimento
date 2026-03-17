package br.zzz.investimento.domain.user;

import br.zzz.investimento.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class UserID extends Identifier {
    private final String value;

    private UserID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static UserID from(String value) {
        return new UserID(value);
    }

    public static UserID unique() {
        return new UserID(UUID.randomUUID().toString());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserID that = (UserID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
