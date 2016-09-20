<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='fashionItemRuleGrid' class="easyui-datagrid" url="fashionItemRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="fashionItemRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#fashionItemRule-buttons">
        <div class="ftitle">标题</div>
        <form id="fashionItemRuleForm" method="post" novalidate>
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
                            <div class="fitem"><label>sex:</label><input name="sex" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>image:</label><input name="image" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>fashionIndex:</label><input name="fashionIndex" class="easyui-validatebox" required="true"></div>
                    </form>
    </div>

    <div id="fashionItemRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveFashionItemRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#fashionItemRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#fashionItemRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'fashionItemRule/list'
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
                                                         ,{field:'sex',title:'sex'}
                                                         ,{field:'image',title:'image'}
                                                         ,{field:'fashionIndex',title:'fashionIndex'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newFashionItemRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editFashionItemRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyFashionItemRule();
                }
            }]

        });


        var purl;
        function newFashionItemRule(){
            $('#fashionItemRuledlg').dialog('open').dialog('setTitle','增加');
            $('#fashionItemRuleForm').form('clear');
            purl = 'fashionItemRule/add';
        }
        function editFashionItemRule(){
            var row = $('#fashionItemRuleGrid').datagrid('getSelected');
            if (row){
                $('#fashionItemRuledlg').dialog('open').dialog('setTitle','修改');
                $('#fashionItemRuleForm').form('clear');
                $('#fashionItemRuleForm').form('load',row);
                purl = 'fashionItemRule/update/'+row.id;
            }
        }
        function saveFashionItemRule(){
            $('#fashionItemRuleForm').form('submit',{
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
                        $('#fashionItemRuledlg').dialog('close'); // close the dialog
                        $('#fashionItemRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyFashionItemRule(){
            var row = $('#fashionItemRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('fashionItemRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#fashionItemRuleGrid').datagrid('reload'); // reload the user data
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
        #fashionItemRuleForm{
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