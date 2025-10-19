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
    private final List<PlayerStats> history = new ArrayList<>();

    public BackwardAggressionService() {
        KieServices ks = KieServices.Factory.get();
        this.kieContainer = ks.getKieClasspathContainer();
    }

    public String simulateRound() {
        int roundNumber = history.size() + 1;
        int pot = 0;

        // Stack-ovi igrača
        int playerChips = 1000;
        int cpuLeftChips = 1000;
        int cpuRightChips = 1000;

        // Brojači događaja za player1
        int preflopRaises = 0;
        int continuationBets = 0;
        int totalBetSize = 0;

        Random rand = new Random();

        // Svaki igrač igra redom po potezu
        String[] players = {"player1", "cpuLeft", "cpuRight"};

        for (int i = 1; i <= 5; i++) { // 5 poteza po rundi
            for (String actor : players) {
                boolean isFold = rand.nextDouble() < 0.1; // 10% šanse da fold-uje
                int betSize = 20 + rand.nextInt(101); // bet između 20-120
                boolean isRaise = rand.nextBoolean();
                if(actor.equals("player1")) {
                    betSize += rand.nextInt(101);
                    if(!isRaise && rand.nextDouble() < 0.8){
                        isRaise = true;
                    }
                }

                if (isFold) {
                    System.out.println(actor + " folds");
                    continue;
                }

                // Update stack-ova
                if (actor.equals("player1")) playerChips -= betSize;
                else if (actor.equals("cpuLeft")) cpuLeftChips -= betSize;
                else cpuRightChips -= betSize;

                pot += betSize;
                System.out.println(actor + " bets " + betSize + " → pot=" + pot
                        + " P1=" + playerChips + " CL=" + cpuLeftChips + " CR=" + cpuRightChips);

                // Brojanje događaja samo za player1
                if (actor.equals("player1")) {
                    totalBetSize += betSize;
                    if (isRaise) preflopRaises++;
                    if (i > 1 && isRaise) continuationBets++; // pojednostavljeno
                }
            }
        }

        int avgBetAllPlayers = (pot - totalBetSize) / 2; // prosečno za ostale CPU igrače
        int avgBetSize = (preflopRaises + continuationBets) > 0 ? totalBetSize / (preflopRaises + continuationBets) : 0;

        PlayerStats ps = new PlayerStats(
                "player1",
                preflopRaises,
                continuationBets,
                avgBetSize,
                avgBetAllPlayers,
                roundNumber
        );

        history.add(ps);

        System.out.println("End of round " + roundNumber + " → PlayerStats: preflopRaises=" + preflopRaises
                + ", continuationBets=" + continuationBets + ", avgBetSize=" + avgBetSize);

        return ps.toString();
    }


    public String isAggressivePlayer(String playerId, List<PlayerStats> history) {
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
            return "Igrač " + playerId + " je AGRESIVAN!";
        } else {
            System.out.println("Igrač " + playerId + " nije agresivan.");
            return "Igrač " + playerId + " nije agresivan.";
        }
    }

    public String testBackwardAggression() {
//        List<PlayerStats> history = new ArrayList<>();
        Random rand = new Random();

//        for (int i = 1; i <= 10; i++) {
//            // Random odlučuje da li će se desiti podcilj
//            int preflopRaises = rand.nextBoolean() ? 4 : 2;
//            int continuationBets = rand.nextBoolean() ? 3 : 1;
//            int avgBetAllPlayers = 80;
//            int avgBetSize = 20 + rand.nextInt(101);            // random avg bet size
//
//            PlayerStats ps = new PlayerStats(
//                    "player1",
//                    preflopRaises,
//                    continuationBets,
//                    avgBetSize,
//                    avgBetAllPlayers,
//                    i
//            );
//            history.add(ps);
//        }

        return isAggressivePlayer("player1", history);
    }
}
