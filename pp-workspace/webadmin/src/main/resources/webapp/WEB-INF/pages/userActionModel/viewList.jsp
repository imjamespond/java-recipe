<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='userActionModelGrid' class="easyui-datagrid" url="userActionModel/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="userActionModeldlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#userActionModel-buttons">
        <div class="ftitle">标题</div>
        <form id="userActionModelForm" method="post" novalidate>
                            <div class="fitem"><label>id:</label><input name="id" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>操作人:</label><input name="userName" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>操作类型:</label><input name="type" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>操作日期:</label><input name="date" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>游戏帐号:</label><input name="pid" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>数量:</label><input name="num" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>理由:</label><input name="reason" class="easyui-validatebox" required="true"></div>
                    </form>
    </div>

    <div id="userActionModel-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveUserActionModel()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#userActionModeldlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#userActionModelGrid').datagrid({
            height: "auto",
            fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'userActionModel/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'id',title:'id'}
                                                         ,{field:'操作人',title:'操作人'}
                                                         ,{field:'操作类型',title:'操作类型'}
                                                         ,{field:'操作日期',title:'操作日期'}
                                                         ,{field:'游戏帐号',title:'游戏帐号'}
                                                         ,{field:'数量',title:'数量'}
                                                         ,{field:'理由',title:'理由'}
                                                ]]
        });


        var purl;
        function newUserActionModel(){
            $('#userActionModeldlg').dialog('open').dialog('setTitle','增加');
            $('#userActionModelForm').form('clear');
            purl = 'userActionModel/add';
        }
        function editUserActionModel(){
            var row = $('#userActionModelGrid').datagrid('getSelected');
            if (row){
                $('#userActionModeldlg').dialog('open').dialog('setTitle','修改');
                $('#userActionModelForm').form('clear');
                $('#userActionModelForm').form('load',row);
                purl = 'userActionModel/update/'+row.id;
            }
        }
        function saveUserActionModel(){
            $('#userActionModelForm').form('submit',{
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
                        $('#userActionModeldlg').dialog('close'); // close the dialog
                        $('#userActionModelGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyUserActionModel(){
            var row = $('#userActionModelGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('userActionModel/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#userActionModelGrid').datagrid('reload'); // reload the user data
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
        #userActionModelForm{
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