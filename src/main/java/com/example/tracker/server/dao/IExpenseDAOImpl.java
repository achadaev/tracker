package com.example.tracker.server.dao;

import com.example.tracker.server.dao.mapper.ExpenseMapper;
import com.example.tracker.server.dao.mapper.ExpenseTypeMapper;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
import com.example.tracker.shared.model.ReviewInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class IExpenseDAOImpl implements IExpenseDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    final static Logger logger = LoggerFactory.getLogger(IExpenseDAOImpl.class);

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public IExpenseDAOImpl() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
    }

    @Override
    public List<Expense> getAllExpenses() {
        return jdbcTemplate.query("SELECT expense.id, expense.type_id, expense.name, expense.date, expense.price, expense.is_archived " +
                "FROM expense", new ExpenseMapper());
    }

    @Override
    public List<Expense> getUsersExpenses(int userId) {
        String query = "SELECT expense.id, expense.type_id, expense.name, expense.date, expense.price, expense.is_archived " +
                "FROM expense JOIN user_expense ON expense.id = user_expense.expense_id " +
                "WHERE user_expense.user_id = ? AND expense.is_archived = 0";
        return jdbcTemplate.query(query, preparedStatement -> preparedStatement
                .setInt(1, userId), new ExpenseMapper());
    }

    private double getTotal(List<Expense> expenseList) {
        double total = 0.0;
        for (Expense expense : expenseList) {
            total += expense.getPrice();
        }

        return total;
    }

    @Override
    public ReviewInfo getReview(int userId) {
        ReviewInfo reviewInfo = new ReviewInfo();

        List<Expense> tempList = getUsersExpenses(userId);
        reviewInfo.setAmount(getTotal(tempList));

        Calendar monthBefore = Calendar.getInstance();
        monthBefore.add(Calendar.MONTH, -1);
        tempList = getExpensesByDate(userId, monthBefore.getTime(), new Date());
        reviewInfo.setMonth(getTotal(tempList));

        Calendar weekBefore = Calendar.getInstance();
        weekBefore.add(Calendar.WEEK_OF_YEAR, -1);
        tempList = getExpensesByDate(userId, weekBefore.getTime(), new Date());
        reviewInfo.setWeek(getTotal(tempList));

        return reviewInfo;
    }

    @Override
    public Expense getExpenseById(int userId, int id) {
        String query = "SELECT expense.id, expense.type_id, expense.name, expense.date, expense.price, expense.is_archived " +
                "FROM expense JOIN user_expense ON expense.id = user_expense.expense_id " +
                "WHERE expense.id = ? AND user_id = ?";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, userId);
        }, new ExpenseMapper()).get(0);
    }

    @Override
    public List<Expense> getExpensesByTypeId(int userId, int typeId) {
        String query = "SELECT expense.id, expense.type_id, expense.name, expense.date, expense.price, expense.is_archived " +
                "FROM expense JOIN user_expense ON expense.id = user_expense.expense_id " +
                "WHERE user_expense.user_id = ? AND type_id = ?";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, typeId);
        }, new ExpenseMapper());
    }

    @Override
    public List<Expense> getExpensesByDate(int userId, Date startDate, Date endDate) {
        String query = "SELECT expense.id, expense.type_id, expense.name, expense.date, expense.price, expense.is_archived " +
                "FROM expense JOIN user_expense ON expense.id = user_expense.expense_id " +
                "WHERE user_expense.user_id = ? AND (expense.date BETWEEN ? AND ?)";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, dateFormat.format(startDate));
            preparedStatement.setString(3, dateFormat.format(endDate));
        }, new ExpenseMapper());
    }

    @Override
    public List<Expense> getExpensesByDateAndTypeId(int userId, int typeId, Date startDate, Date endDate) {
        String query = "SELECT expense.id, expense.type_id, expense.name, expense.date, expense.price, expense.is_archived " +
                "FROM expense JOIN user_expense ON expense.id = user_expense.expense_id " +
                "WHERE user_expense.user_id = ? AND type_id = ? AND (expense.date BETWEEN ? AND ?)";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, typeId);
            preparedStatement.setString(3, dateFormat.format(startDate));
            preparedStatement.setString(4, dateFormat.format(endDate));
        }, new ExpenseMapper());
    }

    @Override
    public Boolean addExpense(Expense expense, int userId) {
        String query = "INSERT INTO expense (type_id, name, date, price) VALUES " +
                "(?, ?, ?, ?)";
        jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {
            preparedStatement.setInt(1, expense.getTypeId());
            preparedStatement.setString(2, expense.getName());
            preparedStatement.setString(3, dateFormat.format(expense.getDate()));
            preparedStatement.setDouble(4, expense.getPrice());
            return preparedStatement.execute();
        });
        return addUserExpense(getLastExpenseId(), userId);
    }

    private int getLastExpenseId() {
        String query = "SELECT expense.id, expense.type_id, expense.name, expense.date, expense.price, expense.is_archived " +
                "FROM expense " +
                "WHERE id = (SELECT MAX(id) FROM expense)";
        return jdbcTemplate.query(query, new ExpenseMapper()).get(0).getId();
    }

    private Boolean addUserExpense(int expenseId, int userId) {
        String query = "INSERT INTO user_expense (expense_id, user_id) VALUES " +
                "(?, ?)";
        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {
            preparedStatement.setInt(1, expenseId);
            preparedStatement.setInt(2, userId);
            return preparedStatement.execute();
        });
    }

    @Override
    public Boolean updateExpense(Expense expense) {
        String query = "UPDATE expense " +
                "SET type_id = ?, " +
                "name = ?, " +
                "date = ?, " +
                "price = ?, " +
                "is_archived = ? " +
                "WHERE id = ?";
        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {
            preparedStatement.setInt(1, expense.getTypeId());
            preparedStatement.setString(2, expense.getName());
            preparedStatement.setString(3, dateFormat.format(expense.getDate()));
            preparedStatement.setDouble(4, expense.getPrice());
            preparedStatement.setInt(5, expense.getIsArchived());
            preparedStatement.setInt(6, expense.getId());
            return preparedStatement.execute();
        });
    }

    private Boolean archiveExpense(int id) {
        String query = "UPDATE expense " +
                "SET is_archived = 1 " +
                "WHERE id = ?";
        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {
            preparedStatement.setInt(1, id);
            return preparedStatement.execute();
        });
    }

    @Override
    public List<Expense> archiveExpenses(List<Integer> ids, int userId) {
        for (int i = 0; i < ids.size(); i++) {
            archiveExpense(ids.get(i));
        }
        return getUsersExpenses(userId);
    }

    private Boolean deleteExpense(int id) {
        String query = "DELETE FROM expense " +
                "WHERE id = ?";
        jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {
            preparedStatement.setInt(1, id);
            return preparedStatement.execute();
        });
        return deleteUserExpense(id);
    }

    private Boolean deleteUserExpense(int id) {
        String query = "DELETE FROM user_expense " +
                "WHERE expense_id = ?";
        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {
            preparedStatement.setInt(1, id);
            return preparedStatement.execute();
        });
    }

    @Override
    public List<Expense> deleteExpenses(List<Integer> ids, int userId) {
        for (int i = 0; i < ids.size(); i++) {
            deleteExpense(ids.get(i));
        }
        return getUsersExpenses(userId);
    }

}
