package com.example.tracker;

import com.example.tracker.dao.IExpenseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class DemoRunner implements CommandLineRunner {

    @Autowired
    IExpenseDao iExpenseDao;

    @Override
    public void run(String... args) throws Exception {
         Connection conn = iExpenseDao.connect();
    }
}
