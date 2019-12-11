package com.example.tracker.server.dao;

import com.example.tracker.shared.model.Expense;

import java.util.List;

public interface IExpenseDao {

    List<Expense> getAllExpenses();

    List<Expense> getExpensesByUser(String login);

    Expense getExpenseById(int id);

    List<Expense> getExpensesByType(String login, int typeID);

    List<Expense> getExpensesByDate(String login, String date);

    List<Expense> getExpensesByLowerInterval(String login, String startDate);

    List<Expense> getExpensesByUpperInterval(String login, String endDate);

    List<Expense> getExpensesByDateInterval(String login, String startDate, String endDate);

    Boolean addExpense(Expense expense);

    Boolean updateExpense(Expense expense);

    List<Expense> deleteExpenses(List<Integer> ids);
}
