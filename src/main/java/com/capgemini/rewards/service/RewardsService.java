package com.capgemini.rewards.service;
import com.capgemini.rewards.dao.RewardRepository;
import com.capgemini.rewards.dto.RewardResponseDTO;

import com.capgemini.rewards.entity.CustomerReward;
import com.capgemini.rewards.exception.CustomerNotFoundException;
import com.capgemini.rewards.exception.CustomerRewardNotFoundException;
import com.capgemini.rewards.util.RewardCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardsService {

    @Autowired
    private RewardRepository repository;

    public List<RewardResponseDTO> calculateAllCustomerRewards() {
        Map<String, List<CustomerReward>> groupedByCustomer = repository.findAll()
                .stream()
                .collect(Collectors.groupingBy(CustomerReward::getCustomerId));

        return groupedByCustomer.entrySet().stream()
                .map(entry -> buildRewardResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public RewardResponseDTO calculateCustomerRewards(String customerId) {
        List<CustomerReward> transactions = repository.findByCustomerId(customerId);
        if (transactions.isEmpty()) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
        }
        return buildRewardResponse(customerId, transactions);
    }

    public CustomerReward addTransaction(CustomerReward transaction) {
        return repository.save(transaction);
    }

    public List<CustomerReward> getAllTransactions() {
        return repository.findAll();
    }

    public void deleteTransaction(Long id) {
        if (!repository.existsById(id)) {
            throw new CustomerRewardNotFoundException("Transaction with ID " + id + " not found.");
        }
        repository.deleteById(id);
    }

    public CustomerReward updateTransaction(Long id, CustomerReward updatedTransaction) {
        return repository.findById(id).map(existing -> {
            existing.setAmount(updatedTransaction.getAmount());
            existing.setCustomerId(updatedTransaction.getCustomerId());
            existing.setTransactionDate(updatedTransaction.getTransactionDate());
            return repository.save(existing);
        }).orElseThrow(() -> new CustomerRewardNotFoundException("Transaction with ID " + id + " not found."));
    }

    private RewardResponseDTO buildRewardResponse(String customerId, List<CustomerReward> transactions) {
        Map<String, Integer> monthlyPoints = new HashMap<>();
        int totalPoints = 0;

        for (CustomerReward tx : transactions) {
            int points = RewardCalculator.calculatePoints(tx.getAmount());
            String month = tx.getTransactionDate().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            monthlyPoints.put(month, monthlyPoints.getOrDefault(month, 0) + points);
            totalPoints += points;
        }

        return new RewardResponseDTO(customerId, monthlyPoints, totalPoints);
    }
} 