package com.example.tracker.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class IUserDaoImpl implements IUserDao {
    final static Logger logger = LoggerFactory.getLogger(IExpenseDaoImpl.class);

    @Override
    public User getUserByName(String name) {
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
            String query = "SELECT user.login, user.pass, user.reg_date " +
                    "FROM user WHERE user.login = ?";
            PreparedStatement prepSt = conn.prepareStatement(query);
            prepSt.setString(1, name);
            ResultSet rs = prepSt.executeQuery();
            User user = new User();
            user.setLogin(rs.getString("login"));
            user.setPassword(rs.getString("pass"));
            user.setRegDate(rs.getString("reg_date"));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
