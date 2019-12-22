package com.example.tracker.shared.model;

import java.util.Date;

public class Expense {
    private int id;
    private int typeId;
    private String name;
    private Date date;
    private double price;

    public Expense(int id, int typeId, String name, Date date, double price) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}