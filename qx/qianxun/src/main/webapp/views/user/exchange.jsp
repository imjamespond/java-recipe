<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="date" uri="/WEB-INF/copycat.tld"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<html>
<head>
<title>积分详情</title>
</head>
<body>
	<div class="userTitle">
		<h2>积分详情</h2>
	</div>
	<div class="userCon">
		<div class="user_exchange">
			<table>
				<tr>
					<th>编号</th>
					<th>时间</th>
					<th>兑换类型</th>
					<th>兑换物品</th>
					<th>数量</th>
					<th>订单</th>
					<th>备注</th>
				</tr>
				<c:forEach items="${list}" var="item" varStatus="status">
					<tr>
						<td>${item.id}</td>
						<td><date:human value="${item.time_}" /></td>
						<td>
						<c:if test="${item.type eq 1}">
							玫瑰
						</c:if>
						<c:if test="${item.type eq 2}">
							钻石
						</c:if>
						<c:if test="${item.type eq 3}">
							积分
						</c:if>
						</td>
						<td>
						<c:if test="${item.item eq 1}">
							京东100元卡
						</c:if>
						</td>
						<td>${item.num}</td>
						<td>${item.invoice}</td>
						<td>${item.remark}</td>		
					</tr>
				</c:forEach>
			</table>
		</div>
		<div style="text-align: center;">
			<tags:pagination page="${page}" url="/exchange/list" />
		</div>
	</div>
</body>
</html>