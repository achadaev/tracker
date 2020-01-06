package com.example.tracker.server.service;

import com.example.tracker.server.dao.IExpenseDAO;
import com.example.tracker.server.dao.IExpenseTypeDAO;
import com.example.tracker.server.dao.IUserDAO;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
import com.example.tracker.shared.model.ReviewInfo;
import com.example.tracker.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Component
public class ExpenseService {
    @Autowired
    private IUserDAO iUserDao;

    @Autowired
    private IExpenseDAO iExpenseDao;

    @Autowired
    private IExpenseTypeDAO iExpenseTypeDAO;

    public User getCurrentUser() {
        String login = UtilsService.getCurrentUsername();
        return iUserDao.getUserByName(login);
    }

    private boolean isAdmin() {
        return "admin".equals(getCurrentUser().getRole());
    }

    public List<Expense> getUsersExpenses() {
        return iExpenseDao.getUsersExpenses(getCurrentUser().getId());
    }

    public Expense getExpenseById(int id) {
        if (isAdmin()) {
            List<Expense> expenseList = iExpenseDao.getAllExpenses();
            for (Expense expense : expenseList) {
                if (expense.getId() == id) {
                    return expense;
                }
            }
            throw new NoSuchElementException("No such Expense");
        } else {
            return iExpenseDao.getExpenseById(getCurrentUser().getId(), id);
        }
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
        List<Expense> expenseList;
        if (isAdmin()) {
            expenseList = iExpenseDao.getAllExpenses();
        } else {
            expenseList = iExpenseDao.getUsersExpenses(getCurrentUser().getId());
        }
        for (Expense temp : expenseList) {
            if (expense.getId() == temp.getId()) {
                return iExpenseDao.updateExpense(expense);
            }
        }
        return addExpense(expense);
    }

    public List<Expense> deleteExpenses(List<Integer> ids) {
        if (isAdmin()) {
            iExpenseDao.deleteExpenses(ids, getCurrentUser().getId());
            return iExpenseDao.getAllExpenses();
        } else {
            return iExpenseDao.deleteExpenses(ids, getCurrentUser().getId());
        }
    }

    public User getUserById(int id) throws AccessDeniedException {
        if (isAdmin() || id == getCurrentUser().getId()) {
            return iUserDao.getUserById(id);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    public Boolean updateUser(User user) throws AccessDeniedException {
        if (isAdmin()) {
            for (User temp : getAllUsers()) {
                if (temp.getId() == user.getId()) {
                    return iUserDao.updateUser(user);
                }
            }
            return iUserDao.addUser(user);
        } else if (user.getId() == getCurrentUser().getId()) {
            return iUserDao.updateUser(user);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    public List<User> getAllUsers() throws AccessDeniedException {
        if (isAdmin()) {
            return iUserDao.getAllUsers();
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    public List<User> deleteUsers(List<Integer> ids) throws AccessDeniedException {
        if (isAdmin()) {
            return iUserDao.deleteUsers(ids);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    public Boolean updateType(ExpenseType type) throws AccessDeniedException {
        if (isAdmin()) {
            for (ExpenseType temp : iExpenseTypeDAO.getTypes()) {
                if (temp.getId() == type.getId()) {
                    return iExpenseTypeDAO.updateType(type);
                }
            }
            return iExpenseTypeDAO.addType(type);
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

}
