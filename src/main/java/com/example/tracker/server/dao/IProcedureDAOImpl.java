package com.example.tracker.server.dao;

import com.example.tracker.server.dao.mapper.ProcedureMapper;
import com.example.tracker.shared.model.Procedure;
import com.example.tracker.shared.model.ReviewInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class IProcedureDAOImpl implements IProcedureDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    final static Logger logger = LoggerFactory.getLogger(IProcedureDAOImpl.class);

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public IProcedureDAOImpl() throws ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
    }

    @Override
    public List<Procedure> getAllExpenses() {
        String query = "SELECT proc.id, proc.type_id, proc.kind, proc.name, proc.date, proc.price, proc.is_archived " +
                "FROM proc " +
                "WHERE kind < 0";
        return jdbcTemplate.query(query, new ProcedureMapper());
    }

    @Override
    public List<Procedure> getAllIncomes() {
        String query = "SELECT proc.id, proc.type_id, proc.kind, proc.name, proc.date, proc.price, proc.is_archived " +
                "FROM proc " +
                "WHERE kind > 0";
        return jdbcTemplate.query(query, new ProcedureMapper());
    }

    @Override
    public List<Procedure> getUsersExpenses(int userId) {
        String query = "SELECT proc.id, proc.type_id, proc.kind, proc.name, proc.date, proc.price, proc.is_archived " +
                "FROM proc JOIN user_proc ON proc.id = user_proc.proc_id " +
                "WHERE kind < 0 AND user_proc.user_id = ? AND proc.is_archived = 0";
        return jdbcTemplate.query(query, preparedStatement -> preparedStatement
                .setInt(1, userId), new ProcedureMapper());
    }

    @Override
    public List<Procedure> getUsersIncomes(int userId) {
        String query = "SELECT proc.id, proc.type_id, proc.kind, proc.name, proc.date, proc.price, proc.is_archived " +
                "FROM proc JOIN user_proc ON proc.id = user_proc.proc_id " +
                "WHERE kind > 0 AND user_proc.user_id = ? AND proc.is_archived = 0";
        return jdbcTemplate.query(query, preparedStatement -> preparedStatement
                .setInt(1, userId), new ProcedureMapper());
    }

    private double getTotal(List<Procedure> procedureList) {
        double total = 0.0;
        for (Procedure procedure : procedureList) {
            total += procedure.getPrice();
        }

        return total;
    }

    @Override
    public ReviewInfo getReview(int userId) {
        ReviewInfo reviewInfo = new ReviewInfo();

        List<Procedure> tempList = getUsersExpenses(userId);
        reviewInfo.setAmount(getTotal(tempList));

        Calendar monthBefore = Calendar.getInstance();
        monthBefore.add(Calendar.MONTH, -1);
        tempList = getExpensesByDate(userId, monthBefore.getTime(), new Date());
        reviewInfo.setMonth(getTotal(tempList));

        Calendar weekBefore = Calendar.getInstance();
        weekBefore.add(Calendar.WEEK_OF_YEAR, -1);
        tempList = getExpensesByDate(userId, weekBefore.getTime(), new Date());
        reviewInfo.setWeek(getTotal(tempList));

        return reviewInfo;
    }

    @Override
    public Procedure getProcedureById(int userId, int id) {
        String query = "SELECT proc.id, proc.type_id, proc.kind, proc.name, proc.date, proc.price, proc.is_archived " +
                "FROM proc JOIN user_proc ON proc.id = user_proc.proc_id " +
                "WHERE proc.id = ? AND user_id = ?";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, userId);
        }, new ProcedureMapper()).get(0);
    }

    @Override
    public List<Procedure> getProceduresByTypeId(int userId, int typeId) {
        String query = "SELECT proc.id, proc.type_id, proc.kind, proc.name, proc.date, proc.price, proc.is_archived " +
                "FROM proc JOIN user_proc ON proc.id = user_proc.proc_id " +
                "WHERE user_proc.user_id = ? AND type_id = ?";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, typeId);
        }, new ProcedureMapper());
    }

    @Override
    public List<Procedure> getExpensesByTypeId(int userId, int typeId) {
        String query = "SELECT proc.id, proc.type_id, proc.kind, proc.name, proc.date, proc.price, proc.is_archived " +
                "FROM proc JOIN user_proc ON proc.id = user_proc.proc_id " +
                "WHERE kind < 0 AND user_proc.user_id = ? AND type_id = ?";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, typeId);
        }, new ProcedureMapper());
    }

    @Override
    public List<Procedure> getIncomesByTypeId(int userId, int typeId) {
        String query = "SELECT proc.id, proc.type_id, proc.kind, proc.name, proc.date, proc.price, proc.is_archived " +
                "FROM proc JOIN user_proc ON proc.id = user_proc.proc_id " +
                "WHERE kind > 0 AND user_proc.user_id = ? AND type_id = ?";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, typeId);
        }, new ProcedureMapper());
    }

    @Override
    public List<Procedure> getExpensesByDate(int userId, Date startDate, Date endDate) {
        String query = "SELECT proc.id, proc.type_id, proc.kind, proc.name, proc.date, proc.price, proc.is_archived " +
                "FROM proc JOIN user_proc ON proc.id = user_proc.proc_id " +
                "WHERE kind < 0 AND user_proc.user_id = ? AND (proc.date BETWEEN ? AND ?)";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, dateFormat.format(startDate));
            preparedStatement.setString(3, dateFormat.format(endDate));
        }, new ProcedureMapper());
    }

    @Override
    public List<Procedure> getIncomesByDate(int userId, Date startDate, Date endDate) {
        String query = "SELECT proc.id, proc.type_id, proc.kind, proc.name, proc.date, proc.price, proc.is_archived " +
                "FROM proc JOIN user_proc ON proc.id = user_proc.proc_id " +
                "WHERE kind > 0 AND user_proc.user_id = ? AND (proc.date BETWEEN ? AND ?)";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, dateFormat.format(startDate));
            preparedStatement.setString(3, dateFormat.format(endDate));
        }, new ProcedureMapper());
    }

    @Override
    public List<Procedure> getExpensesByDateAndTypeId(int userId, int typeId, Date startDate, Date endDate) {
        String query = "SELECT proc.id, proc.type_id, proc.kind, proc.name, proc.date, proc.price, proc.is_archived " +
                "FROM proc JOIN user_proc ON proc.id = user_proc.proc_id " +
                "WHERE kind < 0 AND user_proc.user_id = ? AND type_id = ? AND (proc.date BETWEEN ? AND ?)";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, typeId);
            preparedStatement.setString(3, dateFormat.format(startDate));
            preparedStatement.setString(4, dateFormat.format(endDate));
        }, new ProcedureMapper());
    }

    @Override
    public List<Procedure> getIncomesByDateAndTypeId(int userId, int typeId, Date startDate, Date endDate) {
        String query = "SELECT proc.id, proc.type_id, proc.kind, proc.name, proc.date, proc.price, proc.is_archived " +
                "FROM proc JOIN user_proc ON proc.id = user_proc.proc_id " +
                "WHERE kind > 0 AND user_proc.user_id = ? AND type_id = ? AND (proc.date BETWEEN ? AND ?)";
        return jdbcTemplate.query(query, preparedStatement -> {
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, typeId);
            preparedStatement.setString(3, dateFormat.format(startDate));
            preparedStatement.setString(4, dateFormat.format(endDate));
        }, new ProcedureMapper());
    }

    @Override
    public Boolean addProcedure(Procedure procedure, int userId) {
        String query = "INSERT INTO proc (type_id, kind, name, date, price) VALUES " +
                "(?, ?, ?, ?, ?)";
        jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {
            preparedStatement.setInt(1, procedure.getTypeId());
            preparedStatement.setInt(2, procedure.getKind());
            preparedStatement.setString(3, procedure.getName());
            preparedStatement.setString(4, dateFormat.format(procedure.getDate()));
            preparedStatement.setDouble(5, procedure.getPrice());
            return preparedStatement.execute();
        });
        return addUserProcedure(getLastProcedureId(), userId);
    }

    private int getLastProcedureId() {
        String query = "SELECT proc.id, proc.type_id, proc.kind, proc.name, proc.date, proc.price, proc.is_archived " +
                "FROM proc " +
                "WHERE id = (SELECT MAX(id) FROM proc)";
        return jdbcTemplate.query(query, new ProcedureMapper()).get(0).getId();
    }

    private Boolean addUserProcedure(int expenseId, int userId) {
        String query = "INSERT INTO user_proc (proc_id, user_id) VALUES " +
                "(?, ?)";
        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {
            preparedStatement.setInt(1, expenseId);
            preparedStatement.setInt(2, userId);
            return preparedStatement.execute();
        });
    }

    @Override
    public Boolean updateProcedure(Procedure procedure) {
        String query = "UPDATE proc " +
                "SET type_id = ?, " +
                "kind = ?, " +
                "name = ?, " +
                "date = ?, " +
                "price = ?, " +
                "is_archived = ? " +
                "WHERE id = ?";
        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {
            preparedStatement.setInt(1, procedure.getTypeId());
            preparedStatement.setInt(2, procedure.getKind());
            preparedStatement.setString(3, procedure.getName());
            preparedStatement.setString(4, dateFormat.format(procedure.getDate()));
            preparedStatement.setDouble(5, procedure.getPrice());
            preparedStatement.setInt(6, procedure.getIsArchived());
            preparedStatement.setInt(7, procedure.getId());
            return preparedStatement.execute();
        });
    }

    private Boolean archiveProcedure(int id) {
        String query = "UPDATE proc " +
                "SET is_archived = 1 " +
                "WHERE id = ?";
        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {
            preparedStatement.setInt(1, id);
            return preparedStatement.execute();
        });
    }

    @Override
    public List<Procedure> archiveProcedures(List<Integer> ids, int userId) {
        for (int i = 0; i < ids.size(); i++) {
            archiveProcedure(ids.get(i));
        }
        //TODO return expense or income list
        return getUsersExpenses(userId);
    }

    private Boolean deleteProcedure(int id) {
        String query = "DELETE FROM proc " +
                "WHERE id = ?";
        jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {
            preparedStatement.setInt(1, id);
            return preparedStatement.execute();
        });
        return deleteUserProcedure(id);
    }

    private Boolean deleteUserProcedure(int id) {
        String query = "DELETE FROM user_proc " +
                "WHERE proc_id = ?";
        return jdbcTemplate.execute(query, (PreparedStatementCallback<Boolean>) preparedStatement -> {
            preparedStatement.setInt(1, id);
            return preparedStatement.execute();
        });
    }

    @Override
    public List<Procedure> deleteProcedures(List<Integer> ids, int userId) {
        for (int i = 0; i < ids.size(); i++) {
            deleteProcedure(ids.get(i));
        }
        //TODO same
        return getUsersExpenses(userId);
    }

}
