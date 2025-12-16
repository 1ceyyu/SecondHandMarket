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

    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt));
            byte[] hashedPassword = md.digest(password.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (Exception e) {
            throw new RuntimeException("密码哈希失败: " + e.getMessage());
        }
    }

    // 注册（自动设置admin角色）
    public boolean register(String username, String rawPassword) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (rawPassword == null || rawPassword.length() < 6) {
            throw new IllegalArgumentException("密码长度至少6位");
        }
        if (username.trim().length() > 50) {
            throw new IllegalArgumentException("用户名不能超过50个字符");
        }

        // 检查用户名是否已存在
        if (userDAO.findByUsername(username) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        String salt = generateSalt();
        String passwordHash = hashPassword(rawPassword, salt);

        User newUser = new User();
        newUser.setUsername(username.trim());
        newUser.setPasswordHash(passwordHash);
        newUser.setSalt(salt);

        // 如果是admin用户，设置为admin角色
        if ("admin".equals(username.trim())) {
            newUser.setRole("admin");
        } else {
            newUser.setRole("user");
        }

        return userDAO.save(newUser);
    }

    // 登录验证
    public User login(String username, String rawPassword) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }

        User user = userDAO.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        String inputHash = hashPassword(rawPassword, user.getSalt());

        if (inputHash.equals(user.getPasswordHash())) {
            return user;
        }
        throw new IllegalArgumentException("用户名或密码错误");
    }

    // 设置用户为管理员（只有管理员才能调用）
    public boolean setUserAsAdmin(int userId) {
        return userDAO.updateRole(userId, "admin");
    }

    // 验证是否是管理员
    public boolean isAdmin(User user) {
        return user != null && "admin".equals(user.getRole());
    }
}