<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='taskRuleGrid' class="easyui-datagrid" url="taskRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="taskRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#taskRule-buttons">
        <div class="ftitle">标题</div>
        <form id="taskRuleForm" method="post" novalidate>
                            <div class="fitem"><label>id:</label><input name="id" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>name:</label><input name="name" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>parentId:</label><input name="parentId" class="easyui-validatebox"></div>
                            <div class="fitem"><label>type:</label><input name="type" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>icon:</label><input name="icon" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>farmLevel:</label><input name="farmLevel" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>roomDegree:</label><input name="roomDegree" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>fashionIndex:</label><input name="fashionIndex" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>businessLevel:</label><input name="businessLevel" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>conditionsDesc:</label><input name="conditionsDesc" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>conditionsType:</label><input name="conditionsType" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>conditions:</label><input name="conditions" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>gold:</label><input name="gold" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>memo:</label><input name="memo" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>link:</label><input name="link" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>linkDesc:</label><input name="linkDesc" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>gameCoin:</label><input name="gameCoin" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>farmExp:</label><input name="farmExp" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>businessExp:</label><input name="businessExp" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>familyFunds:</label><input name="familyFunds" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>familyDevote:</label><input name="familyDevote" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>bonusScore:</label><input name="bonusScore" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>items:</label><input name="items" class="easyui-validatebox" required="true"></div>
                    </form>
    </div>

    <div id="taskRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveTaskRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#taskRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#taskRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'taskRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'id',title:'id'}
                                                         ,{field:'name',title:'name'}
                                                         ,{field:'parentId',title:'parentId'}
                                                         ,{field:'type',title:'type'}
                                                         ,{field:'icon',title:'icon'}
                                                         ,{field:'farmLevel',title:'farmLevel'}
                                                         ,{field:'roomDegree',title:'roomDegree'}
                                                         ,{field:'fashionIndex',title:'fashionIndex'}
                                                         ,{field:'businessLevel',title:'businessLevel'}
                                                         ,{field:'conditionsType',title:'conditionsType'}
                                                         ,{field:'conditionsDesc',title:'conditionsDesc'}
                                                         ,{field:'conditions',title:'conditions'}
                                                         ,{field:'gold',title:'gold'}
                                                         ,{field:'memo',title:'memo'}
                                                         ,{field:'link',title:'link'}
                                                         ,{field:'linkDesc',title:'linkDesc'}
                                                         ,{field:'gameCoin',title:'gameCoin'}
                                                         ,{field:'farmExp',title:'farmExp'}
                                                         ,{field:'businessExp',title:'businessExp'}
                                                         ,{field:'familyFunds',title:'familyFunds'}
                                                         ,{field:'familyDevote',title:'familyDevote'}
                                                         ,{field:'bonusScore',title:'bonusScore'}
                                                         ,{field:'items',title:'items'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newTaskRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editTaskRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyTaskRule();
                }
            }]

        });


        var purl;
        function newTaskRule(){
            $('#taskRuledlg').dialog('open').dialog('setTitle','增加');
            $('#taskRuleForm').form('clear');
            purl = 'taskRule/add';
        }
        function editTaskRule(){
            var row = $('#taskRuleGrid').datagrid('getSelected');
            if (row){
                $('#taskRuledlg').dialog('open').dialog('setTitle','修改');
                $('#taskRuleForm').form('clear');
                $('#taskRuleForm').form('load',row);
                purl = 'taskRule/update/'+row.id;
            }
        }
        function saveTaskRule(){
            $('#taskRuleForm').form('submit',{
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
                        $('#taskRuledlg').dialog('close'); // close the dialog
                        $('#taskRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyTaskRule(){
            var row = $('#taskRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('taskRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#taskRuleGrid').datagrid('reload'); // reload the user data
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
        #taskRuleForm{
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