package br.zzz.infrastructure.api;

import br.zzz.infrastructure.investment.persistence.InvestmentRepository;
import br.zzz.infrastructure.wallet.WalletPostgresGateway;
import br.zzz.investimento.application.wallet.retrieve.list.ListWalletsByUserIdUseCase;
import br.zzz.investimento.application.wallet.retrieve.list.ListWalletOutput;
import br.zzz.investimento.domain.investment.InvestmentID;
import br.zzz.investimento.domain.pagination.Pagination;
import br.zzz.investimento.domain.user.UserID;
import br.zzz.investimento.domain.wallet.WalletID;
import br.zzz.investimento.domain.wallet.WalletSearchQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
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
    private ListWalletsByUserIdUseCase listWalletsByUserIdUseCase;

    // When JPA auto-config is disabled in this test, mocking the gateway avoids requiring a JPA repository bean.
    @MockitoBean
    private WalletPostgresGateway walletGateway;

    @MockitoBean
    private InvestmentRepository investmentRepository;

    @Test
    void givenAUserId_whenCallsListWalletsPaginated_thenReturnsWallets() throws Exception {
        final var aUserId = "user-123";
        final var aWalletName = "Renda Fixa";
        final var createdAt = Instant.parse("2026-03-13T10:15:30Z");

        when(listWalletsByUserIdUseCase.execute(any(WalletSearchQuery.class))).thenReturn(
                new Pagination<>(
                        0,
                        10,
                        1,
                        List.of(
                                new ListWalletOutput(
                                        WalletID.from("wallet-1"),
                                        aUserId,
                                        aWalletName,
                                        Set.of(InvestmentID.from("inv-1"), InvestmentID.from("inv-2")),
                                        new BigDecimal("1500.00"),
                                        createdAt,
                                        createdAt,
                                        null
                                )
                        )
                )
        );

        mockMvc.perform(get("/wallets")
                        .queryParam("userId", aUserId)
                        .queryParam("page", "0")
                        .queryParam("perPage", "10")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.perPage").value(10))
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].id").value("wallet-1"))
                .andExpect(jsonPath("$.items[0].userId").value(aUserId))
                .andExpect(jsonPath("$.items[0].name").value(aWalletName))
                .andExpect(jsonPath("$.items[0].totalAmount").value(1500.0));

        verify(listWalletsByUserIdUseCase).execute(
                new WalletSearchQuery(
                        0,
                        10,
                        null,
                        "createdAt",
                        "asc",
                        UserID.from(aUserId)
                )
        );
    }
}
