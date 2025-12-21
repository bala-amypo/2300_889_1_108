package com.example.demo.service;

import com.example.demo.model.User;

import java.util.List;

public interface UserService {

    User createUser(User user);

    User getUserById(Long id);

    User getUserByEmail(String email);

    List<User> getAllUsers();

    // Authentication methods
    boolean register(User user);

    boolean login(String username, String password);
}
