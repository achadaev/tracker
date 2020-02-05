package com.example.tracker.server.dao.mapper;

import com.example.tracker.shared.model.ProcedureType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.tracker.server.constant.DBConstants.*;

public class ProcedureTypeMapper implements RowMapper<ProcedureType> {
    @Override
    public ProcedureType mapRow(ResultSet resultSet, int i) throws SQLException {
        ProcedureType type = new ProcedureType();

        type.setId(resultSet.getInt(ID_COLUMN));
        type.setKind(resultSet.getInt(KIND_COLUMN));
        type.setName(resultSet.getString(NAME_COLUMN));

        return type;
    }
}
