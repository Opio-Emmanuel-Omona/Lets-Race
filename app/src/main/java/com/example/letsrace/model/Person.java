package com.example.letsrace.model;

public class Person {

    String username;
    String weight;
    String height;
    int distanceCovered;
    Long timeSpent;

    public Person(String username, String weight, String height, int distanceCovered, Long timeSpent) {
        this.username = username;
        this.weight = weight;
        this.height = height;
        this.distanceCovered = distanceCovered;
        this.timeSpent = timeSpent;
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }

    public int getDistanceCovered() {
        return distanceCovered;
    }

    public Long getTimeSpent() {
        return timeSpent;
    }

    // Setters
    public void setUsername(String username) {
        this.username = username;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setDistanceCovered(int distanceCovered) {
        this.distanceCovered = distanceCovered;
    }

    public void setTimeSpent(Long timeSpent) {
        this.timeSpent = timeSpent;
    }
}
