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

@Service
public class TiltCepService {

    public void detectTilt() throws InterruptedException {
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

        Thread eventThread = new Thread(() -> {
            try {
                // Simulacija velikog gubitka (bad beat)
                gameEP.insert(new BadBeatEvent(player.getId(), System.currentTimeMillis()));
                Thread.sleep(1000);

                // U narednih 5 minuta - više agresivnih akcija
                for (int i = 0; i < 2; i++) {
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
    }
}
