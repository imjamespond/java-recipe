define('utils/vue-pagination', ['require', 'exports', 'module', 'vue', 'utils/commons'],
	function (require, exports, module) { 
		var Vue = require('vue')

const reg = /^-?\d+$/;
Vue.component('vue-page', {
	template : `
<ul class="pagination" v-bind:class="{'pagination-sm':sm}">
	<li v-if="isFirstPage"><a href="#" @click.prevent="gotoPage(1)" aria-label="Previous">首页</a></li>
	<!--<li v-if="isFirstPage"><a href="#" @click.prevent="gotoPage(curPage-1)" >
		<span aria-hidden="true">&laquo;</span></a></li>-->
	<li v-for="page in pages" v-bind:class="{ 'active' : page.active}">
		<span v-if="page.active" >{{page.page}}</span>
		<a v-else href="#" @click.prevent="gotoPage(page.page)" >{{page.page}}</a>
	</li>
	<li><a href="#" @click.prevent="jumpToPage()" ><span aria-hidden="true">跳至:</span></a></li>
	<li>
	  <a> <input type='text' maxlength="10" size="2" class="border-none padding-none text-center "
	    v-model="inputPage" @click="inputClick"> </a>
	</li>
	<!--<li v-if="isEndPage"><a href="#" @click.prevent="gotoPage(curPage+1)" >
		<span aria-hidden="true">&raquo;</span></a></li>-->
	<li v-if="isEndPage"><a href="#" @click.prevent="gotoPage(total_pages)" aria-label="Next">尾页</a></li>
	<li ><span>共{{total_pages}}页</span></li>
</ul>
`,
	props : { sm:Boolean,size:String },
	data:function(){return {
		isFirstPage:false,
		isEndPage:false,
		pageSize:10,
		pages:[],
		total:0,
		curPage:1,
		inputPage:0
	}},
	methods : {
		gotoPage:function(pg){
			var oldpg = this.curPage
			this.updatePage(pg)
			this.$emit( "PageEvent", pg, oldpg)
		},
		getPages:function(){
			this.pages = [];
			for(var i=0; i<3; i++){
				var page = this.curPage - 1 + i;
				if (page > 0 && page <= this.total_pages) {
					this.pages.push({'page':page,'active':this.curPage==page?true:false});
				}
			}
		},
		setPage:function(page){
			this.total=page.total
			this.pageSize=page.size
			
			this.updatePage(page.page)
		},
		updatePage:function(pg){
			this.inputPage = pg
			this.curPage = pg
			this.isFirstPage = this.curPage>1
			this.isEndPage = this.curPage<this.total_pages
			this.getPages()
			this.$emit("ShowPageEvent",this.total_pages>1)
		},
		refreshPage:function(){
			this.gotoPage(this.curPage)
		},
		jumpToPage:function(){
			if(this.inputPage==this.curPage)
				return
			this.gotoPage(this.inputPage)
		},
		inputClick:function(event){
			$(event.target).select()
		},
		selfPaginate:function(items){
			var size = this.pageSize
			var offset = (this.curPage-1)*size
			var page = {total:items.length, size:this.pageSize, page:this.curPage}
			var pageItems = []
			this.setPage(page)
			for(var i in items){
				if(i>=offset&&i<(offset+size))
					pageItems.push(items[i])
			}
			return pageItems
		}
	},
	watch:{
		inputPage: function(val, oldVal){
			if(!reg.test(val)){
				this.inputPage = oldVal
			}
		}
	},
	computed:{ 
		total_pages:function(){
			return Math.ceil(this.total / this.pageSize);
		}
	}
})

	})