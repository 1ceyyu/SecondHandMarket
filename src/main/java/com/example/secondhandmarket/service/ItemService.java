package com.example.secondhandmarket.service;

import com.example.secondhandmarket.dao.ItemDAO;
import com.example.secondhandmarket.model.Item;
import java.util.List;

public class ItemService {
    private final ItemDAO itemDAO = new ItemDAO();

    public boolean publish(Item item) {
        if (item.getPrice() == null || item.getPrice().signum() <= 0) {
            throw new IllegalArgumentException("价格必须大于零。");
        }
        return itemDAO.save(item);
    }

    public List<Item> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) { return List.of(); }
        return itemDAO.fuzzySearch(keyword.trim());
    }

    public List<Item> getItemsByUserId(int userId) {
        return itemDAO.findByUserId(userId);
    }

    public boolean update(Item item, int userId) {
        // 权限校验在 DAO 层通过 WHERE id=? AND user_id=? 隐式实现
        item.setUserId(userId);
        return itemDAO.update(item);
    }

    public boolean delete(int itemId, int userId) {
        return itemDAO.delete(itemId, userId);
    }
}