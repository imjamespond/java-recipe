<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='familyBuildingRuleGrid' class="easyui-datagrid" url="familyBuildingRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="familyBuildingRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#familyBuildingRule-buttons">
        <div class="ftitle">标题</div>
        <form id="familyBuildingRuleForm" method="post" novalidate>
                            <div class="fitem"><label>buildingId:</label><input name="buildingId" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>name:</label><input name="name" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>type:</label><input name="type" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>level:</label><input name="level" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>levelFunds:</label><input name="levelFunds" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>levelRequirement:</label><input name="levelRequirement" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>memo:</label><input name="memo" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>icon:</label><input name="icon" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>maxLevel:</label><input name="maxLevel" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>levelEffect:</label><input name="levelEffect" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>maxFunds:</label><input name="maxFunds" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>maxItem:</label><input name="maxItem" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>boon:</label><input name="boon" class="easyui-validatebox" required="true"></div>
                    </form>
    </div>

    <div id="familyBuildingRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveFamilyBuildingRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#familyBuildingRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#familyBuildingRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'familyBuildingRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'buildingId',title:'buildingId'}
                                                         ,{field:'name',title:'name'}
                                                         ,{field:'type',title:'type'}
                                                         ,{field:'level',title:'level'}
                                                         ,{field:'levelFunds',title:'levelFunds'}
                                                         ,{field:'levelRequirement',title:'levelRequirement'}
                                                         ,{field:'memo',title:'memo'}
                                                         ,{field:'icon',title:'icon'}
                                                         ,{field:'maxLevel',title:'maxLevel'}
                                                         ,{field:'levelEffect',title:'levelEffect'}
                                                         ,{field:'maxFunds',title:'maxFunds'}
                                                         ,{field:'maxItem',title:'maxItem'}
                                                         ,{field:'boon',title:'boon'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newFamilyBuildingRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editFamilyBuildingRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyFamilyBuildingRule();
                }
            }]

        });


        var purl;
        function newFamilyBuildingRule(){
            $('#familyBuildingRuledlg').dialog('open').dialog('setTitle','增加');
            $('#familyBuildingRuleForm').form('clear');
            purl = 'familyBuildingRule/add';
        }
        function editFamilyBuildingRule(){
            var row = $('#familyBuildingRuleGrid').datagrid('getSelected');
            if (row){
                $('#familyBuildingRuledlg').dialog('open').dialog('setTitle','修改');
                $('#familyBuildingRuleForm').form('clear');
                $('#familyBuildingRuleForm').form('load',row);
                purl = 'familyBuildingRule/update/'+row.id;
            }
        }
        function saveFamilyBuildingRule(){
            $('#familyBuildingRuleForm').form('submit',{
                url: purl,
                method:"POST",
                data:"json",
                onSubmit: function(){
                    return $(this).form('validate');
                },
                success: function(data){
                    var result = eval('('+data+')');
                    if (result.code!='ok'){
                        $.messager.show({
                            title: 'Error',
                            msg: result.message
                        });
                    } else {
                        $('#familyBuildingRuledlg').dialog('close'); // close the dialog
                        $('#familyBuildingRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyFamilyBuildingRule(){
            var row = $('#familyBuildingRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('familyBuildingRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#familyBuildingRuleGrid').datagrid('reload'); // reload the user data
                            } else {
                                $.messager.show({ // show error message
                                    title: 'Error',
                                    msg: result.errorMsg
                                });
                            }
                        },'json');
                    }
                });
            }
        }
    </script>
    <style type="text/css">
        #familyBuildingRuleForm{
            margin:0;
            padding:10px 30px;
        }
        .ftitle{
            font-size:14px;
            font-weight:bold;
            padding:5px 0;
            margin-bottom:10px;
            border-bottom:1px solid #ccc;
        }
        .fitem{
            margin-bottom:5px;
        }
        .fitem label{
            display:inline-block;
            width:80px;
        }
    </style>
    </body>
</html>