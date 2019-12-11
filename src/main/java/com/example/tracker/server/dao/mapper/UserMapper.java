package com.example.tracker.server.dao.mapper;

import com.example.tracker.shared.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();

        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("pass"));
        user.setRegDate(resultSet.getString("reg_date"));

        return user;
    }
}
