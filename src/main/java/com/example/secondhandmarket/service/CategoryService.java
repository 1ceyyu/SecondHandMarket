package com.example.secondhandmarket.service;

import com.example.secondhandmarket.dao.CategoryDAO;
import com.example.secondhandmarket.model.Category;
import java.util.List;

public class CategoryService {
    private final CategoryDAO categoryDAO = new CategoryDAO();

    // 获取所有分类
    public List<Category> getAllCategories() {
        return categoryDAO.findAll();
    }

    // 添加分类
    public boolean addCategory(String name, String description) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("分类名称不能为空");
        }

        // 检查分类是否已存在
        Category existing = categoryDAO.findByName(name.trim());
        if (existing != null) {
            throw new IllegalArgumentException("分类名称已存在: " + name);
        }

        Category category = new Category();
        category.setName(name.trim());
        category.setDescription(description != null ? description.trim() : null);

        return categoryDAO.save(category);
    }

    // 更新分类
    public boolean updateCategory(int id, String name, String description) {
        if (id <= 0) {
            throw new IllegalArgumentException("分类ID无效");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("分类名称不能为空");
        }

        Category category = new Category();
        category.setId(id);
        category.setName(name.trim());
        category.setDescription(description != null ? description.trim() : null);

        return categoryDAO.update(category);
    }

    // 删除分类
    public boolean deleteCategory(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("分类ID无效");
        }

        // 检查分类是否存在
        Category category = categoryDAO.findById(id);
        if (category == null) {
            throw new IllegalArgumentException("分类不存在");
        }

        // 检查分类是否被使用
        if (categoryDAO.isUsed(id)) {
            throw new IllegalStateException("该分类下存在物品，无法删除");
        }

        return categoryDAO.delete(id);
    }

    // 根据ID获取分类
    public Category getCategoryById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("分类ID无效");
        }
        return categoryDAO.findById(id);
    }

    // 验证分类是否存在
    public boolean validateCategoryExists(int categoryId) {
        if (categoryId <= 0) {
            return false;
        }
        return categoryDAO.findById(categoryId) != null;
    }
}