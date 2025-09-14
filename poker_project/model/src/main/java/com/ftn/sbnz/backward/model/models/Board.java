package com.ftn.sbnz.backward.model.models;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Player> players;
    private int SBPosition; // small blind
    private int BBPosition; // big blind
    private List<Card> cards; // karte na stolu
    private int potSize;

    public Board() {}
    public Board(List<Player> players) {
        this.players = players;
        this.SBPosition = 0;
        this.BBPosition = 1;
        this.cards = new ArrayList<>();
        this.potSize = 0;
    }
    public void rotateBlinds(){
        int num_players = players.size();
        this.SBPosition = (this.SBPosition + 1) % num_players;
        this.BBPosition = (this.BBPosition + 1) % num_players;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getSBPosition() {
        return SBPosition;
    }

    public void setSBPosition(int SBPosition) {
        this.SBPosition = SBPosition;
    }

    public int getBBPosition() {
        return BBPosition;
    }

    public void setBBPosition(int BBPosition) {
        this.BBPosition = BBPosition;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public int getPotSize() {
        return potSize;
    }

    public void setPotSize(int potSize) {
        this.potSize = potSize;
    }
}
