


define('signin/user', ['require', 'exports', 'module', 
	'jquery', 'utils/commons', 'utils/vue-alert'],
	function (require, exports, module) {
		var Utils = require('utils/commons')
		var VueAlert = require('utils/vue-alert')
		var User = {
			//登录
			Signin: function (user) {
				User.SigninPost(user, function (data) {
					if (data == "ok") {
						VueAlert.Success("登录成功");
						if (gReferer == "") {
							location.href = Utils.GetAbsUrl("/home");
						} else {
							location.href = gReferer;
						}
					} else {
						VueAlert.Warning("账号密码不正确");
					}
				});
			},
			SigninPost: function (user, onSignin) {

				$.ajax({
					url: Utils.GetAbsUrl("/login.post"),
					type: "post",
					data: user,
					success: onSignin
				});
			},

			Signup: function (user, referer) {
				User.SignupPost(user, function (data) {
					if (data == "ok") {
						VueAlert.Success("注册成功");
						if (referer == "") {
							location.href = "/";
						} else {
							location.href = referer;
						}
					} else {
						MessageBox(data, null);
					}
				});
			},
			SignupPost: function (user, onSignup) {
				$.ajax({
					url: GetAbsUrl("/signuppost"),
					type: "post",
					data: {
						username: user.username,
						password: user.password,
						email: user.email
					},
					success: onSignup
				});
			}
		}
		return User
	})

define('app/signin/main', ['require', 'exports', 'module', 'jquery', 'utils/commons', 'signin/user'],
	function (require, exports, module) {
		var User = require('signin/user')
		var Utils = require('utils/commons')
		$(document).ready(function () {
			$("form[name='form']").submit(function (e) {
				e.preventDefault();
				var user = {
					tenant: $("#tenant").val(),
					username: $("#username").val(),
					password: $("#password").val()
				};
				user.password = Utils.GetSha1(user.password);
				User.Signin(user, "${referer}");
			});
		});
	}
)





requirejs(['./common'], function (common) {
    requirejs(['app/signin/main',]);
});