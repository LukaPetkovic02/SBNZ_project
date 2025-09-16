package com.ftn.sbnz.backward.service.services;

import com.ftn.sbnz.backward.model.models.HandStrengthData;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class HandStrengthDataLoader {

    public List<HandStrengthData> loadHandStrengthData() {
        List<HandStrengthData> data = new ArrayList<>();

        try (InputStream is = getClass().getResourceAsStream("/data/hand_strength.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String line = reader.readLine(); // Skip header

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    HandStrengthData item = new HandStrengthData();
                    item.setCard1(parts[0].trim());
                    item.setCard2(parts[1].trim());
                    boolean suited = "true".equalsIgnoreCase(parts[2].trim()) ||
                            "1".equals(parts[2].trim()) ||
                            "suited".equalsIgnoreCase(parts[2].trim());
                    item.setSuited(suited);
                    item.setHandType(parts[3].trim());
                    item.setHandStrength(Double.parseDouble(parts[4].trim()));
                    item.setPosition(parts[5].trim().replace("\"", ""));

                    data.add(item);
                }
            }

        } catch (Exception e) {
            System.err.println("Error loading hand strength data: " + e.getMessage());
        }

        return data;
    }
}
