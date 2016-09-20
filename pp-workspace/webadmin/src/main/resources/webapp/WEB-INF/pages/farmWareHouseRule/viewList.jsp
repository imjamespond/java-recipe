<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='farmWareHouseRuleGrid' class="easyui-datagrid" url="farmWareHouseRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="farmWareHouseRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#farmWareHouseRule-buttons">
        <div class="ftitle">标题</div>
        <form id="farmWareHouseRuleForm" method="post" novalidate>
                            <div class="fitem"><label>warehouseLevel:</label><input name="warehouseLevel" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>capacity:</label><input name="capacity" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>props:</label><input name="props" class="easyui-validatebox" ></div>
                    </form>
    </div>

    <div id="farmWareHouseRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveFarmWareHouseRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#farmWareHouseRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#farmWareHouseRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'farmWareHouseRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'warehouseLevel',title:'warehouseLevel'}
                                                         ,{field:'capacity',title:'capacity'}
                                                         ,{field:'props',title:'props'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newFarmWareHouseRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editFarmWareHouseRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyFarmWareHouseRule();
                }
            }]

        });


        var purl;
        function newFarmWareHouseRule(){
            $('#farmWareHouseRuledlg').dialog('open').dialog('setTitle','增加');
            $('#farmWareHouseRuleForm').form('clear');
            purl = 'farmWareHouseRule/add';
        }
        function editFarmWareHouseRule(){
            var row = $('#farmWareHouseRuleGrid').datagrid('getSelected');
            if (row){
                $('#farmWareHouseRuledlg').dialog('open').dialog('setTitle','修改');
                $('#farmWareHouseRuleForm').form('clear');
                $('#farmWareHouseRuleForm').form('load',row);
                purl = 'farmWareHouseRule/update/'+row.id;
            }
        }
        function saveFarmWareHouseRule(){
            $('#farmWareHouseRuleForm').form('submit',{
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
                        $('#farmWareHouseRuledlg').dialog('close'); // close the dialog
                        $('#farmWareHouseRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyFarmWareHouseRule(){
            var row = $('#farmWareHouseRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('farmWareHouseRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#farmWareHouseRuleGrid').datagrid('reload'); // reload the user data
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
        #farmWareHouseRuleForm{
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