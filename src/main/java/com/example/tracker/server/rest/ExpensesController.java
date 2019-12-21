package com.example.tracker.server.rest;

import com.example.tracker.server.dao.IExpenseDao;
import com.example.tracker.server.dao.IUserDao;
import com.example.tracker.server.service.ExpenseService;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
import com.example.tracker.shared.model.User;
import org.fusesource.restygwt.client.MethodCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
public class ExpensesController {

    @Autowired
    IExpenseDao iExpenseDao;

    @Autowired
    IUserDao userService;

    @Autowired
    ExpenseService expenseService;

    @GetMapping("/expenses")
    List<Expense> getAllExpenses() {
        return iExpenseDao.getAllExpenses();
    }

    @GetMapping("/expenses/user")
    List<Expense> getUsersExpenses() {
        return expenseService.getUsersExpenses();
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
        return iExpenseDao.getTypes();
    }

    @GetMapping("/expenses/profile")
    User getUser() {
        return expenseService.getCurrentUser();
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