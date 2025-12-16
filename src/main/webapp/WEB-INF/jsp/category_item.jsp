<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>${category.name} - 二手市场</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>
<header class="header">
    <div class="container nav-container">
        <a href="${pageContext.request.contextPath}/" class="logo">
            <i class="fas fa-recycle"></i> 二手市场
        </a>
        <div class="nav-links">
            <c:choose>
                <c:when test="${sessionScope.currentUser != null}">
                    <div class="user-info">
                        <i class="fas fa-user"></i>
                        <span><c:out value="${sessionScope.currentUser.username}"/></span>
                        <c:if test="${sessionScope.currentUser.role == 'admin'}">
                            <span class="badge" style="background: #ff6b6b; color: white; font-size: 11px; padding: 2px 6px; border-radius: 3px; margin-left: 5px;">管理员</span>
                        </c:if>
                    </div>
                    <a href="${pageContext.request.contextPath}/">
                        <i class="fas fa-home"></i> 首页
                    </a>
                    <a href="${pageContext.request.contextPath}/item/profile">
                        <i class="fas fa-box"></i> 我的物品
                    </a>
                    <c:if test="${sessionScope.currentUser.role == 'admin'}">
                        <a href="${pageContext.request.contextPath}/category/list">
                            <i class="fas fa-tags"></i> 分类管理
                        </a>
                    </c:if>
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

<main class="main-content container">
    <div class="card">
        <!-- 分类标题 -->
        <div class="category-header">
            <h1 class="page-title">
                <i class="fas fa-tag" style="color: #4a6bff;"></i> ${category.name}
            </h1>
            <c:if test="${not empty category.description}">
                <p class="page-subtitle">${category.description}</p>
            </c:if>
            <div style="color: #666; margin-bottom: 20px;">
                共有 ${items.size()} 件物品
            </div>
        </div>

        <!-- 返回按钮 -->
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> 返回首页
            </a>
            <a href="${pageContext.request.contextPath}/category/list" class="btn">
                <i class="fas fa-tags"></i> 所有分类
            </a>
        </div>

        <!-- 物品列表 -->
        <c:choose>
            <c:when test="${not empty items}">
                <div class="grid-3">
                    <c:forEach var="item" items="${items}">
                        <div class="item-card">
                            <div class="item-image">
                                <i class="fas fa-box-open"></i>
                            </div>
                            <div class="item-content">
                                <h3 class="item-title">
                                    <c:out value="${item.title}"/>
                                </h3>
                                <div class="item-price">
                                    ¥<fmt:formatNumber value="${item.price}" pattern="#,##0.00"/>
                                </div>
                                <p class="item-description">
                                    <c:out value="${item.description}"/>
                                </p>
                                <div class="item-meta">
                                    <div>
                                        <i class="far fa-clock"></i>
                                        <fmt:formatDate value="${item.publishDate}" pattern="yyyy-MM-dd"/>
                                    </div>
                                    <span class="item-status ${item.status == 'available' ? 'status-available' : 'status-sold'}">
                                            ${item.status == 'available' ? '可交易' : '已售出'}
                                    </span>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-icon">
                        <i class="fas fa-box-open"></i>
                    </div>
                    <h3 class="empty-title">暂无物品</h3>
                    <p class="empty-description">该分类下暂时没有物品</p>
                    <a href="${pageContext.request.contextPath}/" class="btn">
                        <i class="fas fa-arrow-left"></i> 返回首页
                    </a>
                    <c:if test="${sessionScope.currentUser != null}">
                        <a href="${pageContext.request.contextPath}/item/publish" class="btn btn-success" style="margin-left: 10px;">
                            <i class="fas fa-plus-circle"></i> 发布物品
                        </a>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>
</body>
</html>