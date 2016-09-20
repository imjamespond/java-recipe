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

	<form class="form-signin" name='f' action='/security/registry/post' method='POST'>
		<h2 class="form-signin-heading">用户注册</h2>

		用户名:
		<input type="text" class="input-block-level" name="j_username" >
		</br>
		密码:
		<input type="password" class="input-block-level" name='j_password'>
		</br>
		姓名:
		<input type="text" class="input-block-level" name='realname'>
		</br>
		联系方式:
		<input type="text" class="input-block-level" name='contact'>
		</br>
		邮箱:
		<input type="text" class="input-block-level" name='email'>	
		</br>					
		验证码:
		<input type="text" class="input-block-level" name='verify'>
		<img src="${pageContext.request.contextPath }/security/jcaptcha/generate" border="0" id="imageValidate">
		<a href="javascript:void(0);" onclick="document.getElementById('imageValidate').src='${pageContext.request.contextPath }/security/jcaptcha/generate?' + Math.random();">  
看不清,点这换一张</a>
		<br/>

		<button class="btn btn-large btn-primary" type="submit">注册</button>
	</form>

</div>