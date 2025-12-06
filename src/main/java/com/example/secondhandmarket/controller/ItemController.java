package com.example.secondhandmarket.controller;

import com.example.secondhandmarket.model.Item;
import com.example.secondhandmarket.model.User;
import com.example.secondhandmarket.service.ItemService;
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

    private User checkAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = (User) request.getSession().getAttribute("currentUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return null;
        }
        return user;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/search")) {
            // 搜索处理
            String keyword = request.getParameter("keyword");
            List<Item> results = itemService.search(keyword);
            request.setAttribute("items", results);
            request.setAttribute("keyword", keyword);
            request.getRequestDispatcher("/WEB-INF/jsp/search_results.jsp").forward(request, response);
        } else if (pathInfo != null && pathInfo.equals("/profile")) {
            // 个人物品列表
            User currentUser = checkAuth(request, response);
            if (currentUser == null) return;

            List<Item> myItems = itemService.getItemsByUserId(currentUser.getId());
            request.setAttribute("myItems", myItems);
            request.getRequestDispatcher("/WEB-INF/jsp/profile.jsp").forward(request, response);
        } else if (pathInfo != null && pathInfo.equals("/publish")) {
            // 发布物品页面
            User currentUser = checkAuth(request, response);
            if (currentUser == null) return;

            request.getRequestDispatcher("/WEB-INF/jsp/publish_item.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User currentUser = checkAuth(request, response);
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
            System.err.println("Item operation failed: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/item/profile?error=" + action + "Failed");
        }
    }

    private void handlePublish(HttpServletRequest request, User user) throws Exception {
        //  发布物品信息
        Item item = new Item();
        item.setUserId(user.getId());
        item.setTitle(request.getParameter("title"));
        item.setDescription(request.getParameter("description"));
        item.setPrice(new BigDecimal(request.getParameter("price")));
        item.setStatus("available");
        itemService.publish(item);
    }

    private void handleUpdate(HttpServletRequest request, User user) throws Exception {
        // 修改物品
        Item item = new Item();
        item.setId(Integer.parseInt(request.getParameter("itemId")));
        item.setUserId(user.getId());
        item.setTitle(request.getParameter("title"));
        item.setPrice(new BigDecimal(request.getParameter("price")));
        item.setStatus(request.getParameter("status"));

        if (!itemService.update(item, user.getId())) {
            throw new Exception("Update failed or user has no permission.");
        }
    }

    private void handleDelete(HttpServletRequest request, User user) throws Exception {
        //  删除物品
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        if (!itemService.delete(itemId, user.getId())) {
            throw new Exception("Delete failed or user has no permission.");
        }
    }
}