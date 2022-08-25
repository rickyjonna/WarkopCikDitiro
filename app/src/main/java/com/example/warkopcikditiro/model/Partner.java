package com.example.warkopcikditiro.model;

public class Partner {
    private int id;
    private String owner;
    private int profit;

    public Partner() {
    }

    public Partner(int id, String owner, int profit) {
        this.id = id;
        this.owner = owner;
        this.profit = profit;
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

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    @Override
    public String toString() {
        return owner;
    }
}
