<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>设置获奖人</title>
<script type="text/javascript">
	function checkUid(){
		var uid = $("#uid").val();
		if(uid == ""){
			alert("请填写用户ID");
		}
		$.ajax({
			url : "/stage/checkUid",
			type : "post",
			dataType : "json",
			data : {
				uid : uid
			},
			success : function(data){
				var error = data.error;
				if(error == 1){
					alert("改用户不存在，请输入正确的用户ID");
				}else{
					var nickname = data.nickname;
					alert(nickname);
					$("#nickname").html(nickname);
				}
			}
		});
	}
	function setPrizeWithUid(){
		var uid = $("#uid").val(),
			aid = $("#apple_id").val();
		if(uid == ""){
			alert("请填写用户ID");
		}
		$.ajax({
			url : "/stage/setPrizeWithUid",
			type : "post",
			dataType : "json",
			data : {
				uid : uid,
				aid : aid
			},
			success : function(data){
				var error = data.error;
				if(error == 1){
					alert("改用户不存在，请输入正确的用户ID");
				}else{
					alert("设置成功");
				}
			}
		});
	}
</script>
</head>
<body>
	<ol class="breadcrumb">
		<li><a href="/stage/list">千寻小公主</a></li>
		<li class="active">设置获奖人</li>
	</ol>
	<div>
		<input type ="hidden" id="apple_id" value="${applePrize.id }">
		<span>活动期数：${applePrize.prizeid }</span>
		<span>活动描述：${applePrize.description }</span>
		<span>获奖人ID：</span><input id="uid"><button onclick="checkUid()">验证ID合法性</button>
		<span>获奖人游戏昵称：</span><span id="nickname"></span>
		<button onclick="setPrizeWithUid()">设为获奖人</button>
	</div>
</body>
</html>