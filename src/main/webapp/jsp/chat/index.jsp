<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<title>消息服务</title>
<style type="text/css">
#msg {
	min-height: 200px;
	max-height: 500px;
	overflow-y: scroll;
}
</style>
</head>
<body>
	<div class="col-lg-12">
		<c:if test="${not empty session}">
			<h3>
				当前登录用户:<span class="label label-info">${session.username}</span>
			</h3>
		</c:if>

		<div class="panel panel-info">
			<div class="panel-heading">消息服务</div>
			<div class="panel-body">
				<div id="msg" class="well"></div>
				<div class="input-group input-group-lg">
					<input id="msg_input" type="text" class="form-control"
						placeholder="说点啥..."> <span class="input-group-btn">
						<button class="btn btn-default btn-primary" type="button"
							onclick=send()>发送</button>
					</span>
				</div>
				<!-- /input-group -->
			</div>
		</div>
	</div>

	<script type="text/javascript">
		function send(){
			$.post("/test/chat-send",{'touser':'-1','msg':$('#msg_input').val()}, function(data){console.log(data)});
		}
		function onMsg(from, msg){
			$("#msg").append('<p><span class="label label-info">'+from+':</span>'+msg+'</p>');
			if($("#msg p").length>100){
				$("#msg p:first-child").remove();
			}
		}
		var seq = 0;
		function poll(){
			$.ajax({method: "GET",
				url: "/test/chat-get",
				data: {"seq":seq} }).
				done(function(data) {
					if(data=="async-timeout"){
						console.log(data);
					}else{
						console.log(data);
						var obj = jQuery.parseJSON( data );
						for(var i=0; i<obj.length; i++){
							if(obj[i].seq>seq||seq-obj[i].seq>100)
								seq=obj[i].seq;
							onMsg(obj[i].from, obj[i].msg);
						}
					}
					//poll();
					CountDown(5, function(){poll();});//测试是否收到缓存消息
				  }).
				fail(function(jqXHR, textStatus, errorThrown) {
					//try again later
					CountDown(5, function(){poll();});
					console.log(textStatus);
				  });
		}
		$(document).ready(function() {
			poll();
		});

		</script>
</body>
</html>