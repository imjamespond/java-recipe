define('utils/vue-table', ['require', 'exports', 'module', 'vue', 'utils/commons'],
	function (require, exports, module) { 
		var Vue = require('vue')

Vue.component('vue-table-oper', {
	template : `
<span >
	<a class="glyphicon glyphicon-search font-md" href="javascript:void(0)" v-if="args.operation&1" @click="args.view(rowObj)"></a>
	<a class="glyphicon glyphicon-pencil font-md" href="javascript:void(0)" v-if="args.operation&2" @click="args.modify(rowObj)"></a>
	<a class="glyphicon glyphicon-remove font-md" href="javascript:void(0)" v-if="args.operation&4" @click="args.delete(rowObj)"></a>
	<a class="glyphicon glyphicon-ok     font-md" href="javascript:void(0)" v-if="args.operation&8" @click="args.ok(rowObj)"></a>
</span>`,
	props:{args:Object, rowObj:Object, host:Object}
})

Vue.component('vue-table', {
	template: `
<div class="table-responsive" style="min-height: 30px;"><!--v-bind:class="isFull ? 'full-td' : 'auto-td'"-->
	<table class="table table-bordered table-striped table-condensed margin-bottom-none">
	<thead>
		<tr v-if="compAttributes">
			<th v-for="attribute in compAttributes" class="resizable">{{attribute.desc}}</th>	
			
			<th v-for="extra in extras" class="text-center oper-th">
				<component v-if="extra.th" :is="extra.th" :args="extra.args" >
				</component>
				<span v-else>{{extra.title}}</span>
			</th>
		</tr>
	</thead>
	<tbody v-if="compAttributes">
		<tr v-for="(item,id) in compItems" :class="{success:activeId>=0&&activeId==id}" click="activeId=id">
			<td v-for="attribute in compAttributes">
				<span v-if="null!=attribute.html" v-html="getHtml(item.data[attribute.attr], attribute.html)"></span>
				<span v-else>{{getContent(item,attribute.attr)}}</span>
			</td>
			
			<td v-for="extra in extras" class="text-center">
				<component v-if="extra.td" :is="extra.td" :args="extra.args" 
					:row-obj="item" :row-id="id">
				</component>
			</td>
		</tr>
	</tbody>
	<tbody v-else>
		<tr v-for="item in compItems">
			<td v-for="(value,key) in item">{{value}}</td>
		</tr>
	</tbody>
	</table>
</div>`,
	props: {
		compItems: Array,
		compAttributes: Array,
		extras: Array
	},
	data:function(){return { activeId:-1 }},
	methods: {
		getHtml: function (data, html) {
			if ('glyphicon' == html) return '<span class="' + data + '"></span>';
			return data;
		},
		getContent:function(item, attr) {
			var data = item.data ? item.data : item
			if(data != null &&  data[attr] != null){
				var obj = data[attr]
				if(Array.isArray(obj)){
				  var rs = ""
				  for(i in obj)
				    rs+=obj[i]
				  return rs
				} else 
				  return obj
			}
			return '';
		},
		active: function(id){
			this.activeId = id
		}
	},
	watch:{
		compAttributes:function(){
			var thiscomp = this
			this.$nextTick(function(){
			    //var resizables = $(this.$el).find(".resizable")//uncompatible with table-responsive,at least the last th must not added with .resizable
			    //resizables.resizable({handles: 'e',minWidth: 15})
			})
		}
	}
});
		
Vue.component('vue-obj-table', {
	template: `
<div class="table-responsive">
	<table class="table table-bordered table-striped table-condensed">
	<thead>
		<tr>
			<th v-for="column in columns"><strong>{{column}}</strong></th>
		</tr>
	</thead>
	<tbody>
		<tr v-for="item in compItems">
			<td v-for="(value,key) in item">{{value}}</td>
		</tr>
	</tbody>
	</table>
</div>
	`, 
	props: {
		compItems: Array
	},
	data:function(){
		return {columns:[]}
	},
	methods:{
	},
	watch:{
		compItems:function(){
			if(this.compItems&&this.compItems.length){
				this.columns = []
				for ( var col in this.compItems[0]) { this.columns.push(col) }
			}
		}
	}
		
})

Vue.component('vue-breadcrumb-table', {
	template: `
<div>
	<ol class="breadcrumb">
	  <li><a href="#">根目录</a></li>
	  <li v-for="(value, index) in breadcrumbs" :class="{active:index==breadcrumbs.length-1}">
		<span v-if="index==breadcrumbs.length-1" >{{value}}</span>
		<a href="#" v-else @click="gotoBreadcrumb(index)">{{value}}</a>
	  </li>
	</ol>
	<div class="table-responsive">
		
		<table class="table table-bordered table-striped table-condensed">
		<thead>
			<tr>
				<th v-for="column in columns"><strong>{{column}}</strong></th>
			</tr>
		</thead>
		<tbody>
			<tr v-for="item in compItems">
				<td v-for="(value,key, index) in item">
				<span v-if="index">{{value}}</span>
				<a @click="addBreadcrumb(value)" v-else>{{value}}</a>
				</td>
			</tr>
		</tbody>
		</table>
	</div>
</div>
	`, 
	props: {
		compItems: Array
	},
	data:function(){
		return {columns:[], breadcrumbs:[]}
	},
	methods:{
		addBreadcrumb:function(val){
			this.breadcrumbs.push(val)
		},
		gotoBreadcrumb:function(idx){
			var size = this.breadcrumbs.length-idx-1
			this.breadcrumbs.splice(idx+1,size)
		}
	},
	watch:{
		compItems:function(){
			if(this.compItems&&this.compItems.length){
				this.columns = []
				for ( var col in this.compItems[0]) { this.columns.push(col) }
			}
		}
	}
		
})

	})