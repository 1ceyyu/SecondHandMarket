package com.example.secondhandmarket.controller;

import com.example.secondhandmarket.model.Item;
import com.example.secondhandmarket.model.User;
import com.example.secondhandmarket.service.ItemService;
import com.example.secondhandmarket.service.CategoryService;
import com.example.secondhandmarket.util.AuthUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/item/*")
public class ItemController extends HttpServlet {
    private final ItemService itemService = new ItemService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/search")) {
            // 搜索处理
            try {
                String keyword = request.getParameter("keyword");
                if (keyword != null && keyword.length() > 100) {
                    throw new IllegalArgumentException("搜索关键词过长");
                }
                List<Item> results = itemService.search(keyword);
                request.setAttribute("items", results);
                request.setAttribute("keyword", keyword);
                request.setAttribute("categories", categoryService.getAllCategories());
                request.getRequestDispatcher("/WEB-INF/jsp/search_results.jsp").forward(request, response);
            } catch (Exception e) {
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("/WEB-INF/jsp/search_results.jsp").forward(request, response);
            }

        } else if (pathInfo != null && pathInfo.equals("/profile")) {
            // 个人物品列表
            User currentUser = AuthUtil.checkLogin(request, response);
            if (currentUser == null) return;

            List<Item> myItems = itemService.getItemsByUserId(currentUser.getId());
            request.setAttribute("myItems", myItems);
            request.setAttribute("categories", categoryService.getAllCategories());
            request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);

        } else if (pathInfo != null && pathInfo.equals("/publish")) {
            // 发布物品页面
            User currentUser = AuthUtil.checkLogin(request, response);
            if (currentUser == null) return;

            request.setAttribute("categories", categoryService.getAllCategories());
            request.getRequestDispatcher("/WEB-INF/jsp/publish_item.jsp").forward(request, response);

        } else if (pathInfo != null && pathInfo.equals("/by-category")) {
            // 按分类查看物品
            try {
                int categoryId = Integer.parseInt(request.getParameter("id"));
                List<Item> items = itemService.getItemsByCategoryId(categoryId);
                request.setAttribute("items", items);
                request.setAttribute("category", categoryService.getCategoryById(categoryId));
                request.setAttribute("categories", categoryService.getAllCategories());
                request.getRequestDispatcher("/WEB-INF/jsp/category_items.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/index.jsp?error=invalidCategoryId");
            } catch (IllegalArgumentException e) {
                response.sendRedirect(request.getContextPath() + "/index.jsp?error=" + e.getMessage());
            }

        } else {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        User currentUser = AuthUtil.checkLogin(request, response);
        if (currentUser == null) return;

        String action = request.getParameter("action");
        try {
            switch (action) {
                case "publish":
                    handlePublish(request, currentUser);
                    break;
                case "update":
                    handleUpdate(request, currentUser);
                    break;
                case "delete":
                    handleDelete(request, currentUser);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                    return;
            }
            response.sendRedirect(request.getContextPath() + "/item/profile?message=" + action + "Success");
        } catch (Exception e) {
            System.err.println("物品操作失败: " + e.getMessage());
            String errorMsg = e.getMessage() != null ? e.getMessage().replace(" ", "%20") : action + "Failed";
            response.sendRedirect(request.getContextPath() + "/item/profile?error=" + errorMsg);
        }
    }

    private void handlePublish(HttpServletRequest request, User user) throws Exception {
        // 参数验证
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");
        String categoryIdStr = request.getParameter("categoryId");

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("物品标题不能为空");
        }
        if (title.trim().length() > 100) {
            throw new IllegalArgumentException("物品标题不能超过100个字符");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("物品描述不能为空");
        }
        if (description.length() > 1000) {
            throw new IllegalArgumentException("物品描述不能超过1000个字符");
        }

        BigDecimal price;
        try {
            price = new BigDecimal(priceStr);
            if (price.signum() <= 0) {
                throw new IllegalArgumentException("价格必须大于零");
            }
            if (price.compareTo(new BigDecimal("9999999.99")) > 0) {
                throw new IllegalArgumentException("价格不能超过9999999.99");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("价格格式不正确");
        }

        Integer categoryId = null;
        if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
            try {
                categoryId = Integer.parseInt(categoryIdStr);
                if (categoryId <= 0) {
                    throw new IllegalArgumentException("分类ID无效");
                }
                // 验证分类是否存在
                if (!categoryService.validateCategoryExists(categoryId)) {
                    throw new IllegalArgumentException("指定的分类不存在");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("分类ID格式不正确");
            }
        }

        Item item = new Item();
        item.setUserId(user.getId());
        item.setTitle(title.trim());
        item.setDescription(description.trim());
        item.setPrice(price);
        item.setStatus("available");
        item.setCategoryId(categoryId);

        itemService.publish(item);
    }

    private void handleUpdate(HttpServletRequest request, User user) throws Exception {
        // 参数验证
        String itemIdStr = request.getParameter("itemId");
        String title = request.getParameter("title");
        String priceStr = request.getParameter("price");
        String status = request.getParameter("status");
        String categoryIdStr = request.getParameter("categoryId");

        if (itemIdStr == null || itemIdStr.trim().isEmpty()) {
            throw new IllegalArgumentException("物品ID不能为空");
        }

        int itemId;
        try {
            itemId = Integer.parseInt(itemIdStr);
            if (itemId <= 0) {
                throw new IllegalArgumentException("物品ID无效");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("物品ID格式不正确");
        }

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("物品标题不能为空");
        }
        if (title.trim().length() > 100) {
            throw new IllegalArgumentException("物品标题不能超过100个字符");
        }

        BigDecimal price;
        try {
            price = new BigDecimal(priceStr);
            if (price.signum() <= 0) {
                throw new IllegalArgumentException("价格必须大于零");
            }
            if (price.compareTo(new BigDecimal("9999999.99")) > 0) {
                throw new IllegalArgumentException("价格不能超过9999999.99");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("价格格式不正确");
        }

        if (status == null || (!"available".equals(status) && !"sold".equals(status))) {
            throw new IllegalArgumentException("状态必须是available或sold");
        }

        Integer categoryId = null;
        if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
            try {
                categoryId = Integer.parseInt(categoryIdStr);
                if (categoryId <= 0) {
                    throw new IllegalArgumentException("分类ID无效");
                }
                // 验证分类是否存在
                if (!categoryService.validateCategoryExists(categoryId)) {
                    throw new IllegalArgumentException("指定的分类不存在");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("分类ID格式不正确");
            }
        }

        Item item = new Item();
        item.setId(itemId);
        item.setUserId(user.getId());
        item.setTitle(title.trim());
        item.setPrice(price);
        item.setStatus(status);
        item.setCategoryId(categoryId);

        if (!itemService.update(item, user.getId())) {
            throw new Exception("Update failed or user has no permission.");
        }
    }

    private void handleDelete(HttpServletRequest request, User user) throws Exception {
        String itemIdStr = request.getParameter("itemId");

        if (itemIdStr == null || itemIdStr.trim().isEmpty()) {
            throw new IllegalArgumentException("物品ID不能为空");
        }

        int itemId;
        try {
            itemId = Integer.parseInt(itemIdStr);
            if (itemId <= 0) {
                throw new IllegalArgumentException("物品ID无效");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("物品ID格式不正确");
        }

        if (!itemService.delete(itemId, user.getId())) {
            throw new Exception("Delete failed or user has no permission.");
        }
    }
}