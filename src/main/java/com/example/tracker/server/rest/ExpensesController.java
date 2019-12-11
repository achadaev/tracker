package com.example.tracker.server.rest;

import com.example.tracker.server.dao.IExpenseDao;
import com.example.tracker.server.dao.IUserDao;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExpensesController {

    @Autowired
    IExpenseDao iExpenseDao;

    @Autowired
    IUserDao userService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/expenses")
    List<Expense> getAllExpenses() {
        return iExpenseDao.getAllExpenses();
    }

    @GetMapping("{login}/expenses")
    List<Expense> getExpensesByUser(@PathVariable String login) {
        return iExpenseDao.getExpensesByUser(login);
    }

    @GetMapping("{login}/expenses/typeID={typeID}")
     List<Expense> getExpensesByType(@PathVariable String login, @PathVariable int typeID) {
         return iExpenseDao.getExpensesByType(login, typeID);
    }

    @GetMapping("{login}/expenses/date={date}")
     List<Expense> getExpensesByDate(@PathVariable String login, @PathVariable String date) {
         return iExpenseDao.getExpensesByDate(login, date);
    }

    @GetMapping("{login}/expenses/ld={start}")
    List<Expense> getExpensesByLowerInterval(@PathVariable String login, @PathVariable String start) {
        return iExpenseDao.getExpensesByLowerInterval(login, start);
    }

    @GetMapping("{login}/expenses/ud={end}")
    List<Expense> getExpensesByUpperInterval(@PathVariable String login, @PathVariable String end) {
        return iExpenseDao.getExpensesByUpperInterval(login, end);
    }

    @GetMapping("{login}/expenses/ld={start}/ud={end}")
    List<Expense> getExpensesByDateInterval(@PathVariable String login,
                                             @PathVariable String start,
                                             @PathVariable String end) {
         return iExpenseDao.getExpensesByDateInterval(login, start, end);
    }

    @PutMapping("/expenses/add")
    Boolean addExpense(Expense expense) {
        return iExpenseDao.addExpense(expense);
    }

    @PutMapping("/upd")
    Boolean updateExpense(Expense expense) {
         return iExpenseDao.updateExpense(expense);
    }

    @GetMapping("/expenses/user")
    User getUser() {
        return userService.getUserByName(userService.getCurrentUsername());
    }

    @DeleteMapping("expenses/delete")
    List<Expense> deleteExpenses(List<Integer> ids) {
        return iExpenseDao.deleteExpenses(ids);
    }
}