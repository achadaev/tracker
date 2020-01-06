package com.example.tracker.server.rest;

import com.example.tracker.server.dao.IExpenseDAO;
import com.example.tracker.server.dao.IExpenseTypeDAO;
import com.example.tracker.server.dao.IUserDAO;
import com.example.tracker.server.service.ExpenseService;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
import com.example.tracker.shared.model.ReviewInfo;
import com.example.tracker.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.nio.file.AccessDeniedException;
import java.util.*;

@RestController
@RequestMapping("/")
public class ExpensesController {

    @Autowired
    IExpenseDAO iExpenseDao;

    @Autowired
    IExpenseTypeDAO iExpenseTypeDAO;

    @Autowired
    IUserDAO iUserDAO;

    @Autowired
    ExpenseService expenseService;

    @GetMapping("/expenses")
    List<Expense> getAllExpenses() {
        return iExpenseDao.getAllExpenses();
    }

    @GetMapping("/expenses/user-expenses")
    List<Expense> getUsersExpenses() {
        return expenseService.getUsersExpenses();
    }

    @GetMapping("/expenses/review")
    ReviewInfo getReview() {
        return expenseService.getReview();
    }

    @GetMapping("/expenses/id={id}")
    Expense getExpenseById(@PathVariable int id) {
        return expenseService.getExpenseById(id);
    }

    @GetMapping("/expenses/typeId={id}")
    List<Expense> getExpensesByTypeId(@PathVariable int id) {
        return expenseService.getExpensesByTypeId(id);
    }

    @GetMapping("/expenses/typeId={typeId}/{startDate}/{endDate}")
    List<Expense> getExpensesByDateAndTypeId(@PathVariable int typeId,
                                             @PathVariable Date startDate,
                                             @PathVariable Date endDate) {
        return expenseService.getExpensesByDate(typeId, startDate, endDate);
    }

    @GetMapping("/expenses/types")
    List<ExpenseType> getTypes() {
        return iExpenseTypeDAO.getTypes();
    }

    @GetMapping("/expenses/typesId={id}")
    ExpenseType getTypeById(@PathVariable int id) {
        return iExpenseTypeDAO.getTypeById(id);
    }

    @PostMapping(value = "/expenses/add-type", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> addType(@RequestBody ExpenseType type) {
        try {
            return Collections.singletonMap("response", expenseService.updateType(type));
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PutMapping(value = "/expenses/update-type", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> updateType(@RequestBody ExpenseType type) {
        try {
            return Collections.singletonMap("response", expenseService.updateType(type));
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @DeleteMapping("/expenses/delete-types")
    List<ExpenseType> deleteTypes(@RequestBody List<Integer> ids) {
        return iExpenseTypeDAO.deleteTypes(ids);
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
    Map<String, Boolean> addExpense(@RequestBody Expense expense) {
        return Collections.singletonMap("response", expenseService.addExpense(expense));
    }

    @PutMapping(value = "/expenses/update", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> updateExpense(@RequestBody Expense expense) {
        return Collections.singletonMap("response", expenseService.updateExpense(expense));
    }

    @DeleteMapping("/expenses/delete")
    List<Expense> deleteExpenses(@RequestBody List<Integer> ids) {
        return expenseService.deleteExpenses(ids);
    }
}