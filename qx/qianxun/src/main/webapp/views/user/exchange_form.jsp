<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>积分兑换</title>
</head>
<body>
	<div class="userTitle">
		<h2>积分兑换</h2>
	</div>
	<div class="userCon">
		<div class="user_exchange_form">
		
			<div>
				<label>姓名：</label>
				<input type="text" name="name" id="name" value="${name}" />
				<div class="error"></div>
			</div>
			<div>
				<label>手机：</label>
				<input type="text" name="mobile" id="mobile" value="${mobile}" />
				<div class="error"></div>
			</div>
			<div>
				<label>地址：</label>
				<input type="text" name="address" id="address" value="${address}" />
				<div class="error"></div>
			</div>
			<div>
				<label>邮编：</label>
				<input type="text" name="postcode" id="postcode" value="${postcode}" />
				<div class="error"></div>
			</div>
			<!-- <div>
				<label>邮箱：</label>
				<input type="text" name="email" id="email" />
				<div class="error"></div>
			</div> -->
			<div>
				<a href="javascript:;" class="orgBtn subBtn">兑换</a>
			</div>
		</div>
	</div>
<script type="text/javascript">
$(document).ready(function(){
	$(".subBtn").click(function(){
		var hasContact = ${hasContact};
		if(hasContact){
			userFn.subExchangeForm(2);//update
		}else{
			userFn.subExchangeForm(1);//insert
		}
		
	});
});
</script>
</body>
</html>