package com.example.warkopcikditiro.model;

public class Partner {
    private int id;
    private String owner;
    private int percentage;

    public Partner() {
    }

    public Partner(int id, String owner, int percentage) {
        this.id = id;
        this.owner = owner;
        this.percentage = percentage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return owner;
    }
}
