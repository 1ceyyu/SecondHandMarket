package com.example.secondhandmarket.dao;

import com.example.secondhandmarket.model.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    // 发布物品（添加分类ID）
    public boolean save(Item item) {
        item.validate(); // 合法性校验

        String sql = "INSERT INTO items (user_id, title, description, price, category_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getUserId());
            pstmt.setString(2, item.getTitle());
            pstmt.setString(3, item.getDescription());
            pstmt.setBigDecimal(4, item.getPrice());

            // 设置分类ID（可能为null）
            if (item.getCategoryId() != null && item.getCategoryId() > 0) {
                pstmt.setInt(5, item.getCategoryId());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("发布物品失败: " + e.getMessage());
        }
    }

    // 模糊匹配查找（包含分类信息）
    public List<Item> fuzzySearch(String keyword) {
        List<Item> items = new ArrayList<>();
        String searchPattern = "%" + keyword + "%";
        String sql = "SELECT i.*, c.name as category_name " +
                "FROM items i " +
                "LEFT JOIN categories c ON i.category_id = c.id " +
                "WHERE (i.title LIKE ? OR i.description LIKE ?) AND i.status = 'available' " +
                "ORDER BY i.publish_date DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("搜索物品失败: " + e.getMessage());
        }
        return items;
    }

    // 修改物品（添加权限和分类校验）
    public boolean update(Item item) {
        item.validate(); // 合法性校验

        String sql = "UPDATE items SET title = ?, description = ?, price = ?, status = ?, category_id = ? " +
                "WHERE id = ? AND user_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getTitle());
            pstmt.setString(2, item.getDescription());
            pstmt.setBigDecimal(3, item.getPrice());
            pstmt.setString(4, item.getStatus());

            // 设置分类ID
            if (item.getCategoryId() != null && item.getCategoryId() > 0) {
                pstmt.setInt(5, item.getCategoryId());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }

            pstmt.setInt(6, item.getId());
            pstmt.setInt(7, item.getUserId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("物品不存在或您没有修改权限");
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("更新物品失败: " + e.getMessage());
        }
    }

    // 删除物品
    public boolean delete(int itemId, int userId) {
        String sql = "DELETE FROM items WHERE id = ? AND user_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            pstmt.setInt(2, userId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("物品不存在或您没有删除权限");
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("删除物品失败: " + e.getMessage());
        }
    }

    // 根据用户ID查找所有物品（包含分类信息）
    public List<Item> findByUserId(int userId) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT i.*, c.name as category_name " +
                "FROM items i " +
                "LEFT JOIN categories c ON i.category_id = c.id " +
                "WHERE i.user_id = ? ORDER BY i.publish_date DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询用户物品失败: " + e.getMessage());
        }
        return items;
    }

    // 根据分类ID查找物品
    public List<Item> findByCategoryId(int categoryId) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT i.*, c.name as category_name " +
                "FROM items i " +
                "LEFT JOIN categories c ON i.category_id = c.id " +
                "WHERE i.category_id = ? AND i.status = 'available' " +
                "ORDER BY i.publish_date DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    items.add(mapResultSetToItem(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询分类物品失败: " + e.getMessage());
        }
        return items;
    }

    // ResultSet映射到Item对象
    private Item mapResultSetToItem(ResultSet rs) throws SQLException {
        Item item = new Item();
        item.setId(rs.getInt("id"));
        item.setUserId(rs.getInt("user_id"));
        item.setTitle(rs.getString("title"));
        item.setDescription(rs.getString("description"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setStatus(rs.getString("status"));
        item.setPublishDate(rs.getTimestamp("publish_date"));
        item.setCategoryId(rs.getInt("category_id"));
        item.setCategoryName(rs.getString("category_name"));
        return item;
    }
}