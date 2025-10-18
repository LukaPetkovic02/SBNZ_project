package com.ftn.sbnz.backward.model.events;

import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

@Role(Role.Type.EVENT)
@Timestamp("timestamp")
public class BetEvent {
    private String playerId;
    private double amount;
    private long timestamp;

    public BetEvent(String playerId, double amount, long timestamp) {
        this.playerId = playerId;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public BetEvent(String playerId, double amount) {
        this.playerId = playerId;
        this.amount = amount;
        this.timestamp = System.currentTimeMillis();
    }

    public String getPlayerId() {
        return playerId;
    }

    public double getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
