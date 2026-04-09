package br.zzz.investimento.domain.wallet;

import br.zzz.investimento.domain.exceptions.NotificationException;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.user.UserID;
import org.junit.jupiter.api.Test;
import br.zzz.investimento.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    private static final String WALLET_NAME = "Geral";

    @Test
    void givenNullValue_whenFrom_thenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> WalletID.from(null));
    }

    @Test
    void givenSameValue_whenFrom_thenShouldBeEqual() {
        final var id1 = WalletID.from("123");
        final var id2 = WalletID.from("123");

        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
        assertEquals("123", id1.getValue());
    }

    @Test
    void givenTwoUniques_whenCreate_thenShouldBeDifferent() {
        final var id1 = WalletID.unique();
        final var id2 = WalletID.unique();

        assertNotNull(id1.getValue());
        assertNotNull(id2.getValue());
        assertNotEquals(id1, id2);
    }

    @Test
    void givenValues_whenCreate_thenExposeFields() {
        final var userId = UserID.unique();

        final var query = new WalletSearchQuery(
                0,
                10,
                "some terms",
                "createdAt",
                "asc",
                userId);

        assertEquals(0, query.page());
        assertEquals(10, query.perPage());
        assertEquals("some terms", query.terms());
        assertEquals("createdAt", query.sort());
        assertEquals("asc", query.direction());
        assertEquals(userId, query.userId());
    }

    @Test
    void givenSameValues_whenCompare_thenEqualsAndHashCode() {
        final var userId = UserID.unique();

        final var q1 = new WalletSearchQuery(1, 20, "t", "id", "desc", userId);
        final var q2 = new WalletSearchQuery(1, 20, "t", "id", "desc", userId);

        assertEquals(q1, q2);
        assertEquals(q1.hashCode(), q2.hashCode());
    }

    @Test
    void givenValidValues_whenNewWallet_thenCreate() {
        final var userId = UserID.unique();
        final var investments = new HashSet<>(Set.of(InvestmentID.unique(), InvestmentID.unique()));
        final var walletName = "Renda Fixa";

        final var wallet = Wallet.newWallet(userId, walletName, investments);

        assertNotNull(wallet.getId());
        assertEquals(userId, wallet.getUserId());
        assertEquals(walletName, wallet.getName());
        assertEquals(investments, wallet.getInvestments());
        assertNotNull(wallet.getCreatedAt());
        assertNotNull(wallet.getUpdatedAt());
        assertNull(wallet.getDeletedAt());

        assertThrows(UnsupportedOperationException.class,
                () -> wallet.getInvestments().add(InvestmentID.unique()));
    }

    @Test
    void givenEmptyInvestments_whenNewWallet_thenCreateWithEmptySet() {
        final var wallet = Wallet.newWallet(UserID.unique(), WALLET_NAME);

        assertNotNull(wallet.getId());
        assertEquals(WALLET_NAME, wallet.getName());
        assertTrue(wallet.getInvestments().isEmpty());
    }

    @Test
    void givenNullUserId_whenNewWallet_thenThrowsNotificationException() {
        final var exception = assertThrows(
                NotificationException.class,
                () -> Wallet.newWallet(null, WALLET_NAME, Set.of(InvestmentID.unique())));

        assertEquals("Wallet validation failed", exception.getMessage());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("User ID should not be null", exception.getErrors().get(0).message());
    }

    @Test
    void givenNameLongerThan100_whenNewWallet_thenThrowsNotificationException() {
        final var tooLongName = "a".repeat(101);

        final var exception = assertThrows(
                NotificationException.class,
                () -> Wallet.newWallet(UserID.unique(), tooLongName, Set.of()));

        assertEquals("Wallet validation failed", exception.getMessage());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Wallet name should not be greater than 100 characters", exception.getErrors().get(0).message());
    }

    @Test
    void givenNullInvestments_whenNewWallet_thenThrowsNotificationException() {
        final var exception = assertThrows(
                NotificationException.class,
                () -> Wallet.newWallet(UserID.unique(), WALLET_NAME, null));

        assertEquals("Wallet validation failed", exception.getMessage());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Investment IDs should not be null", exception.getErrors().get(0).message());
    }

    @Test
    void givenNullName_whenNewWallet_thenThrowsNotificationException() {
        final var exception = assertThrows(
                NotificationException.class,
                () -> Wallet.newWallet(UserID.unique(), null, Set.of()));

        assertEquals("Wallet validation failed", exception.getMessage());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Wallet name should not be null", exception.getErrors().get(0).message());
    }

    @Test
    void givenBlankName_whenNewWallet_thenThrowsNotificationException() {
        final var exception = assertThrows(
                NotificationException.class,
                () -> Wallet.newWallet(UserID.unique(), "  ", Set.of()));

        assertEquals("Wallet validation failed", exception.getMessage());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Wallet name should not be blank", exception.getErrors().get(0).message());
    }

    @Test
    void givenNullUserId_whenCreateWith_thenThrowsNotificationException() {
        final var exception = assertThrows(
                NotificationException.class,
                () -> Wallet.with(
                        WalletID.unique(),
                        null,
                        WALLET_NAME,
                        Set.of(),
                        Instant.now(),
                        Instant.now(),
                        null));

        assertEquals("Failed to create a Wallet", exception.getMessage());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("User ID should not be null", exception.getErrors().get(0).message());
    }

    @Test
    void givenNullInvestments_whenCreateWith_thenThrowsNotificationException() {
        final var exception = assertThrows(
                NotificationException.class,
                () -> Wallet.with(
                        WalletID.unique(),
                        UserID.unique(),
                        WALLET_NAME,
                        null,
                        Instant.now(),
                        Instant.now(),
                        null));

        assertEquals("Failed to create a Wallet", exception.getMessage());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertEquals("Investment IDs should not be null", exception.getErrors().get(0).message());
    }

    @Test
    void givenSameId_whenCompare_thenEqualsAndHashCode() {
        final var wallet = Wallet.newWallet(UserID.unique(), WALLET_NAME);

        final var other = Wallet.with(
                wallet.getId(),
                UserID.unique(),
                "Outra",
                Set.of(),
                wallet.getCreatedAt(),
                wallet.getUpdatedAt(),
                wallet.getDeletedAt());

        assertEquals(wallet, other);
        assertEquals(wallet.hashCode(), other.hashCode());
    }

    @Test
    void givenNullUserId_whenValidate_thenAppendError() {
        final var notification = Notification.create();

        WalletValidator.validate(null, WALLET_NAME, Set.of(InvestmentID.unique()), notification);

        assertTrue(notification.hasError());
        assertEquals(1, notification.getErrors().size());
        assertEquals("User ID should not be null", notification.getErrors().get(0).message());
    }

    @Test
    void givenNullInvestments_whenValidate_thenAppendError() {
        final var notification = Notification.create();

        WalletValidator.validate(UserID.unique(), WALLET_NAME, null, notification);

        assertTrue(notification.hasError());
        assertEquals(1, notification.getErrors().size());
        assertEquals("Investment IDs should not be null", notification.getErrors().get(0).message());
    }

    @Test
    void givenEmptyInvestments_whenValidate_thenNoError() {
        final var notification = Notification.create();

        WalletValidator.validate(UserID.unique(), WALLET_NAME, Set.of(), notification);

        assertFalse(notification.hasError());
    }
}
