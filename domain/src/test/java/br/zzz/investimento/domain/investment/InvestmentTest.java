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

        assertEquals(BigDecimal.ZERO, investment.getMonthAmount());
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

    @Test
    void givenNegativeAnnualPeriod_whenCreateInvestment_thenThrowsNotificationException() {
        final var exception = assertThrows(
                NotificationException.class,
                () -> Investment.newInvestment(
                        -1,
                        new BigDecimal("1000.00"),
                        new BigDecimal("0.10")));

        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Annual period should be greater than zero", exception.getErrors().get(0).message());
    }

    @Test
    void givenNullAmount_whenCreateInvestment_thenThrowsNotificationException() {
        final var exception = assertThrows(
                NotificationException.class,
                () -> Investment.newInvestment(
                        2,
                        null,
                        new BigDecimal("0.10")));

        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Amount should not be null", exception.getErrors().get(0).message());
    }

    @Test
    void givenNullAnnualPeriod_whenCreateInvestment_thenThrowsNotificationException() {
        final var exception = assertThrows(
                NotificationException.class,
                () -> Investment.newInvestment(
                        null,
                        new BigDecimal("1000.00"),
                        new BigDecimal("0.10")));

        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Annual period should not be null", exception.getErrors().get(0).message());
    }

    @Test
    void givenNullAnnualRate_whenCreateInvestment_thenThrowsNotificationException() {
        final var exception = assertThrows(
                NotificationException.class,
                () -> Investment.newInvestment(
                        2,
                        new BigDecimal("1000.00"),
                        null));

        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Annual rate should not be null", exception.getErrors().get(0).message());
    }

    @Test
    void givenValidInvestment_whenUpdate_thenRecalculateResult() {
        final var investment = Investment.newInvestment(
                2,
                new BigDecimal("1000.00"),
                new BigDecimal("0.10"));

        final var updated = investment.update(
                3,
                new BigDecimal("2000.00"),
                new BigDecimal("0.15"));

        assertEquals(investment.getMonthAmount(), updated.getMonthAmount());
        assertEquals(3, updated.getAnnualPeriod());
        assertEquals(new BigDecimal("2000.00"), updated.getAmount());
        assertEquals(new BigDecimal("0.15"), updated.getAnnualRate());
        assertEquals(new BigDecimal("3041.75"), updated.getResult());
        assertEquals(investment.getId(), updated.getId());
        assertNull(updated.getDeletedAt());
    }

    @Test
    void givenValidInvestment_whenDelete_thenSetDeletedAt() {
        final var investment = Investment.newInvestment(
                2,
                new BigDecimal("1000.00"),
                new BigDecimal("0.10"));

        final var deleted = investment.delete();

        assertNotNull(deleted.getDeletedAt());
        assertEquals(investment.getId(), deleted.getId());
        assertEquals(investment.getAnnualPeriod(), deleted.getAnnualPeriod());
        assertEquals(investment.getAmount(), deleted.getAmount());
        assertEquals(investment.getResult(), deleted.getResult());
        assertEquals(investment.getAnnualRate(), deleted.getAnnualRate());
        assertEquals(investment.getMonthAmount(), deleted.getMonthAmount());
        assertEquals(investment.getWallet(), deleted.getWallet());
    }

    @Test
    void givenOneYearPeriod_whenCalculate_thenReturnCorrectCompoundInterest() {
        final var investment = Investment.newInvestment(
                1,
                new BigDecimal("1000.00"),
                new BigDecimal("0.12"));

        assertEquals(new BigDecimal("1120.00"), investment.getResult());
    }

    @Test
    void givenFiveYearPeriod_whenCalculate_thenReturnCorrectCompoundInterest() {
        final var investment = Investment.newInvestment(
                5,
                new BigDecimal("5000.00"),
                new BigDecimal("0.08"));

        assertEquals(new BigDecimal("7346.64"), investment.getResult());
    }

    @Test
    void givenZeroRate_whenCalculate_thenReturnOriginalAmount() {
        final var investment = Investment.newInvestment(
                3,
                new BigDecimal("1000.00"),
                new BigDecimal("0.00"));

        assertEquals(new BigDecimal("1000.00"), investment.getResult());
    }

    @Test
    void givenMonthlyContribution_whenCreateInvestment_thenIncludeAnnuityInResult() {
        final var investment = Investment.newInvestment(
                1,
                new BigDecimal("1000.00"),
                new BigDecimal("0.12"),
                new BigDecimal("100.00"));

        assertEquals(new BigDecimal("100.00"), investment.getMonthAmount());
        assertEquals(new BigDecimal("2384.65"), investment.getResult());
    }

}