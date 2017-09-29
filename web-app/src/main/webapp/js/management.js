
define('app/management/main', ['require', 'exports', 'module',
	'jquery', 'vue', 'vue_router', 
	'utils/commons', 'utils/vue-alert', 'utils/vue-common',
	'com/tenant', 'com/resource', 'com/res-catalog', 'com/res-apply',
	'com/application','com/query'],
	function (require, exports, module) {
		var $ = require('jquery')
		var Vue = require('vue')
		var VueRouter = require('vue_router')
		var Utils = require('utils/commons')
		var Commons = require('utils/vue-common')
		var Res = require('com/resource')
		var ResApply = require('com/res-apply')
		var Application = require('com/application')
		var Query = require('com/query')

		Vue.use(VueRouter)
		const router = new VueRouter({
			mode: 'history',
			base: Utils.GetAbsUrl('/manage/'),
			routes: [
				{ path: '/', redirect: 'tenant' },
				{ path: '/tenant', component: require('com/tenant') },
				{
					path: '/resource', component: Res.Resource,
					children: [{
						path: '',
						redirect: Commons.mixins.methods.CheckSysAdminOrTenant() ? 'data-source' : 'apply'
					},
					{ path: 'data-source', component: Res.ResDataSource },
					{ path: 'catalog', component: require('com/res-catalog') },
					{ path: 'apply', component: ResApply.ResApply },
					{ path: 'applied', component: ResApply.ResApplied }]
				},
				{
					path: '/application-manage', component: Application.Manage,
					children: [
						{ path: '', redirect: 'to-apply' },
						{ path: 'new', component: null },
						{ path: 'to-apply', component: Application.ToApply },
						{ path: 'applied', component: Application.Applied },
						{ path: 'to-process', component: Application.Process, props: { token: 0 } },
						{ path: 'processed', component: Application.Processed, props: { token: 3 } },
						
					]
				},
				{ path: '/query', component: Query } 
			]
		})

		new Vue({
			router,
			mixins: [Commons.mixins],
			data: { helper: 1, helperMsg: "" },
			mounted: function () {
				this.title()
			},
			updated: function () {
				this.title()
			},
			methods: {
				title: function () {
					var title = $(this.$el).find(".nav-dropdown>ul>._active>a>span").html()
					$(this.$el).find(".nav-dropdown>a>.light-font").html(title)
				},
				onLogin: function () { this.$refs.login.show() },
				logout: function () {
					Utils.Get("/login.logout", null, function () {
						alert("退出成功,确定后将重新登录!")
						Utils.Reload("/manage")
					})
				}
			},
			template: `
		<div id="vue-router">
			<nav class="navbar margin-bottom-md text-center border-none"
				style="margin:0 auto; max-width: 1500px;">
					<div class="collapse navbar-collapse " >
						<ul class="nav navbar-nav  nav-head">
							<li class="dropdown nav-dropdown">
								<a href="#" class="dropdown-toggle relative" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
									<div class="icon_nav icon absolute"></div>
									<span class="light-font" ></span>
									<span class="caret"></span>
								</a>
								<ul class="dropdown-menu overflow-hidden">
									<li > <a class="pointer relative" href="/data-center">
												<div class="icon_nav absolute" style="top: 20px;"></div>
												<span class="light-font" style="margin-left: 30px;">首页</span>	</a> 
										</li>
									<router-link tag="li" v-if="CheckSysAdminOrTenant()" active-class="_active"
										to="/tenant/" > <a class="pointer relative">
											<div class="icon_zuhu absolute" style="top: 20px;"></div>
											<span class="light-font" style="margin-left: 30px;">租户与用户</span>	</a> 
									</router-link>
									<router-link tag="li" active-class="_active"
										to="/resource/" > <a class="pointer relative">
											<div class="icon_nav absolute" style="top: 20px;"></div>
											<span class="light-font" style="margin-left: 30px;  ">数据资源目录</span> </a> 
									</router-link>
									<router-link tag="li" active-class="_active"
										to="/application-manage/" > <a class="pointer relative">
											<div class="icon_update absolute" style="top: 20px;"></div>
											<span class="light-font" style="margin-left: 30px;  ">申请单管理</span>	</a> 
									</router-link> 
									<router-link tag="li" active-class="_active"
									to="/query" > <a class="pointer relative">
										<div class="icon_update absolute" style="top: 20px;"></div>
										<span class="light-font" style="margin-left: 30px;  ">数据查询</span>	</a> 
								</router-link> 
								</ul>
							</li>
						</ul>
					<a href="/data-center" class="pointer"> <img class="padding-top-sm" alt="Brand" src="/data-center/image/logo.png" > </a>
					<ul class="nav navbar-nav nav-head pull-right">
							<li :class="helper?'active':''" @click="helper^=1"> 
								<a class="relative pointer">
								<span class="inline-block"  >
								<div class="icon icon_guide_blue absolute" ></div>
								</span>
								</a>
							</li>
							<li class=""> 
								<a class="relative pointer" @click="onLogin">
								<span class="inline-block"  >
								<div class="icon icon_profile absolute" ></div>
								</span><!---->
								</a>
							</li>
						</ul>
					</div><!-- /.navbar-collapse -->
			</nav>

			<div class="ds-body " :class="{'ds-helper-collapse':!helper}" >
				<!-- 帮助面板 -->
				<vue-helper class="pull-left ds-helper"></vue-helper>
				<router-view></router-view>
			</div>
			<!-- <strong v-else>抱歉,仅管理员才可访问该页面.</strong> -->

			<modal ref="login" :footer="true" :on-confirm="logout" :sm="true">
				<h4 slot="title">登录管理</h4>
				<h5 slot="body" class="text-center">尊敬的 {{GetUserName()}} 确定退出吗?</h5>
			</modal>
		</div>
				`,
		}).$mount('#vue-router')
	}
)

function initManagement() {
	router.afterEach(function (to, from) { })
}


requirejs(['./common'], function (common) {
	requirejs(["ace/ace"],function(){
		requirejs(["ace/mode-sql","ace/ext-language_tools"],function(){
			require(['app/management/main']);
		})
	})
});