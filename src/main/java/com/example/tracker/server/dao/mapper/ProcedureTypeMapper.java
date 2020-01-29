package com.example.tracker.server.dao.mapper;

import com.example.tracker.shared.model.ProcedureType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProcedureTypeMapper implements RowMapper<ProcedureType> {
    @Override
    public ProcedureType mapRow(ResultSet resultSet, int i) throws SQLException {
        ProcedureType type = new ProcedureType();

        type.setId(resultSet.getInt("id"));
        type.setKind(resultSet.getInt("kind"));
        type.setName(resultSet.getString("name"));

        return type;
    }
}
