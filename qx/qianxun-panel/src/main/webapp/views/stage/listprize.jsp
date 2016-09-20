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
		<li><a href="/stage/list">千寻小公主</a></li>
		<li class="active">活动列表</li>
	</ol>
	<div style="margin-bottom: 10px;">
		<span style="padding: 0 50px;float:right"> <a
			href="/stage/create" class="btn btn-primary">创建新活动</a>
		</span>
		<span style="clear:both;display: block;"></span>
	</div>

	<table class="table table-hover" style="margin-top: 10px">
		<tr>
	        <th>活动期数</th>
	        <th>活动描述</th>
	        <th>活动状态</th>
	        <th>获奖人姓名</th>
	        <th>获奖人电话</th>
	        <th>获奖人地址</th>
	        <th>获奖人游戏昵称</th>
	        <th>操作</th>
        </tr>
		<c:forEach items="${applePrizeList}" var="applePrize">
	        <tr>
		        <td>${applePrize.prizeid}</td>
		        <td>${applePrize.description}</td>
		        <td>
		        	<c:if test="${applePrize.state eq 0 }">进行中</c:if>
		        	<c:if test="${applePrize.state eq 1 }">等待领奖</c:if>
		        	<c:if test="${applePrize.state eq 2 }">结束</c:if>
		        	<c:if test="${applePrize.state eq 3 }">已创建未部署</c:if>
	        	</td>
		        <td>${applePrize.name}</td>
		        <td>${applePrize.phone}</td>
		        <td>${applePrize.address}</td>
		        <td>${applePrize.nickname}</td>
				<td>
					<c:if test="${applePrize.state eq 0}">
						<a href="/stage/setPrize/${applePrize.id }">设置该期活动获奖人</a>
					</c:if>
					<c:if test="${applePrize.state eq 3}">
						<a href="/stage/deploy/${applePrize.id }">部署该期活动</a>
					</c:if>
				</td>
			</tr>
        </c:forEach>
	</table>
	<div style="text-align: center;">
		<tags:pagination page="${page}" url="/manager/list" />
	</div>
</body>
</html>