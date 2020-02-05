package com.example.tracker.server.dao.mapper;

import com.example.tracker.shared.model.Procedure;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static com.example.tracker.server.constant.DBConstants.*;

public class ProcedureMapper implements RowMapper<Procedure> {
    @Override
    public Procedure mapRow(ResultSet resultSet, int i) throws SQLException {
        try {
            Procedure procedure = new Procedure();

            procedure.setId(resultSet.getInt(ID_COLUMN));
            procedure.setTypeId(resultSet.getInt(TYPE_ID_COLUMN));
            procedure.setKind(resultSet.getInt(KIND_COLUMN));
            procedure.setUsername(resultSet.getString(USERNAME_COLUMN));
            procedure.setName(resultSet.getString(NAME_COLUMN));
            procedure.setDate(new SimpleDateFormat(DATE_PATTERN).parse(resultSet.getString(DATE_COLUMN)));
            procedure.setPrice(resultSet.getDouble(PRICE_COLUMN));
            procedure.setIsArchived(resultSet.getInt(IS_ARCHIVED_COLUMN));

            return procedure;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
