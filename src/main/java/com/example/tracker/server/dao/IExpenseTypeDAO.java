package com.example.tracker.server.dao;

import com.example.tracker.shared.model.ExpenseType;

import java.util.List;

public interface IExpenseTypeDAO {

    List<ExpenseType> getTypes();

    ExpenseType getTypeById(int id);

    Boolean addType(ExpenseType type);

    Boolean updateType(ExpenseType type);

    List<ExpenseType> deleteTypes(List<Integer> ids);

}
