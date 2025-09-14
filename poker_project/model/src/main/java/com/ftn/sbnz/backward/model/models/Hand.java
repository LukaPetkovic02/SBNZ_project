package com.ftn.sbnz.backward.model.models;

import com.ftn.sbnz.backward.model.enums.HandType;

import java.util.ArrayList;
import java.util.List;

public class Hand { // ruka glavnog igraca - za kog predlazemo akcije
    private List<Card> holeCards; // 2 karte igraca
    private List<Card> allCards; // sve karte (hole + community)
    private HandType handType;
    private int handRank;
    private List<Card> bestHand; // 5 najboljih karata
    private List<Integer> kickers; // za tie-breaking
    private boolean suited;
    private String handCategory; // "PREMIUM","STRONG","PLAYABLE","MARGINAL","WEAK"
    private double handStrength; // 0.0-1.0
    private boolean evaluated = false;

    public Hand(){
        this.holeCards = new ArrayList<>();
        this.allCards = new ArrayList<>();
        this.bestHand = new ArrayList<>();
        this.kickers = new ArrayList<>();
    }

    public Hand(Card card1, Card card2){
        this();
        this.holeCards.add(card1);
        this.holeCards.add(card2);
        this.allCards.add(card1);
        this.allCards.add(card2);
        this.suited = card1.getSuit().equals(card2.getSuit());
    }

    public void addCommunityCards(List<Card> communityCards){
        this.allCards.addAll(communityCards);
        this.evaluated = false;
    }

    public List<Card> getHoleCards() { return holeCards; }
    public void setHoleCards(List<Card> holeCards) { this.holeCards = holeCards; }

    public List<Card> getAllCards() { return allCards; }
    public void setAllCards(List<Card> allCards) { this.allCards = allCards; }

    public HandType getHandType() { return handType; }
    public void setHandType(HandType handType) { this.handType = handType; }

    public int getHandRank() { return handRank; }
    public void setHandRank(int handRank) { this.handRank = handRank; }

    public List<Card> getBestHand() { return bestHand; }
    public void setBestHand(List<Card> bestHand) { this.bestHand = bestHand; }

    public List<Integer> getKickers() { return kickers; }
    public void setKickers(List<Integer> kickers) { this.kickers = kickers; }

    public boolean isSuited() { return suited; }
    public void setSuited(boolean suited) { this.suited = suited; }

    public String getHandCategory() { return handCategory; }
    public void setHandCategory(String handCategory) { this.handCategory = handCategory; }

    public double getHandStrength() { return handStrength; }
    public void setHandStrength(double handStrength) { this.handStrength = handStrength; }

    public boolean isEvaluated() { return evaluated; }
    public void setEvaluated(boolean evaluated) { this.evaluated = evaluated; }

    public boolean isPocketPair(){
        return holeCards.size() == 2 &&
                holeCards.get(0).getRankValue() == holeCards.get(1).getRankValue();
    }

    public boolean isConnectors(){
        if(holeCards.size()!=2) return false;
        int diff = Math.abs(holeCards.get(0).getRankValue() - holeCards.get(1).getRankValue());
        return (diff == 1) || (diff == 12); // A-K
    }
}
