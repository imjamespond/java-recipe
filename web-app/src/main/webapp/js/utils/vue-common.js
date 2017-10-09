
define('utils/vue-common', ['require', 'exports', 'module',
	'vue', 'lodash', 'utils/commons'],
	function (require, exports, module) {
		console.log('vue-common init..')

		//var $ = require('jquery')
		var Vue = require('vue')
		var _ = require('lodash')

		Vue.component('vue-itemlist', {
			template: `
	<span>
		<span v-for="(item, index) in itemList" >{{index>0&&itemList.length>1?'，':''}}{{getAlias(item)}}</span>
	</span>`,
			props: {
				itemList: Array,
				aliasMap: Object
			},
			methods: {
				getAlias: function (item) {
					var alias = null != this.aliasMap && this.aliasMap[item] ? this.aliasMap[item] : item
					return alias
				}
			}
		})

		Vue.component('well', {
			template: `
<div class="well">
	<slot name="title">default title</slot>
	<slot name="body">
      default body
	</slot>
</div>
<!-- /.well -->`
		})

		Vue.component('abs-form', {
			template: `
<div class="full-height" :class="{'form-horizontal':horizontal}">
<div class="full-height relative lightblue-bg info-border" style="border-left-width:3px;border-left-style:solid;">
	<div class=" padding-md">
		<slot name="head"> default title </slot>
	</div>
	<div class="absolute overflow-auto scrollbar scrollbar-md padding-left-md" 
	 :style="{top:top, bottom:bottom, left:left, right:right}">
		<div class="bg-white " 
			:style="{'padding':'0 15px 10px 15px'}">
			<slot name="body">default body	</slot>
		</div>
	</div>
</div>
</div>`,
			props: {
				horizontal: Boolean
				, top: { default: '50px' }, bottom: { default: '0' }, left: { default: '0' }, right: { default: '0' },
			}
		})
		Vue.component('abs-table', {
			template: `
<div class="full-height relative">
	<div class="absolute overflow-auto scrollbar scrollbar-md" 
		:style="{top:top, bottom:bottom, left:left, right:right}">
		<table class="table table-striped table-bordered 
			ds-table fixed-table break-word margin-none">
			<slot name="thead">	</slot>
			<slot name="tbody">default tbody	</slot>
		</table>
	</div>
	<slot name="page"></slot>
</div>`,
			props: { top: { default: '0' }, bottom: { default: '0' }, left: { default: '0' }, right: { default: '0' }, }
		})
		Vue.component('abs-box', {
			template: `
<div class="full-height relative">
	<slot name="head"> </slot>
	<div class="absolute overflow-auto scrollbar scrollbar-md" 
		:class="bodyClass"
		:style="{top:top, bottom:bottom, left:left, right:right}">
		<slot name="body">default body	</slot>
	</div>
	<slot name="footer"></slot>
</div>`,
			props: {
				top: { default: '0' }, bottom: { default: '0' }, left: { default: '0' }, right: { default: '0' },
				bodyClass: Array
			}
		})


		Vue.component('dropdown', {
			template: `
<div class="dropdown">
  <button type="button" class="btn btn-default" 
		data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
    {{title}}
    <span class="caret"></span>
  </button>
  <ul class="dropdown-menu" aria-labelledby="dLabel">
    <li v-for="(item,i) in itemList"><a class="pointer">{{item}}</a></li>
		<li v-for="obj in objects">
			<a class="pointer" @click="$emit('DropDownEvent',obj)">{{obj[objTitle]}}</a>
		</li>
  </ul>
</div>`,
			props: { itemList: Array, objects: Array, objTitle: String, title: String },
			methods: {
				dropdown: function () {
					$(this.$el).dropdown()
				}
			},
			computed: {
			}
		})

		Vue.component('vue-debounce-btn', {
			template: `<button v-bind:type="type" v-bind:class="clazz" name="action" 
	  v-bind:value="value" v-bind:disabled="disabled"
	  @click="on_click">{{name}}</button>`,
			props: {
				name: { "default": "提交", type: String },
				debounceMillis: { "default": 2000, type: Number },
				value: { "default": "foobar", type: String },
				disabled: { "default": false, type: Boolean },
				type: { "default": "button", type: String },
				clazz: { "default": "btn btn-default", type: String },
				callback: Function
			},
			mounted: function () {

			},
			methods: {
				on_click: function () {
					if (!this.callback) return
					this.callback()
					$(this.$el).prop("disabled", true)
					this.getDebounce()()
				},
				getDebounce: function () {
					var _this = this
					return _.debounce(function () {
						$(_this.$el).prop("disabled", false)
					}, _this.debounceMillis)
				}
			}
		})

		Vue.component('vue-submit', {
			template: `
<button :type="type" class="btn" aria-label="Left Align" :disabled="loading" @click="on_click">
  <span class="margin-right-md glyphicon glyphicon-hourglass spinner"
    v-if="loading" :class="isLoading" aria-hidden="true"></span>{{name}}
</button>
	`,
			data: function () { return { loading: false } },
			props: {
				name: { default: "提交", type: String },
				type: { default: "submit", type: String },
				callback: Function
			},
			methods: {
				on_click: function () {
					if (this.callback) {
						this.loading = true
						try {
							this.callback(this)
						} catch (error) {
							console.log(error)
							this.loading = false
						}
					}
				}
			},
			computed: {
				isLoading: function () {
					return this.loading ? [''] : []
				}
			}
		})

		Vue.component('vue-filter', {
			template: `
<input class="form-control" :placeholder="placeholder"
	type="text" v-model:ignore="filter" v-on:keydown.enter.stop.prevent="dummy">`,
			props: {
				onSubmit: Function,
				one: Object, objects: Array, strings: Array, objKeys: Array, placeholder: String,
			},
			data: function () { return { filter: '' } },
			methods: {
				do_filter: _.debounce(function () {
					var _this = this
					var objs = null
					if (this.one != null) {
						objs = {}
						$.each(this.one, function (idx, val) {
							if (idx.toLowerCase().indexOf(_this.filter.toLowerCase()) >= 0)
								objs[idx] = val
						})
					} else if (this.objects != null) {
						objs = []
						$.each(this.objects, function (idx, val) {
							var binggo = false
							for (var i in _this.objKeys) {
								var objKey = _this.objKeys[i]
								if (val[objKey].toLowerCase().indexOf(_this.filter.toLowerCase()) >= 0)
									binggo = true
							}
							if (binggo)
								objs.push(val)
						})
					} else if (this.strings != null) {
						objs = []
						$.each(this.strings, function (idx, val) {
							if (val.toLowerCase().indexOf(_this.filter.toLowerCase()) >= 0)
								objs.push(val)
						})
					}
					this.$emit('FilterEvent', objs)
				}, 1000),
				dummy: function () { }
			},
			watch: {
				filter: function (data) {
					this.filter = data;
					this.do_filter();
				}
			}
		})

		Vue.component('check-all', {
			template: `
	<input type="checkbox" class="pointer" :checked="checked" @click="on_select"> `,
			props: { clazz: String, target: Object, jqWay: { type: Boolean, default: true } },
			data: function () { return { checked: 0 } },
			methods: {
				on_select: function () {
					this.checked ^= 1
					if (!this.jqWay) {
						this.$emit('CheckAllEvent', this.checked)
						return
					}
					var elem = this.target ? this.target.$el : this.$parent.$el
					$(elem).find('.' + this.clazz + ' input').prop("checked", this.checked);
				},
				clear: function () {
					var elem = this.target ? this.target.$el : this.$parent.$el
					$(elem).find('.' + this.clazz + ' input').prop("checked", false);
				},
				get_selected: function () {
					var elem = this.target ? this.target.$el : this.$parent.$el
					return $(elem).find('.' + this.clazz + ' input:checked')
				}
			}
		})
		Vue.component('vue-select-all', {
			template: `
	<span class="pointer" @click="on_select">
			{{select?'全选':'取消全选'}}
	</span>`,
			props: {
				clazz: String, title: String, target: String,
				select: { type: Boolean, default: true }
			},
			methods: {
				on_select: function () {
					var elem = this.$parent.$el
					if (this.target)
						elem = this.$parent.$refs[this.target].$el
					$(elem).find('.' + this.clazz + ' input').prop("checked", this.select);
				},
				clear: function () {
					var elem = this.$parent.$el
					if (this.target)
						elem = this.$parent.$refs[this.target].$el
					$(elem).find('.' + this.clazz + ' input').prop("checked", false);
				},
				get_selected: function () {
					var elem = this.$parent.$el
					if (this.target)
						elem = this.$parent.$refs[this.target].$el
					return $(elem).find('.' + this.clazz + ' input:checked')
				}
			}
		})

		Vue.component('vue-helper', {
			template:
			`
<div class="full-height">
<div class="panel ds-panel margin-none">
	<div class="_bg">
		<div class="panel-heading _info relative">
			<div class="icon_guide absolute"></div>
			<b class="ft5" style="margin-left:30px;">用户指南</b>
			<button type="button" class="btn btn-primary btn-xs " @click=hide
				style="margin-left:60px;margin-top:-2px;width:48px;height:28px;">隐藏
			</button>
		</div>
	</div>
	<div class="panel-body" v-html="$root.helperMsg"> 
	</div>
</div>
</div>
`,
			data: function () { return {} },
			methods: {
				hide: function () { this.$root.helper = 0 }
			},
			watch: {
				'$root.helperMsg': function (newVal, oldVal) { },
				'$root.helper': function (newVal, oldVal) { }
			}
		})

		Vue.component('modal', {
			template: `
<div v-bind:id="modalId" class="modal fade" tabindex="-1" role="dialog">
		<div class="modal-dialog" role="document" v-bind:class="{ 'modal-sm': sm, 'modal-lg': lg }">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">
            	<slot name="title">
              		default title
            	</slot>
				</h4>
			</div>
			<div :id="bodyId" class="outter-body">
				<div class="modal-body">
				<slot name="body"> </slot>
				</div>
			</div>
			<div class="modal-footer" v-if="footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" data-dismiss="modal" @click="on_confirm">确定</button>
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal-dialog -->
</div>
<!-- /.modal -->`,
			props: {
				sm: false, lg: false, footer: false,
				modalTitle: String, modalId: String, bodyId: String,
				onConfirm: Function
			},
			data: function () {
				return {};
			},
			methods: {
				show: function () {
					$(this.$el).modal('show')
				},
				hide: function () {
					$(this.$el).modal('hide')
				},
				on_confirm: function () {
					this.onConfirm()
				}
			}
		})

		Vue.component('loading', {
			template: `
<div class="loadingCirclesG" v-once style="vertical-align: middle;">
	<div class="f_circleG" id="frotateG_01"></div>
	<div class="f_circleG" id="frotateG_02"></div>
	<div class="f_circleG" id="frotateG_03"></div>
	<div class="f_circleG" id="frotateG_04"></div>
	<div class="f_circleG" id="frotateG_05"></div>
	<div class="f_circleG" id="frotateG_06"></div>
	<div class="f_circleG" id="frotateG_07"></div>
	<div class="f_circleG" id="frotateG_08"></div>
</div>
`,
		})

		function tooltip(elem, title) {
			var tooltip = $(elem).tooltip({
				title: title, trigger: "manual click", placement: "bottom",
				template: '<div class="tooltip tooltip-warning" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>'
			}).tooltip("show");
			_.delay(function () {
				tooltip.tooltip('destroy')
			}, 3000)
		}

		//Register a global custom directive
		//Vue.directive('demo', {})
		var mixins = {
			/*directives: {
				focus: {
				inserted: function (el) {
				el.focus()
				}
			}
			},not allow used in mixin*/
			methods: {
				AbsUrl: function (url) {
					return GetAbsUrl(url)
				},
				Action: function () {
					if (this.formType == 1) return "新增"
					else if (this.formType == 2) return "修改"
					return "查看"
				},
				showDate: function (date) {
					return new Date(date).toLocaleString()
				},
				update: function (data) {
					for (var key in data) {
						this[key] = data[key];
					}
				},
				VueDirective: function () {
					Vue.directive('model', function (el, binding) {
						if (binding.arg && binding.arg == "ignore") { console.log(binding.arg); return }
						el.name = binding.expression;
					});
				},
				CheckInputs: function (_form) {
					var elem = null == _form ? this.$el : _form.$el
					var inputs = $(elem).find("input, textarea")
					inputs.each(function () {
						var maxlen = $(this).attr("maxlen")
						if (maxlen && $(this).val().length > maxlen) {
							var ex = "超出最大长度" + maxlen + "个字"
							tooltip(this, ex)
							throw ex
						}
						var required = $(this).attr("required")
						if (required && $(this).val().length == 0) {
							var ex = "不能为空"
							tooltip(this, ex)
							throw ex
						}
					})
				},
				DisableForm: function () { var inputs = $(this.$el).find(":input"); inputs.prop("disabled", true); },
				ClearData: function (_form) {
					if (_form == null) _form = this
					try {
						Object.assign(_form.$data, _form.$options.data());
					} catch (error) {
						$.extend(_form.$data, _form.$options.data());
					}
				},
				GetDomainId: function () { return gTenantId },
				GetUserId: function () { return gUserId },
				GetUserName: function () { return gUserName },
				CheckSysAdmin: function () { return 1 & gRoles },
				CheckTenant: function () { return 2 & gRoles },
				CheckDataAdmin: function () { return 4 & gRoles },
				CheckCustomer: function () { return 8 & gRoles },
				CheckSysAdminOrTenant: function () { return gRoles & (1 | 2) },
				CheckTenantDataProvider: function () {
					return 6 == (gRoles & (4 | 2)) || (gRoles & 1)
				}
			}
		}

		var loading = {
			view: new Vue({
				template:
				`<div id="vue-loading" v-if="if_show"></div>
				`,
				created: function () {
					var thiscomp = this
					$(function () {
						$('<div id="vue-loading"></div>').appendTo('body')
						thiscomp.$mount('#vue-loading')
					})
				},
				data: function () { return { if_show: 0 } },
			}),
			show: function () { VueLoading.view.if_show = 1 },
			hide: function () { VueLoading.view.if_show = 0 }
		}

		module.exports = { mixins: mixins, Loading:loading }
	})