package br.zzz.infrastructure.api;

import br.zzz.infrastructure.investment.persistence.InvestmentRepository;
import br.zzz.infrastructure.wallet.WalletPostgresGateway;
import br.zzz.infrastructure.wallet.persistence.WalletRepository;
import br.zzz.investimento.application.wallet.retrieve.get.FindWalletsByUserIdUseCase;
import br.zzz.investimento.application.wallet.retrieve.get.WalletOutput;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.wallet.WalletID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
@AutoConfigureMockMvc
class WalletAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FindWalletsByUserIdUseCase findWalletsByUserIdUseCase;

    // When JPA auto-config is disabled in this test, mocking the gateway avoids requiring a JPA repository bean.
    @MockitoBean
    private WalletPostgresGateway walletGateway;

    @MockitoBean
    private InvestmentRepository investmentRepository;

    @Test
    void givenAUserId_whenCallsGetWalletByUserId_thenReturnsWallet() throws Exception {
        final var aUserId = "user-123";
        final var createdAt = Instant.parse("2026-03-13T10:15:30Z");

        when(findWalletsByUserIdUseCase.execute(aUserId)).thenReturn(
                new WalletOutput(
                        WalletID.from("wallet-1"),
                        aUserId,
                        Set.of(InvestmentID.from("inv-1"), InvestmentID.from("inv-2")),
                        new BigDecimal("1500.00"),
                        createdAt,
                        createdAt,
                        null
                )
        );

        mockMvc.perform(get("/wallets/{userId}", aUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("wallet-1"))
                .andExpect(jsonPath("$.userId").value(aUserId))
                .andExpect(jsonPath("$.totalAmount").value(1500.0));

        verify(findWalletsByUserIdUseCase).execute(aUserId);
    }
}
