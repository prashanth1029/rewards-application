package com.capgemini.rewards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Schema(description = "Reward summary for a customer, broken down by month")
public class RewardResponseDTO {
    @Schema(description = "Unique customer identifier")
    private String customerId;
    @Schema(description = "Monthly reward points mapped by month name")
    private Map<String, Integer> monthlyPoints;
    @Schema(description = "Total reward points across all months")
    private int totalPoints;

    // Constructors, Getters and Setters

}