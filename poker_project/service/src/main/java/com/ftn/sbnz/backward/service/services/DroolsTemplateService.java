package com.ftn.sbnz.backward.service.services;

import com.ftn.sbnz.backward.model.models.GameState;
import com.ftn.sbnz.backward.model.models.HandStrengthData;
import com.ftn.sbnz.backward.model.models.Player;
import org.drools.template.ObjectDataCompiler;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class DroolsTemplateService {
    @Autowired
    private HandStrengthDataLoader dataLoader;

    @Autowired
    private KieContainer kieContainer;

    public void generateAndExecuteHandStrengthRules(GameState game) {
        try {
            List<HandStrengthData> handData = dataLoader.loadHandStrengthData();
            System.out.println("Loaded " + handData.size() + " hand strength entries from CSV");

            // Podeli podatke na suited i offsuit
            List<HandStrengthData> suitedData = new ArrayList<>();
            List<HandStrengthData> offsuitData = new ArrayList<>();

            for (HandStrengthData data : handData) {
                if (data.isSuited()) {
                    suitedData.add(data);
                } else {
                    offsuitData.add(data);
                }
            }

            // Generiši DRL za suited
            String suitedDRL = generateDRLFromTemplate(suitedData, "/rules/templates/hand_strength_suited.drt");

            // Generiši DRL za offsuit
            String offsuitDRL = generateDRLFromTemplate(offsuitData, "/rules/templates/hand_strength_offsuit.drt");

            String helperFunctions = generateHelperFunctions();

            // Kombinuj DRL
            String combinedDRL =
                    helperFunctions + "\n" + suitedDRL + "\n" + offsuitDRL;

            // Kreiraj session sa generisanim pravilima
            KieSession session = createSessionWithGeneratedRules(combinedDRL);

            // Ostali deo koda ostaje isti...
            session.insert(game);
            for (Player player : game.getPlayers()) {
                session.insert(player);
                session.insert(player.getHand());
            }

            int rulesFired = session.fireAllRules();
            System.out.println("Total rules fired: " + rulesFired);

            session.dispose();

        } catch (Exception e) {
            System.err.println("Error in template processing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String generateDRLFromTemplate(List<HandStrengthData> data, String templatePath) {
        try {
            // Učitaj template
            InputStream templateStream = getClass().getResourceAsStream(templatePath);
            ObjectDataCompiler compiler = new ObjectDataCompiler();

            // Generiši DRL
            String drl = compiler.compile(data, templateStream);

            if (templatePath.contains("offsuit") || templatePath.contains("suited")) {
                drl = drl.replaceFirst("package rules\\.templates;\\s*", "");
            }

            return drl;

        } catch (Exception e) {
            throw new RuntimeException("Error generating DRL from template", e);
        }
    }

    private KieSession createSessionWithGeneratedRules(String drl) {
        try {
            KieServices kieServices = KieServices.Factory.get();

            // Kreiraj in-memory file system
            KieFileSystem kfs = kieServices.newKieFileSystem();

            // Kombinuj helper funkcije i generisana pravila u jedan DRL
            String completeDRL = drl;

            kfs.write("src/main/resources/generated/complete_rules.drl", completeDRL);

            KieBuilder builder = kieServices.newKieBuilder(kfs);
            builder.buildAll();

            if (builder.getResults().hasMessages(Message.Level.ERROR)) {
                System.err.println("Build errors: " + builder.getResults().toString());
                throw new RuntimeException("Error building generated rules");
            }

            KieContainer generatedContainer = kieServices.newKieContainer(builder.getKieModule().getReleaseId());
            return generatedContainer.newKieSession();

        } catch (Exception e) {
            throw new RuntimeException("Error creating session with generated rules", e);
        }
    }

    private String generateHelperFunctions() {
        StringBuilder sb = new StringBuilder();
        sb.append("package rules.templates;\n\n");
        sb.append("import com.ftn.sbnz.backward.model.models.Hand;\n");
        sb.append("import com.ftn.sbnz.backward.model.models.Player;\n\n");

        sb.append("function boolean checkPosition(String allowedPositions, String playerPosition) {\n");
        sb.append("    if (allowedPositions == null || playerPosition == null) return false;\n");
        sb.append("    String[] positions = allowedPositions.split(\" \");\n");
        sb.append("    for (String pos : positions) {\n");
        sb.append("        if (pos.equalsIgnoreCase(playerPosition)) {\n");
        sb.append("            return true;\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("    return false;\n");
        sb.append("}\n\n");

        sb.append("rule \"Initialize Hand Values\"\n");
        sb.append("salience 100\n");
        sb.append("when\n");
        sb.append("    $hand : Hand(handStrength == 0.0)\n");
        sb.append("then\n");
        sb.append("    $hand.setHandStrength(0.0);\n");
        sb.append("    $hand.setHandCategory(\"UNKNOWN\");\n");
        sb.append("end\n\n");

        sb.append("rule \"Default Weak Hand\"\n");
        sb.append("salience 1\n");
        sb.append("when\n");
        sb.append("    $hand : Hand(handStrength == 0.0, handCategory == \"UNKNOWN\")\n");
        sb.append("then\n");
        sb.append("    $hand.setHandStrength(0.25);\n");
        sb.append("    $hand.setHandCategory(\"WEAK\");\n");
        sb.append("    System.out.println(\"RULE FIRED: Default weak hand assigned\");\n");
        sb.append("end\n");

        return sb.toString();
    }
}
