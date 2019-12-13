package com.example.tracker.server.rest;

import com.example.tracker.server.dao.IExpenseDao;
import com.example.tracker.server.dao.IUserDao;
import com.example.tracker.server.service.ExpensesService;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("{login}/expenses")
    List<Expense> getExpensesByUser(@PathVariable int userId) {
        return iExpenseDao.getExpensesByUser(userId);
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

    @PostMapping("/expenses/add")
    Boolean addExpense(Expense expense) {
        return iExpenseDao.addExpense(expense);
    }

    @GetMapping("/expenses/user")
    User getUser() {
        return expensesService.getCurrentUser();
    }

    @DeleteMapping("expenses/delete")
    List<Expense> deleteExpenses(List<Integer> ids) {
        return iExpenseDao.deleteExpenses(ids);
    }
}