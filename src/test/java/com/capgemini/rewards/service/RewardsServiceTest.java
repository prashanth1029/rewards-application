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
import java.util.*;
import java.util.stream.Collectors;

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
        List<CustomerReward> rewardList = Arrays.asList(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));
        when(repository.findAll()).thenReturn(rewardList);
        Map<String, List<CustomerReward>> customerIdCustomerRewardmap = rewardList.stream().collect(Collectors.groupingBy(CustomerReward::getCustomerId));

        List<RewardResponseDTO> rewardResponseDTOList = customerIdCustomerRewardmap.entrySet().stream()
                .map(entry -> rewardsService.buildRewardResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        List<RewardResponseDTO> result = rewardsService.calculateAllCustomerRewards();
        Assertions.assertEquals(rewardResponseDTOList, result);
        List<CustomerReward> rewardList1 = Arrays.asList(
                new CustomerReward(1L, null, 100.0, LocalDate.of(2025, Month.MAY, 1))
        );
        when(repository.findAll()).thenReturn(rewardList1);

        Assertions.assertThrows(NullPointerException.class, () -> {
            rewardsService.calculateAllCustomerRewards();
        });
    }

    @Test
    void testCalculateCustomerRewards() {
        when(repository.findByCustomerId(anyString())).thenReturn(Arrays.asList(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1))));

        RewardResponseDTO result = rewardsService.calculateCustomerRewards("customerId");
        Map<String, Integer> map = new HashMap<>();
        map.put("May", Integer.valueOf(0));
        Assertions.assertEquals(new RewardResponseDTO("customerId", map, 0), result);
    }

    @Test
    void testAddTransaction() {
        when(repository.save(any(CustomerReward.class))).thenReturn(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));

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
        when(repository.save(any(CustomerReward.class))).thenReturn(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));
        CustomerReward customerReward = new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1));
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(customerReward));

        CustomerReward result = rewardsService.updateTransaction(Long.valueOf(1), new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)));
        Assertions.assertEquals(new CustomerReward(Long.valueOf(1), "customerId", Double.valueOf(0), LocalDate.of(2025, Month.MAY, 1)), result);
    }
}

