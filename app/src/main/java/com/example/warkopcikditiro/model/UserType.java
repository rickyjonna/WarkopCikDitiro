package com.example.warkopcikditiro.model;

public class UserType {
    private int id;
    private String information;

    public UserType() {
    }

    public UserType(int id, String information) {
        this.id = id;
        this.information = information;
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

    @Override
    public String toString() {
        return information;
    }
}

