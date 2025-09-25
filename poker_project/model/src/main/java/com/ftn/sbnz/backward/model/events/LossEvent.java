package com.ftn.sbnz.backward.model.events;

public class LossEvent {
    private String playerId;
    private long timestamp;

    public LossEvent(String playerId, long timestamp) {
        this.playerId = playerId;
        this.timestamp = timestamp;
    }
    public LossEvent(String playerId){
        this.playerId = playerId;
        this.timestamp = System.currentTimeMillis();
    }
    public String getPlayerId(){
        return playerId;
    }
    public long getTimestamp(){
        return timestamp;
    }
}
