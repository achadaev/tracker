package com.example.tracker.server.rest;

import com.example.tracker.server.dao.IExpenseDao;
import com.example.tracker.server.dao.IUserDao;
import com.example.tracker.server.service.ExpensesService;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
import com.example.tracker.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.util.Collections;
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
    ExpensesService expensesService;

    @GetMapping("/expenses")
    List<Expense> getAllExpenses() {
        return iExpenseDao.getAllExpenses();
    }

    @GetMapping("/expenses/user")
    List<Expense> getUsersExpenses() {
        return expensesService.getUsersExpenses();
    }

    @GetMapping("/expenses/types")
    List<ExpenseType> getTypes() {
        return iExpenseDao.getTypes();
    }

    @GetMapping("{login}/expenses/date={date}")
     List<Expense> getExpensesByDate(@PathVariable int userId, @PathVariable String date) {
        return iExpenseDao.getExpensesByDate(userId, date);
    }

    @GetMapping("{login}/expenses/ld={start}")
    List<Expense> getExpensesByLowerInterval(@PathVariable int userId, @PathVariable String start) {
        return iExpenseDao.getExpensesByLowerInterval(userId, start);
    }

    @GetMapping("{login}/expenses/ud={end}")
    List<Expense> getExpensesByUpperInterval(@PathVariable int userId, @PathVariable String end) {
        return iExpenseDao.getExpensesByUpperInterval(userId, end);
    }

    @GetMapping("{login}/expenses/ld={start}/ud={end}")
    List<Expense> getExpensesByDateInterval(@PathVariable int userId,
                                             @PathVariable String start,
                                             @PathVariable String end) {
         return iExpenseDao.getExpensesByDateInterval(userId, start, end);
    }

    @GetMapping("/expenses/profile")
    User getUser() {
        return expensesService.getCurrentUser();
    }

    @PostMapping(value="/expenses/add", produces = MediaType.APPLICATION_JSON)
    Map<String, Boolean> addExpense(@RequestBody Expense expense) {
        return Collections.singletonMap("response", expensesService.addExpense(expense));
    }

    @DeleteMapping("/expenses/delete")
    List<Expense> deleteExpenses(@RequestBody List<Integer> ids) {
        return expensesService.deleteExpenses(ids);
    }
}