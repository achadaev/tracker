package com.example.tracker.server.dao.mapper;

import com.example.tracker.shared.model.Procedure;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ProcedureMapper implements RowMapper<Procedure> {
    @Override
    public Procedure mapRow(ResultSet resultSet, int i) throws SQLException {
        try {
            Procedure procedure = new Procedure();

            procedure.setId(resultSet.getInt("id"));
            procedure.setTypeId(resultSet.getInt("type_id"));
            procedure.setKind(resultSet.getInt("kind"));
            procedure.setUsername(resultSet.getString("username"));
            procedure.setName(resultSet.getString("name"));
            procedure.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getString("date")));
            procedure.setPrice(resultSet.getDouble("price"));
            procedure.setIsArchived(resultSet.getInt("is_archived"));

            return procedure;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
