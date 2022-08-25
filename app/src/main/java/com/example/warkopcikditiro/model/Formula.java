package com.example.warkopcikditiro.model;

public class Formula {
    private int product_id;
    private int ingredient_id;
    private int amount;

    public Formula(int product_id, int ingredient_id, int amount) {
        this.product_id = product_id;
        this.ingredient_id = ingredient_id;
        this.amount = amount;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getIngredient_id() {
        return ingredient_id;
    }

    public void setIngredient_id(int ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
