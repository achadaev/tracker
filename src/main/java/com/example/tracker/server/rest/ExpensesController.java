package com.example.tracker.server.rest;

import com.example.tracker.server.dao.IProcedureDAO;
import com.example.tracker.server.dao.IProcedureTypeDAO;
import com.example.tracker.server.dao.IUserDAO;
import com.example.tracker.server.service.ExpenseService;
import com.example.tracker.shared.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.nio.file.AccessDeniedException;
import java.util.*;

@RestController
@RequestMapping("/")
public class ExpensesController {

    @Autowired
    IProcedureDAO iProcedureDao;

    @Autowired
    IProcedureTypeDAO iProcedureTypeDAO;

    @Autowired
    IUserDAO iUserDAO;

    @Autowired
    ExpenseService expenseService;

    @GetMapping("/expenses/all-expenses")
    List<Procedure> getAllExpenses() {
        return iProcedureDao.getAllExpenses();
    }

    @GetMapping("/expenses/all-incomes")
    List<Procedure> getAllIncomes() {
        return iProcedureDao.getAllIncomes();
    }

    @GetMapping("/expenses/user-expenses")
    List<Procedure> getUsersExpenses() {
        return expenseService.getUsersExpenses();
    }

    @GetMapping("/expenses/user-incomes")
    List<Procedure> getUsersIncomes() {
        return expenseService.getUsersIncomes();
    }

    @GetMapping("/expenses/review")
    ReviewInfo getReview() {
        try {
            return expenseService.getReview();
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/expenses/id={id}")
    Procedure getExpenseById(@PathVariable int id) {
        Procedure result = expenseService.getProcedureById(id);
        return result.getKind() < 0 ? result : null;
    }

    @GetMapping("/expenses/typeId={id}")
    List<Procedure> getExpensesByTypeId(@PathVariable int id) {
        try {
            return expenseService.getProceduresByTypeId(id);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/expenses/expense-typeId={typeId}/{startDate}/{endDate}")
    List<Procedure> getExpensesByDateAndTypeId(@PathVariable int typeId,
                                               @PathVariable Date startDate,
                                               @PathVariable Date endDate) {
        try {
            return expenseService.getExpensesByDate(typeId, startDate, endDate);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/expenses/income-typeId={typeId}/{startDate}/{endDate}")
    List<Procedure> getIncomesByDateAndTypeId(@PathVariable int typeId,
                                               @PathVariable Date startDate,
                                               @PathVariable Date endDate) {
        try {
            return expenseService.getIncomesByDate(typeId, startDate, endDate);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/expenses/types")
    List<ProcedureType> getTypes() {
        return iProcedureTypeDAO.getTypes();
    }

    @GetMapping("/expenses/expense-types")
    List<ProcedureType> getExpenseTypes() {
        return iProcedureTypeDAO.getExpenseTypes();
    }

    @GetMapping("/expenses/income-types")
    List<ProcedureType> getIncomeTypes() {
        return iProcedureTypeDAO.getIncomeTypes();
    }

    @GetMapping("/expenses/typesId={id}")
    ProcedureType getTypeById(@PathVariable int id) {
        return iProcedureTypeDAO.getTypeById(id);
    }

    @PostMapping(value = "/expenses/add-type", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> addType(@RequestBody ProcedureType type) {
        try {
            return Collections.singletonMap("response", expenseService.updateType(type));
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping(value = "/expenses/update-type", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> updateType(@RequestBody ProcedureType type) {
        try {
            return Collections.singletonMap("response", expenseService.updateType(type));
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping("/expenses/delete-types")
    List<ProcedureType> deleteTypes(@RequestBody List<Integer> ids) {
        return iProcedureTypeDAO.deleteTypes(ids);
    }

    @GetMapping("/expenses/profile")
    User getUser() {
        return expenseService.getCurrentUser();
    }

    @GetMapping("/expenses/profileId={id}")
    User getUserById(@PathVariable int id) {
        try {
            return expenseService.getUserById(id);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return new User();
    }

    @PostMapping(value = "expenses/add-profile", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> addUser(@RequestBody User user) {
        try {
            return Collections.singletonMap("response", expenseService.updateUser(user));
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping(value = "expenses/update-profile", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> updateUser(@RequestBody User user) {
        try {
            return Collections.singletonMap("response", expenseService.updateUser(user));
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping(value = "expenses/update-pass", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> updatePassword(@RequestBody User user) {
        try {
            return Collections.singletonMap("response", expenseService.updatePassword(user));
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/expenses/all-profiles")
    List<User> getAllUsers() {
        try {
            return expenseService.getAllUsers();
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/expenses/delete-profiles")
    List<User> deleteUsers(@RequestBody List<Integer> ids) {
        try {
            return expenseService.deleteUsers(ids);
        } catch (AccessDeniedException e) {
            e.getStackTrace();
        }
        return new ArrayList<>();
    }

    @PostMapping(value = "/expenses/add", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> addExpense(@RequestBody Procedure procedure) {
        return Collections.singletonMap("response", expenseService.addProcedure(procedure));
    }

    @PutMapping(value = "/expenses/update", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> updateExpense(@RequestBody Procedure procedure) {
        return Collections.singletonMap("response", expenseService.updateProcedure(procedure));
    }

    @PutMapping("/expenses/archive")
    List<Procedure> archiveExpenses(@RequestBody List<Integer> ids) {
        return expenseService.archiveProcedures(ids);
    }

    @DeleteMapping("/expenses/delete")
    List<Procedure> deleteExpenses(@RequestBody List<Integer> ids) {
        return expenseService.deleteProcedures(ids);
    }

    @GetMapping("/expenses/dates-between")
    List<SimpleDate> getDatesBetween() {
        return expenseService.getDatesBetween();
    }

    @GetMapping("/expenses/between")
    List<MonthlyExpense> getExpensesBetween() {
        return expenseService.getExpensesBetween();
    }

    @GetMapping("/expenses/sort/typeId={typeId}/{startDate}/{endDate}/{startIndex}/{quantity}/{isAscending}")
    List<Procedure> getSortedAndFilteredExpenses(@PathVariable int typeId,
                                                 @PathVariable Date startDate,
                                                 @PathVariable Date endDate,
                                                 @PathVariable int startIndex,
                                                 @PathVariable int quantity,
                                                 @PathVariable boolean isAscending) {
        try {
            return expenseService.getSortedAndFilteredExpenses(typeId, startDate, endDate, startIndex, quantity, isAscending);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}