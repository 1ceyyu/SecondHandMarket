<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>用户注册</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        .register-container {
            max-width: 400px;
            margin: 40px auto;
        }
        .register-header {
            text-align: center;
            margin-bottom: 30px;
        }
        .register-header h1 {
            color: #333;
            margin-bottom: 10px;
        }
        .register-header p {
            color: #666;
        }
        .password-strength {
            margin-top: 5px;
            height: 5px;
            background-color: #e0e0e0;
            border-radius: 3px;
            overflow: hidden;
        }
        .strength-bar {
            height: 100%;
            width: 0%;
            transition: width 0.3s ease;
        }
        .password-requirements {
            margin-top: 10px;
            color: #666;
            font-size: 13px;
        }
        .requirement {
            margin-bottom: 3px;
        }
        .requirement.valid {
            color: #20c997;
        }
    </style>
    <script>
        function validatePassword() {
            const password = document.getElementById('password').value;
            const strengthBar = document.querySelector('.strength-bar');
            const requirements = {
                length: password.length >= 6,
                uppercase: /[A-Z]/.test(password),
                lowercase: /[a-z]/.test(password),
                number: /[0-9]/.test(password)
            };

            let strength = 0;
            if (requirements.length) strength += 25;
            if (requirements.uppercase) strength += 25;
            if (requirements.lowercase) strength += 25;
            if (requirements.number) strength += 25;

            strengthBar.style.width = strength + '%';

            // 设置颜色
            if (strength < 50) {
                strengthBar.style.backgroundColor = '#ff6b6b';
            } else if (strength < 75) {
                strengthBar.style.backgroundColor = '#ffa94d';
            } else {
                strengthBar.style.backgroundColor = '#20c997';
            }

            // 更新要求提示
            document.querySelector('.req-length').className =
                'requirement' + (requirements.length ? ' valid' : '');
            document.querySelector('.req-case').className =
                'requirement' + (requirements.uppercase && requirements.lowercase ? ' valid' : '');
            document.querySelector('.req-number').className =
                'requirement' + (requirements.number ? ' valid' : '');
        }

        function validateForm() {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            if (password !== confirmPassword) {
                alert('两次输入的密码不一致！');
                return false;
            }

            return true;
        }
    </script>
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
            <a href="${pageContext.request.contextPath}/login.jsp">
                <i class="fas fa-sign-in-alt"></i> 已有账号？登录
            </a>
        </div>
    </div>
</div>

<div class="main-content">
    <div class="container">
        <div class="register-container">
            <div class="register-header">
                <h1>创建新账户</h1>
                <p>加入我们，开始您的二手物品交易之旅</p>
            </div>

            <!-- 错误提示 -->
            <c:if test="${param.error == 'UsernameTaken'}">
                <div class="alert alert-error">
                    <i class="fas fa-exclamation-circle"></i>
                    注册失败：该用户名已被占用。
                </div>
            </c:if>

            <!-- 注册表单 -->
            <div class="card">
                <form action="${pageContext.request.contextPath}/auth/register"
                      method="post"
                      onsubmit="return validateForm()">

                    <div class="form-group">
                        <label class="form-label">
                            <i class="fas fa-user"></i> 用户名
                        </label>
                        <input type="text"
                               name="username"
                               class="form-control"
                               placeholder="请输入用户名（4-20个字符）"
                               required
                               minlength="4"
                               maxlength="20">
                        <div style="color: #666; font-size: 13px; margin-top: 5px;">
                            用户名将显示在您的所有交易中
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">
                            <i class="fas fa-lock"></i> 密码
                        </label>
                        <input type="password"
                               id="password"
                               name="password"
                               class="form-control"
                               placeholder="请输入密码（至少6位）"
                               required
                               minlength="6"
                               onkeyup="validatePassword()">

                        <div class="password-strength">
                            <div class="strength-bar"></div>
                        </div>

                        <div class="password-requirements">
                            <div class="requirement req-length">
                                <i class="fas fa-circle" style="font-size: 6px; margin-right: 5px;"></i>
                                至少6个字符
                            </div>
                            <div class="requirement req-case">
                                <i class="fas fa-circle" style="font-size: 6px; margin-right: 5px;"></i>
                                包含大小写字母
                            </div>
                            <div class="requirement req-number">
                                <i class="fas fa-circle" style="font-size: 6px; margin-right: 5px;"></i>
                                包含数字
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="form-label">
                            <i class="fas fa-lock"></i> 确认密码
                        </label>
                        <input type="password"
                               id="confirmPassword"
                               class="form-control"
                               placeholder="请再次输入密码"
                               required
                               minlength="6">
                    </div>

                    <div class="form-group">
                        <label style="display: flex; align-items: flex-start; cursor: pointer;">
                            <input type="checkbox" required style="margin-right: 10px; margin-top: 3px;">
                            <span style="color: #666; font-size: 14px;">
                                    我已阅读并同意
                                    <a href="#" style="color: #667eea;">《用户协议》</a>
                                    和
                                    <a href="#" style="color: #667eea;">《隐私政策》</a>
                                </span>
                        </label>
                    </div>

                    <button type="submit" class="btn" style="width: 100%; font-size: 16px;">
                        <i class="fas fa-user-plus"></i> 注册账户
                    </button>
                </form>

                <div style="text-align: center; margin-top: 20px; padding-top: 20px; border-top: 1px solid #eee;">
                    <p style="color: #666; margin-bottom: 10px;">已有账户？</p>
                    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-secondary" style="width: 100%;">
                        <i class="fas fa-sign-in-alt"></i> 立即登录
                    </a>
                </div>
            </div>

            <div style="text-align: center; margin-top: 20px; color: #666;">
                <p><i class="fas fa-shield-alt" style="color: #20c997;"></i> 我们承诺保护您的隐私安全</p>
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