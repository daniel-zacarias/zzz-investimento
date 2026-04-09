package br.zzz.infrastructure.api;

import br.zzz.infrastructure.wallet.models.CreateWalletRequest;
import br.zzz.infrastructure.wallet.models.WalletResponse;
import br.zzz.investimento.application.wallet.create.CreateWalletOutput;
import br.zzz.investimento.domain.pagination.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/wallets")
@Tag(name = "Wallets", description = "Endpoints for managing wallets")
public interface WalletAPI {

    @GetMapping
    @Operation(summary = "List wallets by userId (paginated)", description = "Retrieves wallets and their investments by the user id with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wallets retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pagination.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    Pagination<WalletResponse> listByUserId(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "perPage", defaultValue = "10") int perPage,
            @RequestParam(value = "terms", required = false) String terms,
            @RequestParam(value = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    );

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new wallet", description = "Creates a new wallet for the provided user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wallet created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateWalletOutput.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<?> create(@RequestBody CreateWalletRequest request);
}
