package com.example.warkopcikditiro.model;

public class Table {
    private int id;
    private int number;
    private int extend;
    private String status;
    private int order_id;
    private String order_note;

    public Table() {
    }

    public Table(int id, int tablenumber, int extend, String status, int order_id, String order_note) {
        this.id = id;
        this.number = number;
        this.extend = extend;
        this.status = status;
        this.order_id = order_id;
        this.order_note = order_note;
    }

    public String getorder_note() {
        return order_note;
    }

    public void setorder_note(String order_note) {
        this.order_note = order_note;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public int getnumber() {
        return number;
    }

    public void setnumber(int number) {
        this.number = number;
    }

    public int getextend() {
        return extend;
    }

    public void setextend(int extend) {
        this.extend = extend;
    }

    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        this.status = status;
    }

    public int getorder_id() {
        return order_id;
    }

    public void setorder_id(int order_id) {
        this.order_id = order_id;
    }
}
