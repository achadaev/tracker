package com.example.tracker.server.rest;

import com.example.tracker.server.dao.Expense;
import com.example.tracker.server.dao.IExpenseDao;
import com.example.tracker.server.dao.IUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.util.List;

@RestController
public class ExpensesController {

    @Autowired
    IExpenseDao iExpenseDao;

    @Autowired
    IUserDao userService;

    Connection conn = iExpenseDao.connect();

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/expenses")
    List<Expense> getExpenses() {
        return iExpenseDao.getAllExpenses(conn);
   }

   @GetMapping("{login}/expenses")
   List<Expense> getExpensesByUser(@PathVariable String login) {
       return iExpenseDao.getExpensesByUser(conn, login);
   }

   @GetMapping("{login}/expenses/typeID={typeID}")
    List<Expense> getExpensesByType(@PathVariable String login, @PathVariable int typeID) {
        return iExpenseDao.getExpensesByType(conn, login, typeID);
   }

   @GetMapping("{login}/expenses/date={date}")
    List<Expense> getExpensesByDate(@PathVariable String login, @PathVariable String date) {
        return iExpenseDao.getExpensesByDate(conn, login, date);
   }

    @GetMapping("{login}/expenses/ld={start}")
    List<Expense> getExpensesByLowerInterval(@PathVariable String login, @PathVariable String start) {
        return iExpenseDao.getExpensesByLowerInterval(conn, login, start);
    }

    @GetMapping("{login}/expenses/ud={end}")
    List<Expense> getExpensesByUpperInterval(@PathVariable String login, @PathVariable String end) {
        return iExpenseDao.getExpensesByUpperInterval(conn, login, end);
    }

   @GetMapping("{login}/expenses/ld={start}/ud={end}")
    List<Expense> getExpensesByDateInterval(@PathVariable String login,
                                            @PathVariable String start,
                                            @PathVariable String end) {
        return iExpenseDao.getExpensesByDateInterval(conn, login, start, end);
   }
}