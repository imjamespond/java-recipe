<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<link type="text/css" rel="stylesheet" href="/styles/bootstrap/css/bootstrap.min.css" />
<head>
<title>登录</title>
</head>
<body>
	<div class="container">
		<div class="navbar navbar-default" role="navigation">
			<div class="navbar-header">
				<a class="navbar-brand" href="/">千寻游戏-后台管理</a>
			</div>
			<span style="display:block;clear:both;"></span>
		</div>
	    <div class="well" style="width:400px;margin:70px auto 0;padding:25px;">
			<form:form modelAttribute="manager" action="/manager/login"
							method="post" class="form-horizontal">
				<h3 class="form-signin-heading text-center">管理员登录</h3>
				<div class="form-group" style="margin-top:30px;">
					<label for="email" class="control-label col-xs-2">邮箱</label>
					<div class="col-xs-10">
						<input type="text" id="email" name="email" class="form-control" placeholder="登录邮箱" style="margin-bottom:5px;">
						<form:errors path="email" style="color:red;font-size: 14px;margin-bottom:5px" />
					</div>
				</div>
				<div class="form-group">
					<label for="password" class="control-label col-xs-2">密码</label>
					<div class="col-xs-10">
						<input type="password" name="password" class="form-control" placeholder="登陆密码">
						<form:errors path="password" style="color:red;font-size: 14px" />
					</div>
				</div>
				<div class="text-center">
					<button style="margin-top:10px;width:100px;display:inline;" class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
				</div>
			</form:form>
		</div>
	</div>
</body>
</html>