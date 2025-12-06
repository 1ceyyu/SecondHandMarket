package com.example.secondhandmarket.dao;

import com.example.secondhandmarket.model.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    // 发布物品 (要求 1)
    public boolean save(Item item) {
        String sql = "INSERT INTO items (user_id, title, description, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, item.getUserId());
            pstmt.setString(2, item.getTitle());
            pstmt.setString(3, item.getDescription());
            pstmt.setBigDecimal(4, item.getPrice());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // 模糊匹配查找 (要求 2)
    public List<Item> fuzzySearch(String keyword) {
        List<Item> items = new ArrayList<>();
        String searchPattern = "%" + keyword + "%";
        String sql = "SELECT * FROM items WHERE (title LIKE ? OR description LIKE ?) AND status = 'available' ORDER BY publish_date DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getInt("id"));
                    item.setUserId(rs.getInt("user_id"));
                    item.setTitle(rs.getString("title"));
                    item.setDescription(rs.getString("description"));
                    item.setPrice(rs.getBigDecimal("price"));
                    item.setStatus(rs.getString("status"));
                    item.setPublishDate(rs.getTimestamp("publish_date"));
                    items.add(item);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return items;
    }

    // 修改物品 (要求 4)
    public boolean update(Item item) {
        String sql = "UPDATE items SET title = ?, description = ?, price = ?, status = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, item.getTitle());
            pstmt.setString(2, item.getDescription());
            pstmt.setBigDecimal(3, item.getPrice());
            pstmt.setString(4, item.getStatus());
            pstmt.setInt(5, item.getId());
            pstmt.setInt(6, item.getUserId()); // 权限检查
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // 删除物品 (要求 4)
    public boolean delete(int itemId, int userId) {
        String sql = "DELETE FROM items WHERE id = ? AND user_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, itemId);
            pstmt.setInt(2, userId); // 权限检查
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // 根据用户ID查找所有物品
    public List<Item> findByUserId(int userId) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items WHERE user_id = ? ORDER BY publish_date DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Item item = new Item();
                    item.setId(rs.getInt("id"));
                    item.setUserId(rs.getInt("user_id"));
                    item.setTitle(rs.getString("title"));
                    item.setDescription(rs.getString("description"));
                    item.setPrice(rs.getBigDecimal("price"));
                    item.setStatus(rs.getString("status"));
                    item.setPublishDate(rs.getTimestamp("publish_date"));
                    items.add(item);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return items;
    }
}