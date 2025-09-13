package com.ftn.sbnz.backward.service.services;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ftn.sbnz.backward.model.models.Location;

@Service
public class ActivateBackwardRulesService {
  private final KieContainer kieContainer;

  @Autowired
  public ActivateBackwardRulesService(KieContainer kieContainer) {
    this.kieContainer = kieContainer;
  }

  public void fireRules() {
    KieSession kSession = kieContainer.newKieSession();
    kSession.insert( new Location("Office", "House") );
    kSession.insert( new Location("Kitchen", "House") );
    kSession.insert( new Location("Knife", "Kitchen") );
    kSession.insert( new Location("Cheese", "Kitchen") );
    kSession.insert( new Location("Desk", "Office") );
    kSession.insert( new Location("Chair", "Office") );
    kSession.insert( new Location("Computer", "Desk") );
    kSession.insert( new Location("Draw", "Desk") );

    kSession.insert( "go1" );
    kSession.fireAllRules();
    System.out.println("---");

    kSession.insert( "go2" );
    kSession.fireAllRules();
    System.out.println("---");

    kSession.insert( "go3" );
    kSession.fireAllRules();
    System.out.println("---");

    kSession.insert( new Location("Key", "Draw") );
    kSession.fireAllRules();
    System.out.println("---");

    kSession.insert( "go4" );
    kSession.fireAllRules();
    System.out.println("---");

    kSession.insert( "go5" );
    kSession.fireAllRules();
  }
}
