package com.example.tracker.server.dao;

import com.example.tracker.shared.model.Expense;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
public class IExpenseDaoImpl implements IExpenseDao {

    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    final static Logger logger = LoggerFactory.getLogger(IExpenseDaoImpl.class);

    private String dbFile = "C:\\Projects\\tracker\\src\\main\\resources\\tracker.db";

    private String url = "jdbc:sqlite:" + dbFile;

    public Connection conn;

    public IExpenseDaoImpl() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
    }

    private List<Expense> getResult(ResultSet rs) {
        List<Expense> result = new ArrayList<>();
        try {
            while (rs.next()) {
                Expense exp = new Expense();
                exp.setId(rs.getInt("id"));
                exp.setTypeId(rs.getInt("type_id"));
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
    public List<Expense> getAllExpenses() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement();
            st.execute("SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                    "FROM expenses");
            ResultSet rs = st.getResultSet();
            List<Expense> result = getResult(rs);
            return result;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Expense> getExpensesByUser(String login) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                    "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                    "WHERE user_expenses.user_login = ?";
            PreparedStatement prepSt = conn.prepareStatement(query);
            prepSt.setString(1, login);
            ResultSet rs = prepSt.executeQuery();
            List<Expense> result = getResult(rs);
            return result;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Expense getExpenseById(int id) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                    "FROM expenses " +
                    "WHERE expenses.id = ?";

            PreparedStatement prepSt = conn.prepareStatement(query);
            prepSt.setInt(1, id);
            ResultSet rs = prepSt.executeQuery();
            Expense result = getResult(rs).get(0); // 00000000000
            return result;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Expense> getExpensesByType(String login, int typeID) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                    "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                    "WHERE user_expenses.user_login = ? AND expenses.type_id = ?";

            PreparedStatement prepSt = conn.prepareStatement(query);
            prepSt.setString(1, login);
            prepSt.setInt(2, typeID);
            ResultSet rs = prepSt.executeQuery();
            List<Expense> result = getResult(rs);
            return result;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Expense> getExpensesByDate(String login, String date) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                    "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                    "WHERE user_expenses.user_login = ? AND expenses.date = ?";

            PreparedStatement prepSt = conn.prepareStatement(query);
            prepSt.setString(1, login);
            prepSt.setString(2, date);
            ResultSet rs = prepSt.executeQuery();
            List<Expense> result = getResult(rs);
            return result;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Expense> getExpensesByLowerInterval(String login, String startDate) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                    "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                    "WHERE user_expenses.user_login = ? AND expenses.date >= ?";

            PreparedStatement prepSt = conn.prepareStatement(query);
            prepSt.setString(1, login);
            prepSt.setString(2, startDate);
            ResultSet rs = prepSt.executeQuery();
            List<Expense> result = getResult(rs);
            return result;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Expense> getExpensesByUpperInterval(String login, String endDate) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            String query = "SELECT expenses.id, expenses.type_id, expenses.name, expenses.date, expenses.price " +
                    "FROM expenses JOIN user_expenses ON expenses.id = user_expenses.expense_id " +
                    "WHERE user_expenses.user_login = ? AND expenses.date <= ?";

            PreparedStatement prepSt = conn.prepareStatement(query);
            prepSt.setString(1, login);
            prepSt.setString(2, endDate);
            ResultSet rs = prepSt.executeQuery();
            List<Expense> result = getResult(rs);
            return result;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Expense> getExpensesByDateInterval(String login, String startDate, String endDate) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean addExpense(Expense expense) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            String query = "INSERT INTO expenses VALUES " +
                    "(?, ?, ?, ?, ?)";

            PreparedStatement prepSt = conn.prepareStatement(query);
            prepSt.setInt(1, expense.getId());
            prepSt.setInt(2, expense.getTypeId());
            prepSt.setString(3, expense.getName());
            prepSt.setString(4, expense.getDate());
            prepSt.setInt(5, expense.getPrice());
            prepSt.executeQuery();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
        //TODO get user.login and INSERT INTO user_expenses
    }

    @Override
    public boolean updateExpense(Expense expense) {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(url);
            String query = "UPDATE expenses " +
                    "SET type_id = ?, " +
                        "name = ?, " +
                        "date = ?, " +
                        "price = ? " +
                    "WHERE id = ?";

            PreparedStatement prepSt = conn.prepareStatement(query);
            prepSt.setInt(1, expense.getTypeId());
            prepSt.setString(2, expense.getName());
            prepSt.setString(3, expense.getDate());
            prepSt.setInt(4, expense.getPrice());
            prepSt.setInt(5, expense.getId());
            prepSt.executeQuery();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
