package br.zzz.investimento.domain.exceptions;

import br.zzz.investimento.domain.validation.Error;
import br.zzz.investimento.domain.validation.handler.Notification;

import java.util.List;

public class NotificationException extends DomainException {

    public NotificationException(final String aMessage, final Notification aNotification) {
        super(aMessage, aNotification.getErrors());
    }
}
