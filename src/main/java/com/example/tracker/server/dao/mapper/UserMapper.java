package com.example.tracker.server.dao.mapper;

import com.example.tracker.shared.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.example.tracker.server.constant.DBConstants.*;

public class UserMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        try {
            User user = new User();

            user.setId(resultSet.getInt(ID_COLUMN));
            user.setLogin(resultSet.getString(LOGIN_COLUMN));
            user.setName(resultSet.getString(NAME_COLUMN));
            user.setSurname(resultSet.getString(SURNAME_COLUMN));
            user.setEmail(resultSet.getString(EMAIL_COLUMN));
            user.setPassword(resultSet.getString(PASSWORD_COLUMN));
            user.setRole(resultSet.getString(ROLE_COLUMN));
            user.setRegDate(new SimpleDateFormat(DATE_PATTERN).parse(resultSet.getString(REGISTRATION_DATE_COLUMN)));
            user.setIsActive(resultSet.getInt(IS_ACTIVE_COLUMN));

            return user;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
