package com.example.warkopcikditiro.model;

public class User {
    private int id;
    private int user_type_id;
    private String user_type;
    private String phone_number;
    private String password;
    private String name;
    private String address;

    public User() {
    }

    public User(int id, int user_type_id, String phone_number, String password, String name, String address, String user_type) {
        this.id = id;
        this.user_type_id = user_type_id;
        this.phone_number = phone_number;
        this.password = password;
        this.name = name;
        this.address = address;
        this.user_type = user_type;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_type_id() {
        return user_type_id;
    }

    public void setUser_type_id(int user_type_id) {
        this.user_type_id = user_type_id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
