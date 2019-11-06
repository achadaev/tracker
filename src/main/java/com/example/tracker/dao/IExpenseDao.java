package com.example.tracker.dao;

import java.sql.Connection;
import java.util.List;

public interface IExpenseDao {
    public Connection connect();

    public List<Expense> getAllExpenses(Connection conn);

    public List<Expense> getExpensesByUser(Connection conn, String login);

    public List<Expense> getExpensesByType(Connection conn, String login, int typeID);

    public List<Expense> getExpensesByDate(Connection conn, String login, String date);

    public List<Expense> getExpensesByLowerInterval(Connection conn, String login, String startDate);

    public List<Expense> getExpensesByUpperInterval(Connection conn, String login, String endDate);

    public List<Expense> getExpensesByDateInterval(Connection conn, String login, String startDate, String endDate);
}
