<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>我的资料</title>
</head>
<body>
	<div class="userTitle">
		<h2>我的资料</h2>
	</div>
	<div class="userCon">
		<div class="user_myInfo">
			<div class="user_myImg formRow20">
				<img src="${imageURL}/avatar/${user.id}-150.jpg" onerror="this.src='${mainUrl}/images/default.png'">
			</div>
			<div class="formRow10">
				<span>帐号：</span>
				<span>${user.username}</span>
			</div>
			<div>
				<c:if test="${empty user.email}">
					<span>邮箱：</span>
					<span>您暂未绑定邮箱</span>
					<a href="/user/bind">前往绑定</a>
				</c:if>
				<c:if test="${!empty user.email}">
					<span>邮箱：</span>
					<span>${user.email}</span>
				</c:if>
			</div>
		</div>
	</div>
<c:if test="${not empty update && update}">
	<script type="text/javascript">
	$(document).ready(function(){
	    //Check if the current URL contains '#'
	    if(document.URL.indexOf("#")==-1){
	        // Set the URL to whatever it was plus "#".
	        url = document.URL+"#";
	        location = "#";

	        //Reload the page
	        window.location.reload();
	    }
	});
	</script>
</c:if>
</body>
</html>