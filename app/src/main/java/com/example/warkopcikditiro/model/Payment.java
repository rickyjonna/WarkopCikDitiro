package com.example.warkopcikditiro.model;

public class Payment {
    private int id;
    private String information;
    private int discount;

    public Payment() {
    }

    public Payment(int id, String information, int discount) {
        this.id = id;
        this.information = information;
        this.discount = discount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return information;
    }
}

