<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>经营分析系统</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />

</head>
<body>
	<div class="col-lg-12">
		<div class="well center-block" >
			<c:if test="${not empty session}">
			  	<h3>当前登录用户:<span class="label label-info">${session.username}</span></h3>
				<a href="/signout.get" class="btn btn-default btn-lg btn-block" role="button">退出</a>
			</c:if>
			<c:if test="${empty session}">
			  	<a href="/signin" class="btn btn-primary btn-lg btn-block" role="button">登录</a> 
				<a href="/signup" class="btn btn-default btn-lg btn-block" role="button">新用户</a>
			</c:if>
		</div>
	</div>

	<script type="text/javascript">
		$(document).ready(function() {
			//location.href = index;
		});
	</script>
</body>
</html>