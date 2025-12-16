package com.example.secondhandmarket.dao;

import com.example.secondhandmarket.model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // 创建分类
    public boolean save(Category category) {
        category.validate(); // 合法性校验

        String sql = "INSERT INTO categories (name, description) VALUES (?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getDescription());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // 检查是否是唯一约束冲突
            if (e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("分类名称已存在: " + category.getName());
            }
            throw new RuntimeException("数据库操作失败: " + e.getMessage());
        }
    }

    // 查找所有分类
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories ORDER BY name";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询分类列表失败: " + e.getMessage());
        }
        return categories;
    }

    // 根据ID查找分类
    public Category findById(int id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询分类失败: " + e.getMessage());
        }
        return null;
    }

    // 根据名称查找分类
    public Category findByName(String name) {
        String sql = "SELECT * FROM categories WHERE name = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询分类失败: " + e.getMessage());
        }
        return null;
    }

    // 更新分类
    public boolean update(Category category) {
        category.validate(); // 合法性校验

        String sql = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category.getName());
            pstmt.setString(2, category.getDescription());
            pstmt.setInt(3, category.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("分类不存在或更新失败");
            }
            return true;
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("分类名称已存在: " + category.getName());
            }
            throw new RuntimeException("更新分类失败: " + e.getMessage());
        }
    }

    // 删除分类
    public boolean delete(int id) {
        // 检查分类是否存在
        Category category = findById(id);
        if (category == null) {
            throw new IllegalArgumentException("分类不存在");
        }

        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException("删除分类失败: " + e.getMessage());
        }
    }

    // 检查分类是否被使用
    public boolean isUsed(int categoryId) {
        String sql = "SELECT COUNT(*) FROM items WHERE category_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("检查分类使用情况失败: " + e.getMessage());
        }
        return false;
    }

    // ResultSet映射到Category对象
    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("id"));
        category.setName(rs.getString("name"));
        category.setDescription(rs.getString("description"));
        category.setCreatedAt(rs.getTimestamp("created_at"));
        category.setUpdatedAt(rs.getTimestamp("updated_at"));
        return category;
    }
}