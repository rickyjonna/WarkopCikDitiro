package com.example.warkopcikditiro.model;

public class Agent {
    private int id;
    private int payment_id;
    private String payment_information;
    private String name;
    private int percentage;

    public Agent() {
    }

    public Agent(int id, int payment_id, String name, int percentage) {
        this.id = id;
        this.payment_id = payment_id;
        this.payment_information = payment_information;
        this.name = name;
        this.percentage = percentage;
    }

    public int getId() {
        return id;
    }

    public String getPayment_information() {
        return payment_information;
    }

    public void setPayment_information(String payment_information) {
        this.payment_information = payment_information;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
}
