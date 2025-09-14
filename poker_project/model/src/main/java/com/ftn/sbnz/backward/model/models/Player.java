package com.ftn.sbnz.backward.model.models;

import com.ftn.sbnz.backward.model.enums.BlindType;
import com.ftn.sbnz.backward.model.enums.PlayerStatus;

public class Player {
    private String id;
    private String name;
    private Hand hand;
    private int chips;
    private int position;
    private PlayerStatus status;
    private BlindType blindType;
    private boolean isActive;
    private boolean allIn;
    private int currentBet;
    private int totalBetThisHand;
    private PlayerProfile profile;
    private double avgBetSize;

    private String lastAction;
    private int actionCount;
    private boolean hasActed;

    public Player() {
        this.status = PlayerStatus.ACTIVE;
        this.blindType = BlindType.NONE;
        this.isActive = true;
        this.allIn = false;
        this.currentBet = 0;
        this.totalBetThisHand = 0;
        this.actionCount = 0;
        this.hasActed = false;
    }

    public Player(String id, String name, int initialChips, int position) {
        this();
        this.id = id;
        this.name = name;
        this.chips = initialChips;
        this.position = position;
        this.hand = new Hand();
    }

    public void addChips(int amount) {
        this.chips += amount;
    }

    public void subtractChips(int amount) {
        this.chips = Math.max(0, this.chips - amount);
        if (this.chips == 0) {
            this.allIn = true;
        }
    }

    public void placeBet(int amount) {
        int actualBet = Math.min(amount, chips);
        subtractChips(actualBet);
        this.currentBet += actualBet;
        this.totalBetThisHand += actualBet;
        this.hasActed = true;
        this.actionCount++;

        if (chips == 0) {
            this.allIn = true;
        }
    }

    public void resetForNewRound() {
        this.currentBet = 0;
        this.hasActed = false;
        this.blindType = BlindType.NONE;
        if (status != PlayerStatus.FOLDED) {
            this.status = PlayerStatus.ACTIVE;
        }
    }

    public void fold() {
        this.status = PlayerStatus.FOLDED;
        this.hasActed = true;
        this.lastAction = "FOLD";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Hand getHand() { return hand; }
    public void setHand(Hand hand) { this.hand = hand; }

    public int getChips() { return chips; }
    public void setChips(int chips) { this.chips = chips; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public PlayerStatus getStatus() { return status; }
    public void setStatus(PlayerStatus status) { this.status = status; }

    public BlindType getBlindType() { return blindType; }
    public void setBlindType(BlindType blindType) { this.blindType = blindType; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isAllIn() { return allIn; }
    public void setAllIn(boolean allIn) { this.allIn = allIn; }

    public int getCurrentBet() { return currentBet; }
    public void setCurrentBet(int currentBet) { this.currentBet = currentBet; }

    public int getTotalBetThisHand() { return totalBetThisHand; }
    public void setTotalBetThisHand(int totalBetThisHand) { this.totalBetThisHand = totalBetThisHand; }

    public PlayerProfile getProfile() { return profile; }
    public void setProfile(PlayerProfile profile) { this.profile = profile; }

    public String getLastAction() { return lastAction; }
    public void setLastAction(String lastAction) { this.lastAction = lastAction; }

    public int getActionCount() { return actionCount; }
    public void setActionCount(int actionCount) { this.actionCount = actionCount; }

    public boolean hasActed() { return hasActed; }
    public void setHasActed(boolean hasActed) { this.hasActed = hasActed; }

    public boolean canBet(){
        return status == PlayerStatus.ACTIVE && chips > 0 && !allIn;
    }

    public boolean canCheck(){
        return status == PlayerStatus.ACTIVE && !allIn;
    }

    public boolean isInPosition(){
        // implementirati logiku za poziciju
        return position >= 6;
    }
}
