package com.example.tracker.server.dao;

import com.example.tracker.shared.model.User;

import java.util.List;

public interface IUserDAO {
    User getUserByName(String name);

    User getUserById(int id);

    List<User> getAllUsers();

    Boolean addUser(User user);

    Boolean updateUser(User user);

    List<User> deleteUsers(List<Integer> ids);
}
