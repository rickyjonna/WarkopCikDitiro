package com.example.warkopcikditiro.model;

public class ProductCategory {
    private int id;
    private String information;

    public ProductCategory() {
    }

    public ProductCategory(int id, String information) {
        this.id = id;
        this.information = information;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getinformation() {
        return information;
    }

    public void setinformation(String information) {
        this.information = information;
    }

    @Override
    public String toString() {
        return information;
    }
}
