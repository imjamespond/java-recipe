<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="copycat" uri="http://www.copycat.org/tags"%>
<html>
<head>
<title>用户管理</title>

</head>
<body>
    <ol class="breadcrumb">
		<li><a href="/user/list">用户管理</a></li>
	</ol>
	<!-- <div style="margin-bottom:10px;overflow:hidden;">
		<span class="pull-right"> <a
			href="javascript:void(0);" class="btn btn-primary" onclick="fix()">修正用户邮箱</a>
		</span>
	</div> -->
	<table class="table table-hover" style="margin-top: 10px">
		<tr>
	        <th>用户名称</th>

	        <th>注册时间</th>
	        <th>操作</th>
        </tr>
		<c:forEach items="${userList}" var="user">
	        <tr>
		        <td>${user.username}</td>
		        <!-- <td><img src="${imageURL}/avatar/${user.id}.jpg"></td> -->
		        <td><copycat:format pattern="yyyy年MM月dd日 HH:mm:ss" value="${user.registerTime}"/></td>
				<td>
				    <!-- <a href="/user/deleteAvatar/${user.id}" onclick="return confirm('确定要删除用户的头像吗？')">删除头像</a> -->
				    <a href="/user/delete/${user.id}" onclick="return confirm('确定删除？')">删除</a>
				    <a href="/user/reset/${user.id}" onclick="return confirm('确定？')">重置密码(123123)</a>
				</td>
			</tr>
        </c:forEach>
	</table>
	<div style="text-align: center;">
		<tags:pagination page="${page}" url="/user/list" />
	</div>
<script type="text/javascript">
	function fix(){
		$.ajax({
			url : "/user/fix",
			type : "post",
			success : function(data){
				if(data == "1"){
					alert("修正完成");
				}else{
					var msg = "以下邮箱在修正时存在冲突：" + data;
					alert(msg);
				}
			}
		});
	}
</script>
</body>
</html>