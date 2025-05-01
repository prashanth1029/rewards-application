package com.capgemini.rewards.dao;


import com.capgemini.rewards.entity.CustomerReward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<CustomerReward, Long> {
    List<CustomerReward> findByCustomerId(String customerId);
    List<CustomerReward> findAll();
}