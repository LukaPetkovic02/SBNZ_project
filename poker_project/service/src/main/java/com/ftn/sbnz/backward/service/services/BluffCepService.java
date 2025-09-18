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

        entryPoint.insert(new BluffEvent(player.getId(), System.currentTimeMillis()));
        entryPoint.insert(new BluffEvent(player.getId(), System.currentTimeMillis()+1000));
        entryPoint.insert(new BluffEvent(player.getId(), System.currentTimeMillis()+2000));

        int fired = kieSession.fireAllRules();
        System.out.println("CEP rules fired: " + fired);

        kieSession.dispose();
    }
}
