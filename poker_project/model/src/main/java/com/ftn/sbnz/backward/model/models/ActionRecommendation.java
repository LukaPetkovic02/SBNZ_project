package com.ftn.sbnz.backward.model.models;

import com.ftn.sbnz.backward.model.enums.ActionType;

public class ActionRecommendation {
    private String playerId;
    private ActionType recommendedAction;
    private int betAmount;
    private double confidence;
    private String reasoning;

    public ActionRecommendation(String playerId, ActionType recommendedAction, int betAmount, double confidence, String reasoning) {
        this.playerId = playerId;
        this.recommendedAction = recommendedAction;
        this.betAmount = betAmount;
        this.confidence = confidence;
        this.reasoning = reasoning;
    }
    public ActionRecommendation() {}
    // Getters and setters
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerName) { this.playerId = playerName; }

    public ActionType getRecommendedAction() { return recommendedAction; }
    public void setRecommendedAction(ActionType recommendedAction) { this.recommendedAction = recommendedAction; }

    public int getBetAmount() { return betAmount; }
    public void setBetAmount(int betAmount) { this.betAmount = betAmount; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }

    public String getReasoning() { return reasoning; }
    public void setReasoning(String reasoning) { this.reasoning = reasoning; }
}
