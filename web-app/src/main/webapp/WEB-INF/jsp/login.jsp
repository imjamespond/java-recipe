<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.metasoft.model.Constant"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
	var gReferer = "${referer}"
</script>
<script charset="UTF-8" data-main="js/signin"
	src="<%=Constant.ContextPath%>/js/lib/require.js"></script>
<title>登录</title>
<style type="text/css"></style>
</head>
<body class="white-bg">

	<iframe class="animation" id="iframe-animation" src="<%=Constant.ContextPath%>/html/nebulon.html" style="
		width: 100%;
		height: 100%;
		position: absolute;
		left: 0;
		top: 0;
		border: 0;"></iframe>

	<div class="col-lg-12"style=" margin-top: 50px; ">
		<div class="well center-block " style="max-width: 500px">
			<form class="form-signin" name="form" action="./" method="post">
				<div class="row">
					<div class="col-md-12 text-center">
						<h3 class="form-signin-heading text-center">数据共享中心</h3>
						<input type="text" name="tenant" id="tenant"
							class="form-control input-lg margin-between-md" placeholder="租户名"
							required autofocus /> <input type="text" name="username"
							id="username" class="form-control input-lg margin-between-md"
							placeholder="用户名" required autofocus /> <input type="password"
							name="password" id="password"
							class="form-control input-lg margin-between-md" placeholder="密码"
							required />

						<button class="btn btn-lg btn-primary margin-between-md"
							type="submit">登录</button>
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- /container -->

</body>
</html>