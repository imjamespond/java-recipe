<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE>
<html>
<head>
<title>注册</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<style>
.red{background:transparent url("/images/sprites.png") no-repeat scroll -40px -220px;}
.green{background:transparent url("/images/sprites.png") no-repeat scroll -20px -200px;}
#validator_tip{margin:0 10px;width:20px;height:20px;display:inline-block;}
</style>
</head>
<body>
	<div id="register" class="Area bgfff userCommon">
		<div class="pd30_50">
			<div class="titleA">
				<h1>用户注册</h1>
			</div>
			<div class="formDiv">
				<form name="registerForm" action="/info/save" method="post">
					<div class="overhide">
						<label for="username">用户名：</label> <input type="text"
							name="username" id="username" /> <span class="prompt">请使用6~20位的字母、数字</span>
						<p class="error"></p>
					</div>
					<div class="overhide">
						<label for="password">密码：</label> <input type="password"
							name="password" id="password" /> <span class="prompt">请使用6~20位的字母、数字</span>
						<p class="error"></p>
					</div>
					<div class="overhide">
						<label for="repassword">重复密码：</label> <input type="password"
							name="repassword" id="repassword" /> <span class="prompt">请重新输入一次密码</span>
						<p class="error"></p>
					</div>
					<div class="overhide validateRow">
						<label for="validate">验证码：</label>
						<input type="hidden" name="validate" id="validate" value=0 /><!---->
						<img id="vdImg" title="点击更换" src="/utillity/getTouchCaptcha" />
						<!-- onclick="javascript:userFn.refreshRv(this);" -->
						<span class="prompt"><span id="validator_tip"></span>请点击图中的：<strong>${captcha}</strong>，
						看不清，<a href="javascript:refreshTc();">换一张？</a>
						</span>
						<p class="error"></p>
					</div>
					<div class="overhide pd_l_170 formRow20 ag">
						<input type="checkbox" id="agree" checked="checked" /> <span>我已阅读并同意</span>
						<a href="/agreement" target="_blank">《用户协议》</a> <span
							class="error"></span>
					</div>
					<div class="overhide pd_l_170 sub">
						<button type="submit" class="orgBtn subBtn">注册</button>
						<span>已有帐号？</span><a href="/info/login">立即登录</a>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	var captchaOk = false;
	$(document).ready(function(){
		$("form[name='registerForm']").submit(function(e){
			preventDf(e);
			userFn.register(this, 0);
		});
		
		$("#vdImg").click(function(e){
			var offset = $(this).offset(); //if you really just want the current element's offset
			var relX = Math.floor(e.pageX - offset.left);
			var relY = Math.floor(e.pageY - offset.top);
		  	//$("#debug").html(relX+":"+relY);
		  	if(!captchaOk){
			$.post("/utillity/testCaptcha",{"x":relX,"y":relY},function(data){
			   
			   if(data=="ok"){
				   $("#validator_tip").removeClass();
				   $("#validator_tip").addClass("green");
				   $("#validate").val(1);
				   captchaOk = true;
			   }else{
				   $("#validator_tip").removeClass();
				   $("#validator_tip").addClass("red");
			   }
			   
			});
			}
		});
		
		$("#vdImg").mouseup(function() {
		    $( this ).css( {"padding":"0px"} );
		}).mousedown(function() {
		    $( this ).css( {"padding":"2px"} );
		});
	});
	
	function refreshTc(){
		if(!captchaOk){
			userFn.refreshTc(getById('vdImg'));
		}
	}
	</script>
</body>
</html>