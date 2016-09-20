<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='roomItemRuleGrid' class="easyui-datagrid" url="roomItemRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="roomItemRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#roomItemRule-buttons">
        <div class="ftitle">标题</div>
        <form id="roomItemRuleForm" method="post" novalidate>
                            <div class="fitem"><label>itemsId:</label><input name="itemsId" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>name:</label><input name="name" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>type:</label><input name="type" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>itemtype:</label><input name="itemtype" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>bindingTypes:</label><input name="bindingTypes" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>recyclingPrice:</label><input name="recyclingPrice" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>gamePrice:</label><input name="gamePrice" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>goldPrice:</label><input name="goldPrice" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>farmLevel:</label><input name="farmLevel" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>overlay:</label><input name="overlay" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>desc:</label><input name="desc" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>icon:</label><input name="icon" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>shopSell:</label><input name="shopSell" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>starGift:</label><input name="starGift" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>fansValues:</label><input name="fansValues" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>luxuryDegree:</label><input name="luxuryDegree" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>image:</label><input name="image" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>rotation:</label><input name="rotation" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>repeatPurchase:</label><input name="repeatPurchase" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>largestNumber:</label><input name="largestNumber" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>stack:</label><input name="stack" class="easyui-validatebox" required="true"></div>
                    </form>
    </div>

    <div id="roomItemRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveRoomItemRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#roomItemRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#roomItemRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'roomItemRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'itemsId',title:'itemsId'}
                                                         ,{field:'name',title:'name'}
                                                         ,{field:'type',title:'type'}
                                                         ,{field:'itemtype',title:'itemtype'}
                                                         ,{field:'bindingTypes',title:'bindingTypes'}
                                                         ,{field:'recyclingPrice',title:'recyclingPrice'}
                                                         ,{field:'gamePrice',title:'gamePrice'}
                                                         ,{field:'goldPrice',title:'goldPrice'}
                                                         ,{field:'farmLevel',title:'farmLevel'}
                                                         ,{field:'overlay',title:'overlay'}
                                                         ,{field:'desc',title:'desc'}
                                                         ,{field:'icon',title:'icon'}
                                                         ,{field:'shopSell',title:'shopSell'}
                                                         ,{field:'starGift',title:'starGift'}
                                                         ,{field:'fansValues',title:'fansValues'}
                                                         ,{field:'luxuryDegree',title:'luxuryDegree'}
                                                         ,{field:'image',title:'image'}
                                                         ,{field:'rotation',title:'rotation'}
                                                         ,{field:'repeatPurchase',title:'repeatPurchase'}
                                                         ,{field:'largestNumber',title:'largestNumber'}
                                                         ,{field:'stack',title:'stack'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newRoomItemRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editRoomItemRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyRoomItemRule();
                }
            }]

        });


        var purl;
        function newRoomItemRule(){
            $('#roomItemRuledlg').dialog('open').dialog('setTitle','增加');
            $('#roomItemRuleForm').form('clear');
            purl = 'roomItemRule/add';
        }
        function editRoomItemRule(){
            var row = $('#roomItemRuleGrid').datagrid('getSelected');
            if (row){
                $('#roomItemRuledlg').dialog('open').dialog('setTitle','修改');
                $('#roomItemRuleForm').form('clear');
                $('#roomItemRuleForm').form('load',row);
                purl = 'roomItemRule/update/'+row.id;
            }
        }
        function saveRoomItemRule(){
            $('#roomItemRuleForm').form('submit',{
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
                        $('#roomItemRuledlg').dialog('close'); // close the dialog
                        $('#roomItemRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyRoomItemRule(){
            var row = $('#roomItemRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('roomItemRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#roomItemRuleGrid').datagrid('reload'); // reload the user data
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
        #roomItemRuleForm{
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