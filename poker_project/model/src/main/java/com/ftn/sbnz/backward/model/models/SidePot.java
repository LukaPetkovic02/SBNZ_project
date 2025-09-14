package com.ftn.sbnz.backward.model.models;

import java.util.ArrayList;
import java.util.List;

public class SidePot {
    private int amount;
    private int maxContribution;
    private List<String> eligiblePlayerIds;

    public SidePot() {
        this.eligiblePlayerIds = new ArrayList<>();
    }

    public SidePot(int maxContribution) {
        this();
        this.maxContribution = maxContribution;
    }

    public void addEligiblePlayer(String playerId) {
        if (!eligiblePlayerIds.contains(playerId)) {
            eligiblePlayerIds.add(playerId);
        }
    }

    // Getteri i setteri
    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public int getMaxContribution() { return maxContribution; }
    public void setMaxContribution(int maxContribution) { this.maxContribution = maxContribution; }

    public List<String> getEligiblePlayerIds() { return eligiblePlayerIds; }
    public void setEligiblePlayerIds(List<String> eligiblePlayerIds) { this.eligiblePlayerIds = eligiblePlayerIds; }
}
