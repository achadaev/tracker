package com.example.tracker.dao;

import java.sql.Connection;

public interface IUserDao {
    User getUserByName(String name);
}
