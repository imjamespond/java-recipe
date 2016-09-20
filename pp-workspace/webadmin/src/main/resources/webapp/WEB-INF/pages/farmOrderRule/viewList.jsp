<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='farmOrderRuleGrid' class="easyui-datagrid" url="farmOrderRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="farmOrderRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#farmOrderRule-buttons">
        <div class="ftitle">标题</div>
        <form id="farmOrderRuleForm" method="post" novalidate>
                            <div class="fitem"><label>id:</label><input name="id" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>name:</label><input name="name" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>farmLevel:</label><input name="farmLevel" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>currencyReward:</label><input name="currencyReward" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>expReward:</label><input name="expReward" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>orderRequest:</label><input name="orderRequest" class="easyui-validatebox" required="true"></div>
                    </form>
    </div>

    <div id="farmOrderRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveFarmOrderRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#farmOrderRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#farmOrderRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'farmOrderRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'id',title:'id'}
                                                         ,{field:'name',title:'name'}
                                                         ,{field:'farmLevel',title:'farmLevel'}
                                                         ,{field:'currencyReward',title:'currencyReward'}
                                                         ,{field:'expReward',title:'expReward'}
                                                         ,{field:'orderRequest',title:'orderRequest'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newFarmOrderRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editFarmOrderRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyFarmOrderRule();
                }
            }]

        });


        var purl;
        function newFarmOrderRule(){
            $('#farmOrderRuledlg').dialog('open').dialog('setTitle','增加');
            $('#farmOrderRuleForm').form('clear');
            purl = 'farmOrderRule/add';
        }
        function editFarmOrderRule(){
            var row = $('#farmOrderRuleGrid').datagrid('getSelected');
            if (row){
                $('#farmOrderRuledlg').dialog('open').dialog('setTitle','修改');
                $('#farmOrderRuleForm').form('clear');
                $('#farmOrderRuleForm').form('load',row);
                purl = 'farmOrderRule/update/'+row.id;
            }
        }
        function saveFarmOrderRule(){
            $('#farmOrderRuleForm').form('submit',{
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
                        $('#farmOrderRuledlg').dialog('close'); // close the dialog
                        $('#farmOrderRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyFarmOrderRule(){
            var row = $('#farmOrderRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('farmOrderRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#farmOrderRuleGrid').datagrid('reload'); // reload the user data
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
        #farmOrderRuleForm{
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