<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>防沉迷认证</title>
</head>
<body>
	<div class="userTitle">
		<h2>防沉迷认证</h2>
	</div>
	<div class="userCon">
		<div class="user_fcm">
			<c:if test="${empty user.name}">
				<div class="overhide">
					<label for="">姓名：</label>
					<input type="text" id="realname" name="realname" />
					<div class="error"></div>
				</div>
				<div class="overhide">
					<label for="">身份证：</label>
					<input type="text" id="identity" name="identity" />
					<div class="error"></div>
				</div>
				<button type="button" class="orgBtn subIdBtn" onclick="userFn.submitIdentity()">提交</button>
			</c:if>
			<c:if test="${!empty user.name}">
				<div class="user_onlyText">
					<div class="formRow30">您已通过防沉迷验证！</div>
					<div class="formRow30">姓&emsp;名：${user.name}</div>
					<div>身份证：${user.identity}</div>
				</div>
			</c:if>
		</div>
	</div>
</body>
</html>