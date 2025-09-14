package com.ftn.sbnz.backward.model.models;

public class Hand { // ruka glavnog igraca - za kog predlazemo akcije
    private Card card1;
    private Card card2;
    private boolean suited;
    private String handType; // "PREMIUM","STRONG","PLAYABLE","MARGINAL","WEAK"
    private double handStrength; // 0.0-1.0

    public Hand(){}

    public Hand(Card card1, Card card2){
        this.card1 = card1;
        this.card2 = card2;
        this.suited = card1.getSuit().equals(card2.getSuit());
    }
}
