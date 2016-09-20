<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='qinMaRoomRuleGrid' class="easyui-datagrid" url="qinMaRoomRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="qinMaRoomRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#qinMaRoomRule-buttons">
        <div class="ftitle">标题</div>
        <form id="qinMaRoomRuleForm" method="post" novalidate>
                            <div class="fitem"><label>亲妈id:</label><input name="id" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>房间名称:</label><input name="name" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>x:</label><input name="x" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>y:</label><input name="y" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>装饰品:</label><textarea name="decoratePositions" class="easyui-validatebox" required="true" style="width: 80%" rows="10"></textarea></div>
                    </form>
    </div>

    <div id="qinMaRoomRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveQinMaRoomRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#qinMaRoomRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#qinMaRoomRuleGrid').datagrid({
            height: "auto",
            fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'qinMaRoomRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'id',title:'id'}
                                                         ,{field:'name',title:'name'}
                                                         ,{field:'x',title:'x'}
                                                         ,{field:'y',title:'y'}
                                                         ,{field:'decoratePositions',title:'decoratePositions'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newQinMaRoomRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editQinMaRoomRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyQinMaRoomRule();
                }
            }]

        });


        var purl;
        function newQinMaRoomRule(){
            $('#qinMaRoomRuledlg').dialog('open').dialog('setTitle','增加');
            $('#qinMaRoomRuleForm').form('clear');
            purl = 'qinMaRoomRule/add';
        }
        function editQinMaRoomRule(){
            var row = $('#qinMaRoomRuleGrid').datagrid('getSelected');
            if (row){
                $('#qinMaRoomRuledlg').dialog('open').dialog('setTitle','修改');
                $('#qinMaRoomRuleForm').form('clear');
                $('#qinMaRoomRuleForm').form('load',row);
                purl = 'qinMaRoomRule/update/'+row.id;
            }
        }
        function saveQinMaRoomRule(){
            $('#qinMaRoomRuleForm').form('submit',{
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
                        $('#qinMaRoomRuledlg').dialog('close'); // close the dialog
                        $('#qinMaRoomRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyQinMaRoomRule(){
            var row = $('#qinMaRoomRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('qinMaRoomRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#qinMaRoomRuleGrid').datagrid('reload'); // reload the user data
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
        #qinMaRoomRuleForm{
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