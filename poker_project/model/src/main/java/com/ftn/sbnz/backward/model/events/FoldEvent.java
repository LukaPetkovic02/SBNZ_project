package com.ftn.sbnz.backward.model.events;

public class FoldEvent {
    private String playerId;
    private long timestamp;

    public FoldEvent(String playerId, long timestamp) {
        this.playerId = playerId;
        this.timestamp = timestamp;
    }
    public FoldEvent(String playerId){
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
