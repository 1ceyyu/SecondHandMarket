<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>用户登录</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .login-container {
            max-width: 400px;
            margin: 60px auto;
        }
        .login-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .login-header h1 {
            color: #333;
            margin-bottom: 10px;
        }
        .login-header p {
            color: #666;
        }
        .social-login {
            display: flex;
            gap: 15px;
            margin-bottom: 20px;
        }
        .social-btn {
            flex: 1;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 8px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        .social-btn:hover {
            background-color: #f5f7fa;
        }
        .divider {
            text-align: center;
            margin: 20px 0;
            position: relative;
        }
        .divider:before {
            content: "";
            position: absolute;
            top: 50%;
            left: 0;
            right: 0;
            height: 1px;
            background: #e0e0e0;
        }
        .divider span {
            background: white;
            padding: 0 20px;
            color: #666;
        }
        .forgot-password {
            text-align: center;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<div class="header">
    <div class="container nav-container">
        <a href="${pageContext.request.contextPath}/" class="logo">
            <i class="fas fa-recycle"></i>
            二手市场
        </a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/">
                <i class="fas fa-home"></i> 返回首页
            </a>
        </div>
    </div>
</div>

<div class="main-content">
    <div class="container">
        <div class="login-container">
            <div class="login-header">
                <h1>欢迎回来</h1>
                <p>登录您的账户，开始二手物品交易</p>
            </div>

            <!-- 消息提示 -->
            <c:if test="${param.error != null}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i>
                    用户名或密码错误，或会话已过期。
                </div>
            </c:if>
            <c:if test="${param.message == 'RegisterSuccess'}">
                <div class="alert alert-success">
                    <i class="fas fa-check-circle"></i>
                    注册成功，请登录！
                </div>
            </c:if>

            <!-- 登录表单 -->
            <div class="card">
                <form action="${pageContext.request.contextPath}/auth/login" method="post">
                    <div class="form-group">
                        <label class="form-label">
                            <i class="fas fa-user"></i> 用户名
                        </label>
                        <input type="text"
                               name="username"
                               class="form-control"
                               placeholder="请输入用户名"
                               required>
                    </div>

                    <div class="form-group">
                        <label class="form-label">
                            <i class="fas fa-lock"></i> 密码
                        </label>
                        <input type="password"
                               name="password"
                               class="form-control"
                               placeholder="请输入密码"
                               required>
                    </div>

                    <div class="form-group" style="display: flex; align-items: center; justify-content: space-between;">
                        <div>
                            <input type="checkbox" id="remember" name="remember">
                            <label for="remember" style="margin-left: 5px; color: #666;">记住我</label>
                        </div>
                    </div>

                    <button type="submit" class="btn" style="width: 100%; font-size: 16px;">
                        <i class="fas fa-sign-in-alt"></i> 登录
                    </button>
                </form>

                <div class="divider">
                    <span>还没有账号？</span>
                </div>

                <div style="text-align: center;">
                    <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-secondary" style="width: 100%;">
                        <i class="fas fa-user-plus"></i> 立即注册
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<footer class="footer">
    <div class="container">
        <p style="text-align: center; color: #a0aec0;">
            © 2025 二手物品交易平台. 使用即表示您同意我们的服务条款。
        </p>
    </div>
</footer>
</body>
</html>