<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE>
<html>
<head>
<title>登录</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
</head>
<body>
<div id="login" class="Area bgfff userCommon">
	<div class="pd30_50">
		<div class="titleA">
			<h1>用户登录</h1>
		</div>
		<div class="formDiv">
			<form name="loginForm" action="/user/auth" method="post">
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
					<span>还没帐号？<a href="/user/register">立即注册</a></span>
					<a href="/user/findpsw" class="mg_l_70">忘记密码</a>
				</div>
				<div class="overhide pd_l_270">
					<button type="submit" class="orgBtn subBtn">登录</button>
					<input type="hidden" id="referer" value="" />
				</div>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
window.onload = function(){
	document.forms["loginForm"].onsubmit = function(){
		preventDf();
		login(this);
	}
}

//登录
function login(form){
	var aErrorP = getByClass(form, "error");
	var iErrorNum = aErrorP.length;
	var oUsername = getById("username");
	var oPassword = getById("password");
	var username = oUsername.value;
	var password = oPassword.value;
	var oErrorUn = getByClass(oUsername.parentNode, "error")[0];
	var oErrorPw = getByClass(oPassword.parentNode, "error")[0];
	var blankPatern = /^\s*$/;
	for(i=0;i<iErrorNum;i++){
		aErrorP[i].innerHTML = "";
	}
	if(oUsername == "" || blankPatern.test(username)){
		oUsername.focus();
		oErrorUn.innerHTML = "账号不能为空";
		return false;
	}else if(oPassword == "" || blankPatern.test(password)){
		oPassword.focus();
		oErrorPw.innerHTML = "密码不能为空";
		return false;
	}
	myAjax({
		url : "/user/getSalt",
		type : "post",
		async : false,
		data : {
			username : username
		},
		fnSucc : function(data){
			data = JSON.parse(data);
			if(data != "1"){
				salt = data.salt;
				random = data.random;
				getById("random").value = random;
				getById("salt").value = salt;
			}
		}
	});
	var salt = getById("salt").value,
		random = getById("random").value;
	var randomShaObj = new jsSHA(random,"TEXT");
	random = randomShaObj.getHash("SHA-512", "HEX");
	var shaObj = new jsSHA(password+salt, "TEXT");
	var hashpsd = shaObj.getHash("SHA-512", "HEX");
	var hashpsdShaObj = new jsSHA(hashpsd+random, "TEXT");
	var hashpassword = hashpsdShaObj.getHash("SHA-512", "HEX");
	myAjax({
		type: "post",
		url: "/user/auth",
		async : false,
		data: {
			username : username,
			password: hashpassword
		},
		fnSucc: function(data){
			if(data == "1"){
				oErrorPw.innerHTML = "账号密码不正确";
			}else{
				var referer = getById("referer").value;
				if(referer == ""){
					location.href = "/";
				}else{
					location.href = referer;
				}
			}
		}
	});
}
</script>
</body>
</html>