package com.example.secondhandmarket.controller;

import com.example.secondhandmarket.model.User;
import com.example.secondhandmarket.service.CategoryService;
import com.example.secondhandmarket.util.AuthUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/category/*")
public class CategoryController extends HttpServlet {
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 使用工具类验证管理员权限
        User currentUser = AuthUtil.checkAdmin(request, response);
        if (currentUser == null) return;

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/list")) {
            // 显示分类列表
            request.setAttribute("categories", categoryService.getAllCategories());
            request.getRequestDispatcher("/WEB-INF/jsp/category_list.jsp").forward(request, response);

        } else if (pathInfo.equals("/add")) {
            // 显示添加分类页面
            request.getRequestDispatcher("/WEB-INF/jsp/category_form.jsp").forward(request, response);

        } else if (pathInfo.equals("/edit")) {
            // 显示编辑分类页面
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                if (id <= 0) {
                    throw new IllegalArgumentException("分类ID无效");
                }
                request.setAttribute("category", categoryService.getCategoryById(id));
                request.getRequestDispatcher("/WEB-INF/jsp/category_form.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/category/list?error=invalidId");
            } catch (IllegalArgumentException e) {
                response.sendRedirect(request.getContextPath() + "/category/list?error=" + e.getMessage());
            }

        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 使用工具类验证管理员权限
        User currentUser = AuthUtil.checkAdmin(request, response);
        if (currentUser == null) return;

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo.equals("/save")) {
                String action = request.getParameter("action");

                if ("add".equals(action)) {
                    handleAdd(request);
                    response.sendRedirect(request.getContextPath() + "/category/list?message=addSuccess");

                } else if ("update".equals(action)) {
                    handleUpdate(request);
                    response.sendRedirect(request.getContextPath() + "/category/list?message=updateSuccess");
                } else {
                    throw new IllegalArgumentException("无效的操作类型");
                }

            } else if (pathInfo.equals("/delete")) {
                handleDelete(request);
                response.sendRedirect(request.getContextPath() + "/category/list?message=deleteSuccess");

            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }

        } catch (Exception e) {
            System.err.println("分类操作失败: " + e.getMessage());
            String errorMsg = e.getMessage() != null ? e.getMessage().replace(" ", "%20") : "操作失败";
            response.sendRedirect(request.getContextPath() + "/category/list?error=" + errorMsg);
        }
    }

    private void handleAdd(HttpServletRequest request) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");

        // 参数验证
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("分类名称不能为空");
        }
        if (description != null && description.length() > 200) {
            throw new IllegalArgumentException("分类描述不能超过200个字符");
        }

        if (!categoryService.addCategory(name, description)) {
            throw new RuntimeException("添加分类失败");
        }
    }

    private void handleUpdate(HttpServletRequest request) {
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("分类ID无效");
        }

        String name = request.getParameter("name");
        String description = request.getParameter("description");

        // 参数验证
        if (id <= 0) {
            throw new IllegalArgumentException("分类ID无效");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("分类名称不能为空");
        }
        if (description != null && description.length() > 200) {
            throw new IllegalArgumentException("分类描述不能超过200个字符");
        }

        if (!categoryService.updateCategory(id, name, description)) {
            throw new RuntimeException("更新分类失败");
        }
    }

    private void handleDelete(HttpServletRequest request) {
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("分类ID无效");
        }

        if (id <= 0) {
            throw new IllegalArgumentException("分类ID无效");
        }

        if (!categoryService.deleteCategory(id)) {
            throw new RuntimeException("删除分类失败");
        }
    }
}