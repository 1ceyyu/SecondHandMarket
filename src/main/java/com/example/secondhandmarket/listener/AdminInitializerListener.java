package com.example.secondhandmarket.listener;

import com.example.secondhandmarket.dao.UserDAO;
import com.example.secondhandmarket.model.User;
import com.example.secondhandmarket.service.UserService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AdminInitializerListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("=== 二手市场系统初始化 ===");

        try {
            ensureAdminExists();
            System.out.println("管理员账户检查完成");
        } catch (Exception e) {
            System.err.println("初始化管理员账户失败: " + e.getMessage());
        }

        System.out.println("=== 系统初始化完成 ===");
    }

    private void ensureAdminExists() {
        UserDAO userDAO = new UserDAO();
        UserService userService = new UserService();

        // 检查admin用户是否存在
        User admin = userDAO.findByUsername("admin");

        if (admin == null) {
            System.out.println("正在创建管理员账户...");
            try {
                boolean success = userService.register("admin", "admin123");
                if (success) {
                    System.out.println("✅ 管理员账户创建成功");
                    System.out.println("   用户名: admin");
                    System.out.println("   密码: admin123");
                    System.out.println("   请及时修改默认密码！");
                } else {
                    System.out.println("❌ 管理员账户创建失败");
                }
            } catch (Exception e) {
                System.err.println("❌ 创建管理员账户异常: " + e.getMessage());
            }
        } else {
            System.out.println("✅ 管理员账户已存在");
            // 确保admin用户具有admin角色
            if (!"admin".equals(admin.getRole())) {
                System.out.println("正在更新管理员角色...");
                userDAO.updateRole(admin.getId(), "admin");
                System.out.println("✅ 管理员角色已更新");
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("=== 二手市场系统关闭 ===");
    }
}