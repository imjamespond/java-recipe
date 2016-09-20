<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="date" uri="/WEB-INF/copycat.tld"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<html>
<head>
<title>比赛记录</title>
</head>
<body>
	<div class="userTitle">
		<h2>比赛记录</h2>
	</div>
	<div class="userCon">
		<div class="user_record">
			<table>
				<tr>
					<th>时间</th>
					<!-- <th>比赛场次</th> -->
					<th>名次</th>
				</tr>
		<c:forEach items="${recList}" var="record" varStatus="status">
			<tr>
				<td><date:human value="${record.time}" /></td>
				<%-- <td>${record.mid}</td> --%>
				<td>${record.rank}</td>
			</tr>
		</c:forEach>
			</table>
		</div>
		<div style="text-align: center;">
			<tags:pagination page="${page}" url="/user/record" />
		</div>
	</div>
</body>
</html>