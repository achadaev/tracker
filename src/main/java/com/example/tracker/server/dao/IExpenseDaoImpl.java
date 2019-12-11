package com.example.tracker.server.dao;

import com.example.tracker.server.dao.mapper.ExpenseMapper;
import com.example.tracker.shared.model.Expense;
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

/*
    private String dbFile = "C:\\Projects\\tracker\\src\\main\\resources\\tracker.db";

    private String url = "jdbc:sqlite:" + dbFile;
*/

    public IExpenseDaoImpl() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
    }

    @Override
    public List<Expense> getAllExpenses() {
        return jdbcTemplate.query("SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                "FROM expenses", new ExpenseMapper());
    }

    @Override
    public List<Expense> getExpensesByUser(String login) {
        String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                "WHERE user_expenses.user_login = ?";
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, login);
            }
        }, new ExpenseMapper());
    }

    @Override
    public Expense getExpenseById(int id) {
        String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                "FROM expenses " +
                "WHERE expenses.id = ?";
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, id);
            }
        }, new ExpenseMapper()).get(0);
    }

    @Override
    public List<Expense> getExpensesByType(String login, int typeID) {
        String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                "WHERE user_expenses.user_login = ? AND expenses.type_id = ?";
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, login);
                preparedStatement.setInt(2, typeID);
            }
        }, new ExpenseMapper());
    }

    @Override
    public List<Expense> getExpensesByDate(String login, String date) {
        String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                "WHERE user_expenses.user_login = ? AND expenses.date = ?";
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, date);
            }
        }, new ExpenseMapper());
    }

    @Override
    public List<Expense> getExpensesByLowerInterval(String login, String startDate) {
        String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                "WHERE user_expenses.user_login = ? AND expenses.date >= ?";
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, startDate);
            }
        }, new ExpenseMapper());
    }

    @Override
    public List<Expense> getExpensesByUpperInterval(String login, String endDate) {
        String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                "WHERE user_expenses.user_login = ? AND expenses.date <= ?";
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, endDate);
            }
        }, new ExpenseMapper());
    }

    @Override
    public List<Expense> getExpensesByDateInterval(String login, String startDate, String endDate) {
        String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                "WHERE user_expenses.user_login = ? AND (expenses.date BETWEEN ? AND ?)";
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, login);
                preparedStatement.setString(2, startDate);
                preparedStatement.setString(3, endDate);
            }
        }, new ExpenseMapper());
    }

    @Override
    public Boolean addExpense(Expense expense) {
        String query = "INSERT INTO expenses VALUES " +
                "(?, ?, ?, ?, ?)";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setInt(1, expense.getId());
                preparedStatement.setInt(2, expense.getTypeId());
                preparedStatement.setString(3, expense.getName());
                preparedStatement.setString(4, expense.getDate());
                preparedStatement.setInt(5, expense.getPrice());
                return preparedStatement.execute();
            }
        });
        //TODO get user.login and INSERT INTO user_expenses
    }

    @Override
    public Boolean updateExpense(Expense expense) {
        String query = "UPDATE expenses " +
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
        String query = "DELETE FROM expenses " +
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
