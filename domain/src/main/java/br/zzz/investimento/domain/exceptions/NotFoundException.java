package br.zzz.investimento.domain.exceptions;

import br.zzz.investimento.domain.AggregateRoot;
import br.zzz.investimento.domain.Identifier;
import br.zzz.investimento.domain.validation.Error;

import java.util.List;

public class NotFoundException extends DomainException {
    protected NotFoundException(String aMessage, List<Error> anErrors) {
        super(aMessage, anErrors);
    }

    public static NotFoundException with(final Class<? extends AggregateRoot<?>> anAggregate, final Identifier identifier) {
        final var message = String.format("%s with id %s was not found", anAggregate.getSimpleName(), identifier.getValue());
        return new NotFoundException(message, List.of(new Error(message)));
    }
}
