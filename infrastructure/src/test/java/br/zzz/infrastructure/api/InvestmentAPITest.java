package br.zzz.infrastructure.api;

import br.zzz.infrastructure.investment.client.InvestmentServiceClient;
import br.zzz.infrastructure.investment.models.CreateInvestmentRequest;
import br.zzz.infrastructure.investment.models.CreateInvestmentResult;
import br.zzz.infrastructure.investment.models.InvestmentResponse;
import br.zzz.infrastructure.investment.models.UpdateInvestmentRequest;
import br.zzz.infrastructure.investment.models.UpdateInvestmentResult;
import br.zzz.investimento.domain.investment.InvestmentID;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.zzz.infrastructure.wallet.persistence.WalletRepository;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = {
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
@AutoConfigureMockMvc
class InvestmentAPITest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockitoBean
        private InvestmentServiceClient investmentServiceClient;

        @MockitoBean
        private WalletRepository walletRepository;

        @Test
        void givenAnId_whenCallsGetInvestmentById_thenReturnsInvestment() throws Exception {
                final var anId = InvestmentID.from("inv-123");
                final var createdAt = Instant.parse("2026-03-13T10:15:30Z");

                when(investmentServiceClient.getById(anId.getValue())).thenReturn(
                                new InvestmentResponse(
                                        anId.getValue(),
                                        1000.00,
                                        0.0,
                                        12,
                                        0.01,
                                        1126.83,
                                        createdAt.toString()
                                ));

                mockMvc.perform(get("/investments/{id}", anId.getValue()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(anId.getValue()))
                                .andExpect(jsonPath("$.amount").value(1000.00))
                                .andExpect(jsonPath("$.annualPeriod").value(12))
                                .andExpect(jsonPath("$.annualRate").value(0.01));

                verify(investmentServiceClient).getById(anId.getValue());
        }

        @Test
        void givenAValidBody_whenCallsCreateInvestment_thenReturnsCreated() throws Exception {
                final var expectedId =  InvestmentID.from("inv-234");
                when(investmentServiceClient.create(any(CreateInvestmentRequest.class)))
                                .thenReturn(new CreateInvestmentResult(expectedId.getValue()));

                final var body = new CreateInvestmentRequest(
                                "1000.00",
                                12,
                                "0.01",
                                null,
                                "wallet-123"
                );

                mockMvc.perform(post("/investments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(expectedId.getValue()));

                final var reqCaptor = ArgumentCaptor.forClass(CreateInvestmentRequest.class);
                verify(investmentServiceClient).create(reqCaptor.capture());

                final var req = reqCaptor.getValue();
                assertEquals("1000.00", req.amount());
                assertEquals(12, req.annualPeriod());
                assertEquals("0.01", req.annualRate());
                assertEquals("wallet-123", req.walletId());
        }

        @Test
        void givenAnIdAndValidBody_whenCallsUpdateInvestment_thenReturnsOk() throws Exception {
                final var anId = "inv-345";

                when(investmentServiceClient.update(any(String.class), any(UpdateInvestmentRequest.class)))
                                .thenReturn(new UpdateInvestmentResult(anId));

                final var body = new UpdateInvestmentRequest(
                                "1500.00",
                                18,
                                "0.02",
                                null
                );

                mockMvc.perform(put("/investments/{id}", anId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(anId));

                final var idCaptor = ArgumentCaptor.forClass(String.class);
                final var reqCaptor = ArgumentCaptor.forClass(UpdateInvestmentRequest.class);
                verify(investmentServiceClient).update(idCaptor.capture(), reqCaptor.capture());

                assertEquals(anId, idCaptor.getValue());
                final var req = reqCaptor.getValue();
                assertEquals("1500.00", req.amount());
                assertEquals(18, req.annualPeriod());
                assertEquals("0.02", req.annualRate());
        }

        @Test
        void givenAnId_whenCallsDeleteInvestment_thenReturnsNoContent() throws Exception {
                final var anId = "inv-456";

                mockMvc.perform(delete("/investments/{id}", anId))
                                .andExpect(status().isNoContent());

                verify(investmentServiceClient).deleteById(anId);
        }
}