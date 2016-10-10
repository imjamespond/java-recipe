/* you provide variables below
 var jstreeNode = $('#subject-tree');
 var search = $('#search_input');
 var delete_func = function (sel, jstreeRef)
 var rename_func = function (node, status)
 var create_func = function (node, status)
*/
 var jstreeNode = null;
 var search 	= null;
 var delete_func = null;
 var rename_func = null;
 var create_func = null;
function tree_create() {
	var ref = jstreeNode.jstree(true), sel = ref.get_selected();
	if (!sel.length) {
		sel = ref.create_node("#", {
			"type" : "file",
			"text" : "新增主题"
		});
		if (sel) {
			ref.edit(sel, null, create_func);
		}
	}
	sel = sel[0];
	sel = ref.create_node(sel, {
		"type" : "file",
		"text" : "新增子级主题"
	});
	if (sel) {
		ref.edit(sel, null, create_func);
	}
};
function tree_rename() {
	var ref = jstreeNode.jstree(true), sel = ref.get_selected();
	if (!sel.length) {
		return false;
	}
	sel = sel[0];
	ref.edit(sel, null, rename_func);
};
function tree_delete() {
	var ref = jstreeNode.jstree(true), sel = ref.get_selected();
	if (!sel.length) {
		return false;
	}
	delete_func(sel, ref);
};

function InitJstree(treedata) {
	if (null == jstreeNode)
		return;

	jstreeNode.jstree({
		"plugins" : [ "dnd", "search", "state", "types", "wholerow" ],
		'core' : {
			"check_callback" : true,
			'data' : treedata
		}
	});

	jstreeNode.on('create_node.jstree', function(e, data) {
		// console.log(data.parent+','+data.node.id+','+data.node.text);
	}).jstree();
	jstreeNode.on('move_node.jstree', function(e, data) {
		// console.log(data.node.id+','+data.parent);
		rename_func(data.node, true);
	}).jstree();

	var preSelected = null;
	jstreeNode.on('select_node.jstree', function(e, data) {
		if (preSelected && preSelected == data.selected[0]) {
			jstreeNode.jstree(true).deselect_node(data.selected[0], true);
		}
		preSelected = data.selected[0];
	}).jstree();

	if (null == search)
		return;
	var to = false;
	search.keyup(function() {
		if (to) {
			clearTimeout(to);
		}
		to = setTimeout(function() {
			var v = search.val();
			jstreeNode.jstree(true).search(v);
		}, 250);
	});
}