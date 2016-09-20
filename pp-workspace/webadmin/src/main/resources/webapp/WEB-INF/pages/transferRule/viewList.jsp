<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='transferRuleGrid' class="easyui-datagrid" url="transferRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="transferRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#transferRule-buttons">
        <div class="ftitle">标题</div>
        <form id="transferRuleForm" method="post" novalidate>
                            <div class="fitem"><label>id:</label><input name="id" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>currId:</label><input name="currId" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>currX:</label><input name="currX" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>currY:</label><input name="currY" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>targetId:</label><input name="targetId" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>targetX:</label><input name="targetX" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>targetY:</label><input name="targetY" class="easyui-validatebox" required="true"></div>
                    </form>
    </div>

    <div id="transferRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveTransferRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#transferRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#transferRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'transferRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'id',title:'id'}
                                                         ,{field:'currId',title:'currId'}
                                                         ,{field:'currX',title:'currX'}
                                                         ,{field:'currY',title:'currY'}
                                                         ,{field:'targetId',title:'targetId'}
                                                         ,{field:'targetX',title:'targetX'}
                                                         ,{field:'targetY',title:'targetY'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newTransferRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editTransferRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyTransferRule();
                }
            }]

        });


        var purl;
        function newTransferRule(){
            $('#transferRuledlg').dialog('open').dialog('setTitle','增加');
            $('#transferRuleForm').form('clear');
            purl = 'transferRule/add';
        }
        function editTransferRule(){
            var row = $('#transferRuleGrid').datagrid('getSelected');
            if (row){
                $('#transferRuledlg').dialog('open').dialog('setTitle','修改');
                $('#transferRuleForm').form('clear');
                $('#transferRuleForm').form('load',row);
                purl = 'transferRule/update/'+row.id;
            }
        }
        function saveTransferRule(){
            $('#transferRuleForm').form('submit',{
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
                        $('#transferRuledlg').dialog('close'); // close the dialog
                        $('#transferRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyTransferRule(){
            var row = $('#transferRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('transferRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#transferRuleGrid').datagrid('reload'); // reload the user data
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
        #transferRuleForm{
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