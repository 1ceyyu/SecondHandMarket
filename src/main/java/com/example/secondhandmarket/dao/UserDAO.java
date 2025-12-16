package com.example.secondhandmarket.dao;

import com.example.secondhandmarket.model.User;
import java.sql.*;

public class UserDAO {

    // 查找用户（包含角色）
    public User findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }

        String sql = "SELECT id, username, passwordHash, salt, role FROM users WHERE username = ?";
        User user = null;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("passwordHash"));
                    user.setSalt(rs.getString("salt"));
                    user.setRole(rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询用户失败: " + e.getMessage());
        }
        return user;
    }

    // 保存新用户（设置默认角色）
    public boolean save(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (user.getUsername().trim().length() > 50) {
            throw new IllegalArgumentException("用户名不能超过50个字符");
        }
        if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
            throw new IllegalArgumentException("密码哈希不能为空");
        }

        String sql = "INSERT INTO users (username, passwordHash, salt, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getSalt());
            pstmt.setString(4, user.getRole() != null ? user.getRole() : "user");

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("用户名已存在: " + user.getUsername());
            }
            throw new RuntimeException("保存用户失败: " + e.getMessage());
        }
    }

    // 更新用户角色（用于设置管理员）
    public boolean updateRole(int userId, String role) {
        if (!"admin".equals(role) && !"user".equals(role)) {
            throw new IllegalArgumentException("角色必须是admin或user");
        }

        String sql = "UPDATE users SET role = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, role);
            pstmt.setInt(2, userId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("更新用户角色失败: " + e.getMessage());
        }
    }
}