<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>创建类型</title>
</head>
<body>
	<ol class="breadcrumb">
		<li class="active">编辑</li>
	</ol>

	<div style="width: 800px;margin:0 auto;">
		<form:form modelAttribute="manager" role="form"
			action="/manager/update" method="post" class="form-horizontal">
			<div class="form-group" style="margin-top: 50px;">
				<label class="col-sm-3 control-label"><span class="text-danger">*</span>用户名</label>
				<div class="col-sm-7">
					<form:input path="username" class="form-control" />
				</div>
			</div>
			<form:input type="hidden" path="id" class="form-control" />
			<div class="form-group">
				<label class="col-sm-3 control-label"><span class="text-danger">*</span>邮箱</label>
				<div class="col-sm-7">
					<form:input path="email" class="form-control" />
				</div>
			</div>
			<c:if test="${sessionScope.manager eq 1}">
			<div class="form-group">
				<label class="col-sm-3 control-label"><span class="text-danger">*</span>是否激活</label>
				<div class="col-sm-7">
					<select id="state1" class="form-control">
					<option value="0">否</option>
					<option value="1">是</option>
					</select>
				</div>
				<form:input path="state" type="hidden" id="state2" />
			</div>
			<div class="form-group">
				<label class="col-sm-3 control-label"><span class="text-danger">*</span>角色</label>
				<div class="col-sm-7">
					<select multiple="multiple" id="select1"  class="form-control" style="width: 170px;height:320px;float: left;">
						<option value = "stage">千寻小公主管理员</option>
						<option value = "manager">权限管理员</option>
						<option value = "user">用户管理员</option>
						<option value = "game">游戏管理员</option>
					</select>
					<div class="operate" style="width:90px;margin:0 10px;float:left;text-align:center;">
						<button type="button" id="add" class="btn btn-default" style="width:74px;padding: 2px 5px;font-size:12px;margin-bottom: 3px;">添加》</button>
						<button type="button" id="add_all" class="btn btn-default" style="width:74px;padding: 2px 5px;font-size:12px;margin-bottom: 3px;">全部添加》</button>
						<button type="button" id="remove" class="btn btn-default" style="width:74px;padding: 2px 5px;font-size:12px;margin-bottom: 3px;">《删除</button>
						<button type="button" id="remove_all" class="btn btn-default" style="width:74px;padding: 2px 5px;font-size:12px;">《全部删除</button>
					</div>
					<select multiple="multiple" id="select2" class="form-control" style="width: 170px;height:320px;float: left;">
					</select>
					<form:input path="roles" type="hidden" id="roles" />
				</div>
			</div>
			</c:if>
			<div class="form-group">
				<div class="col-sm-offset-3 col-sm-7">
					<button type="button" onclick="beforeSubmit()" class="btn btn-default">提交</button>
				</div>
			</div>
		</form:form>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		bindSel();
		setRoles()
		setState();
	});
	function setState(){
		var state =  $('#state2').val()
		$('#state1').find("option[value='" + state + "']").attr("selected",true);
	}
	function setRoles(){
		var names = new Array();
		var keys = new Array();
		keys = $('#roles').val().split(',');
		for(j=0;j<keys.length;j++){
			var name = '';
			if(keys[j]=='manager'){
				name = '权限管理员';
			}
			if(keys[j]=='user'){
				name = '用户管理员';
			}
			if(keys[j]=='game'){
				name = '游戏管理员';
			}
			if(keys[j]=='stage'){
				name = '千寻小公主管理员';
			}
			names[j] = name;
		}
		for(i=0;i<keys.length;i++){
			$('#select1').find("option[value='" + keys[i] + "']").remove();
			$('#select2').append('<option value="'+keys[i]+'">'+names[i]+'</option>')
		}
	}
	function beforeSubmit(){
		var state = $('#state1').val()
		var list = $('#select2 option'),
			str = "";
		for(i=0;i<list.length;i++){
			str += list.eq(i).val()+",";
		}
		var roles = str.substring(0,str.lastIndexOf(','));
	    $('#roles').val(roles);
	    $('#state2').val(state);
	    $('form').submit();
	}
	function bindSel(){
		//移到右边
	    $('#add').click(function() {
	        $('#select1 option:selected').appendTo('#select2');
	    });
	    //移到左边
	    $('#remove').click(function() {
	        $('#select2 option:selected').appendTo('#select1');
	    });
	    //全部移到右边
	    $('#add_all').click(function() {
	        $('#select1 option').appendTo('#select2');
	    });
	    //全部移到左边
	    $('#remove_all').click(function() {
	        $('#select2 option').appendTo('#select1');
	    });
	    //双击选项
	    $('#select1').dblclick(function(){
	        $("option:selected",this).appendTo('#select2'); 
	    });
	    //双击选项
	    $('#select2').dblclick(function(){
	       $("option:selected",this).appendTo('#select1');
	    });
	}
	</script>
</body>
</html>