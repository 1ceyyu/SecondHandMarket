package com.example.secondhandmarket.util;

import com.example.secondhandmarket.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthUtil {

    // 检查是否登录
    public static User checkLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User user = (User) request.getSession().getAttribute("currentUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=请先登录");
            return null;
        }
        return user;
    }

    // 检查是否是管理员
    public static User checkAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User user = checkLogin(request, response);
        if (user == null) return null;

        if (!"admin".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=PermissionDenied&message=无管理员权限");
            return null;
        }
        return user;
    }

    // 检查权限（可以指定角色）
    public static User checkRole(HttpServletRequest request, HttpServletResponse response, String requiredRole)
            throws IOException {
        User user = checkLogin(request, response);
        if (user == null) return null;

        if (!requiredRole.equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=PermissionDenied");
            return null;
        }
        return user;
    }

    // 获取当前登录用户
    public static User getCurrentUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("currentUser");
    }

    // 检查是否是当前用户的物品
    public static boolean isItemOwner(HttpServletRequest request, int itemUserId) {
        User user = getCurrentUser(request);
        return user != null && user.getId() == itemUserId;
    }
}