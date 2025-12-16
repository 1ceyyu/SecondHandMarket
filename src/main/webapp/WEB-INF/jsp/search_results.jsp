<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>搜索结果</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
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
            <a href="${pageContext.request.contextPath}/">
                <i class="fas fa-home"></i> 首页
            </a>
            <c:if test="${sessionScope.currentUser != null}">
                <a href="${pageContext.request.contextPath}/item/profile">
                    <i class="fas fa-box"></i> 我的物品
                </a>
                <c:if test="${sessionScope.currentUser.role == 'admin'}">
                    <a href="${pageContext.request.contextPath}/category/list">
                        <i class="fas fa-tags"></i> 分类管理
                    </a>
                </c:if>
                <div class="user-info">
                    <i class="fas fa-user"></i>
                    <span><c:out value="${sessionScope.currentUser.username}"/></span>
                </div>
            </c:if>
        </div>
    </div>
</header>

<main class="main-content container">
    <!-- 搜索头部 -->
    <div class="card">
        <h1 class="page-title">
            <i class="fas fa-search"></i> 搜索结果
        </h1>

        <!-- 搜索框 -->
        <div class="search-box">
            <form action="${pageContext.request.contextPath}/item/search" method="get">
                <i class="fas fa-search search-icon"></i>
                <input type="text"
                       name="keyword"
                       value="<c:out value='${keyword}'/>"
                       class="search-input"
                       placeholder="请输入搜索关键词..."
                       required>
                <button type="submit" class="btn" style="position: absolute; right: 5px; top: 50%; transform: translateY(-50%);">
                    搜索
                </button>
            </form>
        </div>
        <!-- 在搜索框后添加分类筛选 -->
        <div class="category-filter" style="margin: 15px 0;">
            <span style="color: #666; margin-right: 10px;">筛选分类:</span>
            <a href="${pageContext.request.contextPath}/item/search?keyword=${keyword}"
               class="filter-tag ${empty param.categoryId ? 'active' : ''}">
                全部
            </a>
            <c:forEach var="category" items="${categories}">
                <a href="${pageContext.request.contextPath}/item/search?keyword=${keyword}&categoryId=${category.id}"
                   class="filter-tag ${param.categoryId == category.id ? 'active' : ''}">
                        ${category.name}
                </a>
            </c:forEach>
        </div>

        <style>
            .filter-tag {
                display: inline-block;
                padding: 4px 12px;
                margin: 0 5px 5px 0;
                background: #f1f3f5;
                color: #495057;
                border-radius: 20px;
                text-decoration: none;
                font-size: 13px;
                transition: all 0.3s;
            }
            .filter-tag:hover {
                background: #e9ecef;
                text-decoration: none;
            }
            .filter-tag.active {
                background: #4a6bff;
                color: white;
            }
        </style>
        <!-- 搜索统计 -->
        <div class="search-stats">
            <c:choose>
                <c:when test="${not empty items}">
                    <div style="display: flex; align-items: center; gap: 10px; flex-wrap: wrap;">
                            <span style="color: #333; font-weight: 500;">
                                关于 "<span style="color: #667eea;"><c:out value="${keyword}"/></span>" 的搜索结果：
                            </span>
                        <span class="badge badge-primary">
                                共找到 ${items.size()} 件物品
                            </span>
                    </div>
                </c:when>
                <c:otherwise>
                    <div style="color: #333;">
                        未找到与 "<span style="color: #667eea;"><c:out value="${keyword}"/></span>" 相关的物品
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- 搜索结果 -->
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
                <!-- 空状态 -->
                <div class="empty-state">
                    <div class="empty-icon">
                        <i class="fas fa-search"></i>
                    </div>
                    <h3 class="empty-title">未找到相关物品</h3>
                    <p class="empty-description">
                        没有找到与 "<c:out value="${keyword}"/>" 相关的物品，建议您：
                    </p>

                    <!-- 搜索建议 -->
                    <div class="search-suggestions">
                        <a href="${pageContext.request.contextPath}/item/search?keyword=手机" class="search-tag">
                            手机
                        </a>
                        <a href="${pageContext.request.contextPath}/item/search?keyword=笔记本电脑" class="search-tag">
                            笔记本电脑
                        </a>
                        <a href="${pageContext.request.contextPath}/item/search?keyword=书籍" class="search-tag">
                            书籍
                        </a>
                        <a href="${pageContext.request.contextPath}/item/search?keyword=自行车" class="search-tag">
                            自行车
                        </a>
                        <a href="${pageContext.request.contextPath}/item/search?keyword=家电" class="search-tag">
                            家电
                        </a>
                        <a href="${pageContext.request.contextPath}/item/search?keyword=衣物" class="search-tag">
                            衣物
                        </a>
                    </div>

                    <!-- 操作按钮 -->
                    <div style="margin-top: 30px;">
                        <a href="${pageContext.request.contextPath}/" class="btn">
                            <i class="fas fa-arrow-left"></i> 返回首页
                        </a>
                        <c:if test="${sessionScope.currentUser != null}">
                            <a href="${pageContext.request.contextPath}/item/publish" class="btn btn-success" style="margin-left: 10px;">
                                <i class="fas fa-plus-circle"></i> 发布物品
                            </a>
                        </c:if>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- 分页 -->
        <c:if test="${not empty items && items.size() > 12}">
            <div class="pagination">
                <a href="#" class="page-link active">1</a>
                <a href="#" class="page-link">2</a>
                <a href="#" class="page-link">3</a>
                <span style="padding: 8px 12px; color: #888;">...</span>
                <a href="#" class="page-link">下一页</a>
            </div>
        </c:if>
    </div>
</main>

<!-- 页脚 -->
<footer class="footer">
    <div class="container footer-content">
        <div class="footer-section">
            <h3>关于搜索</h3>
            <p>支持模糊匹配搜索，快速找到您需要的二手物品。</p>
        </div>
        <div class="footer-section">
            <h3>搜索提示</h3>
            <p>使用更具体的关键词可以获得更精确的结果。</p>
        </div>
    </div>
    <div class="footer-bottom">
        <p>© 2025 二手交易平台. 搜索服务.</p>
    </div>
</footer>
</body>
</html>