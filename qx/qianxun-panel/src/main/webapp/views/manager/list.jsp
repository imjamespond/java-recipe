<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="copycat" uri="http://www.copycat.org/tags"%>
<html>
<head>
<title>权限管理</title>

</head>
<body>
    <ol class="breadcrumb">
		<li><a href="/manager/list">权限管理</a></li>
	</ol>
	<div style="margin-bottom: 10px;">
		<span style="padding: 0 50px;float:right"> <a
			href="/manager/create" class="btn btn-primary">添加管理员</a>
		</span>
		<span style="clear:both;display: block;"></span>
	</div>
	
	<table class="table table-hover" style="margin-top: 10px">
		<tr>
	        <th>管理员名称</th>
	        <th>email</th>
	        <th>是否启用</th>
	        <th>角色</th>
	        <th>操作</th>
        </tr>
		<c:forEach items="${managerList}" var="manager">
	        <tr>
		        <td>${manager.username}</td>
		        <td>${manager.email}</td>
		        <td>
		        	<c:if test="${manager.state eq 1}">是</c:if>
		        	<c:if test="${manager.state ne 1}">否</c:if>
		        </td>
		        <td style="width:50%;word-break: break-all;">${manager.roles}</td>
				<td>
				    <a href="/manager/edit/${manager.id}">编辑</a>
				    <a href="/manager/abandon/${manager.id}" onclick="return confirm('确定要禁用此用户吗？')">禁用</a>
				    <a href="/manager/delete/${manager.id}" onclick="return confirm('确定要删除此用户吗？')">删除</a>
				</td>
			</tr>
        </c:forEach>
	</table>
	<div style="text-align: center;">
		<tags:pagination page="${page}" url="/manager/list" />
	</div>
</body>
</html>