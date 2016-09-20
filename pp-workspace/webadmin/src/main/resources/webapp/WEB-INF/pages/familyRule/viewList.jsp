<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
	<table id='familyRuleGrid' class="easyui-datagrid" url="familyRule/list" pagination="true" rownumbers="true" fitColumns="true">
	</table>


	<div id="familyRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#familyRule-buttons">
		<div class="ftitle">标题</div>
		<form id="familyRuleForm" method="post" novalidate>
			<div class="fitem"><label>id:</label><input name="id" class="easyui-validatebox" required="true"></div>
			<div class="fitem"><label>name:</label><input name="name" class="easyui-validatebox" required="true"></div>
			<div class="fitem"><label>starName:</label><input name="starName" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem"><label>starId:</label><input name="starId" class="easyui-validatebox" required="true"></div>
			<div class="fitem"><label>starIcon:</label><input name="starIcon" class="easyui-validatebox"></div>
			<div class="fitem"><label>maxMember:</label><input name="maxMember" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>gameCoinDevote:</label><input name="gameCoinDevote" class="easyui-validatebox" required="true"></div>
			<div class="fitem">
				<label>goldCoinDevote:</label><input name="goldCoinDevote" class="easyui-validatebox" required="true"></div>
			<div class="fitem">
				<label>gameCoinFunds:</label><input name="gameCoinFunds" class="easyui-validatebox" required="true"></div>
			<div class="fitem">
				<label>goldCoinFunds:</label><input name="goldCoinFunds" class="easyui-validatebox" required="true"></div>
			<div class="fitem"><label>fundsLimit:</label><input name="fundsLimit" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>propsDevote:</label><input name="propsDevote" class="easyui-validatebox" required="true"></div>
			<div class="fitem"><label>tasksLimit:</label><input name="tasksLimit" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem">
				<label>welcomeCoin:</label><input name="welcomeCoin" class="easyui-validatebox" required="true"></div>
			<div class="fitem"><label>changeCoin:</label><input name="changeCoin" class="easyui-validatebox" required="true">
			</div>
			<div class="fitem"><label>words:</label><input name="words" class="easyui-validatebox" required="true"></div>
			<div class="fitem">
				<label>treeRipeTime1:</label><input name="treeRipeTime1" class="easyui-validatebox" required="true"></div>
			<div class="fitem">
				<label>treeRipeTime2:</label><input name="treeRipeTime2" class="easyui-validatebox" required="true"></div>
		</form>
	</div>

	<div id="familyRule-buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveFamilyRule()">保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#familyRuledlg').dialog('close')">取消</a>
	</div>

	<script type="text/javascript">
		$('#familyRuleGrid').datagrid({
			height: "auto",
			fit: true,
			idField: 'id', method: 'GET', url: 'familyRule/list', pagination: true, rownumbers: "true", fitColumns: "true", frozenColumns: [
				[
					{field: 'ck', checkbox: true}
				]
			], columns: [
				[
					{field: 'id', title: 'id'}
					,
					{field: 'name', title: 'name'}
					,
					{field: 'starName', title: 'starName'}
					,
					{field: 'starId', title: 'starId'}
					,
					{field: 'starIcon', title: 'starIcon'}
					,
					{field: 'maxMember', title: 'maxMember'}
					,
					{field: 'gameCoinDevote', title: 'gameCoinDevote'}
					,
					{field: 'goldCoinDevote', title: 'goldCoinDevote'}
					,
					{field: 'gameCoinFunds', title: 'gameCoinFunds'}
					,
					{field: 'goldCoinFunds', title: 'goldCoinFunds'}
					,
					{field: 'fundsLimit', title: 'fundsLimit'}
					,
					{field: 'propsDevote', title: 'propsDevote'}
					,
					{field: 'tasksLimit', title: 'tasksLimit'}
					,
					{field: 'welcomeCoin', title: 'welcomeCoin'}
					,
					{field: 'changeCoin', title: 'changeCoin'}
					,
					{field: 'words', title: 'words'}
					,
					{field: 'treeRipeTime1', title: 'treeRipeTime1'}
					,
					{field: 'treeRipeTime2', title: 'treeRipeTime2'}
				]
			], toolbar: [
				{
					text: '增加',
					iconCls: 'icon-add',
					handler: function () {
						newFamilyRule();
					}
				},
				{
					text: '修改',
					iconCls: 'icon-edit',
					handler: function () {
						editFamilyRule();
					}
				},
				{
					text: '删除',
					iconCls: 'icon-remove',
					handler: function () {
						destroyFamilyRule();
					}
				}
			]

		});


		var purl;
		function newFamilyRule() {
			$('#familyRuledlg').dialog('open').dialog('setTitle', '增加');
			$('#familyRuleForm').form('clear');
			purl = 'familyRule/add';
		}
		function editFamilyRule() {
			var row = $('#familyRuleGrid').datagrid('getSelected');
			if (row) {
				$('#familyRuledlg').dialog('open').dialog('setTitle', '修改');
				$('#familyRuleForm').form('clear');
				$('#familyRuleForm').form('load', row);
				purl = 'familyRule/update/' + row.id;
			}
		}
		function saveFamilyRule() {
			$('#familyRuleForm').form('submit', {
				url: purl,
				method: "POST",
				data: "json",
				onSubmit: function () {
					return $(this).form('validate');
				},
				success: function (data) {
					var result = eval('(' + data + ')');
					if (result.code != 'ok') {
						$.messager.show({
							title: 'Error',
							msg: result.message
						});
					} else {
						$('#familyRuledlg').dialog('close'); // close the dialog
						$('#familyRuleGrid').datagrid('reload'); // reload the user data
					}
				}

			});
		}
		function destroyFamilyRule() {
			var row = $('#familyRuleGrid').datagrid('getSelected');
			if (row) {
				$.messager.confirm('Confirm', '确定要删除吗?', function (r) {
					if (r) {
						$.post('familyRule/del/' + row.id, function (result) {
							if (result.code == 'ok') {
								$('#familyRuleGrid').datagrid('reload'); // reload the user data
							} else {
								$.messager.show({ // show error message
									title: 'Error',
									msg: result.errorMsg
								});
							}
						}, 'json');
					}
				});
			}
		}
	</script>
	<style type="text/css">
		#familyRuleForm {
			margin: 0;
			padding: 10px 30px;
		}

		.ftitle {
			font-size: 14px;
			font-weight: bold;
			padding: 5px 0;
			margin-bottom: 10px;
			border-bottom: 1px solid #ccc;
		}

		.fitem {
			margin-bottom: 5px;
		}

		.fitem label {
			display: inline-block;
			width: 80px;
		}
	</style>
</body>
</html>