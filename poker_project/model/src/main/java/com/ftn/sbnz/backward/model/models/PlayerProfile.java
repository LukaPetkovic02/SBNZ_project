package com.ftn.sbnz.backward.model.models;

public class PlayerProfile {
    private String profileType;
    private double confidence;
    private Long createdAt;

    public PlayerProfile(){}

    public PlayerProfile( String profileType, double confidence) {
        this.profileType = profileType;
        this.confidence = confidence;
        this.createdAt = System.currentTimeMillis();
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
