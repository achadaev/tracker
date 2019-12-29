package com.example.tracker.server.dao;

import com.example.tracker.server.dao.mapper.UserMapper;
import com.example.tracker.shared.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class IUserDAOImpl implements IUserDAO {

    @Autowired
    JdbcTemplate jdbcTemplate;

    final static Logger logger = LoggerFactory.getLogger(IUserDAOImpl.class);

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public User getUserByName(String name) {
        String query = "SELECT user.id, user.login, user.name, user.surname, user.email, user.pass, user.role, user.reg_date  " +
                "FROM user WHERE user.login = ?";

        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setString(1, name);
            }
        }, new UserMapper()).get(0);
    }

    @Override
    public User getUserById(int id) {
        String query = "SELECT user.id, user.login, user.name, user.surname, user.email, user.pass, user.role, user.reg_date  " +
                "FROM user " +
                "WHERE id = ?";
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, id);
            }
        }, new UserMapper()).get(0);
    }

    @Override
    public List<User> getAllUsers() {
        String query = "SELECT user.id, user.login, user.name, user.surname, user.email, user.pass, user.role, user.reg_date  " +
                "FROM user";

        return jdbcTemplate.query(query, new UserMapper());
    }

    @Override
    public Boolean addUser(User user) {
        String query = "INSERT INTO user (login, name, surname, email, pass, role, reg_date) VALUES " +
                "(?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                setUserFields(preparedStatement, user);
                return preparedStatement.execute();
            }
        });
        return false;
    }

    @Override
    public Boolean updateUser(User user) {
        String query = "UPDATE user " +
                "SET login = ?, " +
                "name = ?, " +
                "surname = ?, " +
                "email = ?, " +
                "pass = ?, " +
                "role = ?, " +
                "reg_date = ? " +
                "WHERE id = ?";
        jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                setUserFields(preparedStatement, user);
                preparedStatement.setInt(8, user.getId());
                return preparedStatement.execute();
            }
        });
        return false;
    }

    private void setUserFields(PreparedStatement preparedStatement, User user) throws SQLException {
        preparedStatement.setString(1, user.getLogin());
        preparedStatement.setString(2, user.getName());
        preparedStatement.setString(3, user.getSurname());
        preparedStatement.setString(4, user.getEmail());
        preparedStatement.setString(5, user.getPassword());
        preparedStatement.setString(6, user.getRole());
        preparedStatement.setString(7, dateFormat.format(user.getRegDate()));
    }

    private Boolean deleteUser(int id) {
        String query = "DELETE FROM user " +
                "WHERE id = ?";
        jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setInt(1, id);
                return preparedStatement.execute();
            }
        });
        return true;
    }

    @Override
    public List<User> deleteUsers(List<Integer> ids) {
        for (int i = 0; i < ids.size(); i++) {
            deleteUser(ids.get(i));
        }
        return getAllUsers();
    }
}
