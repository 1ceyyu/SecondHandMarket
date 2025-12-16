package com.example.secondhandmarket.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private int id;
    private int userId;
    private String title;
    private String description;
    private BigDecimal price;
    private String status;
    private Timestamp publishDate;
    private Integer categoryId;  // 新增：分类ID
    private String categoryName; // 用于显示分类名称（非数据库字段）

    // 合法性校验方法
    public void validate() {
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
        if (price == null || price.signum() <= 0) {
            throw new IllegalArgumentException("价格必须大于零");
        }
        if (price.compareTo(new BigDecimal("9999999.99")) > 0) {
            throw new IllegalArgumentException("价格不能超过9999999.99");
        }
        if (status == null || (!"available".equals(status) && !"sold".equals(status))) {
            throw new IllegalArgumentException("状态必须是available或sold");
        }
        if (categoryId != null && categoryId <= 0) {
            throw new IllegalArgumentException("分类ID无效");
        }
    }
}