package com.example.tracker.server.dao;

import com.example.tracker.server.dao.mapper.ExpenseTypeMapper;
import com.example.tracker.shared.model.ExpenseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class IExpenseTypeDAOImpl implements IExpenseTypeDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    final static Logger logger = LoggerFactory.getLogger(IExpenseDAOImpl.class);

    @Override
    public List<ExpenseType> getTypes() {
        String query = "SELECT  expense_type.id, expense_type.name " +
                "FROM expense_type";
        return jdbcTemplate.query(query, new ExpenseTypeMapper());
    }

    @Override
    public ExpenseType getTypeById(int id) {
        String query = "SELECT expense_type.id, expense_type.name  " +
                "FROM expense_type " +
                "WHERE id = ?";
        return jdbcTemplate.query(query, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                preparedStatement.setInt(1, id);
            }
        }, new ExpenseTypeMapper()).get(0);
    }

    @Override
    public Boolean addType(ExpenseType type) {
        String query = "INSERT INTO expense_type (name) VALUES " +
                "(?)";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setString(1, type.getName());
                return preparedStatement.execute();
            }
        });
    }

    @Override
    public Boolean updateType(ExpenseType type) {
        String query = "UPDATE expense_type " +
                "SET name = ? " +
                "WHERE id = ?";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setString(1, type.getName());
                preparedStatement.setInt(2, type.getId());
                return preparedStatement.execute();
            }
        });
    }

    private Boolean deleteType(int id) {
        String query = "DELETE FROM expense_type " +
                "WHERE id = ?";
        return jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement preparedStatement) throws SQLException, DataAccessException {
                preparedStatement.setInt(1, id);
                return preparedStatement.execute();
            }
        });
    }

    @Override
    public List<ExpenseType> deleteTypes(List<Integer> ids) {
        for (int i = 0; i < ids.size(); i++) {
            deleteType(ids.get(i));
        }
        return getTypes();
    }
}
