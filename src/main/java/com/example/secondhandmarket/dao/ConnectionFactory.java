package com.example.secondhandmarket.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = "jdbc:mysql://localhost/secondhand_db" + "?serverTimezone=Asia/Shanghai" + "&useSSL=false" + "&characterEncoding=UTF-8" + "&useLegacyDatetimeCode=false";
    private static final String USER = "root";
    private static final String PASS = "200588liu";

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