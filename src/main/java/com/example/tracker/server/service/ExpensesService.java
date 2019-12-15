package com.example.tracker.server.service;

import com.example.tracker.server.dao.IExpenseDao;
import com.example.tracker.server.dao.IUserDao;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExpensesService {
    @Autowired
    private IUserDao iUserDao;

    @Autowired
    private IExpenseDao iExpenseDao;

    public User getCurrentUser() {
        String login = UtilsService.getCurrentUsername();
        return iUserDao.getUserByName(login);
    }

    public List<Expense> getUsersExpenses() {
        return iExpenseDao.getUsersExpenses(getCurrentUser().getId());
    }

    public Boolean addExpense(Expense expense) {
        return iExpenseDao.addExpense(expense, getCurrentUser().getId());
    }

    public List<Expense> deleteExpenses(List<Integer> ids) {
        return iExpenseDao.deleteExpenses(ids, getCurrentUser().getId());
    }
}
