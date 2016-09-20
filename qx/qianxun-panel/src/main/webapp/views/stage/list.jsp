<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="copycat" uri="http://www.copycat.org/tags"%>
<html>
<head>
<title>千寻小公主管理</title>
<link type="text/css" rel="stylesheet" href="/styles/bootstrap/css/bootstrap.min.css" />
<script type="text/javascript" src="/styles/bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
    <ol class="breadcrumb">
		<li><a href="/stage/list">千寻小公主管理</a></li>
	</ol>
	<div style="margin-bottom: 10px;">
		<span style="padding: 0 50px;float:right"> <a
			href="/stage/listPrize" class="btn btn-primary">活动列表</a>
		</span>
		<span style="clear:both;display: block;"></span>
	</div>
	
	<table class="table table-hover" style="margin-top: 10px">
		<tr>
	        <th>用户名</th>
	        <th>游戏昵称</th>
	        <th>活动头像</th>
	        <th>游戏背景图</th>
	        <th>操作</th>
        </tr>
		<c:forEach items="${stageList}" var="stage">
	        <tr>
		        <td>${stage.username}</td>
		        <td>${stage.nickname}</td>
		        <td><img src="${imageURL}/stage/${stage.uid}.jpg"></td>
		        <td><img src="${imageURL}/gameimage/${stage.uid}.jpg"></td>
				<td>
					<a href="/user/deleteStageimage/${stage.uid}" onclick="return confirm('确定要删除用户的头像吗？')">删除头像</a>
					<a href="/user/deleteGameimage/${stage.uid}" onclick="return confirm('确定要删除用户的头像吗？')">删除头像</a>
				    <a href="/stage/abandon/${stage.id}" onclick="return confirm('确定要禁用此用户吗？')">禁用</a>
				</td>
			</tr>
        </c:forEach>
	</table>
	<div style="text-align: center;">
		<tags:pagination page="${page}" url="/manager/list" />
	</div>
</body>
</html>