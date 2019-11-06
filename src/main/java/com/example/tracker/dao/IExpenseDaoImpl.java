package com.example.tracker.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import java.text.ParseException;
import java.util.Date;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class IExpenseDaoImpl implements IExpenseDao {

    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    final static Logger logger = LoggerFactory.getLogger(IExpenseDaoImpl.class);

    @Override
    public Connection connect() {
        String dbFile = "C:\\Projects\\tracker\\src\\main\\resources\\tracker.db";
        String url = "jdbc:sqlite:" + dbFile;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            logger.info("Connected to database");
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }
        return conn;
    }

    private List<Expense> getResult(ResultSet rs) {
        List<Expense> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Expense exp = new Expense();
                exp.setId(rs.getInt("id"));
                exp.setType_id(rs.getInt("type_id"));
                exp.setName(rs.getString("name"));
                exp.setDate(rs.getString("date"));
                exp.setPrice(rs.getInt("price"));
                result.add(exp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<Expense> getAllExpenses(Connection conn) {
        try {
            Statement st = conn.createStatement();
            st.execute("SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                    "FROM expenses");
            ResultSet rs = st.getResultSet();
            List<Expense> result = getResult(rs);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Expense> getExpensesByUser(Connection conn, String login) {
        try {
            String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                    "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                    "WHERE user_expenses.user_login = ?";
            PreparedStatement prepSt = conn.prepareStatement(query);
            prepSt.setString(1, login);
            ResultSet rs = prepSt.executeQuery();
            List<Expense> result = getResult(rs);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Expense> getExpensesByType(Connection conn, String login, int typeID) {
        try {
            String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                    "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                    "WHERE user_expenses.user_login = ? AND expenses.type_id = ?";

            PreparedStatement prepSt = conn.prepareStatement(query);
            prepSt.setString(1, login);
            prepSt.setInt(2, typeID);
            ResultSet rs = prepSt.executeQuery();
            List<Expense> result = getResult(rs);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Expense> getExpensesByDate(Connection conn, String login, String date) {
        try {
            String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                    "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                    "WHERE user_expenses.user_login = ? AND expenses.date = ?";

            PreparedStatement prepSt = conn.prepareStatement(query);
            prepSt.setString(1, login);
            prepSt.setString(2, date);
            ResultSet rs = prepSt.executeQuery();
            List<Expense> result = getResult(rs);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Expense> getExpensesByLowerInterval(Connection conn, String login, String startDate) {
        try {
            String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                    "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                    "WHERE user_expenses.user_login = ? AND expenses.date >= ?";

            PreparedStatement prepSt = conn.prepareStatement(query);
            prepSt.setString(1, login);
            prepSt.setString(2, startDate);
            ResultSet rs = prepSt.executeQuery();
            List<Expense> result = getResult(rs);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Expense> getExpensesByUpperInterval(Connection conn, String login, String endDate) {
        try {
            String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                    "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                    "WHERE user_expenses.user_login = ? AND expenses.date <= ?";

            PreparedStatement prepSt = conn.prepareStatement(query);
            prepSt.setString(1, login);
            prepSt.setString(2, endDate);
            ResultSet rs = prepSt.executeQuery();
            List<Expense> result = getResult(rs);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Expense> getExpensesByDateInterval(Connection conn, String login, String startDate, String endDate) {
        try {
            String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                    "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                    "WHERE user_expenses.user_login = ? AND (expenses.date BETWEEN ? AND ?)";

            PreparedStatement prepSt = conn.prepareStatement(query);
            prepSt.setString(1, login);
            prepSt.setString(2, startDate);
            prepSt.setString(3, endDate);
            ResultSet rs = prepSt.executeQuery();
            List<Expense> result = getResult(rs);
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
