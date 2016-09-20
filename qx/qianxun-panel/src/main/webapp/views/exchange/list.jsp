<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="date" uri="/WEB-INF/copycat.tld"%>
<html>
<head>
<title>千寻游戏-兑换积分管理</title>
<style type="text/css">
#vote width=20px;
</style>
</head>
<body>
<div class="wrapper">
	<div style="overflow:hidden">
		<h3>兑换积分管理</h3>
		<div id="vote">
		<p id="display"></p>
		<span>id:</span>
		<input id="id_input" type="text" value=0 />&nbsp;
		<span>uid</span>
		<input id="uid_input" type="text" value=0 />&nbsp;
		<span>订单</span>
		<input id="invoice_input" type="text" value=0 />&nbsp;
		<span>备注</span>
		<input id="remark_input" type="text" value=0 />&nbsp;
		<a id="submit_btn" class="btn btn-default" style="margin-bottom:5px;">确定</a>&nbsp;
		</div>
		<table class="table table-bordered">
			<tr>
				<td>id</td>
				<td>uid</td>
				<td>类型</td>
				<td>数量</td>
				<td>时间</td>
				<td>订单</td>
				<td>备注</td>
			</tr>
			<c:forEach items="${list}" var="item">
				<tr>
					<td><a href="#" class="id_cls">${item.id}</a></td>
					<td><a href="#" id="uid_${item.id}" class="uid_cls">${item.uid}</a></td>
					<td>${item.type_}</td>
					<td>${item.num}</td>
					<td><date:human value="${item.time_}" /></td>
					<td><span id="invoice_${item.id}">${item.invoice}</span></td>
					<td><span id="remark_${item.id}">${item.remark}</span></td>
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
		
		$.getJSON("/exchange/contact/"+$(this).html(),function( data ) {	
			var display = "";
			for(x in data){
				display += "<p>"+x+":"+data[x]+"</p>"
			}
			$("#display").html(display);
		});
	});
	
	$(".id_cls").click(function(){
		$("#id_input").val($(this).html());
		$("#uid_input").val($("#uid_"+$(this).html()).html());
		$("#invoice_input").val($("#invoice_"+$(this).html()).html());
		$("#remark_input").val($("#remark_"+$(this).html()).html());
	});
	
	$("#submit_btn").click(function() {
		$.post( "/exchange/update",
			{ "id" : $("#id_input").val(),"uid" : $("#uid_input").val(), "invoice" : $("#invoice_input").val(), "remark" : $("#remark_input").val()},
		function( data ) {
			$("#display").html("更新完毕,请刷新页面/"+Math.random());
		});
	});
});
</script>
</body>
</html>