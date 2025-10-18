package com.ftn.sbnz.backward.service.services;

import com.ftn.sbnz.backward.model.models.PlayerStats;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BackwardAggressionService {

    private final KieContainer kieContainer;

    public BackwardAggressionService() {
        KieServices ks = KieServices.Factory.get();
        this.kieContainer = ks.getKieClasspathContainer();
    }

    public boolean isAggressivePlayer(String playerId, List<PlayerStats> history) {
        KieSession kieSession = kieContainer.newKieSession("backwardKSession");

        // globalne strukture za praćenje stanja
        Map<String, Integer> playerCounters = new HashMap<>();
        Map<String, Integer> playerRounds = new HashMap<>();

        playerCounters.put(playerId, 0);
        playerRounds.put(playerId, history.size());

        kieSession.setGlobal("statsList", history);
        kieSession.setGlobal("playerCounters", playerCounters);
        kieSession.setGlobal("playerRounds", playerRounds);

        // ubacujemo samo ID igrača da bi se pokrenula pravila
        kieSession.insert(playerId);
        kieSession.fireAllRules();
        kieSession.dispose();

        boolean aggressive = playerCounters.get(playerId) >= 6;
        if (aggressive) {
            System.out.println("Igrač " + playerId + " je AGRESIVAN!");
        } else {
            System.out.println("Igrač " + playerId + " nije agresivan.");
        }
        return aggressive;
    }

    public void testBackwardAggression() {
        List<PlayerStats> history = new ArrayList<>();
        Random rand = new Random();

        for (int i = 1; i <= 10; i++) {
            // Random odlučuje da li će se desiti podcilj
            int preflopRaises = rand.nextBoolean() ? 4 : 2;
            int continuationBets = rand.nextBoolean() ? 3 : 1;
            int avgBetAllPlayers = 80;
            int avgBetSize = 20 + rand.nextInt(101);            // random avg bet size

            PlayerStats ps = new PlayerStats(
                    "player1",
                    preflopRaises,
                    continuationBets,
                    avgBetSize,
                    avgBetAllPlayers,
                    i
            );
            history.add(ps);
        }

        isAggressivePlayer("player1", history);
    }
}
