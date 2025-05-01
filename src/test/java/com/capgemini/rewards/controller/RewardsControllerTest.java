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
        RewardResponseDTO rewardResponseDTO = new RewardResponseDTO("customerId", map, 0);
        List<RewardResponseDTO> list = Arrays.asList(rewardResponseDTO);
        when(service.calculateAllCustomerRewards()).thenReturn(list);

        ResponseEntity<List<RewardResponseDTO>> result = rewardsController.getAllRewards();
        Assertions.assertEquals(list, result.getBody());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testGetRewardsByCustomer() {
        Map<String, Integer> map = new HashMap<>();
        map.put("monthlyPoints", Integer.valueOf(0));
        RewardResponseDTO rewardResponseDTO = new RewardResponseDTO("customerId", map, 0);
        when(service.calculateCustomerRewards(anyString())).thenReturn(rewardResponseDTO);

        ResponseEntity<RewardResponseDTO> result = rewardsController.getRewardsByCustomer("customerId");
        Assertions.assertEquals(rewardResponseDTO, result.getBody());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testAddTransaction() {
        CustomerReward customerReward = new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1));
        when(service.addTransaction(any(CustomerReward.class))).thenReturn(customerReward);

        ResponseEntity<CustomerReward> result = rewardsController.addTransaction(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));
        Assertions.assertEquals(customerReward, result.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
    }

    @Test
    void testGetAllTransactions() {
        List<CustomerReward> rewardList = Arrays.asList(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));
        when(service.getAllTransactions()).thenReturn(rewardList);

        ResponseEntity<List<CustomerReward>> result = rewardsController.getAllTransactions();
        Assertions.assertEquals(rewardList, result.getBody());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    void testDeleteTransaction() {
        ResponseEntity<String> result = rewardsController.deleteTransaction(Long.valueOf(1));
        verify(service).deleteTransaction(anyLong());

        Assertions.assertEquals("deleted successfully", result.getBody());
        Assertions.assertEquals(HttpStatus.GONE, result.getStatusCode());
    }

    @Test
    void testUpdateTransaction() {
        CustomerReward customerReward = new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1));
        when(service.updateTransaction(anyLong(), any(CustomerReward.class))).thenReturn(customerReward);

        ResponseEntity<CustomerReward> result = rewardsController.updateTransaction(Long.valueOf(1), new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));
        Assertions.assertEquals(customerReward, result.getBody());
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }

}

