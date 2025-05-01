package com.capgemini.rewards.controller;

import com.capgemini.rewards.dto.RewardResponseDTO;
import com.capgemini.rewards.entity.CustomerReward;
import com.capgemini.rewards.service.RewardsService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class RewardsControllerTest {
    @Mock
    RewardsService service;
    @InjectMocks
    RewardsController rewardsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllRewards() {
        Map<String, Integer> map = new HashMap<>();
        map.put("monthlyPoints", Integer.valueOf(0));
        when(service.calculateAllCustomerRewards()).thenReturn(Arrays.asList(new RewardResponseDTO("customerId", map, 0)));

        ResponseEntity<List<RewardResponseDTO>> result = rewardsController.getAllRewards();

        Assertions.assertEquals(new ResponseEntity<List<RewardResponseDTO>>(Arrays.asList(new RewardResponseDTO("customerId", map, 0)), null, HttpStatus.CONTINUE), result);
    }

    @Test
    void testGetRewardsByCustomer() {
        Map<String, Integer> map = new HashMap<>();
        map.put("monthlyPoints", Integer.valueOf(0));
        when(service.calculateCustomerRewards(anyString())).thenReturn(new RewardResponseDTO("customerId", map, 0));

        ResponseEntity<RewardResponseDTO> result = rewardsController.getRewardsByCustomer("customerId");
        Assertions.assertEquals(new ResponseEntity<RewardResponseDTO>(new RewardResponseDTO("customerId", map, 0), null, HttpStatus.CONTINUE), result);
    }

    @Test
    void testAddTransaction() {
        when(service.addTransaction(any(CustomerReward.class))).thenReturn(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));

        ResponseEntity<CustomerReward> result = rewardsController.addTransaction(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));
        Assertions.assertEquals(new ResponseEntity<CustomerReward>(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)), null, HttpStatus.CONTINUE), result);
    }

    @Test
    void testGetAllTransactions() {
        when(service.getAllTransactions()).thenReturn(Arrays.asList(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1))));

        ResponseEntity<List<CustomerReward>> result = rewardsController.getAllTransactions();
        Assertions.assertEquals(new ResponseEntity<List<CustomerReward>>(Arrays.asList(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1))), null, HttpStatus.CONTINUE), result);
    }

    @Test
    void testDeleteTransaction() {
        ResponseEntity<String> result = rewardsController.deleteTransaction(Long.valueOf(1));
        verify(service).deleteTransaction(anyLong());
        Assertions.assertEquals(new ResponseEntity<String>("body", null, HttpStatus.CONTINUE), result);
    }

    @Test
    void testUpdateTransaction() {
        when(service.updateTransaction(anyLong(), any(CustomerReward.class))).thenReturn(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));

        ResponseEntity<CustomerReward> result = rewardsController.updateTransaction(Long.valueOf(1), new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));
        Assertions.assertEquals(new ResponseEntity<CustomerReward>(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)), null, HttpStatus.CONTINUE), result);
    }

}

