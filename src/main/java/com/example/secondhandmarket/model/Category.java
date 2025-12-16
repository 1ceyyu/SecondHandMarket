package com.example.secondhandmarket.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private int id;
    private String name;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // 合法性校验方法
    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("分类名称不能为空");
        }
        if (name.trim().length() > 50) {
            throw new IllegalArgumentException("分类名称不能超过50个字符");
        }
        if (description != null && description.length() > 200) {
            throw new IllegalArgumentException("分类描述不能超过200个字符");
        }
    }
}