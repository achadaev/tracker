package com.example.tracker.server.dao;

import com.example.tracker.server.dao.mapper.ExpenseMapper;
import com.example.tracker.server.service.ExpensesService;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;

@Component
public class IExpenseDaoImpl implements IExpenseDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    final static Logger logger = LoggerFactory.getLogger(IExpenseDaoImpl.class);

    public IExpenseDaoImpl() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
    }

    @Override
    public List<Expense> getAllExpenses() {
        return jdbcTemplate.query("SELECT expense.id, expense.type_id, expense.name, expense.date, expense.price " +
                "FROM expense", new ExpenseMapper());
    }

    @Override
    public List<Expense> getUsersExpenses(int id) {
        String query = "SELECT expense.id, expense.type_id, expense.name, expense.date, expense.price " +
                "FROM expense JOIN user_expense ON expense.id = user_expense.expense_id " +
                "WHERE user_expense.user_id = ?";
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, id);
            }
        }, new ExpenseMapper());
    }

    @Override
    public Expense getExpenseById(int id) {
        String query = "SELECT expense.id, expense.type_id, expense.name, expense.date, expense.price " +
                "FROM expense " +
                "WHERE expense.id = ?";
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, id);
            }
        }, new ExpenseMapper()).get(0);
    }

    @Override
    public List<Expense> getExpensesByDate(int userId, String date) {
        String query = "SELECT expense.id, expense.type_id, expense.name, expense.date, expense.price " +
                "FROM expense JOIN user_expense ON expense.id = user_expense.expense_id " +
                "WHERE user_expense.user_id = ? AND expense.date = ?";
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, date);
            }
        }, new ExpenseMapper());
    }

    @Override
    public List<Expense> getExpensesByLowerInterval(int userId, String startDate) {
        String query = "SELECT expense.id, expense.type_id, expense.name, expense.date, expense.price " +
                "FROM expense JOIN user_expense ON expense.id = user_expense.expense_id " +
                "WHERE user_expense.user_id = ? AND expense.date >= ?";
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, startDate);
            }
        }, new ExpenseMapper());
    }

    @Override
    public List<Expense> getExpensesByUpperInterval(int userId, String endDate) {
        String query = "SELECT expense.id, expense.type_id, expense.name, expense.date, expense.price " +
                "FROM expense JOIN user_expense ON expense.id = user_expense.expense_id " +
                "WHERE user_expense.user_id = ? AND expense.date <= ?";
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, endDate);
            }
        }, new ExpenseMapper());
    }

    @Override
    public List<Expense> getExpensesByDateInterval(int userId, String startDate, String endDate) {
        String query = "SELECT expense.id, expense.type_id, expense.name, expense.date, expense.price " +
                "FROM expense JOIN user_expense ON expense.id = user_expense.expense_id " +
                "WHERE user_expense.user_id = ? AND (expense.date BETWEEN ? AND ?)";
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, startDate);
                preparedStatement.setString(3, endDate);
            }
        }, new ExpenseMapper());
    }

    @Override
    public Boolean addExpense(Expense expense, int userId) {
        String query = "INSERT INTO expense (type_id, name, date, price) VALUES " +
                "(?, ?, ?, ?)";
        jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setInt(1, expense.getTypeId());
                preparedStatement.setString(2, expense.getName());
                preparedStatement.setString(3, expense.getDate());
                preparedStatement.setInt(4, expense.getPrice());
                return preparedStatement.execute();
            }
        });
        return addUserExpense(expense.getId(), userId);
        //TODO get user.login and INSERT INTO user_expenses
    }

    private Boolean addUserExpense(int expenseId, int userId) {
        String query = "INSERT INTO user_expense (expense_id, user_id) VALUES " +
                "(?, ?)";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setInt(1, expenseId);
                preparedStatement.setInt(2, userId);
                return preparedStatement.execute();
            }
        });
    }

    @Override
    public Boolean updateExpense(Expense expense) {
        String query = "UPDATE expense " +
                "SET type_id = ?, " +
                "name = ?, " +
                "date = ?, " +
                "price = ? " +
                "WHERE id = ?";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setInt(1, expense.getTypeId());
                preparedStatement.setString(2, expense.getName());
                preparedStatement.setString(3, expense.getDate());
                preparedStatement.setInt(4, expense.getPrice());
                preparedStatement.setInt(5, expense.getId());
                return preparedStatement.execute();
            }
        });
    }

    private Boolean deleteExpense(int id) {
        String query = "DELETE FROM expense " +
                "WHERE id = ?";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setInt(1, id);
                return preparedStatement.execute();
            }
        });
        //TODO delete data from user_expenses
    }

    @Override
    public List<Expense> deleteExpenses(List<Integer> ids) {
        for (int i = 0; i < ids.size(); i++) {
            deleteExpense(ids.get(i));
        }

        return getAllExpenses();
    }
}
