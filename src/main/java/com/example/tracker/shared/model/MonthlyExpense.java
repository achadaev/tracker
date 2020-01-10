package com.example.tracker.shared.model;

import java.util.List;

public class MonthlyExpense {
    List<Double> expenses;

    public MonthlyExpense(List<Double> expenses) {
        this.expenses = expenses;
    }

    public MonthlyExpense() {
    }

    public List<Double> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Double> expenses) {
        this.expenses = expenses;
    }

    @Override
    public String toString() {
        return "MonthlyExpense{" +
                "expenses=" + expenses +
                '}';
    }
}
