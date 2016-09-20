<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>账号安全</title>
</head>
<body>
	<div class="userTitle">
		<h2>账号安全</h2>
	</div>
	<div class="userCon">
		<div class="user_bind">
			<c:if test="${empty user.email}">
				<form name="passwordForm">
					<div class="overhide">
						<label>邮箱：</label>
						<input type="text" id="email" name="email" />
						<button type="button" class="orgBtn getCpBtn" onclick="userFn.getCaptcha(this)">获取验证码</button>
						<div class="error"></div>
					</div>
					<div class="overhide">
						<label>验证码：</label>
						<input type="text" name="captcha" id="captcha">
						<div class="error"></div>
					</div>
				</form>
				<button type="button" class="orgBtn subCpBtn" onclick="userFn.checkCaptcha()">提交</button>
			</c:if>
			<c:if test="${!empty user.email}">
				<div class="user_onlyText">
					<span>您已成功绑定邮箱：${user.email}</span>
				</div>
			</c:if>
		</div>
	</div>
</body>
</html>