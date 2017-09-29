define('utils/vue-alert', ['require', 'exports', 'module', 'jquery','vue', 'utils/commons'],
	function (require, exports, module) {

		console.log("vue-alert init");
		var Vue = require('vue')
		var VueAlert = {
			view: new Vue({
				template: `
<div class="alert-fixed">
  <transition-group
    name="alert-list"
    tag="div"
    v-on:leave="leave" >
    <div class="alert alert-dismissible alert-list-item" role="alert" 
			v-bind:class="elem.style" 
			v-bind:key="elem.key" 
			v-for="(elem, index) in elems" >
			<span class="msgbox">
	            {{elem.msg}}
			</span>
			<button type="button" class="close" aria-label="Close" v-on:click="alertRemove(index)">
				<span aria-hidden="true">&times;</span>
			</button>
		
	</div>
  </transition-group>
</div>
`,
				created: function () { 
					var thiscomp = this
					$(function () {
						$('<div id="vue-alert"></div>').appendTo('body')
						thiscomp.$mount('#vue-alert')
					})
				},
				data: { elems: [] },
				methods: {
					alertRemove: function (index) {
						this.elems.splice(index, 1);
					},
					alertAdd: function (message, style) {
						this.elems.push({ 'msg': message, 'style': style, 'key': message + Math.random() });
						var elems = this.elems
						window.setTimeout(function (ems) { elems.splice(0, 1); }, 5000, elems);
					},
					leave: function (el) {
						console.log("leave")
						$(el).css({ opacity: 0, transform: "translateX(100px)" })
					}
				}
			}),
			Success: function (message) {
				this.view.alertAdd(message, { 'alert-success': true });
			},
			Warning: function (message) {
				this.view.alertAdd(message, { 'alert-warning': true });
			},
			Danger: function (message) {
				this.view.alertAdd(message, { 'alert-danger': true });
			}
		}

		module.exports = VueAlert;
	})