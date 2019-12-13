package com.example.tracker.server.dao;

import com.example.tracker.shared.model.User;

public interface IUserDao {
    User getUserByName(String name);
}
