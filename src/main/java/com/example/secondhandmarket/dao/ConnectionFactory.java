package com.example.secondhandmarket.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = "jdbc:mysql://10.100.164.26:3306/secondhand_db" + "?serverTimezone=Asia/Shanghai" + "&useSSL=false" + "&characterEncoding=UTF-8" + "&useLegacyDatetimeCode=false";
    private static final String USER = "remote_admin";
    private static final String PASS = "AdminPassword123!";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}