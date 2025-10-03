package com.ftn.sbnz.backward.model.models;

import com.ftn.sbnz.backward.model.enums.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck { // spil
    private List<Card> cards;
    public Deck(){
        cards = new ArrayList<>();
        initializeDeck();
    }

    private void initializeDeck() {
        cards.clear();
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"};

        for (Suit suit : Suit.values()) {
            for (String rank : ranks) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public void reset() {
        initializeDeck();
    }

    public void shuffle(){
        Collections.shuffle(cards);
    }

    public List<Card> dealCards(int numberOfCards){ // bice 2 za igrace, i po 3 ili 1 za flop, turn, river
        if (numberOfCards > cards.size()){
            throw new IllegalArgumentException("Nema dovoljno karata u spilu!");
        }

        List<Card> hand = new ArrayList<>(cards.subList(0, numberOfCards));
        cards.subList(0, numberOfCards).clear();
        return hand;
    }
    public void removeCard(Card card){
        cards.removeIf(c -> c.getRank().equals(card.getRank()) && c.getSuit() == card.getSuit());
    }
}
