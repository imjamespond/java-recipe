<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="date" uri="/WEB-INF/copycat.tld"%>
<html>
<head>
<title>千寻游戏-明细管理</title>
<style type="text/css">
#vote width=20px;
</style>
</head>
<body>
<div class="wrapper">
	<div style="overflow:hidden">
		<h3>明细管理</h3>
<p id="display"></p>
		<table class="table table-bordered">
			<tr>
				<td>id</td>
				<td>uid</td>
				<td>类型</td>
				<td>数量</td>
				<td>时间</td>
				<td>备注</td>
			</tr>
			<c:forEach items="${list}" var="item">
				<tr>
					<td><a href="#" class="id_cls">${item.id}</a></td>
					<td><a href="#" id="uid_${item.id}" class="uid_cls">${item.uid}</a></td>
					<td>${item.type}</td>
					<td>${item.num}</td>
					<td><date:human value="${item.logDate}" /></td>
					<td>${item.discription}</td>
				</tr>
			</c:forEach>
		</table>
		<div style="text-align: center;">
			<tags:pagination page="${page}" url="/user/loggerlist" />
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	$(".uid_cls").click(function(){
		$("#display").html("");
		
		$.getJSON("/user/get/"+$(this).html(),function( data ) {	
			var display = "";
			for(x in data){
				display += "<p>"+x+":"+data[x]+"</p>"
			}
			$("#display").html("<p>username:"+data["username"]+"</p>");
		});
	});
});
</script>
</body>
</html>