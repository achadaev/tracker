package com.example.tracker.server.webServices;

import com.example.tracker.shared.model.Expense;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Controller
@Path("/expenses")
public class ExpenseWebServiceImpl {

    final static Logger logger = LoggerFactory.getLogger(ExpenseWebServiceImpl.class);

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
/*
    private IExpenseDao expenseService;
    Connection conn = expenseService.connect();
*/

    public ExpenseWebServiceImpl() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Expense> getAllExpenses() {
        String dbFile = "C:\\Projects\\tracker\\src\\main\\resources\\tracker.db";
        String url = "jdbc:sqlite:" + dbFile;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            logger.info("Connected to database");
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }

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

    @GET
    @Path("/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Expense> getExpenseByUser(@PathParam("login") String login) {
        String dbFile = "C:\\Projects\\tracker\\src\\main\\resources\\tracker.db";
        String url = "jdbc:sqlite:" + dbFile;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            logger.info("Connected to database");
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }

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
}
