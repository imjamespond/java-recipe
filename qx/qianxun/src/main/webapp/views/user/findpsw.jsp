<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE>
<html>
<head>
<title>找回密码</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
</head>
<body>
<div id="findpsw" class="Area bgfff userCommon">
	<div class="pd30_50">
		<div class="titleA">
			<h1>找回密码</h1>
		</div>
		<div class="formDiv">
			<form name="findpswForm">
				<div class="overhide">
					<label for="email">邮箱：</label>
					<input type="text" name="email" id="email" />
					<button type="button" id="getCaptcha" class="orgBtn" onclick="userFn.getPswCaptcha(this)">获取验证码</button>
					<p class="error"></p>
				</div>
				<div class="overhide">
					<label for="captcha">验证码：</label>
					<input type="text" name="captcha" id="captcha" />
					<p class="error"></p>
				</div>
				<div class="overhide formRow30 pd_l_270">
					<button type="submit" class="orgBtn subBtn">提交</button>
				</div>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	$("form[name='findpswForm']").submit(function(e){
		preventDf(e);
		userFn.checkPswCaptcha(e);
	});
});
</script>
</body>
</html>