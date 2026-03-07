package br.zzz.investimento.domain.investment;

import static org.junit.jupiter.api.Assertions.*;

import br.zzz.investimento.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class InvestmentTest {

    @Test
    void givenValidValues_whenCreateInvestment_thenCalculateAnnualCompoundResult() {
        final var investment = Investment.newInvestment(
                2,
                new BigDecimal("1500.00"),
                new BigDecimal("0.10"));

        assertEquals(new BigDecimal("1815.00"), investment.getResult());
    }

    @Test
    void givenAnnualPeriodZero_whenCreateInvestment_thenThrowsNotificationException() {
        final var exception = assertThrows(
                NotificationException.class,
                () -> Investment.newInvestment(
                        0,
                        new BigDecimal("1000.00"),
                        new BigDecimal("0.10")));

        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Annual period should be greater than zero", exception.getErrors().get(0).message());
    }

}