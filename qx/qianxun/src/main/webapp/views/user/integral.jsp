<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="date" uri="/WEB-INF/copycat.tld"%>
<html>
<head>
<title>个人资产</title>
</head>
<body>
	<div class="userTitle">
		<h2>千寻积分</h2>
	</div>
	<div class="userCon">
		<div class="user_integral">
			<table>
				<tr>
					<th>时间</th>
					<th>类型</th>
					<th>描述</th>
					<th>数目</th>
					<th>余额</th>
					<!-- <th>事件</th>
					<th>积分余额</th> -->
				</tr>
		<c:forEach items="${integralList}" var="integral" varStatus="status">
			<tr>
				<td><date:human value="${integral.logDate}" /></td>
				<td>
				<c:if test="${integral.type eq 1}">
					玫瑰
				</c:if>
				<c:if test="${integral.type eq 2}">
					钻石
				</c:if>
				<c:if test="${integral.type eq 3}">
					积分
				</c:if>
				</td>
				<td>${integral.discription}</td>
				<td>${integral.num}</td>
				<td>${integral.current}</td>		
			</tr>
		</c:forEach>
			</table>
		</div>
	</div>
</body>
</html>