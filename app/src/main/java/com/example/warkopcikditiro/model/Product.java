package com.example.warkopcikditiro.model;

public class Product {
    private int id;
    private String name;
    private int price;
    private int discount;
    private int totalprice;
    private int totalstock;
    private int minimumstock;
    private String category;
    private int total;
    private int status_id;
    private String status;
    private int orderlist_id;
    private String unit;

    public Product() {
    }

    public Product(int id, String name, int price, int discount, int totalprice, int totalstock, String category, int total, int status_id, String status, int orderlist_id, int minimumstock, String unit) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = discount;
        this.totalprice = totalprice;
        this.totalstock = totalstock;
        this.category = category;
        this.total = total;
        this.status_id = status_id;
        this.status = status;
        this.orderlist_id = orderlist_id;
        this.minimumstock = minimumstock;
        this.unit = unit;
    }

    public int getMinimumstock() {
        return minimumstock;
    }

    public void setMinimumstock(int minimumstock) {
        this.minimumstock = minimumstock;
    }

    public int getorderlist_id() {
        return orderlist_id;
    }

    public void setorderlist_id(int order_id) {
        this.orderlist_id = order_id;
    }

    public int getstatus_id() {
        return status_id;
    }

    public void setstatus_id(int status_id) {
        this.status_id = status_id;
    }

    public String getstatus() {
        return status;
    }

    public void setstatus(String status) {
        this.status = status;
    }

    public int gettotal() {
        return total;
    }

    public void settotal(int total) {
        this.total = total;
    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getname() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setname(String name) {
        this.name = name;
    }

    public int getprice() {
        return price;
    }

    public void setprice(int price) {
        this.price = price;
    }

    public int getdiscount() {
        return discount;
    }

    public void setdiscount(int discount) {
        this.discount = discount;
    }

    public int gettotalprice() {
        return totalprice;
    }

    public void settotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public int gettotalstock() {
        return totalstock;
    }

    public void settotalstock(int totalstock) {
        this.totalstock = totalstock;
    }

    public String getcategory() {
        return category;
    }

    public void setcategory(String category) {
        this.category = category;
    }

    public void increasetotal(){
        total = total + 1;
    }

    public void decreasetotal() {
        total = total - 1;
    }

    public void minustotal(int decreaser) {
        total = total - decreaser;
    }
}
