<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='farmLevelRuleGrid' class="easyui-datagrid" url="farmLevelRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="farmLevelRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#farmLevelRule-buttons">
        <div class="ftitle">标题</div>
        <form id="farmLevelRuleForm" method="post" novalidate>
                            <div class="fitem"><label>level:</label><input name="level" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>needExp:</label><input name="needExp" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>maxGold:</label><input name="maxGold" class="easyui-validatebox" required="true"></div>
                    </form>
    </div>

    <div id="farmLevelRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveFarmLevelRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#farmLevelRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#farmLevelRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'farmLevelRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'level',title:'level'}
                                                         ,{field:'needExp',title:'needExp'}
                                                         ,{field:'maxGold',title:'maxGold'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newFarmLevelRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editFarmLevelRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyFarmLevelRule();
                }
            }]

        });


        var purl;
        function newFarmLevelRule(){
            $('#farmLevelRuledlg').dialog('open').dialog('setTitle','增加');
            $('#farmLevelRuleForm').form('clear');
            purl = 'farmLevelRule/add';
        }
        function editFarmLevelRule(){
            var row = $('#farmLevelRuleGrid').datagrid('getSelected');
            if (row){
                $('#farmLevelRuledlg').dialog('open').dialog('setTitle','修改');
                $('#farmLevelRuleForm').form('clear');
                $('#farmLevelRuleForm').form('load',row);
                purl = 'farmLevelRule/update/'+row.id;
            }
        }
        function saveFarmLevelRule(){
            $('#farmLevelRuleForm').form('submit',{
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
                        $('#farmLevelRuledlg').dialog('close'); // close the dialog
                        $('#farmLevelRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyFarmLevelRule(){
            var row = $('#farmLevelRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('farmLevelRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#farmLevelRuleGrid').datagrid('reload'); // reload the user data
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
        #farmLevelRuleForm{
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