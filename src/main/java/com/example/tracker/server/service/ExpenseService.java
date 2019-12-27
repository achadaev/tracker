package com.example.tracker.server.service;

import com.example.tracker.server.dao.IExpenseDAO;
import com.example.tracker.server.dao.IUserDAO;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ReviewInfo;
import com.example.tracker.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class ExpenseService {
    @Autowired
    private IUserDAO iUserDao;

    @Autowired
    private IExpenseDAO iExpenseDao;

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

    public ReviewInfo getReview() {
        return iExpenseDao.getReview(getCurrentUser().getId());
    }

    public List<Expense> getExpensesByTypeId(int id) {
        if (id == 0) {
            return getUsersExpenses();
        } else {
            return iExpenseDao.getExpensesByTypeId(getCurrentUser().getId(), id);
        }
    }

    public List<Expense> getExpensesByDate(int typeId, Date startDate, Date endDate) {
        if (typeId == 0) {
            return iExpenseDao.getExpensesByDate(getCurrentUser().getId(), startDate, endDate);
        } else {
            return iExpenseDao.getExpensesByDateAndTypeId(getCurrentUser().getId(), typeId, startDate, endDate);
        }
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
