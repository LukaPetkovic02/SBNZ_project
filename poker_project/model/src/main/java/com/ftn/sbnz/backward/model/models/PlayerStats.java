package com.ftn.sbnz.backward.model.models;

public class PlayerStats {
    private String playerId;
    private int preflopRaises;
    private int continuationBets;
    private double avgBetSize;
    private double avgBetAllPlayers;
    private int round;

    public PlayerStats(String playerId, int preflopRaises, int continuationBets, double avgBetSize, double avgBetAllPlayers, int round) {
        this.playerId = playerId;
        this.preflopRaises = preflopRaises;
        this.continuationBets = continuationBets;
        this.avgBetSize = avgBetSize;
        this.avgBetAllPlayers = avgBetAllPlayers;
        this.round = round;
    }

    public String getPlayerId() { return playerId; }
    public int getPreflopRaises() { return preflopRaises; }
    public int getContinuationBets() { return continuationBets; }
    public double getAvgBetSize() { return avgBetSize; }
    public double getAvgBetAllPlayers() { return avgBetAllPlayers; }
    public int getRound() {return round;}
}
