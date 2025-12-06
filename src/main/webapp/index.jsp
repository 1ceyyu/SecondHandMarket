<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>二手物品交易平台</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
</head>
<body>
<!-- 导航栏 -->
<header class="header">
    <div class="container nav-container">
        <a href="${pageContext.request.contextPath}/" class="logo">
            <i class="fas fa-recycle"></i>
            二手市场
        </a>

        <div class="nav-links">
            <c:choose>
                <c:when test="${sessionScope.currentUser != null}">
                    <div class="user-info">
                        <i class="fas fa-user"></i>
                        <span>欢迎, <c:out value="${sessionScope.currentUser.username}"/>!</span>
                    </div>
                    <a href="${pageContext.request.contextPath}/item/profile">
                        <i class="fas fa-box"></i> 我的物品
                    </a>
                    <a href="${pageContext.request.contextPath}/item/publish">
                        <i class="fas fa-plus-circle"></i> 发布物品
                    </a>
                    <a href="${pageContext.request.contextPath}/auth/logout">
                        <i class="fas fa-sign-out-alt"></i> 退出
                    </a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login.jsp">
                        <i class="fas fa-sign-in-alt"></i> 登录
                    </a>
                    <a href="${pageContext.request.contextPath}/register.jsp">
                        <i class="fas fa-user-plus"></i> 注册
                    </a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</header>

<!-- 主要内容 -->
<main class="main-content container">
    <!-- 搜索框 -->
    <div class="search-box card">
        <h2 style="text-align: center; margin-bottom: 20px; color: #333;">
            找到您心仪的二手物品
        </h2>
        <form action="${pageContext.request.contextPath}/item/search" method="get">
            <div style="position: relative;">
                <i class="fas fa-search search-icon"></i>
                <input type="text"
                       name="keyword"
                       class="search-input"
                       placeholder="输入物品名称或描述关键词，如：笔记本电脑、自行车、书籍等..."
                       required>
                <button type="submit" class="btn" style="position: absolute; right: 5px; top: 50%; transform: translateY(-50%);">
                    <i class="fas fa-search"></i> 搜索
                </button>
            </div>
        </form>
    </div>
    <c:if test="${sessionScope.currentUser != null}">
        <div class="card">
            <h2 style="margin-bottom: 20px;">
                <i class="fas fa-fire" style="color: #ff6b6b;"></i> 立即发布您的闲置物品
            </h2>
            <div style="text-align: center;">
                <a href="${pageContext.request.contextPath}/item/publish" class="btn btn-success" style="font-size: 18px; padding: 15px 40px;">
                    <i class="fas fa-plus-circle"></i> 发布闲置物品
                </a>
                <p style="margin-top: 15px; color: #666;">
                    让闲置物品找到新主人，同时获得额外收入
                </p>
            </div>
        </div>
    </c:if>
</main>
</body>
</html>