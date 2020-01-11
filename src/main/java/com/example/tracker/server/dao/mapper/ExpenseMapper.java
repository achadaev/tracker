package com.example.tracker.server.dao.mapper;

import com.example.tracker.shared.model.Expense;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ExpenseMapper implements RowMapper<Expense> {
    @Override
    public Expense mapRow(ResultSet resultSet, int i) throws SQLException {
        try {
            Expense expense = new Expense();

            expense.setId(resultSet.getInt("id"));
            expense.setTypeId(resultSet.getInt("type_id"));
            expense.setName(resultSet.getString("name"));
            expense.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getString("date")));
            expense.setPrice(resultSet.getDouble("price"));
            expense.setIsArchived(resultSet.getInt("is_archived"));

            return expense;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
