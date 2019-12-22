package com.example.tracker.server.dao;

import com.example.tracker.shared.model.User;

public interface IUserDAO {
    User getUserByName(String name);
}
