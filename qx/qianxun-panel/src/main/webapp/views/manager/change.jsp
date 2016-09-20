<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<html>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<link type="text/css" rel="stylesheet" href="/styles/bootstrap/css/bootstrap.min.css" />
<head>
<title>修改密码</title>
</head>
<body>
    <ol class="breadcrumb">
		<li><a href="/manager/change/${sessionScope.id}">修改密码</a></li>
	</ol>
    <div style="width:350px;margin:0 auto;">
	<form class="form-signin" role="form" action="/manager/password" method = "post">
		<input type="password" name="oldPassword" class="form-control" placeholder="旧密码" style="margin-bottom:10px;">
		<input type="password" name="newPasswordf" class="form-control" placeholder="新密码" style="margin-bottom:10px;">
		<input type="password" name="newPasswords" class="form-control" placeholder="重复新密码">
		<c:if test="${!empty requestScope.error}"><span style="color:red">${requestScope.error}</span></c:if>
		<button style="margin-top:10px;width:100px;" class="btn btn-lg btn-primary btn-block" type="submit">修改</button>
	</form>
	</div>
	</div>
</body>
</html>