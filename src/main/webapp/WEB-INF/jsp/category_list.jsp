<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:if test="${sessionScope.currentUser == null || sessionScope.currentUser.role != 'admin'}">
    <c:redirect url="${pageContext.request.contextPath}/index.jsp?error=PermissionDenied"/>
</c:if>
<!DOCTYPE html>
<html>
<head>
    <title>分类管理 - 二手市场</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .data-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .data-table th {
            background: #f8f9fa;
            padding: 12px;
            text-align: left;
            border-bottom: 2px solid #dee2e6;
            color: #495057;
            font-weight: 600;
        }
        .data-table td {
            padding: 12px;
            border-bottom: 1px solid #dee2e6;
            vertical-align: middle;
        }
        .data-table tr:hover {
            background-color: #f8f9fa;
        }
        .btn-small {
            padding: 4px 10px;
            font-size: 13px;
            margin-right: 5px;
        }
        .category-count {
            display: inline-block;
            background: #e9ecef;
            color: #495057;
            padding: 2px 8px;
            border-radius: 10px;
            font-size: 12px;
            margin-left: 5px;
        }
    </style>
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
                    <a href="${pageContext.request.contextPath}/item/publish">
                        <i class="fas fa-plus-circle"></i> 发布物品
                    </a>
                    <c:if test="${sessionScope.currentUser.role == 'admin'}">
                        <a href="${pageContext.request.contextPath}/category/list" class="active">
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
        <h1 class="page-title">
            <i class="fas fa-tags"></i> 商品分类管理
        </h1>
        <p class="page-subtitle">管理商品分类，方便物品归类</p>

        <!-- 消息提示 -->
        <c:if test="${not empty param.message}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i>
                <c:choose>
                    <c:when test="${param.message == 'addSuccess'}">分类添加成功！</c:when>
                    <c:when test="${param.message == 'updateSuccess'}">分类更新成功！</c:when>
                    <c:when test="${param.message == 'deleteSuccess'}">分类删除成功！</c:when>
                </c:choose>
            </div>
        </c:if>
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">
                <i class="fas fa-exclamation-circle"></i>
                <c:choose>
                    <c:when test="${param.error == 'invalidId'}">分类ID无效！</c:when>
                    <c:when test="${param.error == 'operationFailed'}">操作失败！</c:when>
                    <c:otherwise><c:out value="${param.error}"/></c:otherwise>
                </c:choose>
                <c:if test="${not empty param.msg}">: <c:out value="${param.msg}"/></c:if>
            </div>
        </c:if>

        <!-- 操作按钮 -->
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> 返回首页
            </a>
            <a href="${pageContext.request.contextPath}/category/add" class="btn">
                <i class="fas fa-plus"></i> 添加分类
            </a>
        </div>

        <!-- 分类列表 -->
        <c:choose>
            <c:when test="${not empty categories}">
                <table class="data-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>分类名称</th>
                        <th>描述</th>
                        <th>创建时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="category" items="${categories}">
                        <tr>
                            <td>${category.id}</td>
                            <td>
                                <strong>${category.name}</strong>
                                <a href="${pageContext.request.contextPath}/item/by-category?id=${category.id}"
                                   class="category-count" title="查看该分类下的物品">
                                    <i class="fas fa-box"></i> 查看物品
                                </a>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty category.description}">
                                        ${category.description}
                                    </c:when>
                                    <c:otherwise>
                                        <span style="color: #999; font-style: italic;">无描述</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <fmt:formatDate value="${category.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/category/edit?id=${category.id}"
                                   class="btn btn-small">
                                    <i class="fas fa-edit"></i> 编辑
                                </a>
                                <form action="${pageContext.request.contextPath}/category/delete"
                                      method="post"
                                      style="display: inline;"
                                      onsubmit="return confirm('确定要删除分类【${category.name}】吗？此操作不可撤销！')">
                                    <input type="hidden" name="id" value="${category.id}">
                                    <button type="submit" class="btn btn-small btn-danger">
                                        <i class="fas fa-trash"></i> 删除
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-icon">
                        <i class="fas fa-tags"></i>
                    </div>
                    <h3 class="empty-title">暂无分类</h3>
                    <p class="empty-description">请先添加商品分类</p>
                    <a href="${pageContext.request.contextPath}/category/add" class="btn">
                        <i class="fas fa-plus-circle"></i> 添加分类
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<footer class="footer">
    <div class="container">
        <p style="text-align: center; color: #a0aec0;">
            © 2025 二手物品交易平台. 管理员：${sessionScope.currentUser.username}
        </p>
    </div>
</footer>

<script>
    // 防止表单重复提交
    document.addEventListener('DOMContentLoaded', function() {
        const forms = document.querySelectorAll('form');
        forms.forEach(form => {
            form.addEventListener('submit', function(e) {
                const submitBtn = this.querySelector('button[type="submit"]');
                if (submitBtn) {
                    submitBtn.disabled = true;
                    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> 处理中...';
                }
            });
        });
    });
</script>
</body>
</html>