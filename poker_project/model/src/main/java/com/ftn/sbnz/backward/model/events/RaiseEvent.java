package com.ftn.sbnz.backward.model.events;

public class RaiseEvent {
    private String playerId;
    private long timestamp;
    private Integer amount;

    public RaiseEvent(String playerId, long timestamp, Integer amount) {
        this.playerId = playerId;
        this.timestamp = timestamp;
        this.amount = amount;
    }
    public RaiseEvent(String playerId, Integer amount) {
        this.playerId = playerId;
        this.timestamp = System.currentTimeMillis();
        this.amount = amount;
    }
    public Integer getAmount() {
        return amount;
    }
    public String getPlayerId(){
        return playerId;
    }
    public long getTimestamp(){
        return timestamp;
    }
}
