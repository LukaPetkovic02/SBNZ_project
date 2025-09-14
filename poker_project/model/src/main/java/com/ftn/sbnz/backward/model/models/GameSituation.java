package com.ftn.sbnz.backward.model.models;

import java.util.List;

public class GameSituation {
    // INPUT
    private Player player; // igrac za kog preporucujemo potez
    private int currentPot; // ukupno cipova u potu
    private List<Card> cards; // karte na stolu, na osnovu toga i zakljucujemo koja je faza igre (preflop,flop,turn,river)
    private List<Player> activePlayers; // protivnici koji jos nisu foldovali, i sve njihove informacije
    private int SBPosition, BBPosition;
    // OUTPUT
    private String suggestedAction; // output - preporucen potez (FOLD, CALL, CHECK, RAISE, ALL-IN)
    private String explanation; // objasnjenje

    public void suggestAction(){

    }


}
