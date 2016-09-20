<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="date" uri="/WEB-INF/copycat.tld"%>
<html>
<head>
<title>千寻游戏-话题管理</title>
<style type="text/css">
#vote width=20px;
</style>
</head>
<body>
<div class="wrapper">
	<div style="overflow:hidden">
		<h3>话题管理</h3>
		<div id="vote">
<p id="display"></p>
		</div>
		<table class="table table-bordered">
			<tr>
				<td>id</td>
				<td>stype</td>
				<td>uid</td>
				<td>username</td>
				<td>title</td>
				<td colspan=4>content|createTime|replytime|置顶 0否1是</td>
				<td>删除</td>
			</tr>
			<c:forEach items="${list}" var="item">
				<tr>
					<td><a href="#" class="id_cls">${item.id}</a></td>
					<td>${item.stype}</td>
					<td><a href="#" id="uid_${item.id}" class="uid_cls">${item.uid}</a></td>
					<td>${item.username}</td>
					<td><textarea style="width: 200px;">${item.title}</textarea></td>
					<td><textarea style="width: 500px;">${item.content}</textarea></td>
					<td><date:human value="${item.createTime}" /></td>
					<td><date:human value="${item.replyTime}" /></td>
					<td><a href="/forum/topic_top/${item.id}" class="uid_cls">${item.top}</a></td>
					<td><a href="/forum/topic_remove/${item.id}" class="uid_cls">删除</a></td>
				</tr>
			</c:forEach>
		</table>
		<div style="text-align: center;">
			<tags:pagination page="${page}" url="/exchange/list" />
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