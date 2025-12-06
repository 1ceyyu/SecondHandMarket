package com.example.secondhandmarket.service;

import com.example.secondhandmarket.dao.UserDAO;
import com.example.secondhandmarket.model.User;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class UserService {
    private final UserDAO userDAO = new UserDAO();
    private static final int SALT_LENGTH = 16;

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // 密码哈希 (要求 3)
    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashedPassword = md.digest(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (Exception e) { throw new RuntimeException("Password hashing failed.", e); }
    }

    // 注册 (要求 3)
    public boolean register(String username, String rawPassword) {
        if (userDAO.findByUsername(username) != null) { return false; }

        String salt = generateSalt();
        String passwordHash = hashPassword(rawPassword, salt);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(passwordHash);
        newUser.setSalt(salt);

        return userDAO.save(newUser);
    }

    // 登录验证
    public User login(String username, String rawPassword) {
        User user = userDAO.findByUsername(username);
        if (user == null) { return null; }

        String inputHash = hashPassword(rawPassword, user.getSalt());

        if (inputHash.equals(user.getPasswordHash())) {
            return user;
        }
        return null;
    }
}