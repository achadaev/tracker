package com.example.tracker.server.rest;

import com.example.tracker.server.dao.IProcedureDAO;
import com.example.tracker.server.dao.IProcedureTypeDAO;
import com.example.tracker.server.dao.IUserDAO;
import com.example.tracker.server.service.ProcedureService;
import com.example.tracker.shared.model.*;
import org.apache.commons.collections.map.SingletonMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;

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
    ProcedureService procedureService;

    @GetMapping("/expenses/logout")
    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        cookie.setPath("/tracker_war_exploded");
        response.addCookie(cookie);
    }

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
        return procedureService.getUsersExpenses();
    }

    @GetMapping("/expenses/user-incomes")
    List<Procedure> getUsersIncomes() {
        return procedureService.getUsersIncomes();
    }

    @GetMapping("/expenses/review")
    ReviewInfo getReview() {
        try {
            return procedureService.getReview();
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/expenses/expense-type-review")
    Map<String, Double> getExpensesReviewByTypes() {
        return procedureService.getExpensesReviewByTypes();
    }

    @GetMapping("/expenses/income-type-review")
    Map<String, Double> getIncomesReviewByTypes() {
        return procedureService.getIncomesReviewByTypes();
    }

    @GetMapping("/expenses/id={id}")
    Procedure getExpenseById(@PathVariable int id) {
        return procedureService.getProcedureById(id);
    }

    @GetMapping("/expenses/typeId={id}")
    List<Procedure> getProceduresByTypeId(@PathVariable int id) {
        try {
            return procedureService.getProceduresByTypeId(id);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/expenses/typeId={id}/user={userId}")
    List<Procedure> getProceduresByTypeId(@PathVariable int id, @PathVariable int userId) {
        try {
            return procedureService.getProceduresByTypeId(id, userId);
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
            return procedureService.getProceduresByDate(typeId, startDate, endDate);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/expenses/expense-typeId={typeId}/{startDate}/{endDate}/user={userId}")
    List<Procedure> getExpensesByDateAndTypeId(@PathVariable int typeId,
                                                 @PathVariable Date startDate,
                                                 @PathVariable Date endDate,
                                                 @PathVariable int userId) {
        try {
            return procedureService.getProceduresByDate(typeId, startDate, endDate, userId);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/expenses/income-typeId={typeId}/{startDate}/{endDate}")
    List<Procedure> getProceduresByDateAndTypeId(@PathVariable int typeId,
                                                 @PathVariable Date startDate,
                                                 @PathVariable Date endDate) {
        try {
            return procedureService.getProceduresByDate(typeId, startDate, endDate);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/expenses/income-typeId={typeId}/{startDate}/{endDate}/user={userId}")
    List<Procedure> getProceduresByDateAndTypeId(@PathVariable int typeId,
                                                 @PathVariable Date startDate,
                                                 @PathVariable Date endDate,
                                                 @PathVariable int userId) {
        try {
            return procedureService.getProceduresByDate(typeId, startDate, endDate, userId);
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
            return Collections.singletonMap("response", procedureService.updateType(type));
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping(value = "/expenses/update-type", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> updateType(@RequestBody ProcedureType type) {
        try {
            return Collections.singletonMap("response", procedureService.updateType(type));
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
        return procedureService.getCurrentUser();
    }

    @GetMapping("/expenses/profileId={id}")
    User getUserById(@PathVariable int id) {
        try {
            return procedureService.getUserById(id);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return new User();
    }

    @PostMapping(value = "/expenses/add-profile", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> addUser(@RequestBody User user) {
        try {
            return Collections.singletonMap("response", procedureService.updateUser(user));
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping(value = "/expenses/update-profile", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> updateUser(@RequestBody User user) {
        try {
            return Collections.singletonMap("response", procedureService.updateUser(user));
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping(value = "/expenses/update-pass", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> updatePassword(@RequestBody User user) {
        try {
            return Collections.singletonMap("response", procedureService.updatePassword(user));
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/expenses/all-profiles")
    List<User> getAllUsers() {
        try {
            return procedureService.getAllUsers();
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @PutMapping("/expenses/archive-profiles")
    List<User> archiveUsers(@RequestBody List<Integer> ids) {
        try {
            return procedureService.archiveUsers(ids);
        } catch (AccessDeniedException e) {
            e.getStackTrace();
        }
        return new ArrayList<>();
    }

    @PostMapping(value = "/expenses/add", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> addExpense(@RequestBody Procedure procedure) {
        return Collections.singletonMap("response", procedureService.addProcedure(procedure));
    }

    @PutMapping(value = "/expenses/update", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> updateExpense(@RequestBody Procedure procedure) {
        return Collections.singletonMap("response", procedureService.updateProcedure(procedure));
    }

    @PutMapping("/expenses/archive")
    List<Procedure> archiveExpenses(@RequestBody List<Integer> ids) {
        return procedureService.archiveProcedures(ids);
    }

    @GetMapping("/expenses/dates-between")
    List<SimpleDate> getDatesBetween() {
        return procedureService.getDatesBetween();
    }

    @GetMapping("/expenses/between")
    List<MonthlyExpense> getExpensesBetween() {
        return procedureService.getExpensesBetween();
    }

    @GetMapping("/expenses/sort/typeId={typeId}/{startDate}/{endDate}/{startIndex}/{quantity}/{isAscending}")
    List<Procedure> getSortedAndFilteredProcedures(@PathVariable int typeId,
                                                 @PathVariable Date startDate,
                                                 @PathVariable Date endDate,
                                                 @PathVariable int startIndex,
                                                 @PathVariable int quantity,
                                                 @PathVariable boolean isAscending) {
        try {
            return procedureService.getSortedAndFilteredProcedures(typeId, startDate, endDate, startIndex, quantity, isAscending);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GetMapping("/expenses/sort/typeId={typeId}/{startDate}/{endDate}/{startIndex}/{quantity}/{isAscending}/user={userId}")
    List<Procedure> getSortedAndFilteredProcedures(@PathVariable int typeId,
                                                 @PathVariable Date startDate,
                                                 @PathVariable Date endDate,
                                                 @PathVariable int startIndex,
                                                 @PathVariable int quantity,
                                                 @PathVariable boolean isAscending,
                                                 @PathVariable int userId) {
        try {
            return procedureService.getSortedAndFilteredProcedures(typeId, startDate, endDate, startIndex, quantity, isAscending, userId);
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}