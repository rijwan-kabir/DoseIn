package com.momentum.dosein.service;

import com.momentum.dosein.model.User;
import java.util.List;

public class UserService {
    private static final String USER_FILE = "users.ser";
    private final FileStorageService<User> storage = new FileStorageService<>();

    /** Returns User if credentials match, otherwise null */
    public User login(String username, String password) {
        List<User> all = storage.loadData(USER_FILE);
        return all.stream()
                .filter(u -> u.getUsername().equals(username)
                        && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    /** Registers a new user; false if username exists */
    public boolean register(User user) {
        List<User> all = storage.loadData(USER_FILE);
        boolean exists = all.stream()
                .anyMatch(u -> u.getUsername().equals(user.getUsername()));
        if (exists) return false;
        all.add(user);
        storage.saveData(USER_FILE, all);
        return true;
    }
}
