<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='farmSeedRuleGrid' class="easyui-datagrid" url="farmSeedRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="farmSeedRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#farmSeedRule-buttons">
        <div class="ftitle">标题</div>
        <form id="farmSeedRuleForm" method="post" novalidate>
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
                            <div class="fitem"><label>growthTime:</label><input name="growthTime" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>output:</label><input name="output" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>production:</label><input name="production" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>harvestEditor:</label><input name="harvestEditor" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>cropsReward:</label><input name="cropsReward" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>expReward:</label><input name="expReward" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>growthImage:</label><input name="growthImage" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>matureImage:</label><input name="matureImage" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>seedsExp:</label><input name="seedsExp" class="easyui-validatebox" required="true"></div>
                    </form>
    </div>

    <div id="farmSeedRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveFarmSeedRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#farmSeedRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#farmSeedRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'farmSeedRule/list'
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
                                                         ,{field:'growthTime',title:'growthTime'}
                                                         ,{field:'output',title:'output'}
                                                         ,{field:'production',title:'production'}
                                                         ,{field:'harvestEditor',title:'harvestEditor'}
                                                         ,{field:'cropsReward',title:'cropsReward'}
                                                         ,{field:'expReward',title:'expReward'}
                                                         ,{field:'growthImage',title:'growthImage'}
                                                         ,{field:'matureImage',title:'matureImage'}
                                                         ,{field:'seedsExp',title:'seedsExp'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newFarmSeedRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editFarmSeedRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyFarmSeedRule();
                }
            }]

        });


        var purl;
        function newFarmSeedRule(){
            $('#farmSeedRuledlg').dialog('open').dialog('setTitle','增加');
            $('#farmSeedRuleForm').form('clear');
            purl = 'farmSeedRule/add';
        }
        function editFarmSeedRule(){
            var row = $('#farmSeedRuleGrid').datagrid('getSelected');
            if (row){
                $('#farmSeedRuledlg').dialog('open').dialog('setTitle','修改');
                $('#farmSeedRuleForm').form('clear');
                $('#farmSeedRuleForm').form('load',row);
                purl = 'farmSeedRule/update/'+row.id;
            }
        }
        function saveFarmSeedRule(){
            $('#farmSeedRuleForm').form('submit',{
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
                        $('#farmSeedRuledlg').dialog('close'); // close the dialog
                        $('#farmSeedRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyFarmSeedRule(){
            var row = $('#farmSeedRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('farmSeedRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#farmSeedRuleGrid').datagrid('reload'); // reload the user data
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
        #farmSeedRuleForm{
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