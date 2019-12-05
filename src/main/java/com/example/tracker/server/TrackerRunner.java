package com.example.tracker.server;

import com.example.tracker.server.dao.IExpenseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Component
public class TrackerRunner implements CommandLineRunner {

    @Autowired
    IExpenseDao iExpenseDao;

    @Override
    public void run(String... args) throws Exception {
    }
}
