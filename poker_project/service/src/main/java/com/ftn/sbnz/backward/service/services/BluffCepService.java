package com.ftn.sbnz.backward.service.services;

import com.ftn.sbnz.backward.model.events.BluffEvent;
import com.ftn.sbnz.backward.model.models.Player;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.springframework.stereotype.Service;

@Service
public class BluffCepService {

    public void detectBluffs(){
        KieServices ks = KieServices.Factory.get();
        KieContainer kieContainer = ks.newKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("cepKsession");

        EntryPoint entryPoint = kieSession.getEntryPoint("player-actions");

        Player player = new Player("1","Drools1",1000,1);
        kieSession.insert(player);

        new Thread(() -> {
            kieSession.fireUntilHalt();
        }).start();
        
        new Thread(() -> {
            try {
                entryPoint.insert(new BluffEvent(player.getId()));
                Thread.sleep(1000);

                entryPoint.insert(new BluffEvent(player.getId()));
                Thread.sleep(1000);

                entryPoint.insert(new BluffEvent(player.getId()));
                Thread.sleep(1000);

                kieSession.halt();
                kieSession.dispose();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
