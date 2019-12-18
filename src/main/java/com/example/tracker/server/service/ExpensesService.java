package com.example.tracker.server.service;

import com.example.tracker.server.dao.IExpenseDao;
import com.example.tracker.server.dao.IUserDao;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

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

    public Expense getExpenseById(int id) {
        List<Expense> userExpenses = getUsersExpenses();
        for (Expense expense : userExpenses) {
            if (expense.getId() == id) {
                return expense;
            }
        }
        throw new NoSuchElementException("No such expense");
    }

    public Boolean addExpense(Expense expense) {
        return iExpenseDao.addExpense(expense, getCurrentUser().getId());
    }

    public Boolean updateExpense(Expense expense) {
        List<Expense> userExpenses = getUsersExpenses();
        for (Expense temp : userExpenses) {
            if (expense.getId() == temp.getId()) {
                return iExpenseDao.updateExpense(expense);
            }
        }
        return addExpense(expense);
    }

    public List<Expense> deleteExpenses(List<Integer> ids) {
        return iExpenseDao.deleteExpenses(ids, getCurrentUser().getId());
    }
}
