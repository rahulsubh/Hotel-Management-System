package com.rahul.service;

import com.rahul.entities.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    List<User> getAllUsers();
    User getUserById(String userId);
    User updateUser(User user, String userId);
    void deleteUser(String userId);
}
