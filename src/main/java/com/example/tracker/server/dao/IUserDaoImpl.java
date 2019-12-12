package com.example.tracker.server.dao;

import com.example.tracker.server.dao.mapper.UserMapper;
import com.example.tracker.server.service.UserDetailsServiceImpl;
import com.example.tracker.shared.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class IUserDaoImpl implements IUserDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    final static Logger logger = LoggerFactory.getLogger(IUserDaoImpl.class);

    @Override
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    @Override
    public User getUserByName(String name) {
        User user = new User();
        String query = "SELECT user.id, user.login, user.pass, user.reg_date " +
                "FROM user WHERE user.login = ?";

        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, name);
            }
        }, new UserMapper()).get(0);
    }
}
