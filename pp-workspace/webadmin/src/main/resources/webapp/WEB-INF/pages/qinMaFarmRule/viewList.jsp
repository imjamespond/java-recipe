<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='qinMaFarmRuleGrid' class="easyui-datagrid" url="qinMaFarmRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="qinMaFarmRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#qinMaFarmRule-buttons">
        <div class="ftitle">标题</div>
        <form id="qinMaFarmRuleForm" method="post" novalidate>
                            <div class="fitem"><label>亲妈id:</label><input name="id" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>农场名称:</label><input name="name" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>农场等级:</label><input name="level" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>农田数据:</label><textarea name="fieldsValue" class="easyui-validatebox" required="true" style="width: 80%" rows="10"></textarea></div>
                    </form>
    </div>

    <div id="qinMaFarmRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveQinMaFarmRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#qinMaFarmRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#qinMaFarmRuleGrid').datagrid({
            height: "auto",
            fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'qinMaFarmRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'id',title:'id'}
                                                         ,{field:'name',title:'name'}
                                                         ,{field:'level',title:'level'}
                                                         ,{field:'fieldsValue',title:'fieldsValue'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newQinMaFarmRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editQinMaFarmRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyQinMaFarmRule();
                }
            }]

        });


        var purl;
        function newQinMaFarmRule(){
            $('#qinMaFarmRuledlg').dialog('open').dialog('setTitle','增加');
            $('#qinMaFarmRuleForm').form('clear');
            purl = 'qinMaFarmRule/add';
        }
        function editQinMaFarmRule(){
            var row = $('#qinMaFarmRuleGrid').datagrid('getSelected');
            if (row){
                $('#qinMaFarmRuledlg').dialog('open').dialog('setTitle','修改');
                $('#qinMaFarmRuleForm').form('clear');
                $('#qinMaFarmRuleForm').form('load',row);
                purl = 'qinMaFarmRule/update/'+row.id;
            }
        }
        function saveQinMaFarmRule(){
            $('#qinMaFarmRuleForm').form('submit',{
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
                        $('#qinMaFarmRuledlg').dialog('close'); // close the dialog
                        $('#qinMaFarmRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyQinMaFarmRule(){
            var row = $('#qinMaFarmRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('qinMaFarmRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#qinMaFarmRuleGrid').datagrid('reload'); // reload the user data
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
        #qinMaFarmRuleForm{
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