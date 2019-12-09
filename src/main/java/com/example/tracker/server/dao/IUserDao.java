package com.example.tracker.server.dao;

import com.example.tracker.shared.model.User;

public interface IUserDao {
    String getCurrentUsername();
    User getUserByName(String name);
}
