package com.example.warkopcikditiro.model;

public class Ingredient {
    private int id;
    private String name;
    private String unit;
    private int amount;
    private int minimalamount;

    public Ingredient() {
    }

    public Ingredient(int id, String name, String unit, int amount, int minimalamount) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.amount = amount;
        this.minimalamount = minimalamount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getMinimalamount() {
        return minimalamount;
    }

    public void setMinimalamount(int minimalamount) {
        this.minimalamount = minimalamount;
    }
}
