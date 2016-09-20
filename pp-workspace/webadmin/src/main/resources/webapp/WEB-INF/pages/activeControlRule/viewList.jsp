<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='activeControlRuleGrid' class="easyui-datagrid" url="activeControlRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="activeControlRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#activeControlRule-buttons">
        <div class="ftitle">标题</div>
        <form id="activeControlRuleForm" method="post" novalidate>
                            <div class="fitem"><label>activeId:</label><input name="activeId" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>name:</label><input name="name" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>level:</label><input name="level" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>devote:</label><input name="devote" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>frontNotice:</label><input name="frontNotice" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>openNotice:</label><input name="openNotice" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>openDate:</label><input name="openDate" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>openTime:</label><input name="openTime" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>afterNotice:</label><input name="afterNotice" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>endDate:</label><input name="endDate" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>endTime:</label><input name="endTime" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>endNotice:</label><input name="endNotice" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>cycleSettings:</label><input name="cycleSettings" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>contentDesc:</label><input name="contentDesc" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>rewardDesc:</label><input name="rewardDesc" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>itemsDisplay:</label><input name="itemsDisplay" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>time:</label><input name="time" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>sceneCoordinates:</label><input name="sceneCoordinates" class="easyui-validatebox" required="true"></div>
                    </form>
    </div>

    <div id="activeControlRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveActiveControlRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#activeControlRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#activeControlRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'activeControlRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'activeId',title:'activeId'}
                                                         ,{field:'name',title:'name'}
                                                         ,{field:'level',title:'level'}
                                                         ,{field:'devote',title:'devote'}
                                                         ,{field:'frontNotice',title:'frontNotice'}
                                                         ,{field:'openNotice',title:'openNotice'}
                                                         ,{field:'openDate',title:'openDate'}
                                                         ,{field:'openTime',title:'openTime'}
                                                         ,{field:'afterNotice',title:'afterNotice'}
                                                         ,{field:'endDate',title:'endDate'}
                                                         ,{field:'endTime',title:'endTime'}
                                                         ,{field:'endNotice',title:'endNotice'}
                                                         ,{field:'cycleSettings',title:'cycleSettings'}
                                                         ,{field:'contentDesc',title:'contentDesc'}
                                                         ,{field:'rewardDesc',title:'rewardDesc'}
                                                         ,{field:'itemsDisplay',title:'itemsDisplay'}
                                                         ,{field:'time',title:'time'}
                                                         ,{field:'sceneCoordinates',title:'sceneCoordinates'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newActiveControlRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editActiveControlRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyActiveControlRule();
                }
            }]

        });


        var purl;
        function newActiveControlRule(){
            $('#activeControlRuledlg').dialog('open').dialog('setTitle','增加');
            $('#activeControlRuleForm').form('clear');
            purl = 'activeControlRule/add';
        }
        function editActiveControlRule(){
            var row = $('#activeControlRuleGrid').datagrid('getSelected');
            if (row){
                $('#activeControlRuledlg').dialog('open').dialog('setTitle','修改');
                $('#activeControlRuleForm').form('clear');
                $('#activeControlRuleForm').form('load',row);
                purl = 'activeControlRule/update/'+row.id;
            }
        }
        function saveActiveControlRule(){
            $('#activeControlRuleForm').form('submit',{
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
                        $('#activeControlRuledlg').dialog('close'); // close the dialog
                        $('#activeControlRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyActiveControlRule(){
            var row = $('#activeControlRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('activeControlRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#activeControlRuleGrid').datagrid('reload'); // reload the user data
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
        #activeControlRuleForm{
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