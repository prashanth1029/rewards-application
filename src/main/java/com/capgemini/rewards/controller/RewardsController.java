package com.capgemini.rewards.controller;

import com.capgemini.rewards.dto.RewardResponseDTO;
import com.capgemini.rewards.entity.CustomerReward;
import com.capgemini.rewards.service.RewardsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("rewards")
public class RewardsController {

    @Autowired
    private RewardsService service;

    @Operation(summary = "Get reward points for all customers", description = "Fetch all customer rewards and total points")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved rewards",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RewardResponseDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<RewardResponseDTO>> getAllRewards() {
        return ResponseEntity.ok(service.calculateAllCustomerRewards());
    }

    @Operation(summary = "Get reward points for a specific customer", description = "Fetch reward points for a single customer by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved rewards",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = RewardResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{customerId}")
    public ResponseEntity<RewardResponseDTO> getRewardsByCustomer(@PathVariable String customerId) {
        return ResponseEntity.ok(service.calculateCustomerRewards(customerId));
    }

    @Operation(summary = "Add a new customer transaction", description = "Create a new transaction for a customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction successfully created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerReward.class))),
            @ApiResponse(responseCode = "400", description = "Invalid transaction input")
    })
    @PostMapping("/createReward")
    public ResponseEntity<CustomerReward> addTransaction(@Valid @RequestBody CustomerReward transaction) {
        return new ResponseEntity<>(service.addTransaction(transaction), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all customer transactions", description = "Retrieve a list of all customer transactions")
    @ApiResponse(responseCode = "200", description = "List of all transactions retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerReward.class)))
    @GetMapping("/getAllRewards")
    public ResponseEntity<List<CustomerReward>> getAllTransactions() {
        return ResponseEntity.ok(service.getAllTransactions());
    }

    @Operation(summary = "Delete a customer transaction by ID", description = "Delete a specific transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "410", description = "Transaction successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @DeleteMapping("/deleteReward/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        service.deleteTransaction(id);
        return new ResponseEntity<>("deleted successfully",HttpStatus.GONE);
    }

    @Operation(summary = "Update an existing transaction by ID", description = "Update the details of a specific transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction successfully updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerReward.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found")
    })
    @PutMapping("/editReward/{id}")
    public ResponseEntity<CustomerReward> updateTransaction(@PathVariable Long id, @Valid @RequestBody CustomerReward transaction) {
        return ResponseEntity.ok(service.updateTransaction(id, transaction));
    }

}
