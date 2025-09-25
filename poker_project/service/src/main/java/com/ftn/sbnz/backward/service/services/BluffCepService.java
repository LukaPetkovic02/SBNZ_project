package com.ftn.sbnz.backward.service.services;

import com.ftn.sbnz.backward.model.events.BluffEvent;
import com.ftn.sbnz.backward.model.events.FoldEvent;
import com.ftn.sbnz.backward.model.events.LossEvent;
import com.ftn.sbnz.backward.model.models.Player;
import com.ftn.sbnz.backward.model.models.PlayerProfile;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.springframework.stereotype.Service;

@Service
public class BluffCepService {

    public void detectBluffs() throws InterruptedException{
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("cepKsession");

        EntryPoint entryPoint = kieSession.getEntryPoint("player-actions");

        Player player1 = new Player("1","Drools1",1000,1);
        player1.setProfile(new PlayerProfile("UNKNOWN",0.0));

        Player player2 = new Player("2", "Drools2", 1000, 2);
        player2.setProfile(new PlayerProfile("UNKNOWN", 0.0));

        kieSession.insert(player1);
        kieSession.insert(player2);

        System.out.println("Player 1 profile type before: " + player1.getProfile().getProfileType());
        System.out.println("Player 2 profile type before: " + player2.getProfile().getProfileType());

        Thread fireThread = new Thread(() -> kieSession.fireUntilHalt());
        fireThread.start();

        Thread eventThread = new Thread(() -> {
            try {
                // CEP događaji za oba igrača → oba postaju FREQUENT_BLUFFER
                for (int i = 0; i < 3; i++) {
                    entryPoint.insert(new BluffEvent(player1.getId()));
                    entryPoint.insert(new BluffEvent(player2.getId()));
                    Thread.sleep(500);
                }

                // Dodajemo i LossEvent-ove da pokrenu Tilt logiku
                for (int i = 0; i < 6; i++) {
                    kieSession.insert(new LossEvent(player1.getId()));
                    Thread.sleep(300);
                }

                // Player 1 nastavlja da blefira → confidence raste → ide u TILT
                for (int i = 0; i < 5; i++) {
                    kieSession.insert(new BluffEvent(player1.getId()));
                    Thread.sleep(500);
                }

                // Player 2 folduje → confidence opada → vraća se u STABLE
                for (int i = 0; i < 5; i++) {
                    kieSession.insert(new FoldEvent(player2.getId()));
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        eventThread.start();

        eventThread.join();

        Thread.sleep(500);

        System.out.println("Player 1 profile after: " + player1.getProfile().getProfileType()
                + " confidence: " + String.format("%.2f", player1.getProfile().getConfidence()));
        System.out.println("Player 2 profile after: " + player2.getProfile().getProfileType()
                + " confidence: " + String.format("%.2f", player2.getProfile().getConfidence()));
        kieSession.halt();
        kieSession.dispose();
    }
}
