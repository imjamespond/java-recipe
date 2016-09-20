<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="date" uri="/WEB-INF/copycat.tld"%>
<html>
<head>
<title>千寻游戏-飞行棋玩家管理</title>
<style type="text/css">
#vote width=20px;
</style>
</head>
<body>
<div class="wrapper">
	<div style="overflow:hidden">
		<h3>飞行棋玩家管理</h3>
		<a href="/flyuser/listByLogindate">登录时间↓</a>/
		<a href="/flyuser/listByTotaldays">登录天数↓</a>/
		<a href="/flyuser/listByTotaltime">线上时间↓</a>/
		<a href="/flyuser/listByConsecutive">连续登录↓</a>
		<table class="table table-bordered">
			<tr>
				<td>id</td>
				<td>username</td>
				<td>nickname</td>
				<td>gender</td>
				<!-- <td>rose</td>
				<td>gems</td>
				<td>credit</td>
				<td>score</td>
				<td>charm</td> -->
				<td>consecutive</td>
				<td>totaldays</td>
				<td>totaltime(时:分)</td>
				<td>login date</td>
				<td>create date</td>
			</tr>
			<c:forEach items="${list}" var="item">
				<tr>
					<td><a href="#" class="id_cls">${item.id}</a></td>
					<td>${item.username}</td>
					<td>${item.nickname}</td>
					<td>${item.gender}</td>
					<td>${item.consecutive}</td>
					<td>${item.totaldays}</td>
					<td><span class="totaltime">${item.totaltime}</span></td>		
					<td><date:human value="${item.loginDate}" /></td>
					<td><date:human value="${item.createdate}" /></td>
				</tr>
			</c:forEach>
		</table>
		<div style="text-align: center;">
			<tags:pagination page="${page}" url="${pageUrl}" />
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	$(".totaltime").each(function(){
		var mills = parseInt($(this).html());
		var hour = Math.floor(mills/1000/3600);
		var minute = Math.floor(mills/1000/60)-hour*60;
		$(this).html(hour+":"+minute);
	});
});
</script>
</body>
</html>