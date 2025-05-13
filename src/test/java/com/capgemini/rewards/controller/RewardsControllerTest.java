package com.capgemini.rewards.controller;

import com.capgemini.rewards.dto.RewardResponseDTO;
import com.capgemini.rewards.entity.CustomerReward;
import com.capgemini.rewards.exception.CustomerNotFoundException;
import com.capgemini.rewards.exception.CustomerRewardNotFoundException;
import com.capgemini.rewards.service.RewardsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
        assertEquals(list, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("customerId", result.getBody().get(0).getCustomerId());
        assertNotEquals(120, result.getBody().get(0).getTotalPoints());
        assertNotEquals(2, result.getBody().get(0).getMonthlyPoints().size());
        assertNotEquals(50, result.getBody().get(0).getMonthlyPoints().get("January"));
    }

    @Test
    void testGetRewardsByCustomer() {
        Map<String, Integer> map = new HashMap<>();
        map.put("monthlyPoints", Integer.valueOf(0));
        RewardResponseDTO rewardResponseDTO = new RewardResponseDTO("customerId", map, 0);
        when(service.calculateCustomerRewards(anyString())).thenReturn(rewardResponseDTO);
        ResponseEntity<RewardResponseDTO> result = rewardsController.getRewardsByCustomer("customerId");
        assertEquals(rewardResponseDTO, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());

        when(service.calculateCustomerRewards(anyString())).thenReturn(null);
        ResponseEntity<RewardResponseDTO> resultt = rewardsController.getRewardsByCustomer("customerId");
        assertNull(resultt.getBody());
        assertEquals(HttpStatus.OK, resultt.getStatusCode());

        when(service.calculateCustomerRewards("c123"))
                .thenThrow(new CustomerNotFoundException("Customer with ID c123 not found."));
        CustomerNotFoundException thrown = assertThrows(CustomerNotFoundException.class, () -> {
            rewardsController.getRewardsByCustomer("c123");
        });
        assertEquals("Customer with ID c123 not found.", thrown.getMessage());
    }

    @Test
    void testAddTransaction() {
        CustomerReward customerReward = new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1));
        when(service.addTransaction(any(CustomerReward.class))).thenReturn(customerReward);
        ResponseEntity<CustomerReward> result = rewardsController.addTransaction(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));
        assertEquals(customerReward, result.getBody());
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        CustomerReward transaction = new CustomerReward(1L, "customerId", 100.0, LocalDate.now());
        when(service.addTransaction(any(CustomerReward.class))).thenReturn(null);
        ResponseEntity<CustomerReward> resultt = rewardsController.addTransaction(transaction);
        assertNull(resultt.getBody());
    }

    @Test
    void testGetAllTransactions() {
        List<CustomerReward> rewardList = Arrays.asList(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));
        when(service.getAllTransactions()).thenReturn(rewardList);
        ResponseEntity<List<CustomerReward>> result = rewardsController.getAllTransactions();
        assertEquals(rewardList, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());

        when(service.getAllTransactions()).thenReturn(Collections.emptyList());
        ResponseEntity<List<CustomerReward>> resultt = rewardsController.getAllTransactions();
        assertNotNull(resultt.getBody());
        assertTrue(resultt.getBody().isEmpty());
        assertEquals(HttpStatus.OK, resultt.getStatusCode());

        when(service.getAllTransactions()).thenReturn(null);
        ResponseEntity<List<CustomerReward>> res = rewardsController.getAllTransactions();
        assertNull(res.getBody());
        assertEquals(HttpStatus.OK, res.getStatusCode());

        when(service.getAllTransactions()).thenThrow(new RuntimeException("Internal server error"));
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            rewardsController.getAllTransactions();
        });
        assertEquals("Internal server error", thrown.getMessage());
    }

    @Test
    void testDeleteTransaction() {
        ResponseEntity<String> result = rewardsController.deleteTransaction(Long.valueOf(1));
        verify(service).deleteTransaction(anyLong());

        assertEquals("deleted successfully", result.getBody());
        assertEquals(HttpStatus.GONE, result.getStatusCode());

        Long id = 999L;
        doThrow(new CustomerRewardNotFoundException("Transaction with ID 999 not found."))
                .when(service).deleteTransaction(id);
        CustomerRewardNotFoundException exception = assertThrows(
                CustomerRewardNotFoundException.class,
                () -> rewardsController.deleteTransaction(id)
        );
        assertEquals("Transaction with ID 999 not found.", exception.getMessage());
        verify(service).deleteTransaction(id);
    }

    @Test
    void testUpdateTransaction() {
        CustomerReward customerReward = new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1));
        when(service.updateTransaction(anyLong(), any(CustomerReward.class))).thenReturn(customerReward);

        ResponseEntity<CustomerReward> result = rewardsController.updateTransaction(Long.valueOf(1), new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));
        assertEquals(customerReward, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());

        Long transactionId = 1L;
        CustomerReward updatedReward = new CustomerReward(transactionId, "customerId", 0.0, LocalDate.of(2025, Month.MAY, 1));
        when(service.updateTransaction(eq(transactionId), any(CustomerReward.class)))
                .thenThrow(new CustomerRewardNotFoundException("Transaction with ID " + transactionId + " not found."));

        CustomerRewardNotFoundException exception = assertThrows(CustomerRewardNotFoundException.class, () -> {
            rewardsController.updateTransaction(transactionId, updatedReward);
        });
        assertTrue(exception.getMessage().contains("Transaction with ID " + transactionId + " not found"));

    }

}

