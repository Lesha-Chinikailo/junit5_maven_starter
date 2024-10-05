package com.dmdev.junit.service;

import ch.qos.logback.core.util.StringUtil;
import com.dmdev.junit.dao.UserDao;
import com.dmdev.junit.dto.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;
    private final List<User> users = new ArrayList<User>();

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAll() {
        return users;
    }

    public boolean delete(Integer userId){
        return userDao.delete(userId);
    }

    public boolean addUser(User user) {
        return users.add(user);
    }

    public Optional<User> login(String username, String password) {
        if(StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password)) {
            throw new IllegalArgumentException("Username or password are required");
        }
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .filter(user -> user.getPassword().equals(password))
                .findFirst();
    }
}
