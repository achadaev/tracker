package com.example.tracker.shared.model;

public class Expense {
    private int id;
    private int type_id;
    private String name;
    private String date;
    private int price;

    public Expense(int id, int type_id, String name, String date, int price) {
        this.id = id;
        this.type_id = type_id;
        this.name = name;
        this.date = date;
        this.price = price;
    }

    public Expense() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}