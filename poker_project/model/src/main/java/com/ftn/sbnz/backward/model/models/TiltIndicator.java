package com.ftn.sbnz.backward.model.models;

public class TiltIndicator {
    private String playerId;
    private String level; // npr. "HIGH_TILT"
    private long timestamp;

    public TiltIndicator(String playerId, String level, long timestamp) {
        this.playerId = playerId;
        this.level = level;
        this.timestamp = timestamp;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getLevel() {
        return level;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
