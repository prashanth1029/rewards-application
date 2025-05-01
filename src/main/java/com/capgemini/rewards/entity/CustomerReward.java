package com.capgemini.rewards.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "customer_reward")
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Customer transaction entity representing purchase information")
public class CustomerReward {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique ID of the transaction", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    @NotBlank(message = "Customer ID is required")
    @Schema(description = "Unique customer identifier", example = "cust123")
    @Column(name = "customer_id")
    private String customerId;
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    @Schema(description = "Transaction amount in USD", example = "120.5")
    private Double amount;
    @NotNull(message = "Transaction date is required")
    @Past(message = "Transaction date cannot be in the future")
    @Schema(description = "Date when the transaction occurred", example = "2024-03-15")
    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    // Getters and Setters
}