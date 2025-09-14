package com.ftn.sbnz.backward.model.models;

import com.ftn.sbnz.backward.model.enums.GamePhase;
import com.ftn.sbnz.backward.model.enums.PlayerStatus;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private String gameId;
    private List<Player> players;
    private List<Card> communityCards;
    private Deck deck;
    private GamePhase phase;
    private int pot;
    private int currentBet;
    private int minRaise;
    private int smallBlindAmount;
    private int bigBlindAmount;
    private int dealerPosition;
    private int currentPlayerIndex;
    private String currentPlayerId;
    private boolean bettingComplete;
    private List<String> actionHistory;
    private long handStartTime;
    private int handNumber;

    // Side pots za all-in situacije
    private List<SidePot> sidePots;

    public GameState() {
        this.players = new ArrayList<>();
        this.communityCards = new ArrayList<>();
        this.deck = new Deck();
        this.phase = GamePhase.PRE_GAME;
        this.pot = 0;
        this.currentBet = 0;
        this.minRaise = 0;
        this.smallBlindAmount = 5;
        this.bigBlindAmount = 10;
        this.dealerPosition = 0;
        this.currentPlayerIndex = 0;
        this.bettingComplete = false;
        this.actionHistory = new ArrayList<>();
        this.sidePots = new ArrayList<>();
        this.handStartTime = System.currentTimeMillis();
        this.handNumber = 1;
    }

    public GameState(String gameId, List<String> playerNames, int initialChips) {
        this();
        this.gameId = gameId;

        // Kreiraj igrače
        for (int i = 0; i < playerNames.size(); i++) {
            Player player = new Player("player" + i, playerNames.get(i), initialChips, i);
            this.players.add(player);
        }
    }

    // Metode za game management
    public void addToPot(int amount) {
        this.pot += amount;
    }

    public void nextPlayer() {
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } while (players.get(currentPlayerIndex).getStatus() == PlayerStatus.FOLDED ||
                players.get(currentPlayerIndex).isAllIn());

        this.currentPlayerId = players.get(currentPlayerIndex).getId();
    }

    public void moveToNextPhase() {
        switch (phase) {
            case PRE_GAME:
                this.phase = GamePhase.PREFLOP;
                break;
            case PREFLOP:
                this.phase = GamePhase.FLOP;
                dealCommunityCards(3);
                break;
            case FLOP:
                this.phase = GamePhase.TURN;
                dealCommunityCards(1);
                break;
            case TURN:
                this.phase = GamePhase.RIVER;
                dealCommunityCards(1);
                break;
            case RIVER:
                this.phase = GamePhase.SHOWDOWN;
                break;
            case SHOWDOWN:
                this.phase = GamePhase.FINISHED;
                break;
        }
        resetBettingRound();
    }

    public void resetBettingRound() {
        this.currentBet = 0;
        this.bettingComplete = false;
        for (Player player : players) {
            player.resetForNewRound();
        }
    }

    private void dealCommunityCards(int count) {
        List<Card> newCards = deck.dealCards(count);
        communityCards.addAll(newCards);

        // Dodaj community karte svim igračima
        for (Player player : players) {
            player.getHand().addCommunityCards(newCards);
        }
    }

    public Player getCurrentPlayer() {
        if (currentPlayerIndex < players.size()) {
            return players.get(currentPlayerIndex);
        }
        return null;
    }

    public List<Player> getActivePlayers() {
        return players.stream()
                .filter(p -> p.getStatus() == PlayerStatus.ACTIVE ||
                        p.getStatus() == PlayerStatus.ALL_IN)
                .collect(ArrayList::new, (list, item) -> list.add(item), ArrayList::addAll);
    }

    public boolean isGameFinished() {
        return phase == GamePhase.FINISHED ||
                getActivePlayers().size() <= 1;
    }

    // Getteri i setteri za Drools
    public String getGameId() { return gameId; }
    public void setGameId(String gameId) { this.gameId = gameId; }

    public List<Player> getPlayers() { return players; }
    public void setPlayers(List<Player> players) { this.players = players; }

    public List<Card> getCommunityCards() { return communityCards; }
    public void setCommunityCards(List<Card> communityCards) { this.communityCards = communityCards; }

    public Deck getDeck() { return deck; }
    public void setDeck(Deck deck) { this.deck = deck; }

    public GamePhase getPhase() { return phase; }
    public void setPhase(GamePhase phase) { this.phase = phase; }

    public int getPot() { return pot; }
    public void setPot(int pot) { this.pot = pot; }

    public int getCurrentBet() { return currentBet; }
    public void setCurrentBet(int currentBet) { this.currentBet = currentBet; }

    public int getMinRaise() { return minRaise; }
    public void setMinRaise(int minRaise) { this.minRaise = minRaise; }

    public int getSmallBlindAmount() { return smallBlindAmount; }
    public void setSmallBlindAmount(int smallBlindAmount) { this.smallBlindAmount = smallBlindAmount; }

    public int getBigBlindAmount() { return bigBlindAmount; }
    public void setBigBlindAmount(int bigBlindAmount) { this.bigBlindAmount = bigBlindAmount; }

    public int getDealerPosition() { return dealerPosition; }
    public void setDealerPosition(int dealerPosition) { this.dealerPosition = dealerPosition; }

    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public void setCurrentPlayerIndex(int currentPlayerIndex) { this.currentPlayerIndex = currentPlayerIndex; }

    public String getCurrentPlayerId() { return currentPlayerId; }
    public void setCurrentPlayerId(String currentPlayerId) { this.currentPlayerId = currentPlayerId; }

    public boolean isBettingComplete() { return bettingComplete; }
    public void setBettingComplete(boolean bettingComplete) { this.bettingComplete = bettingComplete; }

    public List<String> getActionHistory() { return actionHistory; }
    public void setActionHistory(List<String> actionHistory) { this.actionHistory = actionHistory; }

    public List<SidePot> getSidePots() { return sidePots; }
    public void setSidePots(List<SidePot> sidePots) { this.sidePots = sidePots; }

    public int getHandNumber() { return handNumber; }
    public void setHandNumber(int handNumber) { this.handNumber = handNumber; }
}
