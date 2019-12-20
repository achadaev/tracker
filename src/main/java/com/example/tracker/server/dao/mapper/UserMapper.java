package com.example.tracker.server.dao.mapper;

import com.example.tracker.shared.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        try {
            User user = new User();

            user.setId(resultSet.getInt("id"));
            user.setLogin(resultSet.getString("login"));
            user.setPassword(resultSet.getString("pass"));
            user.setRegDate(new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getString("reg_date")));

            return user;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
