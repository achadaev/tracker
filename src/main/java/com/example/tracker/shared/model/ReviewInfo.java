package com.example.tracker.shared.model;

public class ReviewInfo {
    double monthChange;
    double amount;
    double month;
    double week;

    public ReviewInfo() {
    }

    public ReviewInfo(double monthChange, double amount, double month, double week) {
        this.monthChange = monthChange;
        this.amount = amount;
        this.month = month;
        this.week = week;
    }

    public double getMonthChange() {
        return monthChange;
    }

    public void setMonthChange(double monthChange) {
        this.monthChange = monthChange;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getMonth() {
        return month;
    }

    public void setMonth(double month) {
        this.month = month;
    }

    public double getWeek() {
        return week;
    }

    public void setWeek(double week) {
        this.week = week;
    }
}
