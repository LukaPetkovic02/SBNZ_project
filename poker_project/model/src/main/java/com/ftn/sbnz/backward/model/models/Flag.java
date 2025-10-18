package com.ftn.sbnz.backward.model.models;

import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

@Role(Role.Type.EVENT)
@Timestamp("timestamp")
public class Flag {
    private String playerId;
    private String type; // npr. "PREFLOP_RAISE", "CONT_BET", "HIGH_AVG_BET"
    private int round;
    private long timestamp = System.currentTimeMillis();


    public Flag(String playerId, String type, int round) {
        this.playerId = playerId;
        this.type = type;
        this.round = round;
    }

    public String getPlayerId() { return playerId; }
    public String getType() { return type; }
    public int getRound() { return round; }

    @Override
    public String toString() {
        return "Flag{" + type + " in round " + round + "}";
    }
}
