package com.ftn.sbnz.backward.model.models;

public class Player {
    private String name;
    private Hand hand;
    private double stackSize;
    private PlayerProfile profile;
    private Boolean isActive;
    private double avgBetSize;
    public Player(){}

    public Player(String name, double stackSize, String position, PlayerProfile profile, Hand hand) {
        this.name = name;
        this.stackSize = stackSize;
        this.profile = profile;
        this.hand = hand;
        this.isActive = true;
        this.avgBetSize = 0.0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStackSize() {
        return stackSize;
    }

    public void setStackSize(double stackSize) {
        this.stackSize = stackSize;
    }

    public PlayerProfile getProfile() {
        return profile;
    }

    public void setProfile(PlayerProfile profile) {
        this.profile = profile;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public double getAvgBetSize() {
        return avgBetSize;
    }

    public void setAvgBetSize(double avgBetSize) {
        this.avgBetSize = avgBetSize;
    }
}
