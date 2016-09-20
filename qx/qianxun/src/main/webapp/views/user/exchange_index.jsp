<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="date" uri="/WEB-INF/copycat.tld"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ page import="com.qianxun.model.Constant" %>
<html>
<head>
<title>积分兑换</title>
</head>
<body>
	<div class="userTitle">
		<h2>积分兑换</h2>
	</div>
	<div class="userCon">
		<div class="user_exchange_index">
			<div class="exchange_info">
				<div>用户名&emsp;：${username}</div>
				<div>游戏昵称：${nickname}</div>
				<div>千寻积分：${credit}</div>
				<div><a href="/exchange/list">查看兑换记录</a></div>
			</div>
			
			<table>
				<tr>
					<th>奖品</th>
					<th>所需积分</th>
					<th>操作</th>
				</tr>
				<tr>
					<td class="td0">
						<a href="http://item.jd.com/1107854.html" target="_blank">
							<img alt="" src="<%= Constant.MAIN_HTTP %>/images/JDcard_100.jpg">
						</a>
					</td>
					<td class="td1">100</td>
					<td class="td2">
						<a href="javascript:;" class="orgBtn integralBtn" need="100">兑换</a>
					</td>
				</tr>
			</table>
			<input type="hidden" id="credit" value="${credit}" />
		</div>
	</div>
<script type="text/javascript">
$(document).ready(function(){
	$(".integralBtn").each(function(){
		var credit = parseInt($("#credit").val());
		var need = parseInt($(this).attr("need"));
		if(credit < need){
			$(this).addClass("disa");
		}
	});
	$(".integralBtn").click(function(){
		var This = $(this);
		var reg = /\bdisa\b/;
		var sClass = This.attr("class");
		if(reg.test(sClass)){
			return false;
		}
		var credit = parseInt($("#credit").val());
		var need = parseInt(This.attr("need"));
		if(credit < need){
			return false;
		}else{
			location.href = "/exchange/form";
		}
	});
});
</script>
</body>
</html>