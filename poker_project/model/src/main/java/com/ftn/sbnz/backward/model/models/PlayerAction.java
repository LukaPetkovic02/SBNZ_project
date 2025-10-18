package com.ftn.sbnz.backward.model.models;

import com.ftn.sbnz.backward.model.enums.ActionType;

public class PlayerAction {
    private String playerId;
    private String gameId;
    private ActionType actionType;
    private int amount;
    private boolean valid = true;
    private String errorMessage;
    private boolean isAllIn = false;
    private long timestamp;
    private double confidence;
    private String reasoning;

    public PlayerAction() {
        this.timestamp = System.currentTimeMillis();
    }

    public PlayerAction(String playerId, ActionType actionType, int amount,
                        double confidence, String reasoning) {
        this();
        this.playerId = playerId;
        this.actionType = actionType;
        this.amount = amount;
        this.confidence = confidence;
        this.reasoning = reasoning;
    }

    // Getteri i setteri
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }

    public ActionType getActionType() { return actionType; }
    public void setActionType(ActionType actionType) { this.actionType = actionType; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public boolean isAllIn() { return isAllIn; }
    public void setAllIn(boolean allIn) { isAllIn = allIn; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public double getConfidence() {
        return confidence;
    }
    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
    public String getReasoning() {
        return reasoning;
    }
    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }
    public String getConfidenceLevel() {
        if (confidence >= 0.8) return "High";
        if (confidence >= 0.6) return "Medium";
        if (confidence >= 0.4) return "Low";
        return "Very Low";
    }
}
