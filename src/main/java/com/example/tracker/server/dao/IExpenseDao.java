package com.example.tracker.server.dao;

import com.example.tracker.shared.model.Expense;

import java.sql.Connection;
import java.util.List;

public interface IExpenseDao {
    Connection connect();

    List<Expense> getAllExpenses(Connection conn);

    List<Expense> getExpensesByUser(Connection conn, String login);

    List<Expense> getExpensesByType(Connection conn, String login, int typeID);

    List<Expense> getExpensesByDate(Connection conn, String login, String date);

    List<Expense> getExpensesByLowerInterval(Connection conn, String login, String startDate);

    List<Expense> getExpensesByUpperInterval(Connection conn, String login, String endDate);

    List<Expense> getExpensesByDateInterval(Connection conn, String login, String startDate, String endDate);
}
