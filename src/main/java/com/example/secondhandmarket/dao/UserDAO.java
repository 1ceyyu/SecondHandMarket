package com.example.secondhandmarket.dao;

import com.example.secondhandmarket.model.User;
import java.sql.*;


public class UserDAO {
    // 查找用户 (用于登录和注册检查)
    public User findByUsername(String username) {
        String sql = "SELECT id, username, passwordHash, salt FROM users WHERE username = ?";
        User user = null;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                // **核心检查点：** 只有当结果集有下一行时，才创建 User 对象
                if (rs.next()) { // 👈 这一行必须正确执行，如果查询无结果，则 rs.next() 为 false
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("passwordHash"));
                    user.setSalt(rs.getString("salt"));
                }
            }
        } catch (SQLException e) {
            // **检查点：** 如果数据库连接或查询失败，这里会打印错误
            e.printStackTrace();
        }
        // 如果查询无结果，user 应该为 null
        return user;
    }

    // 保存新用户
    public boolean save(User user) {
        String sql = "INSERT INTO users (username, passwordHash, salt) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPasswordHash());
            pstmt.setString(3, user.getSalt());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}