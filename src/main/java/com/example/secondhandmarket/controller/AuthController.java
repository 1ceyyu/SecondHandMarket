package com.example.secondhandmarket.controller;

import com.example.secondhandmarket.model.User;
import com.example.secondhandmarket.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/auth/*")
public class AuthController extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) { response.sendError(HttpServletResponse.SC_BAD_REQUEST); return; }

        if (pathInfo.equals("/register")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            if (userService.register(username, password)) {
                response.sendRedirect(request.getContextPath() + "/login.jsp?message=RegisterSuccess");
            } else {
                response.sendRedirect(request.getContextPath() + "/register.jsp?error=UsernameTaken");
            }
        } else if (pathInfo.equals("/login")) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            User user = userService.login(username, password);
            if (user != null) {
                request.getSession().setAttribute("currentUser", user);
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp?error=InvalidCredentials");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.equals("/logout")) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/index.jsp?message=LoggedOut");
        }
    }
}