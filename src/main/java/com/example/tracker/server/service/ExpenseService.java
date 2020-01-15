package com.example.tracker.server.service;

import com.example.tracker.server.dao.IExpenseDAO;
import com.example.tracker.server.dao.IExpenseTypeDAO;
import com.example.tracker.server.dao.IUserDAO;
import com.example.tracker.shared.model.*;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;
import java.util.*;

@Component
public class ExpenseService {
    @Autowired
    private IUserDAO iUserDao;

    @Autowired
    private IExpenseDAO iExpenseDao;

    @Autowired
    private IExpenseTypeDAO iExpenseTypeDAO;

    private final static Logger LOG = LoggerFactory.getLogger(ExpenseService.class);

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

    public ReviewInfo getReview() throws AccessDeniedException {
        if (isAdmin()) {
            ReviewInfo result = new ReviewInfo();
            for (User user : getAllUsers()) {
                ReviewInfo temp = iExpenseDao.getReview(user.getId());
                result.setWeek(result.getWeek() + temp.getWeek());
                result.setMonth(result.getMonth() + temp.getMonth());
                result.setAmount(result.getAmount() + temp.getAmount());
            }
            return result;
        } else {
            return iExpenseDao.getReview(getCurrentUser().getId());
        }
    }

    public List<Expense> getExpensesByTypeId(int id) throws AccessDeniedException {
        if (isAdmin()) {
            List<Expense> result = new ArrayList<>();
            if (id == 0) {
                return iExpenseDao.getAllExpenses();
            } else {
                for (User user : getAllUsers()) {
                    result.addAll(iExpenseDao.getExpensesByTypeId(user.getId(), id));
                }
                return result;
            }
        } else {
            if (id == 0) {
                return getUsersExpenses();
            } else {
                return iExpenseDao.getExpensesByTypeId(getCurrentUser().getId(), id);
            }
        }
    }

    public List<Expense> getExpensesByDate(int typeId, Date startDate, Date endDate) throws AccessDeniedException {
        if (isAdmin()) {
            List<Expense> result = new ArrayList<>();
            if (typeId == 0) {
                for (User user : getAllUsers()) {
                    result.addAll(iExpenseDao.getExpensesByDate(user.getId(), startDate, endDate));
                }
                return result;
            } else {
                for (User user : getAllUsers()) {
                    result.addAll(iExpenseDao.getExpensesByDateAndTypeId(user.getId(), typeId, startDate, endDate));
                }
                return result;
            }
        } else {
            if (typeId == 0) {
                return iExpenseDao.getExpensesByDate(getCurrentUser().getId(), startDate, endDate);
            } else {
                return iExpenseDao.getExpensesByDateAndTypeId(getCurrentUser().getId(), typeId, startDate, endDate);
            }
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

    public List<Expense> archiveExpenses(List<Integer> ids) {
        if (isAdmin()) {
            iExpenseDao.archiveExpenses(ids, getCurrentUser().getId());
            return iExpenseDao.getAllExpenses();
        } else {
            return iExpenseDao.archiveExpenses(ids, getCurrentUser().getId());
        }
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

    public List<SimpleDate> getDatesBetween() {
        List<Expense> expenseList;
        if (isAdmin()) {
            expenseList = iExpenseDao.getAllExpenses();
        } else {
            expenseList = getUsersExpenses();
        }
        if (!expenseList.isEmpty()) {
            Collections.sort(expenseList);
            DateTime first = new DateTime(expenseList.get(0).getDate()).dayOfMonth().withMinimumValue();
            DateTime last = new DateTime(expenseList.get(expenseList.size() - 1).getDate());
            List<SimpleDate> datesBetween = new ArrayList<>();

            while (first.compareTo(last) <= 0) {
                datesBetween.add(new SimpleDate(first.toDate()));
                first = first.plusMonths(1).dayOfMonth().withMinimumValue();
            }

            return datesBetween;
        }
        return new ArrayList<>();
    }

    public List<MonthlyExpense> getExpensesBetween() {
        Calendar calendar = Calendar.getInstance();
        List<SimpleDate> dates = getDatesBetween();
        List<MonthlyExpense> expensesBetween = new ArrayList<>();

        if (dates.size() > 1) {
            DateTime first = new DateTime(dates.get(0).getDate());
            DateTime last = new DateTime(dates.get(dates.size() - 1).getDate());
            calendar.setTime(first.toDate());

            Date firstDay;
            Date lastDay;

            while (calendar.getTime().compareTo(last.toDate()) <= 0) {
                calendar.set(Calendar.DATE, 1);
                firstDay = calendar.getTime();
                calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                lastDay = calendar.getTime();
                List<Double> monthlyExpenses = new ArrayList<>();

                for (ExpenseType type : iExpenseTypeDAO.getTypes()) {
                    try {
                        monthlyExpenses.add(getMonthlyExpenses(type.getId(), getExpensesByDate(type.getId(), firstDay, lastDay)));
                    } catch (AccessDeniedException e) {
                        e.printStackTrace();
                    }
                }

                expensesBetween.add(new MonthlyExpense(monthlyExpenses));
                calendar.add(Calendar.MONTH, 1);
                calendar.set(Calendar.DATE, 1);
            }

            return expensesBetween;

        } else if (dates.size() == 1) {
            calendar.setTime(new DateTime(dates.get(0).getDate()).toDate());
            List<Double> monthlyExpenses = new ArrayList<>();

            for (ExpenseType type : iExpenseTypeDAO.getTypes()) {
                monthlyExpenses.add(getMonthlyExpenses(type.getId(), getUsersExpenses()));
            }

            expensesBetween.add(new MonthlyExpense(monthlyExpenses));

            return expensesBetween;

        } else {
            return new ArrayList<>();
        }
    }

    private Double getMonthlyExpenses(int typeId, List<Expense> expenseList) {
        double monthly = 0.0;
        for (Expense expense : expenseList) {
            if (expense.getTypeId() == typeId) {
                monthly += expense.getPrice();
            }
        }
        return monthly;
    }
}
