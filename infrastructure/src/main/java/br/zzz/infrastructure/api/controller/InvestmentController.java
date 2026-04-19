package br.zzz.infrastructure.api.controller;

import br.zzz.infrastructure.api.InvestmentAPI;
import br.zzz.infrastructure.investment.client.InvestmentServiceClient;
import br.zzz.infrastructure.investment.models.CreateInvestmentRequest;
import br.zzz.infrastructure.investment.models.CreateInvestmentResult;
import br.zzz.infrastructure.investment.models.InvestmentResponse;
import br.zzz.infrastructure.investment.models.UpdateInvestmentRequest;
import br.zzz.infrastructure.investment.models.UpdateInvestmentResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class InvestmentController implements InvestmentAPI {

    private final InvestmentServiceClient investmentServiceClient;

    public InvestmentController(final InvestmentServiceClient investmentServiceClient) {
        this.investmentServiceClient = Objects.requireNonNull(investmentServiceClient);
    }

    @Override
    public ResponseEntity<?> create(final CreateInvestmentRequest request) {
        final CreateInvestmentResult output = investmentServiceClient.create(request);
        return ResponseEntity.created(URI.create("/api/investments/" + output.id())).body(output);
    }

    @Override
    public InvestmentResponse getById(final String id) {
        return investmentServiceClient.getById(id);
    }

    @Override
    public ResponseEntity<?> update(final String id, final UpdateInvestmentRequest request) {
        final UpdateInvestmentResult output = investmentServiceClient.update(id, request);
        return ResponseEntity.ok(output);
    }

    @Override
    public ResponseEntity<Void> deleteById(final String id) {
        investmentServiceClient.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
