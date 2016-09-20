<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='identityRuleGrid' class="easyui-datagrid" url="identityRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="identityRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#identityRule-buttons">
        <div class="ftitle">标题</div>
        <form id="identityRuleForm" method="post" novalidate>
                            <div class="fitem"><label>type:</label><input name="type" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>boonRate:</label><input name="boonRate" class="easyui-validatebox" required="true"></div>
                    </form>
    </div>

    <div id="identityRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveIdentityRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#identityRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#identityRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'identityRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'type',title:'type'}
                                                         ,{field:'boonRate',title:'boonRate'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newIdentityRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editIdentityRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyIdentityRule();
                }
            }]

        });


        var purl;
        function newIdentityRule(){
            $('#identityRuledlg').dialog('open').dialog('setTitle','增加');
            $('#identityRuleForm').form('clear');
            purl = 'identityRule/add';
        }
        function editIdentityRule(){
            var row = $('#identityRuleGrid').datagrid('getSelected');
            if (row){
                $('#identityRuledlg').dialog('open').dialog('setTitle','修改');
                $('#identityRuleForm').form('clear');
                $('#identityRuleForm').form('load',row);
                purl = 'identityRule/update/'+row.id;
            }
        }
        function saveIdentityRule(){
            $('#identityRuleForm').form('submit',{
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
                        $('#identityRuledlg').dialog('close'); // close the dialog
                        $('#identityRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyIdentityRule(){
            var row = $('#identityRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('identityRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#identityRuleGrid').datagrid('reload'); // reload the user data
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
        #identityRuleForm{
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