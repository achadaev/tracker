package com.example.tracker.server;

import com.example.tracker.server.dao.IProcedureDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TrackerRunner implements CommandLineRunner {

    @Autowired
    IProcedureDAO iProcedureDao;

    @Override
    public void run(String... args) {
    }
}
