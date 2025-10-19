package com.ftn.sbnz.backward.service.services;

import com.ftn.sbnz.backward.model.events.*;
import com.ftn.sbnz.backward.model.models.Hand;
import com.ftn.sbnz.backward.model.models.Player;
import com.ftn.sbnz.backward.model.models.PlayerProfile;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TiltCepService {

    public String detectTilt() throws InterruptedException {
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("cepKsession");

        EntryPoint actionsEP = kieSession.getEntryPoint("player-actions");
        EntryPoint gameEP = kieSession.getEntryPoint("game-events");

        Player player = new Player("1", "EmotionalPlayer", 1000, 1);
        player.setAvgBetSize(100); // prosečan bet
        player.setProfile(new PlayerProfile("STABLE", 0.3));

        Hand strongHand = new Hand();
        strongHand.setHandStrength(0.8);
        player.setHand(strongHand);

        kieSession.insert(player);

        System.out.println("Before: " + player.getProfile().getProfileType());

        Thread fireThread = new Thread(() -> kieSession.fireUntilHalt());
        fireThread.start();
        simulateScenario();
        Random rand = new Random();
        Thread eventThread = new Thread(() -> {
            try {
                // Simulacija velikog gubitka (bad beat)
                gameEP.insert(new BadBeatEvent(player.getId(), System.currentTimeMillis()));
                Thread.sleep(1000);


                // U narednih 5 minuta - više agresivnih akcija
                for (int i = 0; i < 3; i++) {
                    actionsEP.insert(new AggressiveActionEvent(player.getId()));
                    Thread.sleep(400);
                }

                // Betovi veći od proseka → 200 > 2 * 100
                for (int i = 0; i < 3; i++) {
                    actionsEP.insert(new BetEvent(player.getId(), 250));
                    Thread.sleep(400);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        eventThread.start();
        eventThread.join();
        Thread.sleep(1000);

        System.out.println("After: " + player.getProfile().getProfileType()
                + " confidence: " + String.format("%.2f", player.getProfile().getConfidence()));

        kieSession.halt();
        kieSession.dispose();

        return "After: " + player.getProfile().getProfileType()
                + " confidence: " + String.format("%.2f", player.getProfile().getConfidence());
    }

    public void simulateScenario() {
        StringBuilder log = new StringBuilder();
        int pot = 0;
        int player1Chips = 2000;
        int player2Chips = 2000;
        int player3Chips = 2000;

        log.append("Starting round simulation...\n");

        // === Pre-BadBeat runda ===
        log.append("Player 2 bets 100\n");
        player2Chips -= 100; pot += 100;
        log.append("P2 chips=" + player2Chips + " pot=" + pot + "\n");

        log.append("Player 3 calls 100\n");
        player3Chips -= 100; pot += 100;
        log.append("P3 chips=" + player3Chips + " pot=" + pot + "\n");

        log.append("Player 1 raises to 300\n");
        player1Chips -= 300; pot += 300;
        log.append("P1 chips=" + player1Chips + " pot=" + pot + "\n");
        log.append("Aggressive action detected for Player 1\n\n");

        log.append("Player 2 folds\n");
        log.append("Player 3 calls 300\n");
        player3Chips -= 300; pot += 300;
        log.append("P3 chips=" + player3Chips + " pot=" + pot + "\n");

        log.append("Player 1 bets 200\n");
        player1Chips -= 200; pot += 200;
        log.append("P1 chips=" + player1Chips + " pot=" + pot + "\n");
        log.append("Aggressive action detected for Player 1\n\n");

        // === BAD BEAT DOGAĐAJ ===
        log.append("Player 1 loses a massive pot with a strong hand!\n");
        player1Chips -= 800;
        log.append("BadBeatEvent detected for Player 1\n");
        log.append("P1 chips=" + player1Chips + " pot reset to 0\n\n");
        pot = 0;

        // === Post-BadBeat - normalni potezi i 3 agresivna poteza ===
        log.append("Player 2 bets 150\n");
        player2Chips -= 150; pot += 150;
        log.append("P2 chips=" + player2Chips + " pot=" + pot + "\n");

        log.append("Player 3 raises to 300\n");
        player3Chips -= 300; pot += 300;
        log.append("P3 chips=" + player3Chips + " pot=" + pot + "\n");

        // Prvi agresivan potez
        log.append("Player 1 raises to 600\n");
        player1Chips -= 600; pot += 600;
        log.append("P1 chips=" + player1Chips + " pot=" + pot + "\n");
        log.append("Aggressive action detected for Player 1\n\n");

        log.append("Player 3 folds\n");
        log.append("Player 2 calls 600\n");
        player2Chips -= 600; pot += 600;
        log.append("P2 chips=" + player2Chips + " pot=" + pot + "\n");

        // Drugi agresivan potez
        log.append("Player 1 bets 400\n");
        player1Chips -= 400; pot += 400;
        log.append("P1 chips=" + player1Chips + " pot=" + pot + "\n");
        log.append("Aggressive action detected for Player 1\n\n");

        log.append("Player 2 calls 400\n");
        player2Chips -= 400; pot += 400;
        log.append("P2 chips=" + player2Chips + " pot=" + pot + "\n");

        // Treći agresivan potez
        log.append("Player 1 goes all-in for 500\n");
        player1Chips -= 500; pot += 500;
        log.append("P1 chips=" + player1Chips + " pot=" + pot + "\n");
        log.append("Aggressive action detected for Player 1\n\n");

        log.append("Player 2 folds. Player 1 wins the pot!\n");
        player1Chips += pot; pot = 0;

        log.append("\nRound finished.\n");
        log.append("Final stacks: P1=" + player1Chips + ", P2=" + player2Chips + ", P3=" + player3Chips + "\n");

        System.out.println(log.toString());
    }

}
