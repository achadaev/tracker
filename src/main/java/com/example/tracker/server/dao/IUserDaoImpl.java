package com.example.tracker.server.dao;

import com.example.tracker.server.service.UserDetailsServiceImpl;
import com.example.tracker.shared.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class IUserDaoImpl implements IUserDao {
    final static Logger logger = LoggerFactory.getLogger(IUserDaoImpl.class);

    @Override
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @Override
    public User getUserByName(String name) {
        String dbFile = "C:\\Projects\\tracker\\src\\main\\resources\\tracker.db";
        String url = "jdbc:sqlite:" + dbFile;
        Connection conn;
        try {
            conn = DriverManager.getConnection(url);
            logger.info("Connected to database");
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
            logger.info(e.getMessage());
        }
        return null;
    }
}
