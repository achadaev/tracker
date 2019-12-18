package com.example.tracker.server.dao;

import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
import com.google.gwt.user.client.ui.ListBox;

import java.util.List;
import java.util.Map;

public interface IExpenseDao {

    List<Expense> getAllExpenses();

    List<Expense> getUsersExpenses(int id);

    List<ExpenseType> getTypes();

    Expense getExpenseById(int id);

    Boolean addExpense(Expense expense, int userId);

    Boolean updateExpense(Expense expense);

    List<Expense> deleteExpenses(List<Integer> ids, int userId);
}
