package com.ftn.sbnz.backward.service.services;

import com.ftn.sbnz.backward.model.events.BluffEvent;
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

        Player player = new Player("1","Drools1",1000,1);
        player.setProfile(new PlayerProfile("UNKNOWN",0.0));
        System.out.println("Player profile type before: " + player.getProfile().getProfileType());

        kieSession.insert(player);

        Thread fireThread = new Thread(() -> kieSession.fireUntilHalt());
        fireThread.start();

        Thread eventThread = new Thread(() -> {
            try {
                entryPoint.insert(new BluffEvent(player.getId()));
                Thread.sleep(1000);

                entryPoint.insert(new BluffEvent(player.getId()));
                Thread.sleep(1000);

                entryPoint.insert(new BluffEvent(player.getId()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        eventThread.start();

        eventThread.join();

        Thread.sleep(500);

        System.out.println("Player profile type after: " + player.getProfile().getProfileType());
        kieSession.halt();
        kieSession.dispose();
    }
}
