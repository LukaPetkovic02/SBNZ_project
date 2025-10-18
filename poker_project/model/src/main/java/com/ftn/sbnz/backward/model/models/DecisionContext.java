package com.ftn.sbnz.backward.model.models;

import com.ftn.sbnz.backward.model.enums.GamePhase;

public class DecisionContext {
    private String playerId;
    private GamePhase gamePhase;
    private double handStrength;
    private String handCategory;
    private int position;
    private int chipStack;
    private int potSize;
    private int currentBet;
    private int betToCall;
    private double potOdds;
    private int numActivePlayers;
    private double aggression;
    private double bluffProbability;

    // Getters and setters
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public GamePhase getGamePhase() { return gamePhase; }
    public void setGamePhase(GamePhase gamePhase) { this.gamePhase = gamePhase; }

    public double getHandStrength() { return handStrength; }
    public void setHandStrength(double handStrength) { this.handStrength = handStrength; }

    public String getHandCategory() { return handCategory; }
    public void setHandCategory(String handCategory) { this.handCategory = handCategory; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public int getChipStack() { return chipStack; }
    public void setChipStack(int chipStack) { this.chipStack = chipStack; }

    public int getPotSize() { return potSize; }
    public void setPotSize(int potSize) { this.potSize = potSize; }

    public int getCurrentBet() { return currentBet; }
    public void setCurrentBet(int currentBet) { this.currentBet = currentBet; }

    public int getBetToCall() { return betToCall; }
    public void setBetToCall(int betToCall) { this.betToCall = betToCall; }

    public double getPotOdds() { return potOdds; }
    public void setPotOdds(double potOdds) { this.potOdds = potOdds; }

    public int getNumActivePlayers() { return numActivePlayers; }
    public void setNumActivePlayers(int numActivePlayers) { this.numActivePlayers = numActivePlayers; }

    public double getAggression() { return aggression; }
    public void setAggression(double aggression) { this.aggression = aggression; }

    public double getBluffProbability() { return bluffProbability; }
    public void setBluffProbability(double bluffProbability) { this.bluffProbability = bluffProbability; }

}
