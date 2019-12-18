package com.example.tracker.server.dao.mapper;

import com.example.tracker.shared.model.ExpenseType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpenseTypeMapper implements RowMapper<ExpenseType> {
    @Override
    public ExpenseType mapRow(ResultSet resultSet, int i) throws SQLException {
        ExpenseType type = new ExpenseType();

        type.setId(resultSet.getInt("id"));
        type.setName(resultSet.getString("name"));

        return type;
    }
}
