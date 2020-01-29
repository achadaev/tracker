package com.example.tracker.server.dao;

import com.example.tracker.shared.model.ProcedureType;

import java.util.List;

public interface IProcedureTypeDAO {

    List<ProcedureType> getTypes();

    List<ProcedureType> getExpenseTypes();

    List<ProcedureType> getIncomeTypes();

    ProcedureType getTypeById(int id);

    Boolean addType(ProcedureType type);

    Boolean updateType(ProcedureType type);

    List<ProcedureType> deleteTypes(List<Integer> ids);

}
