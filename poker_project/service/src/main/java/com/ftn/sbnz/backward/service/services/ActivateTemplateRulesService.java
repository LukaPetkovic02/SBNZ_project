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

  public String fireRules(String card1Rank, String card1Suit, String card2Rank, String card2Suit) {

      String ret = "";
      GameState game = createInitialGame(card1Rank, card1Suit, card2Rank, card2Suit);

      templateService.generateAndExecuteHandStrengthRules(game);

      System.out.println("=== HAND STRENGTH RESULTS ===");
      for (Player player : game.getPlayers()) {
        Hand hand = player.getHand();
        if(player.getName().equals("Drools1")){ // ispisi samo za glavnog igraca
            ret = "Hand strength: " + hand.getHandStrength() + " hand category: " + hand.getHandCategory();
          System.out.printf("Player: %s | Cards: %s | Strength: %.2f | Category: %s%n",
                  player.getName(),
                  formatCards(hand.getHoleCards()),
                  hand.getHandStrength(),
                  hand.getHandCategory()
          );
        }
      }
      System.out.println("================================");
      return ret;
  }

  private GameState createInitialGame(String card1Rank, String card1Suit, String card2Rank, String card2Suit) {
    List<String> playerNames = Arrays.asList("Drools1","cpu1","cpu2","cpu3","cpu4");
    String gameId = "test-game-" + System.currentTimeMillis();

    GameState game = new GameState(gameId, playerNames, 1000);
    game.setPhase(GamePhase.PREFLOP);

    // dealTestHands(game);
    dealSpecificHand(game, card1Rank, card1Suit, card2Rank, card2Suit);

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

  private void dealSpecificHand(GameState game, String card1Rank, String card1Suit, String card2Rank, String card2Suit) {
      Deck deck = game.getDeck();
      deck.shuffle();

      // Deal specific cards to Drools1
      Suit suit1 = Suit.valueOf(card1Suit.toUpperCase());
      Suit suit2 = Suit.valueOf(card2Suit.toUpperCase());

      Card card1 = new Card(card1Rank.toUpperCase(), suit1);
      Card card2 = new Card(card2Rank.toUpperCase(), suit2);

      // Remove these specific cards from deck to avoid duplicates
      deck.removeCard(card1);
      deck.removeCard(card2);

      // Set hand for Drools1
      Player mainPlayer = game.getPlayers().stream()
              .filter(p -> p.getName().equals("Drools1"))
              .findFirst()
              .orElseThrow(() -> new RuntimeException("Drools1 player not found"));

      Hand hand = new Hand(card1, card2);
      mainPlayer.setHand(hand);
      System.out.println("Dealt to " + mainPlayer.getName() + ": " + card1 + ", " + card2);

      // Deal random hands to other players
      for (Player player : game.getPlayers()) {
          if (!player.getName().equals("Drools1")) {
              List<Card> cards = deck.dealCards(2);
              Hand randomHand = new Hand(cards.get(0), cards.get(1));
              player.setHand(randomHand);
          }
      }
  }

  private String formatCards(List<Card> cards) {
    if (cards.size() != 2) return "No cards";
    return cards.get(0).toString() + ", " + cards.get(1).toString();
  }
}
