define('com/home', ['require', 'exports', 'module', 'jquery', 'chart',
	'utils/commons', 'utils/vue-common' ],
	function (require, exports, module) {
		var $ = require('jquery')
		var Chart = require('chart')
		var Commons = require('utils/vue-common')
		var Utils = require('utils/commons')
		var GetJSON = Utils.GetJSON
		var Post = Utils.Post
		var Assert = Utils.Assert

		const VueHome = {
			template: ` 
<div> 
	<div class="col-md-6 margin-bottom-md">
		<div class="panel light-font ">
			<div class="panel-heading light-border-bottom">
				数据资源注册状况</div>
			<div class="panel-body relative" style="height: 200px;">
				<div class="absolute" 
					v-if="catalogSummary==null"> <loading></loading></div>
				<canvas id="chart-1" ></canvas>
				
			</div>
		</div>
	</div>
	<div class="col-md-6 margin-bottom-md">
		<div class="panel light-font ">
			<div class="panel-heading light-border-bottom">
				数据资源共享状况</div>
			<div class="panel-body relative" style="height: 200px;">
				<div class="absolute" 
					v-if="dataSourceSummary==null"> <loading></loading></div>
				<canvas id="chart-2" ></canvas>
			</div>
		</div>
	</div>

	<div class="col-md-3 ">
		<div class="panel light-font ">
			<div class="panel-heading light-border-bottom">
				共享数据资源分类</div>
			<div class="panel-body">
				<res-share :catalog-stats="catalogStats"></res-share>
			</div>
		</div>
	</div>

	<div class="col-md-3 ">
		<div class="panel light-font ">
			<div class="panel-heading light-border-bottom">
				最近访问资源</div>
			<div class="panel-body">
				<resent-res :resent-res="resentRes"></resent-res>
			</div>
		</div>
	</div>

	<div class="col-md-6 ">
	<div class="row">
		<hot-tables ref='hot1' title="热门数据表" ></hot-tables> 
	</div>
	</div>
	
</div>`,
			mixins: [Commons.mixins],
			created: function () {
				this.$root.helper = 0
				this.VueDirective()
			},
			mounted: function () {
				this.load()
			},
			data: function () { return { catalogStats: {}, resentRes: {},
			 catalogSummary: null, dataSourceSummary: null } },
			provide: {
				createChart: 'createChart'
			},
			methods: {
				load: function () {
					var hot1Chart = this.$refs.hot1.getCanvas()
					hot1Chart.height = 150

					var chart_1 = document.getElementById("chart-1")
					chart_1.height = 200

					var chart_2 = document.getElementById("chart-2")
					chart_2.height = 200

					var _this = this;
					GetJSON('/management/modelCatalogStats.load', null, function (data) {
						_this.catalogStats = data
					});
					GetJSON('/management/modelCatalogSummary.load', null, function (data) {
						_this.catalogSummary = data
						_this.createChart(chart_2.getContext('2d'), data, 'rgba(54, 162, 235, 0.5)')
					});
					GetJSON('/management/dataSourceSummary.load', null, function (data) {
						_this.dataSourceSummary = data
						_this.createChart(chart_1.getContext('2d'), data, 'rgba(75, 192, 192, 0.5)')
					});

					GetJSON('/management/usageStats.load', null, function (data) {
						_this.createChart(hot1Chart.getContext('2d'), data, 'rgba(54, 162, 235, 0.5)', 'horizontalBar')
					});

					GetJSON('/management/recentlyVisited.load', null, function (data) {
						_this.resentRes = data
					});
				},
				createChart: function (ctx, objs, color, type) {
					var labels = [], data = []
					for (var key in objs) {
						labels.push(key)
						data.push(objs[key])
					}
					type = type ? type : 'bar'
					return new Chart(ctx, {
						type: type,
						data: {
							labels: labels,
							datasets: [{
								//label: '# of Votes',
								data: data,
								backgroundColor: color ? color : 'rgba(255, 99, 132, 0.8)'
							}]
						},
						options: {
							maintainAspectRatio: false,
							onResize: function (chart, size) {
								chart.canvas.parentNode.style.height = '200px';
							},
							scales: {
								yAxes: [{
									ticks: {
										beginAtZero: true
									}
								}],
								xAxes: [{
									ticks: {
										fixedStepSize: 1
									}
								}]
							},
							legend: {
								display: false
							}
						},
					});
				}
			},//methods
			components: {
				'res-share': {
					template: `
<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
  <div class="panel panel-info"
		v-for="(db,key,i) in catalogStats">
    <div class="panel-heading" role="tab" :id="'heading_'+i">
      <h5 class="panel-title ">
        <a role="button" data-toggle="collapse" data-parent="#accordion" 
					:href="'#collapse_'+i" aria-expanded="true" :aria-controls="'collapse_'+i">
          {{key}}
        </a>
				<span class="ft7">{{count(db)}}个分类</span> 
      </h5>
    </div>
    <div :id="'collapse_'+i" class="panel-collapse collapse" role="tabpanel" 
			:aria-labelledby="'heading_'+i"
			:class="{in:i==0}">
      <div class="panel-body padding-none"> 
				<ul class="list-group margin-none home_">
					<li class="list-group-item"
						v-for="(num,key_catalog) in db">
						<span class="badge bg-info">{{num}}</span>
						{{key_catalog}}
					</li>
				</ul>
			</div>
    </div>
  </div>
</div>
				`,
					props: { catalogStats: Object },
					methods: {
						count: function (obj) {
							var count = 0
							for (var k in obj) {
								count++
							}
							return count
						},
					}
				},
				'resent-res': {
					template: `
				<ul class="list-group margin-none ">
					<li class="list-group-item"
						v-for="(num,key) in resentRes">
						<span class="badge bg-info">{{num}}</span>
						{{key}}
					</li>
				</ul>
				`,
					props: { resentRes: Object },
				},
				'hot-tables': {
					template: `
				<div class="col-md-12">
					<div class="panel light-font ">
					<div class="panel-heading light-border-bottom">
						{{title}}</div>
					<div class="panel-body">
					<canvas ></canvas>
					</div>
					</div>
				</div>
				`,
					props: { title: String },
					data: function () { return {} },
					methods: {
						getCanvas: function () {
							return this.$el.getElementsByTagName('canvas')[0]
						}
					}
				}

			}//components
		};
		return VueHome
	})

define('app/home/main', ['require', 'exports', 'module',
	'jquery', 'vue', 'vue_router',
	'utils/commons', 'utils/vue-alert', 'utils/vue-common',
	'com/home', 'com/res-catalog.addon'],
	function (require, exports, module) {
		var $ = require('jquery')
		var Vue = require('vue')
		var VueRouter = require('vue_router')
		var Utils = require('utils/commons')
		var GetJSON = Utils.GetJSON
		var Post = Utils.Post
		var Assert = Utils.Assert
		var Commons = require('utils/vue-common')
		var Home = require('com/home')
		var VueAlert = require('utils/vue-alert')
		var Addon = require('com/res-catalog.addon')

		new Vue({ 
			mixins: [Commons.mixins],  
			data:function(){return {query:"", results:null, 
				navs:[  {url:"/manage/tenant",title:"租户与用户",clazz:"icon_zuhu"},
				{url:"/manage/resource",title:"数据资源目录",clazz:"icon_nav"},
				{url:"/manage/application-manage",title:"申请单管理",clazz:"icon_update"},
				{url:"/manage/query",title:"数据查询",clazz:"icon_update"}
			]}},
			methods: {
				onLogin: function () { this.$refs.login.show() },
				logout: function () {
					Utils.Get("/login.logout", null, function () {
						alert("退出成功,确定后将重新登录!")
						Utils.Reload("/manage")
					})
				},
				getUrl:function(url){
					return Utils.GetAbsUrl(url)
				},
				search:function(){
					this.$refs.search.show()
					this.results=null
					var _this = this;
					GetJSON('/management/SearchTableModels.load', {query:this.query}, function (data) {
						_this.results = data
						console.log(data)
					});
				},
				addToList:function(){
					var form = $(this.$refs.search.$el).find("form")
					var data = form.serialize() 
					Assert(data.length, "请选择要申请的逻辑模型")
					var _this = this
					Post("/logic-request/add-tables.post", data,
					  function(data){
		    			Assert(data=="ok", data)
							VueAlert.Success('添加成功')
		    	})
				},
			}, 
			components: {
				home: Home, 'rs-models': Addon.ResModelTable
			},
			template: `
  <div style="margin:0 auto; max-width: 1500px;">
<nav class="navbar margin-bottom-md text-center border-none">
		<div class="collapse navbar-collapse " >
			<ul class="nav navbar-nav  nav-head">
				<li class="dropdown nav-dropdown">
					<a href="#" class="dropdown-toggle relative" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
						<div class="icon_nav icon absolute"></div>
						<span class="light-font">首页</span>
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu overflow-hidden">
						<li active-class="_active" v-for="nav in navs"> 
							<a class="pointer relative" :href="getUrl(nav.url)">
									<div class=" absolute" style="top: 20px;"
										:class="[nav.clazz]"></div>
									<span class="light-font" style="margin-left: 30px;"></span> {{nav.title}}	</a> 
							</li> 
					</ul>
				</li>
			</ul>
			
			<a href="/data-center" class="pointer"> <img class="padding-top-sm" alt="Brand" src="/data-center/image/logo.png" > </a>
			
			<ul class="nav navbar-nav nav-head pull-right"> 
				<li class=""> 
					<a class="relative pointer" @click="onLogin">
					<span class="inline-block"  >
					<div class="icon icon_profile absolute" ></div>
					</span><!---->
					</a>
				</li>
			</ul>

			<div class="navbar-form navbar-right margin-none padding-top-sm">
        <div class="form-group">
          <input type="text" class="form-control input-sm" placeholder="Search"
						v-model="query">
        </div>
        <button type="button" class="btn btn-default btn-sm light-font"
					@click="search">检索</button>
      </div>

		</div><!-- /.navbar-collapse -->
</nav>

			<home></home>
			<modal ref="login" :footer="true" :on-confirm="logout" :sm="true">
				<h4 slot="title">登录管理</h4>
				<h5 slot="body" class="text-center">尊敬的 {{GetUserName()}} 确定退出吗?</h5>
			</modal>

			<modal ref="search" :footer="true" :on-confirm="addToList"  >
				<h4 slot="title">检索结果</h4>
				<div slot="body">
					<form class="">
					<h5 class="text-center">确定将选中的逻辑模型加入待申请列表</h5>
					<rs-models :tables="results" :detail="function(){}"
						valueName="tableModelId" inputName="objectIds" :type="4"></rs-models>
					</form>
				</div>
			</modal>
		</div>
				`,
		}).$mount('#vue-router')
	})

requirejs(['./common'], function (common) {
	requirejs(['app/home/main',]);
});