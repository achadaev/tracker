package com.example.tracker.shared.model;

public class Expense {
    private int id;
    private int typeId;
    private String name;
    private String date;
    private int price;

    public Expense(int id, int typeId, String name, String date, int price) {
        this.id = id;
        this.typeId = typeId;
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

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
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