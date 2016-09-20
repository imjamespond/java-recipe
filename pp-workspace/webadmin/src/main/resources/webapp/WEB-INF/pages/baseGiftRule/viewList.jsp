<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='baseGiftRuleGrid' class="easyui-datagrid" url="baseGiftRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="baseGiftRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#baseGiftRule-buttons">
        <div class="ftitle">标题</div>
        <form id="baseGiftRuleForm" method="post" novalidate>
                            <div class="fitem"><label>presentId:</label><input name="presentId" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>presentType:</label><input name="presentType" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>itemId:</label><input name="itemId" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>num:</label><input name="num" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>validityTime:</label><input name="validityTime" class="easyui-validatebox" required="true"></div>
                    </form>
    </div>

    <div id="baseGiftRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveBaseGiftRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#baseGiftRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#baseGiftRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'baseGiftRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'presentId',title:'presentId'}
                                                         ,{field:'presentType',title:'presentType'}
                                                         ,{field:'itemId',title:'itemId'}
                                                         ,{field:'num',title:'num'}
                                                         ,{field:'validityTime',title:'validityTime'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newBaseGiftRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editBaseGiftRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyBaseGiftRule();
                }
            }]

        });


        var purl;
        function newBaseGiftRule(){
            $('#baseGiftRuledlg').dialog('open').dialog('setTitle','增加');
            $('#baseGiftRuleForm').form('clear');
            purl = 'baseGiftRule/add';
        }
        function editBaseGiftRule(){
            var row = $('#baseGiftRuleGrid').datagrid('getSelected');
            if (row){
                $('#baseGiftRuledlg').dialog('open').dialog('setTitle','修改');
                $('#baseGiftRuleForm').form('clear');
                $('#baseGiftRuleForm').form('load',row);
                purl = 'baseGiftRule/update/'+row.id;
            }
        }
        function saveBaseGiftRule(){
            $('#baseGiftRuleForm').form('submit',{
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
                        $('#baseGiftRuledlg').dialog('close'); // close the dialog
                        $('#baseGiftRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyBaseGiftRule(){
            var row = $('#baseGiftRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('baseGiftRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#baseGiftRuleGrid').datagrid('reload'); // reload the user data
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
        #baseGiftRuleForm{
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