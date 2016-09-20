<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<html>
<head>
<title>千寻游戏-游戏列表</title>
<style type="text/css">
#vote width=20px;
</style>
</head>
<body>
<div class="wrapper">
	<div style="overflow:hidden">
		<a href="/game/disableall" class="btn btn-default" style="margin-bottom:5px;">作废全部使用中的游戏</a>&nbsp;
		<div id="vote">
		<p id="display"></p>
		<span>游戏id:</span>
		<input id="gid_input" type="text" value=0 />&nbsp;
		<span>榜单</span>
		<select id="type_sel">
		</select>&nbsp;
		<span>票数</span>
		<input id="vote_input" type="text" value=0 />&nbsp;
		<span>趋势</span>
		<input id="trend_input" type="text" value=0 />&nbsp;
		<a id="vote_btn" class="btn btn-default" style="margin-bottom:5px;">确定</a>&nbsp;
		<a id="trend_btn" class="btn btn-default" style="margin-bottom:5px;">更新趋势</a>&nbsp;
		<a id="rank_btn" class="btn btn-default" style="margin-bottom:5px;">更新排行</a>&nbsp;
		</div>
		<table class="table table-bordered">
		<tr>
		<td></td>
		<td></td>
		<td colspan=10>投票/方向(-1下降,0持平,1上升)</td>
		</tr>
			<tr>
				<td>id</td>
				<td>游戏名</td>
				<td>1</td>
				<td>2</td>
				<td>3</td>
				<td>4</td>
				<td>5</td>
				<td>6</td>
				<td>7</td>
				<td>8</td>
				<td>9</td>
				<td>0</td>
				<td>游戏类型</td>
				<td>状态</td>
				<td>操作</td>
			</tr>
			<c:forEach items="${gameList}" var="game">
				<tr>
					<td><a href="#" class="id_cls">${game.id}</a></td>
					<td>${game.gname}</td>
					<td><c:if test="${!empty game.gid}"><span id="vote_${game.id}_1">${game.vote_1}</span>/<span id="trend_${game.id}_1">${game.trend_1}</span></c:if></td>
					<td><c:if test="${!empty game.gid}"><span id="vote_${game.id}_2">${game.vote_2}</span>/<span id="trend_${game.id}_2">${game.trend_2}</span></c:if></td>
					<td><c:if test="${!empty game.gid}"><span id="vote_${game.id}_3">${game.vote_3}</span>/<span id="trend_${game.id}_3">${game.trend_3}</span></c:if></td>
					<td><c:if test="${!empty game.gid}"><span id="vote_${game.id}_4">${game.vote_4}</span>/<span id="trend_${game.id}_4">${game.trend_4}</span></c:if></td>
					<td><c:if test="${!empty game.gid}"><span id="vote_${game.id}_5">${game.vote_5}</span>/<span id="trend_${game.id}_5">${game.trend_5}</span></c:if></td>
					<td><c:if test="${!empty game.gid}"><span id="vote_${game.id}_6">${game.vote_6}</span>/<span id="trend_${game.id}_6">${game.trend_6}</span></c:if></td>
					<td><c:if test="${!empty game.gid}"><span id="vote_${game.id}_7">${game.vote_7}</span>/<span id="trend_${game.id}_7">${game.trend_7}</span></c:if></td>
					<td><c:if test="${!empty game.gid}"><span id="vote_${game.id}_8">${game.vote_8}</span>/<span id="trend_${game.id}_8">${game.trend_8}</span></c:if></td>
					<td><c:if test="${!empty game.gid}"><span id="vote_${game.id}_9">${game.vote_9}</span>/<span id="trend_${game.id}_9">${game.trend_9}</span></c:if></td>
					<td><c:if test="${!empty game.gid}"><span id="vote_${game.id}_0">${game.vote_0}</span>/<span id="trend_${game.id}_0">${game.trend_0}</span></c:if></td>
					<td>
						<c:if test="${game.type eq 0}">端游</c:if>
						<c:if test="${game.type eq 1}">页游</c:if>
						<c:if test="${game.type eq 2}">手游</c:if>
					</td>
					<td>
						<c:if test="${game.state eq 0}">作废</c:if>
						<c:if test="${game.state eq 1}">使用中</c:if>
					</td>
					<td>
						<c:if test="${game.state eq 0}">
							<a href="/game/updatestate/${game.id}">启用</a>
						</c:if>
						<c:if test="${game.state eq 1}">
							<a href="/game/updatestate/${game.id}">作废</a>
						</c:if>
						<a href="/game/remove/${game.id}">移除</a>&nbsp;
					</td>
				</tr>
			</c:forEach>
		</table>
		<div style="text-align: center;">
			<tags:pagination page="${page}" url="/game/list" />
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	for(var i=0;i<10;i++){
		$("#type_sel").append("<option>"+i+"</option>");
	}
	
	$("#vote_btn").click(function() {
		$.post( "/game/updatevote",{ "id" : $("#gid_input").val(), "vote" : $("#vote_input").val(), "trend" : $("#trend_input").val(), "type" : $("#type_sel").val()}, function( data ) {
			$("#display").html("更新完毕,请刷新页面/"+Math.random());
		});
	});
	$("#rank_btn").click(function() {
		$.post( "/game/updaterank",function( data ) {
			$("#display").html("更新完毕,请刷新页面/"+Math.random());
		});
	});
	$("#trend_btn").click(function() {
		$.post( "/game/updatetrend",function( data ) {
			$("#display").html("更新完毕,请刷新页面/"+Math.random());
		});
	});
	
	$(".id_cls").click(function(){
		$("#gid_input").val($(this).html());
		$("#vote_input").val($("#vote_"+$(this).html()+"_"+$("#type_sel").val()).val());//vote_gid_type
		$("#trend_input").val($("#trend_"+$(this).html()+"_"+$("#type_sel").val()).val());
	});
});
</script>
</body>
</html>