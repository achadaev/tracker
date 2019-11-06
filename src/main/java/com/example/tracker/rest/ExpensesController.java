package com.example.tracker.rest;

import com.example.tracker.dao.Expense;
import com.example.tracker.dao.IExpenseDao;
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

    @GetMapping("/expenses")
    List<Expense> getExpenses() {
        Connection conn = iExpenseDao.connect();
        return iExpenseDao.getAllExpenses(conn);
   }

   @GetMapping("{login}/expenses")
   List<Expense> getExpensesByUser(@PathVariable String login) {
       Connection conn = iExpenseDao.connect();
       return iExpenseDao.getExpensesByUser(conn, login);
   }

   @GetMapping("{login}/expenses/typeID={typeID}")
    List<Expense> getExpensesByType(@PathVariable String login, @PathVariable int typeID) {
        Connection conn = iExpenseDao.connect();
        return iExpenseDao.getExpensesByType(conn, login, typeID);
   }

   @GetMapping("{login}/expenses/date={date}")
    List<Expense> getExpensesByDate(@PathVariable String login, @PathVariable String date) {
        Connection conn = iExpenseDao.connect();
        return iExpenseDao.getExpensesByDate(conn, login, date);
   }

    @GetMapping("{login}/expenses/lI={start}")
    List<Expense> getExpensesByLowerInterval(@PathVariable String login, @PathVariable String start) {
        Connection conn = iExpenseDao.connect();
        return iExpenseDao.getExpensesByLowerInterval(conn, login, start);
    }

    @GetMapping("{login}/expenses/uI={end}")
    List<Expense> getExpensesByUpperInterval(@PathVariable String login, @PathVariable String end) {
        Connection conn = iExpenseDao.connect();
        return iExpenseDao.getExpensesByUpperInterval(conn, login, end);
    }

   @GetMapping("{login}/expenses/lI={start}/uI={end}")
    List<Expense> getExpensesByDateInterval(@PathVariable String login,
                                            @PathVariable String start,
                                            @PathVariable String end) {
        Connection conn = iExpenseDao.connect();
        return iExpenseDao.getExpensesByDateInterval(conn, login, start, end);
   }
}