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
    private String status; // 'available', 'sold'
    private Timestamp publishDate;
}
