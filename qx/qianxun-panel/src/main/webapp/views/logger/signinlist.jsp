<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="date" uri="/WEB-INF/copycat.tld"%>
<html>
<head>
<title>千寻游戏-登录管理</title>
<style type="text/css">
#vote width=20px;
</style>
</head>
<body>
<div class="wrapper">
	<div style="overflow:hidden">
		<h3>登录管理</h3>
<p id="display"></p>
		<table class="table table-bordered">
			<tr>
				<td>id</td>
				<td>uid</td>
				<td>时间</td>
				<td>ip</td>
			</tr>
			<c:forEach items="${list}" var="item">
				<tr>
					<td><a href="#" class="id_cls">${item.id}</a></td>
					<td><a href="#" id="uid_${item.id}" class="uid_cls">${item.uid}</a></td>
					<td><date:human value="${item.date_}" /></td>
					<td><a class="ip_cls">${item.ip}</a></td>
				</tr>
			</c:forEach>
		</table>
		<div style="text-align: center;">
			<tags:pagination page="${page}" url="/user/signinlist" />
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
	
	$(".ip_cls").click(function(){
		var a = $(this);
		$.getJSON("/user/getip?ip="+$(this).html(),function( data ) {
			ipData = data["data"];
			ipInfo = "";
			if(ipData!=null){
				ipInfo+="country:"+ipData["country"];
				ipInfo+=",area:"+ipData["area"];
				ipInfo+=",region:"+ipData["region"];
				ipInfo+=",city:"+ipData["city"];
				ipInfo+=",county:"+ipData["county"];
				ipInfo+=",isp:"+ipData["isp"];
				a.parent().append("</br><span>"+ipInfo+"</span>");
			}
			
		});		
	});
});
</script>
</body>
</html>