<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='playerRuleGrid' class="easyui-datagrid" url="playerRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="playerRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#playerRule-buttons">
        <div class="ftitle">标题</div>
        <form id="playerRuleForm" method="post" novalidate>
                            <div class="fitem"><label>id:</label><input name="id" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>level:</label><input name="level" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>exp:</label><input name="exp" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>type:</label><input name="type" class="easyui-validatebox" required="true"></div>
                    </form>
    </div>

    <div id="playerRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="savePlayerRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#playerRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#playerRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'playerRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'id',title:'id'}
                                                         ,{field:'level',title:'level'}
                                                         ,{field:'exp',title:'exp'}
                                                         ,{field:'type',title:'type'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newPlayerRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editPlayerRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyPlayerRule();
                }
            }]

        });


        var purl;
        function newPlayerRule(){
            $('#playerRuledlg').dialog('open').dialog('setTitle','增加');
            $('#playerRuleForm').form('clear');
            purl = 'playerRule/add';
        }
        function editPlayerRule(){
            var row = $('#playerRuleGrid').datagrid('getSelected');
            if (row){
                $('#playerRuledlg').dialog('open').dialog('setTitle','修改');
                $('#playerRuleForm').form('clear');
                $('#playerRuleForm').form('load',row);
                purl = 'playerRule/update/'+row.id;
            }
        }
        function savePlayerRule(){
            $('#playerRuleForm').form('submit',{
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
                        $('#playerRuledlg').dialog('close'); // close the dialog
                        $('#playerRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyPlayerRule(){
            var row = $('#playerRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('playerRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#playerRuleGrid').datagrid('reload'); // reload the user data
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
        #playerRuleForm{
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