<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${sessionScope.currentUser == null}">
    <c:redirect url="${pageContext.request.contextPath}/login.jsp"/>
</c:if>
<!DOCTYPE html>
<html>
<head>
    <title>发布二手物品</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <script>
        function updatePricePreview() {
            const price = document.getElementById('price').value;
            const preview = document.getElementById('price-preview');
            if (price && !isNaN(price) && parseFloat(price) > 0) {
                preview.innerHTML = '¥' + parseFloat(price).toFixed(2);
            } else {
                preview.innerHTML = '¥0.00';
            }
        }

        function updatePreview() {
            const title = document.getElementById('title').value || '物品标题';
            const price = document.getElementById('price').value || '0.00';
            const desc = document.getElementById('description').value || '物品描述...';

            document.getElementById('preview-title').textContent = title;
            document.getElementById('preview-price').textContent = '¥' + parseFloat(price).toFixed(2);
            document.getElementById('preview-desc').textContent = desc;
        }

        function validateForm() {
            const title = document.getElementById('title').value.trim();
            const price = document.getElementById('price').value;
            const description = document.getElementById('description').value.trim();

            if (!title) {
                alert('请输入物品标题');
                return false;
            }

            if (!price || parseFloat(price) <= 0) {
                alert('请输入有效的价格');
                return false;
            }

            if (!description) {
                alert('请输入物品描述');
                return false;
            }

            return true;
        }
    </script>
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
        </div>
    </div>
</header>

<main class="main-content container">
    <!-- 发布步骤 -->
    <div class="card">
        <div class="publish-steps">
            <div class="step active">
                <div class="step-number">1</div>
                <div class="step-label">填写信息</div>
            </div>
            <div class="step">
                <div class="step-number">2</div>
                <div class="step-label">预览确认</div>
            </div>
            <div class="step">
                <div class="step-number">3</div>
                <div class="step-label">发布成功</div>
            </div>
        </div>

        <!-- 页面标题 -->
        <h1 class="page-title" style="text-align: center;">
            <i class="fas fa-plus-circle"></i> 发布闲置物品
        </h1>
        <p class="page-subtitle" style="text-align: center;">
            让闲置物品找到新主人，创造更多价值
        </p>

        <!-- 发布表单 -->
        <form action="${pageContext.request.contextPath}/item" method="post" onsubmit="return validateForm()">
            <input type="hidden" name="action" value="publish">

            <div class="grid-2">
                <!-- 左列：表单 -->
                <div>
                    <div class="card" style="height: 100%;">
                        <h3 style="margin-bottom: 20px; color: #333;">
                            <i class="fas fa-edit"></i> 物品信息
                        </h3>

                        <!-- 物品标题 -->
                        <div class="form-group">
                            <label class="form-label">
                                <i class="fas fa-heading"></i> 物品标题 *
                            </label>
                            <input type="text"
                                   id="title"
                                   name="title"
                                   class="form-control"
                                   placeholder="请输入物品标题，例如：iPhone 13 256GB"
                                   maxlength="100"
                                   onkeyup="updatePreview()"
                                   required>
                            <div style="color: #888; font-size: 14px; margin-top: 5px;">
                                建议包含品牌、型号、规格等信息
                            </div>
                        </div>

                        <!-- 分类选择 -->
                        <div class="form-group">
                            <label class="form-label">
                                <i class="fas fa-tags"></i> 物品分类 *
                            </label>
                            <select name="categoryId" class="form-control" required>
                                <option value="">请选择分类</option>
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.id}" ${param.categoryId == category.id ? 'selected' : ''}>
                                            ${category.name}
                                        <c:if test="${not empty category.description}"> - ${category.description}</c:if>
                                    </option>
                                </c:forEach>
                            </select>
                            <div style="color: #888; font-size: 14px; margin-top: 5px;">
                                选择合适的分类有助于买家快速找到您的物品
                            </div>
                        </div>

                        <!-- 价格 -->
                        <div class="form-group">
                            <label class="form-label">
                                <i class="fas fa-tag"></i> 价格 (¥) *
                            </label>
                            <div style="position: relative;">
                                    <span style="position: absolute; left: 15px; top: 50%; transform: translateY(-50%); color: #666; font-weight: bold;">
                                        ¥
                                    </span>
                                <input type="number"
                                       id="price"
                                       name="price"
                                       class="form-control"
                                       style="padding-left: 40px;"
                                       placeholder="0.00"
                                       step="0.01"
                                       min="0.01"
                                       onkeyup="updatePricePreview(); updatePreview()"
                                       required>
                            </div>

                            <!-- 价格预览 -->
                            <div class="price-preview">
                                <div style="color: #666; margin-bottom: 5px;">预计收入：</div>
                                <div id="price-preview" style="font-size: 24px; font-weight: bold; color: #ff6b6b;">
                                    ¥0.00
                                </div>
                            </div>
                        </div>

                        <!-- 描述 -->
                        <div class="form-group">
                            <label class="form-label">
                                <i class="fas fa-align-left"></i> 物品描述 *
                            </label>
                            <textarea id="description"
                                      name="description"
                                      class="form-control"
                                      placeholder="请详细描述物品的新旧程度、使用情况、有无瑕疵、配件是否齐全等信息..."
                                      rows="8"
                                      maxlength="1000"
                                      onkeyup="updatePreview()"
                                      required></textarea>
                            <div style="color: #888; font-size: 14px; margin-top: 5px;">
                                详细的描述有助于提高交易成功率
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 右列：预览 -->
                <div>
                    <div class="card" style="height: 100%;">
                        <h3 style="margin-bottom: 20px; color: #333;">
                            <i class="fas fa-eye"></i> 发布预览
                        </h3>

                        <!-- 预览卡片 -->
                        <div class="item-card" style="margin-bottom: 20px;">
                            <div class="item-image">
                                <i class="fas fa-box-open"></i>
                            </div>
                            <div class="item-content">
                                <h3 class="item-title" id="preview-title">物品标题</h3>
                                <div class="item-price" id="preview-price">¥0.00</div>
                                <p class="item-description" id="preview-desc" style="min-height: 100px;">
                                    物品描述...
                                </p>
                                <div class="item-meta">
                                    <i class="far fa-clock"></i> 即将发布...
                                </div>
                            </div>
                        </div>

                        <!-- 发布提示 -->
                        <div style="background: #fff9e6; border-radius: 8px; padding: 15px; border-left: 4px solid #ffc107;">
                            <h4 style="color: #856404; margin-bottom: 10px;">
                                <i class="fas fa-lightbulb"></i> 发布提示
                            </h4>
                            <ul style="color: #856404; margin: 0; padding-left: 20px; font-size: 14px;">
                                <li>确保物品描述真实准确</li>
                                <li>设置合理的价格</li>
                                <li>保持联系方式畅通</li>
                                <li>及时回复买家咨询</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 操作按钮 -->
            <div class="form-actions">
                <a href="${pageContext.request.contextPath}/" class="btn btn-secondary" style="flex: 1;">
                    <i class="fas fa-times"></i> 取消
                </a>
                <button type="submit" class="btn" style="flex: 2;">
                    <i class="fas fa-paper-plane"></i> 确认发布
                </button>
            </div>
        </form>
    </div>
</main>

<!-- 页脚 -->
<footer class="footer">
    <div class="container">
        <div class="footer-bottom">
            <p>© 2025 二手交易平台. 发布物品请遵守平台规则.</p>
        </div>
    </div>
</footer>

<script>
    // 初始化预览
    updatePreview();
    updatePricePreview();
</script>
</body>
</html>