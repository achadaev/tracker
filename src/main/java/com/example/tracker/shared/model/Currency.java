package com.example.tracker.shared.model;

import java.util.Map;

public class Currency {
    Map<String, Double> rates;
    String base;
    String date;

    public Currency() {
    }

    public Currency(Map<String, Double> rates, String base, String date) {
        this.rates = rates;
        this.base = base;
        this.date = date;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "rates=" + rates +
                ", base='" + base + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}