package br.zzz.infrastructure.api;

import br.zzz.infrastructure.wallet.models.WalletResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/wallets")
@Tag(name = "Wallets", description = "Endpoints for retrieving wallets")
public interface WalletAPI {

    @GetMapping("/{userId}")
    @Operation(summary = "Get wallet by userId", description = "Retrieves the wallet and its investments by the user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wallet retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = WalletResponse.class))),
            @ApiResponse(responseCode = "404", description = "Wallet not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    WalletResponse getByUserId(@PathVariable(value = "userId") String userId);
}
