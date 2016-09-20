<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.qianxun.model.Constant" %>
<!DOCTYPE>
<html>
<head>
<title>登录</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<script type="text/javascript" src="<%= Constant.MAIN_HTTP %>/scripts/sha512.js"></script>
</head>
<body>
<div id="login" class="Area bgfff userCommon">
	<div class="pd30_50">
		<div class="titleA">
			<h1>用户登录</h1>
		</div>
		<div class="formDiv">
			<form name="loginForm" action="./" method="post">
				<div class="overhide">
					<label for="username">用户名：</label>
					<input type="text" name="username" id="username" />
					<input type="hidden" id="random">
					<input type="hidden" id="salt">
					<p class="error"></p>
				</div>
				<div class="overhide">
					<label for="password">密码：</label>
					<input type="password" name="password" id="password" />
					<p class="error"></p>
				</div>
				<div class="overhide pd_l_270 formRow20">
					<span>还没帐号？<a href="/info/register">立即注册</a></span>
					<a href="/info/findpsw" class="mg_l_70">忘记密码</a>
				</div>
				<div class="overhide pd_l_270">
					<button type="submit" class="orgBtn subBtn">登录</button>
					<input type="hidden" id="referer" value="${referer}" />
				</div>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	$("form[name='loginForm']").submit(function(e){
		preventDf(e);
		userFn.login(this);
	});
});
</script>
</body>
</html>