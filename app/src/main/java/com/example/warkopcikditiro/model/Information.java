package com.example.warkopcikditiro.model;

public class Information {
    private String order_information;
    private String order_note;
    private int order_vendor_id;
    private int order_id;

    public Information() {
    }

    public Information(String order_information, String order_note, int order_vendor_id, int order_id) {
        this.order_information = order_information;
        this.order_note = order_note;
        this.order_vendor_id = order_vendor_id;
        this.order_id = order_id;
    }

    public String getorder_information() {
        return order_information;
    }

    public void setorder_information(String order_information) {
        this.order_information = order_information;
    }

    public String getorder_note() {
        return order_note;
    }

    public void setorder_note(String order_note) {
        this.order_note = order_note;
    }

    public int getorder_vendor_id() {
        return order_vendor_id;
    }

    public void setorder_vendor_id(int order_vendor_id) {
        this.order_vendor_id = order_vendor_id;
    }

    public int getorder_id() {
        return order_id;
    }

    public void setorder_id(int order_id) {
        this.order_id = order_id;
    }
}
