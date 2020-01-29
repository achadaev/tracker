package com.example.tracker.server.dao;

import com.example.tracker.server.dao.mapper.ProcedureTypeMapper;
import com.example.tracker.shared.model.ProcedureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IProcedureTypeDAOImpl implements IProcedureTypeDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    final static Logger logger = LoggerFactory.getLogger(IProcedureDAOImpl.class);

    @Override
    public List<ProcedureType> getTypes() {
        String query = "SELECT  proc_type.id, proc_type.kind, proc_type.name " +
                "FROM proc_type";
        return jdbcTemplate.query(query, new ProcedureTypeMapper());
    }

    @Override
    public List<ProcedureType> getExpenseTypes() {
        String query = "SELECT  proc_type.id, proc_type.kind, proc_type.name " +
                "FROM proc_type " +
                "WHERE kind < 0";
        return jdbcTemplate.query(query, new ProcedureTypeMapper());
    }

    @Override
    public List<ProcedureType> getIncomeTypes() {
        String query = "SELECT  proc_type.id, proc_type.kind, proc_type.name " +
                "FROM proc_type " +
                "WHERE kind > 0";
        return jdbcTemplate.query(query, new ProcedureTypeMapper());
    }

    @Override
    public ProcedureType getTypeById(int id) {
        String query = "SELECT proc_type.id, proc_type.kind, proc_type.name  " +
                "FROM proc_type " +
                "WHERE id = ?";
        return jdbcTemplate.query(query, preparedStatement -> preparedStatement
                .setInt(1, id), new ProcedureTypeMapper()).get(0);
    }

    @Override
    public Boolean addType(ProcedureType type) {
        String query = "INSERT INTO proc_type (kind, name) VALUES " +
                "(?, ?)";
        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {
            preparedStatement.setInt(1, type.getKind());
            preparedStatement.setString(2, type.getName());
            return preparedStatement.execute();
        });
    }

    @Override
    public Boolean updateType(ProcedureType type) {
        String query = "UPDATE proc_type " +
                "SET name = ?, kind = ? " +
                "WHERE id = ?";
        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {
            preparedStatement.setString(1, type.getName());
            preparedStatement.setInt(2, type.getKind());
            preparedStatement.setInt(3, type.getId());
            return preparedStatement.execute();
        });
    }

    private Boolean deleteType(int id) {
        String query = "DELETE FROM proc_type " +
                "WHERE id = ?";
        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {
            preparedStatement.setInt(1, id);
            return preparedStatement.execute();
        });
    }

    @Override
    public List<ProcedureType> deleteTypes(List<Integer> ids) {
        for (int i = 0; i < ids.size(); i++) {
            deleteType(ids.get(i));
        }
        return getTypes();
    }
}
