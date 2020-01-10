package com.example.tracker.shared.model;

import java.util.Date;

public class SimpleDate {
    Date date;

    public SimpleDate(Date date) {
        this.date = date;
    }

    public SimpleDate() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "SimpleDate{" +
                "date=" + date +
                '}';
    }
}
