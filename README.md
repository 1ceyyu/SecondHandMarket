# 二手市场交易平台

一个基于 Java Servlet + JSP 的二手物品交易平台，支持用户注册、登录、发布物品、搜索物品、管理个人物品等功能。

## 功能特性

-  **用户认证**
    - 注册（密码加盐哈希存储）
    - 登录与注销
    - 会话管理
-  **物品管理**
    - 发布二手物品
    - 修改物品信息与状态
    - 删除个人物品
    - 查看个人物品列表
-  **搜索功能**
    - 支持标题/描述模糊搜索
    - 仅显示可交易物品
-  **前端界面**
    - 响应式设计
    - 支持实时预览发布内容
    - 友好的消息提示

## 技术栈

| 层 | 技术 |
|----|------|
| 后端 | Java Servlet、JSP |
| 数据库 | MySQL 8.0+ |
| 前端 | HTML、CSS、JSP + JSTL、Font Awesome |
| 工具 | JDBC、Lombok |

## 项目结构

```
SecondHandMarket/
├── .mvn/                    # Maven 配置文件目录
├── src/                     # 源代码目录
│   ├── main/               # 主源代码目录
│   │   ├── java/           # Java 源代码
│   │   │   └── com/example/secondhandmarket/
│   │   │       ├── controller/     # 控制器层
│   │   │       ├── dao/            # 数据访问层
│   │   │       ├── model/          # 数据模型层
│   │   │       └── service/        # 服务层
│   │   ├── resources/      # 资源文件目录
│   │   └── webapp/         # Web 应用资源
│   │       ├── static/     # 静态资源
│   │       │   └── css/    # 样式文件
│   │       ├── WEB-INF/    # Web 配置文件
│   │       │   ├── web.xml # Web 部署描述符
│   │       │   └── jsp/    # JSP 页面
│   │       └── *.jsp       # 直接访问的 JSP 页面
│   └── test/               # 测试代码目录
├── .gitignore              # Git 忽略文件配置
├── pom.xml                 # Maven 项目配置
└── README.md               # 项目说明文档
```

## 详细目录说明
**1.Java 源代码结构 (src/main/java/com/example/secondhandmarket/)**
   ```
   controller/          # Servlet 控制器
   ├── AuthController.java     # 处理认证相关请求（登录/注册/注销）
   └── ItemController.java     # 处理物品相关请求（发布/修改/删除/搜索）
dao/                 # 数据访问对象（Data Access Object）
├── ConnectionFactory.java  # 数据库连接工厂
├── ItemDAO.java           # 物品数据访问
└── UserDAO.java           # 用户数据访问

model/               # 实体类模型
├── Item.java        # 物品实体类
└── User.java        # 用户实体类

service/             # 业务逻辑层
├── ItemService.java  # 物品业务逻辑
└── UserService.java  # 用户业务逻辑
```
** Web 资源结构 (src/main/webapp/)**
   ```
   webapp/
   ├── static/           # 静态资源目录
   │   └── css/         # 样式表目录
   │       └── style.css # 全局样式文件
   │
   ├── WEB-INF/          # 受保护的 Web 资源
   │   ├── web.xml      # Web 应用部署描述符
   │   └── jsp/         # 受保护的 JSP 页面（需通过 Servlet 访问）
   │       ├── profile.jsp        # 个人物品管理页面
   │       ├── publish_item.jsp   # 物品发布页面
   │       └── search_results.jsp # 搜索结果页面
   │
   ├── index.jsp         # 首页（可直接访问）
   ├── login.jsp         # 登录页面（可直接访问）
   └── register.jsp      # 注册页面（可直接访问）
   ```
##  测试账户

项目已内置两个测试账户，方便快速体验：

| 用户名 | 密码 | 用途 |
|--------|------|------|
| `lby` | `123456` | 普通用户测试 |
| `admin` | `111111` | 普通用户测试 |

> **注意：** 密码在数据库中已通过 SHA-256 + 盐值进行哈希存储，登录时系统会自动验证。

## 数据库设计

### 用户表 `users`
```sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    passwordHash VARCHAR(255) NOT NULL,
    salt VARCHAR(50) NOT NULL
);
```

### 物品表 `items`
```sql
CREATE TABLE items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    status ENUM('available', 'sold') DEFAULT 'available',
    publish_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

## 配置说明

1. **数据库连接**  
   修改 `ConnectionFactory.java` 中的连接信息：
   ```java
   private static final String URL = "jdbc:mysql://your-server:3306/secondhand_db";
   private static final String USER = "your-username";
   private static final String PASS = "your-password";
   ```

2. **依赖库**  
   确保项目中包含以下依赖：
    - `mysql-connector-java`
    - `jakarta.servlet-api`
    - `lombok`
    - `jstl`

## 运行流程

1. **登录/注册**
    - 使用测试账户或自行注册新账户
    - 系统会自动跳转到首页

2. **核心功能**
    - **发布物品**：点击导航栏"发布物品"
    - **搜索物品**：在首页搜索框中输入关键词
    - **管理物品**：点击"我的物品"查看、修改、删除已发布物品

3. **物品状态管理**
    - 可交易 (`available`)
    - 已售出 (`sold`)
    - 状态可在"我的物品"页面中随时修改

## 页面说明

| 页面 | 路径                              | 功能 |
|------|---------------------------------|------|
| 首页 | `/`                             | 搜索入口、发布引导 |
| 登录 | `/login.jsp`                    | 用户登录 |
| 注册 | `/register.jsp`                 | 新用户注册 |
| 发布物品 | `/WEB-INF/jsp/publish_item.jsp` | 填写物品信息并发布 |
| 我的物品 | `/item/profile.jsp`             | 管理个人发布的物品 |
| 搜索结果 | `/item/search_results.jsp`      | 显示搜索到的物品列表 |

## 安全特性

- 密码使用 SHA-256 + 随机盐值进行哈希存储
- 用户会话管理，防止未授权访问
- SQL 参数化查询，防止注入攻击
- 用户只能操作自己的物品（权限验证）

## 部署说明

1. 创建 MySQL 数据库 `secondhand_db`
2. 导入项目中的 SQL 表结构
3. 配置 Tomcat 服务器
4. 部署 WAR 包或项目文件夹
5. 启动服务并访问应用

## 许可证
本项目仅供学习使用，遵循 MIT 许可证。