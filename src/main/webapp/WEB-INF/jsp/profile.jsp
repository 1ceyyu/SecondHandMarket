<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${sessionScope.currentUser == null}">
    <c:redirect url="${pageContext.request.contextPath}/login.jsp"/>
</c:if>
<!DOCTYPE html>
<html>
<head>
    <title>我的物品管理</title>
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
            <a href="${pageContext.request.contextPath}/item/publish">
                <i class="fas fa-plus-circle"></i> 发布物品
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
            <a href="${pageContext.request.contextPath}/auth/logout">
                <i class="fas fa-sign-out-alt"></i> 退出
            </a>
        </div>
    </div>
</header>

<main class="main-content container">
    <!-- 页面标题 -->
    <div class="card">
        <h1 class="page-title">
            <i class="fas fa-boxes"></i> 我的物品管理
        </h1>
        <p class="page-subtitle">在这里管理您发布的所有二手物品</p>

        <!-- 消息提示 -->
        <c:if test="${not empty param.message}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                <c:choose>
                    <c:when test="${param.message == 'publishSuccess'}">物品发布成功！</c:when>
                    <c:when test="${param.message == 'updateSuccess'}">物品更新成功！</c:when>
                    <c:when test="${param.message == 'deleteSuccess'}">物品删除成功！</c:when>
                    <c:otherwise>操作成功！</c:otherwise>
                </c:choose>
            </div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i>
                <c:choose>
                    <c:when test="${param.error == 'publishFailed'}">物品发布失败！</c:when>
                    <c:when test="${param.error == 'updateFailed'}">物品更新失败！</c:when>
                    <c:when test="${param.error == 'deleteFailed'}">物品删除失败！</c:when>
                    <c:otherwise>操作失败！</c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <!-- 快速操作 -->
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> 返回首页
            </a>
            <a href="${pageContext.request.contextPath}/item/publish" class="btn">
                <i class="fas fa-plus"></i> 发布新物品
            </a>
        </div>

        <!-- 物品列表 -->
        <c:choose>
            <c:when test="${not empty myItems}">
                <!-- 统计信息 -->
                <div style="display: flex; gap: 20px; margin-bottom: 25px; flex-wrap: wrap;">
                    <div style="background: #f8f9ff; padding: 15px; border-radius: 8px; flex: 1; min-width: 150px;">
                        <div style="font-size: 24px; font-weight: bold; color: #667eea;">${myItems.size()}</div>
                        <div style="color: #666; font-size: 14px;">总物品数</div>
                    </div>
                    <c:set var="availableCount" value="0" />
                    <c:forEach var="item" items="${myItems}">
                        <c:if test="${item.status == 'available'}">
                            <c:set var="availableCount" value="${availableCount + 1}" />
                        </c:if>
                    </c:forEach>
                    <div style="background: #f0f9f4; padding: 15px; border-radius: 8px; flex: 1; min-width: 150px;">
                        <div style="font-size: 24px; font-weight: bold; color: #20c997;">${availableCount}</div>
                        <div style="color: #666; font-size: 14px;">可交易物品</div>
                    </div>
                    <c:set var="soldCount" value="0" />
                    <c:forEach var="item" items="${myItems}">
                        <c:if test="${item.status == 'sold'}">
                            <c:set var="soldCount" value="${soldCount + 1}" />
                        </c:if>
                    </c:forEach>
                    <div style="background: #fff5f5; padding: 15px; border-radius: 8px; flex: 1; min-width: 150px;">
                        <div style="font-size: 24px; font-weight: bold; color: #ff6b6b;">${soldCount}</div>
                        <div style="color: #666; font-size: 14px;">已售出物品</div>
                    </div>
                </div>

                <!-- 物品网格 -->
                <div class="grid-3">
                    <c:forEach var="item" items="${myItems}">
                        <div class="item-card">
                            <!-- 物品图标 -->
                            <div class="item-image">
                                <i class="fas fa-box-open"></i>
                            </div>

                            <div class="item-content">
                                <!-- 标题和状态 -->
                                <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 10px;">
                                    <h3 class="item-title" style="flex: 1;">
                                        <c:out value="${item.title}"/>
                                    </h3>
                                    <span class="item-status ${item.status == 'available' ? 'status-available' : 'status-sold'}">
                                            ${item.status == 'available' ? '可交易' : '已售出'}
                                    </span>
                                </div>

                                <div class="item-meta">
                                    <span style="color: #666;">
                                        <i class="fas fa-tag"></i>
                                        <c:choose>
                                            <c:when test="${not empty item.categoryName}">
                                                ${item.categoryName}
                                            </c:when>
                                            <c:otherwise>
                                                未分类
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>

                                <!-- 价格 -->
                                <div class="item-price">
                                    ¥<fmt:formatNumber value="${item.price}" pattern="#,##0.00"/>
                                </div>

                                <!-- 描述 -->
                                <p class="item-description">
                                    <c:out value="${item.description}"/>
                                </p>

                                <!-- 操作按钮 -->
                                <div style="display: flex; gap: 10px; margin-top: 20px; flex-wrap: wrap;">
                                    <!-- 修改状态表单 -->
                                    <form action="${pageContext.request.contextPath}/item" method="post" style="flex: 1;">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="itemId" value="${item.id}">
                                        <input type="hidden" name="title" value="<c:out value='${item.title}'/>">
                                        <input type="hidden" name="price" value="<c:out value='${item.price}'/>">
                                        <select name="status" class="form-control" style="margin-bottom: 10px; font-size: 14px;">
                                            <option value="available" ${item.status == 'available' ? 'selected' : ''}>可交易</option>
                                            <option value="sold" ${item.status == 'sold' ? 'selected' : ''}>已售出</option>
                                        </select>
                                        <button type="submit" class="btn" style="width: 100%; padding: 8px;">
                                            <i class="fas fa-edit"></i> 更新状态
                                        </button>
                                    </form>

                                    <!-- 删除表单 -->
                                    <form action="${pageContext.request.contextPath}/item" method="post"
                                          onsubmit="return confirm('确定要删除物品【<c:out value="${item.title}"/>】吗？此操作不可撤销。')">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="itemId" value="${item.id}">
                                        <button type="submit" class="btn btn-danger" style="padding: 8px 15px;">
                                            <i class="fas fa-trash"></i> 删除
                                        </button>
                                    </form>
                                </div>

                                <!-- 发布时间 -->
                                <div class="item-meta">
                                    <i class="far fa-clock"></i>
                                    发布时间：<fmt:formatDate value="${item.publishDate}" pattern="yyyy-MM-dd HH:mm"/>
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
                        <i class="fas fa-box-open"></i>
                    </div>
                    <h3 class="empty-title">您还没有发布任何物品</h3>
                    <p class="empty-description">发布您的第一件闲置物品，让它找到新主人</p>
                    <a href="${pageContext.request.contextPath}/item/publish" class="btn">
                        <i class="fas fa-plus-circle"></i> 立即发布物品
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<!-- 页脚 -->
<footer class="footer">
    <div class="container footer-content">
        <div class="footer-section">
            <h3>二手市场</h3>
            <p>一个安全、便捷的二手物品交易平台，让闲置物品重新发挥价值。</p>
        </div>
        <div class="footer-section">
            <h3>帮助中心</h3>
            <p>客服电话：400-123-4567</p>
            <p>服务时间：9:00-18:00</p>
        </div>
        <div class="footer-section">
            <h3>快速链接</h3>
            <p><a href="${pageContext.request.contextPath}/" style="color: #a0aec0;">首页</a></p>
            <p><a href="${pageContext.request.contextPath}/item/publish" style="color: #a0aec0;">发布物品</a></p>
        </div>
    </div>
    <div class="footer-bottom">
        <p>© 2025 二手交易平台. 版权所有.</p>
    </div>
</footer>
</body>
</html>