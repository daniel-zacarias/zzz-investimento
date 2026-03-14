package br.zzz.infrastructure.api;

import br.zzz.infrastructure.investment.models.CreateInvestmentRequest;
import br.zzz.infrastructure.investment.models.UpdateInvestmentRequest;
import br.zzz.investimento.application.investment.create.CreateInvestmentCommand;
import br.zzz.investimento.application.investment.create.CreateInvestmentOutput;
import br.zzz.investimento.application.investment.create.CreateInvestmentUseCase;
import br.zzz.investimento.application.investment.delete.DeleteInvestmentByIdUseCase;
import br.zzz.investimento.application.investment.retrieve.get.InvestmentOutput;
import br.zzz.investimento.application.investment.retrieve.get.FindInvestmentByIdUseCase;
import br.zzz.investimento.application.investment.update.UpdateInvestmentCommand;
import br.zzz.investimento.application.investment.update.UpdateInvestmentOutput;
import br.zzz.investimento.application.investment.update.UpdateInvestmentUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import br.zzz.infrastructure.investment.persistence.InvestmentRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

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
        private CreateInvestmentUseCase createInvestmentUseCase;

        @MockitoBean
        private FindInvestmentByIdUseCase findInvestmentByIdUseCase;

        @MockitoBean
        private UpdateInvestmentUseCase updateInvestmentUseCase;

        @MockitoBean
        private DeleteInvestmentByIdUseCase deleteInvestmentByIdUseCase;

        @MockitoBean
        private InvestmentRepository investmentRepository;

        @Test
        void givenAnId_whenCallsGetInvestmentById_thenReturnsInvestment() throws Exception {
                final var anId = "inv-123";
                final var createdAt = Instant.parse("2026-03-13T10:15:30Z");

                when(findInvestmentByIdUseCase.execute(anId)).thenReturn(
                                new InvestmentOutput(
                                                anId,
                                                12,
                                                new BigDecimal("1000.00"),
                                                new BigDecimal("1126.83"),
                                                new BigDecimal("0.01"),
                                                createdAt,
                                                createdAt,
                                                null));

                mockMvc.perform(get("/investments/{id}", anId))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(anId))
                                .andExpect(jsonPath("$.amount").value(1000.00))
                                .andExpect(jsonPath("$.annualPeriod").value(12))
                                .andExpect(jsonPath("$.annualRate").value(0.01));

                verify(findInvestmentByIdUseCase).execute(anId);
        }

        @Test
        void givenAValidBody_whenCallsCreateInvestment_thenReturnsCreated() throws Exception {
                final var expectedId = "inv-234";
                when(createInvestmentUseCase.execute(any(CreateInvestmentCommand.class)))
                                .thenReturn(CreateInvestmentOutput.from(expectedId));

                final var body = new CreateInvestmentRequest(
                                "1000.00",
                                12,
                                "0.01"
                );

                mockMvc.perform(post("/investments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(expectedId));

                final var commandCaptor = ArgumentCaptor.forClass(CreateInvestmentCommand.class);
                verify(createInvestmentUseCase).execute(commandCaptor.capture());

                final var aCommand = commandCaptor.getValue();
                assertEquals(0, aCommand.amount().compareTo(new BigDecimal("1000.00")));
                assertEquals(12, aCommand.annualPeriod());
                assertEquals(0, aCommand.annualRate().compareTo(new BigDecimal("0.01")));
        }

        @Test
        void givenAnIdAndValidBody_whenCallsUpdateInvestment_thenReturnsOk() throws Exception {
                final var anId = "inv-345";

                when(updateInvestmentUseCase.execute(any(UpdateInvestmentCommand.class)))
                                .thenReturn(new UpdateInvestmentOutput(anId));

                final var body = new UpdateInvestmentRequest(
                                "1500.00",
                                18,
                                "0.02"
                );

                mockMvc.perform(put("/investments/{id}", anId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(anId));

                final var commandCaptor = ArgumentCaptor.forClass(UpdateInvestmentCommand.class);
                verify(updateInvestmentUseCase).execute(commandCaptor.capture());

                final var aCommand = commandCaptor.getValue();
                assertEquals(anId, aCommand.id());
                assertEquals(0, aCommand.amount().compareTo(new BigDecimal("1500.00")));
                assertEquals(18, aCommand.annualPeriod());
                assertEquals(0, aCommand.annualRate().compareTo(new BigDecimal("0.02")));
        }

        @Test
        void givenAnId_whenCallsDeleteInvestment_thenReturnsNoContent() throws Exception {
                final var anId = "inv-456";

                mockMvc.perform(delete("/investments/{id}", anId))
                                .andExpect(status().isNoContent());

                verify(deleteInvestmentByIdUseCase).execute(anId);
        }
}