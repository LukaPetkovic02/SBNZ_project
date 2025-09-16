package com.ftn.sbnz.backward.model.models;

public class HandStrengthData {
    private String card1;
    private String card2;
    private boolean suited;
    private String handType;
    private double handStrength;
    private String position;

    // Constructors
    public HandStrengthData() {}

    // Getteri i setteri
    public String getCard1() { return card1; }
    public void setCard1(String card1) { this.card1 = card1; }

    public String getCard2() { return card2; }
    public void setCard2(String card2) { this.card2 = card2; }

    public boolean isSuited() { return suited; }
    public void setSuited(boolean suited) {
        this.suited = suited;
    }

    public String getHandType() { return handType; }
    public void setHandType(String handType) { this.handType = handType; }

    public double getHandStrength() { return handStrength; }
    public void setHandStrength(double handStrength) { this.handStrength = handStrength; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
}
