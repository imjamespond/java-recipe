<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>修改密码</title>
</head>
<body>
	<div class="userTitle">
		<h2>修改密码</h2>
	</div>
	<div class="userCon">
		<div class="user_password">
			<form name="passwordForm">
				<div class="formDiv">
					<label for="oldp">旧&emsp;密&emsp;码：</label>
					<input type="password" name="oldp" id="oldp" />
					<p class="error"></p>
				</div>
				<div class="formDiv">
					<label for="newp">新&emsp;密&emsp;码：</label>
					<input type="password" name="newp" id="newp" />
					<p class="error"></p>
				</div>
				<div class="formDiv">
					<label for="repeatp">重复新密码：</label>
					<input type="password" name="repeatp" id="repeatp" />
					<p class="error"></p>
				</div>
				<div class="formDiv">
					<button type="button" class="orgBtn subPwBtn" onclick="userFn.changePassword()">提交</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>