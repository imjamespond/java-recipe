<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='wordRuleGrid' class="easyui-datagrid" url="wordRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="wordRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#wordRule-buttons">
        <div class="ftitle">标题</div>
        <form id="wordRuleForm" method="post" novalidate>
                            <div class="fitem"><label>id:</label><input name="id" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>words:</label><input name="words" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>type:</label><input name="type" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>memo:</label><input name="memo" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>state:</label><input name="state" class="easyui-validatebox" required="true"></div>
                    </form>
    </div>

    <div id="wordRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveWordRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#wordRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#wordRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'wordRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'id',title:'id'}
                                                         ,{field:'words',title:'words'}
                                                         ,{field:'type',title:'type'}
                                                         ,{field:'memo',title:'memo'}
                                                         ,{field:'state',title:'state'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newWordRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editWordRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyWordRule();
                }
            }]

        });


        var purl;
        function newWordRule(){
            $('#wordRuledlg').dialog('open').dialog('setTitle','增加');
            $('#wordRuleForm').form('clear');
            purl = 'wordRule/add';
        }
        function editWordRule(){
            var row = $('#wordRuleGrid').datagrid('getSelected');
            if (row){
                $('#wordRuledlg').dialog('open').dialog('setTitle','修改');
                $('#wordRuleForm').form('clear');
                $('#wordRuleForm').form('load',row);
                purl = 'wordRule/update/'+row.id;
            }
        }
        function saveWordRule(){
            $('#wordRuleForm').form('submit',{
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
                        $('#wordRuledlg').dialog('close'); // close the dialog
                        $('#wordRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyWordRule(){
            var row = $('#wordRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('wordRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#wordRuleGrid').datagrid('reload'); // reload the user data
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
        #wordRuleForm{
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