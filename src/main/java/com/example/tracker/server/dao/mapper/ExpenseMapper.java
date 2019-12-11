package com.example.tracker.server.dao.mapper;

import com.example.tracker.shared.model.Expense;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpenseMapper implements RowMapper<Expense> {
    @Override
    public Expense mapRow(ResultSet resultSet, int i) throws SQLException {
        Expense expense = new Expense();

        expense.setId(resultSet.getInt("id"));
        expense.setTypeId(resultSet.getInt("type_id"));
        expense.setName(resultSet.getString("name"));
        expense.setDate(resultSet.getString("date"));
        expense.setPrice(resultSet.getInt("price"));

        return expense;
    }
}
