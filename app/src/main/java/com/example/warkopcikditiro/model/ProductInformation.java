package com.example.warkopcikditiro.model;

public class ProductInformation {
    private int product_id;
    private int total;
    private int order_id;
    private int table_id;
    private int table_number;
    private int table_extend;
    private String information;
    private int orderlist_id;

    public ProductInformation() {
    }

    public ProductInformation(int product_id, int total, int order_id, int table_id, String information, int table_number, int table_extend, int orderlist_id) {
        this.product_id = product_id;
        this.total = total;
        this.order_id = order_id;
        this.table_id = table_id;
        this.information = information;
        this.table_number = table_number;
        this.table_extend = table_extend;
        this.orderlist_id = orderlist_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getOrderlist_id() {
        return orderlist_id;
    }

    public void setOrderlist_id(int orderlist_id) {
        this.orderlist_id = orderlist_id;
    }

    public int getTable_number() {
        return table_number;
    }

    public void setTable_number(int table_number) {
        this.table_number = table_number;
    }

    public int getTable_extend() {
        return table_extend;
    }

    public void setTable_extend(int table_extend) {
        this.table_extend = table_extend;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
