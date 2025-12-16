package com.example.secondhandmarket.service;

import com.example.secondhandmarket.dao.ItemDAO;
import com.example.secondhandmarket.model.Item;
import java.util.List;

public class ItemService {
    private final ItemDAO itemDAO = new ItemDAO();
    private final CategoryService categoryService = new CategoryService();

    public boolean publish(Item item) {
        item.validate(); // 基础合法性校验

        // 验证分类是否存在（如果指定了分类）
        if (item.getCategoryId() != null && item.getCategoryId() > 0) {
            if (!categoryService.validateCategoryExists(item.getCategoryId())) {
                throw new IllegalArgumentException("指定的分类不存在");
            }
        }

        return itemDAO.save(item);
    }

    public List<Item> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }
        if (keyword.trim().length() > 100) {
            throw new IllegalArgumentException("搜索关键词过长");
        }
        return itemDAO.fuzzySearch(keyword.trim());
    }

    public List<Item> getItemsByUserId(int userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("用户ID无效");
        }
        return itemDAO.findByUserId(userId);
    }

    public boolean update(Item item, int userId) {
        item.validate(); // 基础合法性校验
        item.setUserId(userId); // 设置用户ID用于权限校验

        // 验证分类是否存在（如果指定了分类）
        if (item.getCategoryId() != null && item.getCategoryId() > 0) {
            if (!categoryService.validateCategoryExists(item.getCategoryId())) {
                throw new IllegalArgumentException("指定的分类不存在");
            }
        }

        return itemDAO.update(item);
    }

    public boolean delete(int itemId, int userId) {
        if (itemId <= 0) {
            throw new IllegalArgumentException("物品ID无效");
        }
        if (userId <= 0) {
            throw new IllegalArgumentException("用户ID无效");
        }
        return itemDAO.delete(itemId, userId);
    }

    // 根据分类ID获取物品
    public List<Item> getItemsByCategoryId(int categoryId) {
        if (categoryId <= 0) {
            throw new IllegalArgumentException("分类ID无效");
        }
        return itemDAO.findByCategoryId(categoryId);
    }
}