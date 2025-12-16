package com.example.secondhandmarket.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    private String username;
    private String passwordHash;
    private String salt;
    private String role;  // 新增：用户角色字段
}