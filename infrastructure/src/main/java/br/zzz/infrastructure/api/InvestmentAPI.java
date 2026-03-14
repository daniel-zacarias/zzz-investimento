package br.zzz.infrastructure.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.zzz.infrastructure.investment.models.CreateInvestmentRequest;
import br.zzz.infrastructure.investment.models.InvestmentResponse;
import br.zzz.infrastructure.investment.models.UpdateInvestmentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/investments")
@Tag(name = "Investments", description = "Endpoints for managing investments")
public interface InvestmentAPI {
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new investment", description = "Creates a new investment with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Investment created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvestmentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<?> create(@RequestBody CreateInvestmentRequest request);

    @GetMapping("/{id}")
    @Operation(summary = "Get an investment by ID", description = "Retrieves the details of an investment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Investment retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvestmentResponse.class))),
            @ApiResponse(responseCode = "404", description = "Investment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    InvestmentResponse getById(@PathVariable(value = "id") String id);

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing investment", description = "Updates the details of an existing investment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Investment updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvestmentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Investment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<?> update(@PathVariable(value = "id") String id, @RequestBody UpdateInvestmentRequest request);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an investment by ID", description = "Deletes an investment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Investment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Investment not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ResponseEntity<Void> deleteById(@PathVariable(value = "id") String id);
}
