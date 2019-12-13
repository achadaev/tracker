package com.example.tracker.server.service;

import com.example.tracker.server.dao.IUserDao;
import com.example.tracker.shared.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class ExpensesService {
    @Autowired
    private IUserDao iUserDao;

    public User getCurrentUser() {
        String login = UtilsService.getCurrentUsername();
        return iUserDao.getUserByName(login);
    }
}
