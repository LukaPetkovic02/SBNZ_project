package com.ftn.sbnz.backward.service.services;

import com.ftn.sbnz.backward.model.enums.GamePhase;
import com.ftn.sbnz.backward.model.enums.Suit;
import com.ftn.sbnz.backward.model.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ActivateTemplateRulesService {
  private final DroolsTemplateService templateService;

  @Autowired
  public ActivateTemplateRulesService(DroolsTemplateService templateService) {
    this.templateService = templateService;
  }

  public void fireRules() {


      GameState game = createInitialGame();

      templateService.generateAndExecuteHandStrengthRules(game);

      System.out.println("=== HAND STRENGTH RESULTS ===");
      for (Player player : game.getPlayers()) {
        Hand hand = player.getHand();
        System.out.printf("Player: %s | Cards: %s | Strength: %.2f | Category: %s%n",
                player.getName(),
                formatCards(hand.getHoleCards()),
                hand.getHandStrength(),
                hand.getHandCategory()
        );
      }
      System.out.println("================================");

  }

  private GameState createInitialGame() {
    List<String> playerNames = Arrays.asList("Drools1","cpu1","cpu2","cpu3","cpu4");
    String gameId = "test-game-" + System.currentTimeMillis();

    GameState game = new GameState(gameId, playerNames, 1000);
    game.setPhase(GamePhase.PREFLOP);

    // dealTestHands(game);
    dealRandomHands(game);

    return game;
  }

//  private void dealTestHands(GameState game) {
//    // Test hands za demonstraciju različitih strengths
//    List<List<Card>> testHands = Arrays.asList(
//            Arrays.asList(new Card("A", Suit.SPADE), new Card("A", Suit.HEART)),    // AA - PREMIUM
//            Arrays.asList(new Card("K", Suit.DIAMOND), new Card("K", Suit.CLUB)),   // KK - PREMIUM
//            Arrays.asList(new Card("A", Suit.SPADE), new Card("K", Suit.SPADE)),    // AKs - PREMIUM
//            Arrays.asList(new Card("A", Suit.HEART), new Card("Q", Suit.DIAMOND)),  // AQo - PLAYABLE
//            Arrays.asList(new Card("7", Suit.CLUB), new Card("2", Suit.DIAMOND))    // 72o - WEAK
//    );
//
//    for (int i = 0; i < game.getPlayers().size(); i++) {
//      Player player = game.getPlayers().get(i);
//      List<Card> cards = testHands.get(i);
//
//      Hand hand = new Hand(cards.get(0), cards.get(1));
//      player.setHand(hand);
//
//      System.out.println("Dealt to " + player.getName() + ": " +
//              cards.get(0) + ", " + cards.get(1));
//    }
//  }

  private void dealRandomHands(GameState game) {
    Deck deck = game.getDeck();
    deck.shuffle();
    if (deck == null) {
      throw new IllegalStateException("Deck is not initialized!");
    }

    for (Player player : game.getPlayers()) {
      List<Card> cards = deck.dealCards(2);

      Hand hand = new Hand(cards.get(0), cards.get(1));
      player.setHand(hand);

      System.out.println("Dealt to " + player.getName() + ": " +
              cards.get(0) + ", " + cards.get(1));
    }
  }

  private String formatCards(List<Card> cards) {
    if (cards.size() != 2) return "No cards";
    return cards.get(0).toString() + ", " + cards.get(1).toString();
  }
}
