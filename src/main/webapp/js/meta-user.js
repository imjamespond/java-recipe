//***user方法集***//
var User = {
	//刷新验证码
	RefreshRv: function(obj) {
	   	obj.src = "/utillity/getRegValidate?"+Math.random();
	},
	
	//登录
	Signin: function(user, referer){
		User.SigninPost(user, function(data){
			if(data == "ok"){
				MessageBox("登录成功",null);
				if(referer==""){
					location.href = "/";
				}else{
					location.href = referer;
				}
			}else{
				MessageBox("账号密码不正确",null);
			}
		});
	},
	SigninPost: function(user, onSignin){

		$.ajax({
			url : "/signinpost",
			type : "post",
			data : {
				username : user.username,
				password : user.password
			},
			success : onSignin
		});
	},

	//
	Signup: function(user, referer){
		User.SignupPost(user, function(data){
			if(data == "ok"){
				MessageBox("注册成功",null);
				if(referer==""){
					location.href = "/";
				}else{
					location.href = referer;
				}
			}else{
				MessageBox(data,null);
			}
		});
	},
	SignupPost: function(user, onSignup){
		$.ajax({
			url : "/signuppost",
			type : "post",
			data : {
				username : user.username,
				password: user.password,
				email: user.email
			},
			success : onSignup
		});
	},
	
	//退出登录
	Signout: function(){
		$.ajaxSetup({cache: false});
		$.ajax({
			url : "/signout",
			type : "get",
			async : false,
			success : function(data){
				if(data == "1"){
					location.href = "/";
				}else{
					alert("请求失败，请稍后再尝试");
				}
			},
			error : function(){
				alert("请求失败，请稍后再尝试");
			}
		});
	}
}


