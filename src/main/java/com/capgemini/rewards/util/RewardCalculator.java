package com.capgemini.rewards.util;

public class RewardCalculator {
    public static int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (int) (2 * (amount - 100));
            points += 50; // Between 50 and 100
        } else if (amount > 50) {
            points += (int) (amount - 50);
        }
        return points;
    }
}