package com.ftn.sbnz.backward.model.models;


import com.ftn.sbnz.backward.model.enums.Suit;

import java.util.Objects;

public class Card implements Comparable<Card>{
    private String rank;
    private Suit suit;
    public Card(){}

    public Card(String rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getRankValue(){
        switch (rank){
            case "2": return 2;
            case "3": return 3;
            case "4": return 4;
            case "5": return 5;
            case "6": return 6;
            case "7": return 7;
            case "8": return 8;
            case "9": return 9;
            case "T": return 10;
            case "J": return 11;
            case "Q": return 12;
            case "K": return 13;
            case "A": return 14;
            default: return 0;
        }
    }

    @Override
    public int compareTo(Card other){
        return Integer.compare(this.getRankValue(), other.getRankValue());
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return Objects.equals(rank, card.rank) && suit == card.suit;
    }

    @Override
    public int hashCode(){
        return Objects.hash(rank, suit);
    }

    @Override
    public String toString() {
        String suitSymbol;
        switch (suit) {
            case SPADE:   suitSymbol = "♠"; break;
            case HEART:   suitSymbol = "♥"; break;
            case DIAMOND: suitSymbol = "♦"; break;
            case CLUB:    suitSymbol = "♣"; break;
            default:      suitSymbol = "?"; break;
        }
        return rank + suitSymbol;
    }
}
