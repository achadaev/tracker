package com.example.tracker.dao;

import java.sql.Connection;
import java.util.List;

public interface IUserDao {
    Connection connect();

    User getUserByName(Connection conn, String name);
}
