package com.ftn.sbnz.backward.model.events;

import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

@Role(Role.Type.EVENT)
@Timestamp("timestamp")
public class BluffEvent {
    private String playerId;
    private long timestamp;

    public BluffEvent(String playerId, long timestamp) {
        this.playerId = playerId;
        this.timestamp = timestamp;
    }
    public String getPlayerId(){
        return playerId;
    }
    public long getTimestamp(){
        return timestamp;
    }
}
