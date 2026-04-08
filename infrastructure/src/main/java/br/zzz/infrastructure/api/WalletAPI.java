package br.zzz.infrastructure.api;

import br.zzz.infrastructure.wallet.models.CreateWalletRequest;
import br.zzz.infrastructure.wallet.models.WalletResponse;
import br.zzz.investimento.application.wallet.create.CreateWalletOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/wallets")
@Tag(name = "Wallets", description = "Endpoints for managing wallets")
public interface WalletAPI {

    @GetMapping("/{userId}")
    @Operation(summary = "List wallets by userId", description = "Retrieves wallets and their investments by the user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wallets retrieved successfully", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = WalletResponse.class)))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    List<WalletResponse> getByUserId(@PathVariable(value = "userId") String userId);

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
