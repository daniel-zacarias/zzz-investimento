package br.zzz.infrastructure.api.controller;

import br.zzz.infrastructure.investment.presenter.InvestmentPresenter;
import br.zzz.infrastructure.utils.BigDecimalUtils;
import br.zzz.investimento.application.investment.create.CreateInvestmentCommand;
import br.zzz.investimento.application.investment.create.CreateInvestmentUseCase;
import br.zzz.investimento.application.investment.delete.DeleteInvestmentByIdUseCase;
import br.zzz.investimento.application.investment.retrieve.get.FindInvestmentByIdUseCase;
import br.zzz.investimento.application.investment.update.UpdateInvestmentCommand;
import br.zzz.investimento.application.investment.update.UpdateInvestmentUseCase;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Objects;

import org.springframework.http.ResponseEntity;

import br.zzz.infrastructure.api.InvestmentAPI;
import br.zzz.infrastructure.investment.models.CreateInvestmentRequest;
import br.zzz.infrastructure.investment.models.InvestmentResponse;
import br.zzz.infrastructure.investment.models.UpdateInvestmentRequest;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvestmentController implements InvestmentAPI {

    private final CreateInvestmentUseCase createInvestmentUseCase;

    private final DeleteInvestmentByIdUseCase deleteInvestmentByIdUseCase;

    private final FindInvestmentByIdUseCase findInvestmentByIdUseCase;

    private final UpdateInvestmentUseCase updateInvestmentUseCase;

    public InvestmentController(final CreateInvestmentUseCase createInvestmentUseCase,
                                final DeleteInvestmentByIdUseCase deleteInvestmentByIdUseCase,
                                final FindInvestmentByIdUseCase findInvestmentByIdUseCase,
                                final UpdateInvestmentUseCase updateInvestmentUseCase) {
        this.createInvestmentUseCase = Objects.requireNonNull(createInvestmentUseCase);
        this.deleteInvestmentByIdUseCase = Objects.requireNonNull(deleteInvestmentByIdUseCase);
        this.findInvestmentByIdUseCase = Objects.requireNonNull(findInvestmentByIdUseCase);
        this.updateInvestmentUseCase = Objects.requireNonNull(updateInvestmentUseCase);
    }

    @Override
    public ResponseEntity<?> create(CreateInvestmentRequest request) {
        final var command = CreateInvestmentCommand.with(
                new BigDecimal(request.amount()),
                request.annualPeriod(),
                new BigDecimal(request.annualRate()),
                BigDecimalUtils.parseOrZero(request.monthAmount()),
                request.walletId());
        final var output = createInvestmentUseCase.execute(command);
        return ResponseEntity.created(URI.create("/api/investments/" + output.id())).body(output);
    }

    @Override
    public InvestmentResponse getById(String id) {
        return InvestmentPresenter.present(findInvestmentByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> update(String id, UpdateInvestmentRequest request) {
        final var command = UpdateInvestmentCommand.with(
                id,
                new BigDecimal(request.amount()),
                request.annualPeriod(),
                new BigDecimal(request.annualRate()),
                BigDecimalUtils.parseOrZero(request.monthAmount()));

        final var output = updateInvestmentUseCase.execute(command);
        return ResponseEntity.ok(output);
    }

    @Override
    public ResponseEntity<Void> deleteById(String id) {
        deleteInvestmentByIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

}
