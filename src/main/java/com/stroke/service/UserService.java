package com.stroke.service;

import com.stroke.model.User;
import com.stroke.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User findByUsername(String username) {
        System.out.println("Finding user by username: " + username);
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            System.out.println("User found, stored password: " + user.getPassword());
        } else {
            System.out.println("User not found: " + username);
        }
        return user;
    }

    public boolean validateLogin(String username, String encryptedPassword) {
        User user = findByUsername(username);
        if (user == null) {
            System.out.println("User not found: " + username);
            return false;
        }
        System.out.println("Comparing passwords - Request: " + encryptedPassword + ", Stored: " + user.getPassword());
        boolean isValid = encryptedPassword.equals(user.getPassword());
        System.out.println("Password validation result: " + isValid);
        return isValid;
    }

    public void registerUser(String username, String encryptedPassword) throws Exception {
        System.out.println("Registering user: " + username + ", encrypted password: " + encryptedPassword);
        if (findByUsername(username) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        System.out.println("User registered successfully: " + username);
    }
}