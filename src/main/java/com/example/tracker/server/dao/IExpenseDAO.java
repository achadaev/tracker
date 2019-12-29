package com.example.tracker.server.dao;

import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
import com.example.tracker.shared.model.ReviewInfo;

import java.util.Date;
import java.util.List;

public interface IExpenseDAO {

    List<Expense> getAllExpenses();

    List<Expense> getUsersExpenses(int id);

    ReviewInfo getReview(int id);

    List<ExpenseType> getTypes();

    Expense getExpenseById(int userId, int id);

    List<Expense> getExpensesByTypeId(int userId, int typeId);

    List<Expense> getExpensesByDate(int userId, Date startDate, Date endDate);

    List<Expense> getExpensesByDateAndTypeId(int userId, int typeId, Date startDate, Date endDate);

    Boolean addExpense(Expense expense, int userId);

    Boolean updateExpense(Expense expense);

    List<Expense> deleteExpenses(List<Integer> ids, int userId);
}
