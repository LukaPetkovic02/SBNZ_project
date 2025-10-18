package com.ftn.sbnz.backward.service.services;

import com.ftn.sbnz.backward.model.enums.ActionType;
import com.ftn.sbnz.backward.model.enums.GamePhase;
import com.ftn.sbnz.backward.model.enums.PlayerStatus;
import com.ftn.sbnz.backward.model.enums.Suit;
import com.ftn.sbnz.backward.model.models.*;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ForwardChainingDecisionService {

    private KieContainer kieContainer;

    public ForwardChainingDecisionService() {
        initializeRulesEngine();
    }

    public PlayerAction suggestNextMove(GameState gameState, String playerId) {
        try {
            KieSession session = kieContainer.newKieSession("forwardKSession");

            // Create decision context
            DecisionContext context = createDecisionContext(gameState, playerId);

            // Insert facts
            session.insert(gameState);
            session.insert(context);

            // Insert all players and hands
            for (Player player : gameState.getPlayers()) {
                session.insert(player);
                session.insert(player.getHand());
            }

            // Create and insert action recommendation
            ActionRecommendation recommendation = new ActionRecommendation(context.getPlayerId(), null, 0, 0.0, null);
            session.insert(recommendation);

            System.out.println("DecisionContext debug:");
            System.out.println(" - playerId: " + context.getPlayerId());
            System.out.println(" - handStrength: " + context.getHandStrength());
            System.out.println(" - handCategory: " + context.getHandCategory());
            // Fire rules
            int rulesFired = session.fireAllRules();
            System.out.println("Forward chaining rules fired: " + rulesFired);

            // Extract recommendation
            PlayerAction suggestedAction = new PlayerAction();
            suggestedAction.setPlayerId(playerId);
            suggestedAction.setActionType(recommendation.getRecommendedAction());
            suggestedAction.setAmount(recommendation.getBetAmount());
            suggestedAction.setConfidence(recommendation.getConfidence());
            suggestedAction.setReasoning(recommendation.getReasoning());

            session.dispose();
            return suggestedAction;

        } catch (Exception e) {
            System.err.println("Error in forward chaining decision: " + e.getMessage());
            e.printStackTrace();
            return createDefaultAction(playerId);
        }
    }

    private DecisionContext createDecisionContext(GameState gameState, String playerId) {
        Player currentPlayer = gameState.getPlayers().stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Player not found: " + playerId));

        DecisionContext context = new DecisionContext();
        context.setPlayerId(playerId);
        context.setGamePhase(gameState.getPhase());
        context.setHandStrength(currentPlayer.getHand().getHandStrength());
        context.setHandCategory(currentPlayer.getHand().getHandCategory());
        context.setPosition(currentPlayer.getPosition());
        context.setChipStack(currentPlayer.getChips());
        context.setPotSize(gameState.getPot());
        context.setCurrentBet(gameState.getCurrentBet());
        context.setBetToCall(calculateBetToCall(gameState, currentPlayer));
        context.setPotOdds(calculatePotOdds(gameState, currentPlayer));
        context.setNumActivePlayers(getActivePlayersCount(gameState));
        context.setAggression(calculateAggression(gameState));
        context.setBluffProbability(calculateBluffProbability(gameState, currentPlayer));

        return context;
    }

    private int calculateBetToCall(GameState gameState, Player player) {
        return Math.max(0, gameState.getCurrentBet() - player.getCurrentBet());
    }

    private double calculatePotOdds(GameState gameState, Player player) {
        int betToCall = calculateBetToCall(gameState, player);
        if (betToCall == 0) return 0.0;
        return (double) betToCall / (gameState.getPot() + betToCall);
    }

    private int getActivePlayersCount(GameState gameState) {
        return (int) gameState.getPlayers().stream()
                .filter(p -> p.getStatus() == PlayerStatus.ACTIVE)
                .count();
    }

    private double calculateAggression(GameState gameState) {
        // Simple aggression calculation based on betting patterns
        double totalBets = gameState.getPlayers().stream()
                .mapToDouble(Player::getCurrentBet)
                .sum();
        return totalBets / (gameState.getBigBlindAmount() * 10.0); // Normalized aggression
    }

    private double calculateBluffProbability(GameState gameState, Player player) {
        // Basic bluff probability based on betting patterns and position
        double baseProbability = 0.1;

        if (player.getPosition() == 3 || player.getPosition() == 4) {
            baseProbability += 0.05;
        }

        if (gameState.getCurrentBet() > gameState.getBigBlindAmount() * 3) {
            baseProbability += 0.1;
        }

        return Math.min(0.5, baseProbability);
    }

    private PlayerAction createDefaultAction(String playerId) {
        PlayerAction action = new PlayerAction();
        action.setPlayerId(playerId);
        action.setActionType(ActionType.FOLD);
        action.setAmount(0);
        action.setConfidence(0.5);
        action.setReasoning("Default action due to error");
        return action;
    }

    private void initializeRulesEngine() {
        try {
            KieServices kieServices = KieServices.Factory.get();
            this.kieContainer = kieServices.getKieClasspathContainer(); // loads from resources + kmodule.xml
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize forward chaining rules engine", e);
        }
    }

    public void testForwardDecision() {
        // Kreiramo stanje igre (GameState) — simulacija
        List<String> playerNames = Arrays.asList("Uros", "Marko", "Nikola");
        GameState gameState = new GameState("testGame1", playerNames, 2000);

        gameState.setPhase(GamePhase.FLOP);
        gameState.setPot(1500);
        gameState.setCurrentBet(100);
        gameState.setBigBlindAmount(50);
        gameState.setDealerPosition(0);
        gameState.setCurrentPlayerIndex(0);
        gameState.setCurrentPlayerId(gameState.getPlayers().get(0).getId());

        // Postavi ruke
        Card cardA = new Card("A", Suit.SPADE); // As ♠
        Card cardK = new Card("K", Suit.SPADE); // K ♠
        Hand hand1 = new Hand(cardA, cardK);
        hand1.setHandStrength(0.85);
        hand1.setHandCategory("PREMIUM");

        Card cardQ = new Card("Q", Suit.HEART);
        Card cardJ = new Card("J", Suit.DIAMOND);
        Hand hand2 = new Hand(cardQ, cardJ);
        hand2.setHandStrength(0.55);
        hand2.setHandCategory("STRONG");

        Card card7 = new Card("7", Suit.CLUB);
        Card card5 = new Card("5", Suit.HEART);
        Hand hand3 = new Hand(card7, card5);
        hand3.setHandStrength(0.35);
        hand3.setHandCategory("PLAYABLE");

// Poveži ruke sa igračima
        gameState.getPlayers().get(0).setHand(hand1);
        gameState.getPlayers().get(1).setHand(hand2);
        gameState.getPlayers().get(2).setHand(hand3);


        for (Player p : gameState.getPlayers()) {
            p.setStatus(PlayerStatus.ACTIVE);
        }

        // Poziv metode koja pokreće KIE sesiju i pravila
        String currentPlayerId = gameState.getPlayers().get(0).getId();

        PlayerAction result = suggestNextMove(gameState, currentPlayerId);

        // Ispis rezultata
        System.out.println(">>> Player: " + gameState.getPlayers().get(0).getName());
        System.out.println(">>> Preporučena akcija: " + result.getActionType());
        System.out.println(">>> Iznos: " + result.getAmount());
        System.out.println(">>> Poverenje: " + result.getConfidence());
        System.out.println(">>> Obrazloženje: " + result.getReasoning());
    }
}