<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../common/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
	<style type="text/css">
		body {
			padding-top: 40px;
			padding-bottom: 40px;
			background-color: #f5f5f5;
		}

		.form-signin {
			max-width: 300px;
			padding: 19px 29px 29px;
			margin: 0 auto 20px;
			background-color: #fff;
			border: 1px solid #e5e5e5;
			-webkit-border-radius: 5px;
			-moz-border-radius: 5px;
			border-radius: 5px;
			-webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
			-moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
			box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
		}

		.form-signin .form-signin-heading,
		.form-signin .checkbox {
			margin-bottom: 10px;
		}

		.form-signin input[type="text"],
		.form-signin input[type="password"] {
			font-size: 16px;
			height: auto;
			margin-bottom: 15px;
			padding: 7px 9px;
		}

	</style>

</head>
<div class="container">

	<form class="form-signin" name='f' action='/j_spring_security_check' method='POST'>
		<h2 class="form-signin-heading">用户登录</h2>

		<input type="text" class="input-block-level" placeholder="Username" name="j_username" value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}">
		<input type="password" class="input-block-level" placeholder="Password" name='j_password'>
		<br/>
        <label class="checkbox">
			<input type="checkbox" value="remember-me" name='_spring_security_remember_me'> 记住我
		</label>
		<c:if test="${not empty sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}">
			<div class="alert">${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message}</div>
		</c:if>
		<button class="btn btn-large btn-primary" type="submit">登录</button>
		<button class="btn btn-large btn-primary" type="button" onclick="window.location.href='registry/?backurl='+window.location.href; ">注册</button>
	</form>

</div>