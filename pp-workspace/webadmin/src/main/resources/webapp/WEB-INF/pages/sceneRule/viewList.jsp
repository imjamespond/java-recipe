<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='sceneRuleGrid' class="easyui-datagrid" url="sceneRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="sceneRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#sceneRule-buttons">
        <div class="ftitle">标题</div>
        <form id="sceneRuleForm" method="post" novalidate>
                            <div class="fitem"><label>id:</label><input name="id" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>name:</label><input name="name" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>type:</label><input name="type" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>withTheScreen:</label><input name="withTheScreen" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>pkType:</label><input name="pkType" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>monster:</label><input name="monster" class="easyui-validatebox"></div>
                            <div class="fitem"><label>npc:</label><input name="npc" class="easyui-validatebox"></div>
                            <div class="fitem"><label>imagePath:</label><input name="imagePath" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>musicPath:</label><input name="musicPath" class="easyui-validatebox"></div>
                            <div class="fitem"><label>shareType:</label><input name="shareType" class="easyui-validatebox" required="true"></div>
                    </form>
    </div>

    <div id="sceneRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveSceneRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#sceneRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#sceneRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'sceneRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'id',title:'id'}
                                                         ,{field:'name',title:'name'}
                                                         ,{field:'type',title:'type'}
                                                         ,{field:'withTheScreen',title:'withTheScreen'}
                                                         ,{field:'pkType',title:'pkType'}
                                                         ,{field:'monster',title:'monster'}
                                                         ,{field:'npc',title:'npc'}
                                                         ,{field:'imagePath',title:'imagePath'}
                                                         ,{field:'musicPath',title:'musicPath'}
                                                         ,{field:'shareType',title:'shareType'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newSceneRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editSceneRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroySceneRule();
                }
            }]

        });


        var purl;
        function newSceneRule(){
            $('#sceneRuledlg').dialog('open').dialog('setTitle','增加');
            $('#sceneRuleForm').form('clear');
            purl = 'sceneRule/add';
        }
        function editSceneRule(){
            var row = $('#sceneRuleGrid').datagrid('getSelected');
            if (row){
                $('#sceneRuledlg').dialog('open').dialog('setTitle','修改');
                $('#sceneRuleForm').form('clear');
                $('#sceneRuleForm').form('load',row);
                purl = 'sceneRule/update/'+row.id;
            }
        }
        function saveSceneRule(){
            $('#sceneRuleForm').form('submit',{
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
                        $('#sceneRuledlg').dialog('close'); // close the dialog
                        $('#sceneRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroySceneRule(){
            var row = $('#sceneRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('sceneRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#sceneRuleGrid').datagrid('reload'); // reload the user data
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
        #sceneRuleForm{
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