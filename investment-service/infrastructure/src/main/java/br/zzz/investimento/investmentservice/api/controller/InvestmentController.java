package br.zzz.investimento.investmentservice.api.controller;

import br.zzz.investimento.application.investment.create.CreateInvestmentCommand;
import br.zzz.investimento.application.investment.create.CreateInvestmentUseCase;
import br.zzz.investimento.application.investment.delete.DeleteInvestmentByIdUseCase;
import br.zzz.investimento.application.investment.retrieve.get.FindInvestmentByIdUseCase;
import br.zzz.investimento.application.investment.update.UpdateInvestmentCommand;
import br.zzz.investimento.application.investment.update.UpdateInvestmentUseCase;
import br.zzz.investimento.investmentservice.investment.models.CreateInvestmentRequest;
import br.zzz.investimento.investmentservice.investment.models.InvestmentResponse;
import br.zzz.investimento.investmentservice.investment.models.UpdateInvestmentRequest;
import br.zzz.investimento.investmentservice.investment.presenter.InvestmentPresenter;
import br.zzz.investimento.investmentservice.utils.BigDecimalUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("/investments")
public class InvestmentController {

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

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateInvestmentRequest request) {
        final var command = CreateInvestmentCommand.with(
                new BigDecimal(request.amount()),
                request.annualPeriod(),
                new BigDecimal(request.annualRate()),
                BigDecimalUtils.parseOrZero(request.monthAmount()),
                request.walletId());
        final var output = createInvestmentUseCase.execute(command);
        return ResponseEntity.created(URI.create("/api/investments/" + output.id())).body(output);
    }

    @GetMapping("/{id}")
    public InvestmentResponse getById(@PathVariable String id) {
        return InvestmentPresenter.present(findInvestmentByIdUseCase.execute(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody UpdateInvestmentRequest request) {
        final var command = UpdateInvestmentCommand.with(
                id,
                new BigDecimal(request.amount()),
                request.annualPeriod(),
                new BigDecimal(request.annualRate()),
                BigDecimalUtils.parseOrZero(request.monthAmount()));

        final var output = updateInvestmentUseCase.execute(command);
        return ResponseEntity.ok(output);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        deleteInvestmentByIdUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
