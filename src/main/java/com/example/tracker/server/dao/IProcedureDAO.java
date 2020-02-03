package com.example.tracker.server.dao;

import com.example.tracker.shared.model.Procedure;
import com.example.tracker.shared.model.ReviewInfo;

import java.util.Date;
import java.util.List;

public interface IProcedureDAO {

    List<Procedure> getAllExpenses();

    List<Procedure> getAllIncomes();

    List<Procedure> getUsersExpenses(int userId);

    List<Procedure> getUsersIncomes(int userId);

    ReviewInfo getReview(int userId);

    Procedure getProcedureById(int userId, int id);

    List<Procedure> getProceduresByTypeId(int userId, int typeId);

    List<Procedure> getExpensesByDate(int userId, Date startDate, Date endDate);

    List<Procedure> getIncomesByDate(int userId, Date startDate, Date endDate);

    List<Procedure> getExpensesByDateAndTypeId(int userId, int typeId, Date startDate, Date endDate);

    List<Procedure> getIncomesByDateAndTypeId(int userId, int typeId, Date startDate, Date endDate);

    Boolean addProcedure(Procedure procedure, int userId);

    Boolean updateProcedure(Procedure procedure);

    List<Procedure> archiveProcedures(List<Integer> ids, int userId);

    List<Procedure> deleteProcedures(List<Integer> ids, int userId);

}
