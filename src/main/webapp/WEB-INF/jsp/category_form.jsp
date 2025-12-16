<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${sessionScope.currentUser == null || sessionScope.currentUser.role != 'admin'}">
    <c:redirect url="${pageContext.request.contextPath}/index.jsp?error=PermissionDenied"/>
</c:if>
<!DOCTYPE html>
<html>
<head>
    <title>${empty category ? '添加' : '编辑'}分类 - 二手市场</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        .form-container {
            max-width: 600px;
            margin: 0 auto;
        }
        .form-actions {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }
    </style>
    <script>
        function validateForm() {
            const name = document.getElementById('name').value.trim();
            const description = document.getElementById('description').value;

            if (!name) {
                alert('分类名称不能为空');
                return false;
            }

            if (name.length > 50) {
                alert('分类名称不能超过50个字符');
                return false;
            }

            if (description.length > 200) {
                alert('分类描述不能超过200个字符');
                return false;
            }

            return true;
        }
    </script>
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
                    <a href="${pageContext.request.contextPath}/category/list" class="active">
                        <i class="fas fa-tags"></i> 分类管理
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

<main class="main-content container">
    <div class="form-container">
        <div class="card">
            <h1 class="page-title" style="text-align: center;">
                <i class="fas fa-tags"></i> ${empty category ? '添加新分类' : '编辑分类'}
            </h1>

            <form action="${pageContext.request.contextPath}/category/save" method="post" onsubmit="return validateForm()">
                <input type="hidden" name="action" value="${empty category ? 'add' : 'update'}">
                <c:if test="${not empty category}">
                    <input type="hidden" name="id" value="${category.id}">
                </c:if>

                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-tag"></i> 分类名称 *
                    </label>
                    <input type="text"
                           id="name"
                           name="name"
                           class="form-control"
                           placeholder="请输入分类名称（不超过50个字符）"
                           value="${category.name}"
                           required
                           maxlength="50">
                    <div style="color: #666; font-size: 13px; margin-top: 5px;">
                        分类名称将显示在物品发布和搜索页面
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">
                        <i class="fas fa-align-left"></i> 分类描述
                    </label>
                    <textarea id="description"
                              name="description"
                              class="form-control"
                              placeholder="请输入分类描述（可选，不超过200个字符）"
                              rows="4"
                              maxlength="200">${category.description}</textarea>
                    <div style="color: #666; font-size: 13px; margin-top: 5px;">
                        描述该分类包含的物品类型，帮助用户正确选择分类
                    </div>
                </div>

                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/category/list" class="btn btn-secondary" style="flex: 1;">
                        <i class="fas fa-times"></i> 取消
                    </a>
                    <button type="submit" class="btn" style="flex: 2;">
                        <i class="fas fa-save"></i> ${empty category ? '添加分类' : '更新分类'}
                    </button>
                </div>
            </form>
        </div>
    </div>
</main>
</body>
</html>