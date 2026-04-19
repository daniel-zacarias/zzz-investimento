package br.zzz.investimento.domain.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler aHandler);

    <T> T validate(Validation<T> aValidation);

    List<Error> getErrors();

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default Error firstError(){
        if (hasError()){
            return getErrors().get(0);
        }
        return null;
    }

    interface Validation<T> {
        T validate();
    }
}
