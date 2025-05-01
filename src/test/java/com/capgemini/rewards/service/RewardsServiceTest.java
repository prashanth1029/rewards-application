package com.capgemini.rewards.service;

import com.capgemini.rewards.dao.RewardRepository;
import com.capgemini.rewards.dto.RewardResponseDTO;
import com.capgemini.rewards.entity.CustomerReward;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class RewardsServiceTest {
    @Mock
    RewardRepository repository;
    @InjectMocks
    RewardsService rewardsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateAllCustomerRewards() {
        when(repository.findAll()).thenReturn(Arrays.asList(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1))));

        List<RewardResponseDTO> result = rewardsService.calculateAllCustomerRewards();
        Map<String, Integer> map = new HashMap<>();
        map.put("MAY", Integer.valueOf(0));
        Assertions.assertEquals(Arrays.asList(new RewardResponseDTO("customerId", map, 0)), result);
    }

    @Test
    void testCalculateCustomerRewards() {
        when(repository.findByCustomerId(anyString())).thenReturn(Arrays.asList(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1))));

        RewardResponseDTO result = rewardsService.calculateCustomerRewards("customerId");
        Map<String, Integer> map = new HashMap<>();
        map.put("monthlyPoints", Integer.valueOf(0));
        Assertions.assertEquals(new RewardResponseDTO("customerId", map, 0), result);
    }

    @Test
    void testAddTransaction() {
        when(repository.save(any(CustomerReward.class))).thenReturn(new CustomerReward());

        CustomerReward result = rewardsService.addTransaction(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));
        Assertions.assertEquals(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)), result);
    }

    @Test
    void testGetAllTransactions() {
        when(repository.findAll()).thenReturn(Arrays.asList(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1))));

        List<CustomerReward> result = rewardsService.getAllTransactions();
        Assertions.assertEquals(Arrays.asList(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1))), result);
    }

    @Test
    void testDeleteTransaction() {
        when(repository.existsById(any(Long.class))).thenReturn(true);

        rewardsService.deleteTransaction(Long.valueOf(1));
        verify(repository).deleteById(any(Long.class));
    }

    @Test
    void testUpdateTransaction() {
        when(repository.save(any(CustomerReward.class))).thenReturn(new CustomerReward());
        when(repository.findById(any(Long.class))).thenReturn(null);

        CustomerReward result = rewardsService.updateTransaction(Long.valueOf(1), new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));
        Assertions.assertEquals(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)), result);
    }
}

