<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <table id='moneyTreeRuleGrid' class="easyui-datagrid" url="moneyTreeRule/list" pagination="true" rownumbers="true" fitColumns="true">
    </table>


    <div id="moneyTreeRuledlg" class="easyui-dialog" modal="true" data-options="resizable:true" style="width:600px;height:400px;padding:10px 20px" closed="true" buttons="#moneyTreeRule-buttons">
        <div class="ftitle">标题</div>
        <form id="moneyTreeRuleForm" method="post" novalidate>
                            <div class="fitem"><label>level:</label><input name="level" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>blessingMax:</label><input name="blessingMax" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>numberOfBlessing:</label><input name="numberOfBlessing" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>gameCoin:</label><input name="gameCoin" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>acquireDevote:</label><input name="acquireDevote" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>blessing:</label><input name="blessing" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>outputPar:</label><input name="outputPar" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>rewardPar1:</label><input name="rewardPar1" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>rewardPar2:</label><input name="rewardPar2" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>rewardPar3:</label><input name="rewardPar3" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>dropPar:</label><input name="dropPar" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>dropDenomination:</label><input name="dropDenomination" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>rewardEdit:</label><input name="rewardEdit" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>dropFrequency:</label><input name="dropFrequency" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>dropChance:</label><input name="dropChance" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>dropPosition:</label><input name="dropPosition" class="easyui-validatebox" required="true"></div>
                            <div class="fitem"><label>positions:</label><input name="positions" class="easyui-validatebox"></div>
                    </form>
    </div>

    <div id="moneyTreeRule-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveMoneyTreeRule()">保存</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#moneyTreeRuledlg').dialog('close')">取消</a>
    </div>

    <script type="text/javascript">
        $('#moneyTreeRuleGrid').datagrid({
					height: "auto",
					fit: true,
            idField:'id'
            ,method:'GET'
            ,url:'moneyTreeRule/list'
            ,pagination:true
            ,rownumbers:"true",fitColumns:"true"
            ,frozenColumns:[[{field : 'ck',checkbox : true}
            ]]
            ,columns:[[                                     {field:'level',title:'level'}
                                                         ,{field:'blessingMax',title:'blessingMax'}
                                                         ,{field:'numberOfBlessing',title:'numberOfBlessing'}
                                                         ,{field:'gameCoin',title:'gameCoin'}
                                                         ,{field:'acquireDevote',title:'acquireDevote'}
                                                         ,{field:'blessing',title:'blessing'}
                                                         ,{field:'outputPar',title:'outputPar'}
                                                         ,{field:'rewardPar1',title:'rewardPar1'}
                                                         ,{field:'rewardPar2',title:'rewardPar2'}
                                                         ,{field:'rewardPar3',title:'rewardPar3'}
                                                         ,{field:'dropPar',title:'dropPar'}
                                                         ,{field:'dropDenomination',title:'dropDenomination'}
                                                         ,{field:'rewardEdit',title:'rewardEdit'}
                                                         ,{field:'dropFrequency',title:'dropFrequency'}
                                                         ,{field:'dropChance',title:'dropChance'}
                                                         ,{field:'dropPosition',title:'dropPosition'}
                                                         ,{field:'positions',title:'positions'}
                                                ]]
            ,toolbar:[{
                text:'增加',
                iconCls:'icon-add',
                handler:function(){
                    newMoneyTreeRule();
                }
            },{
                text:'修改',
                iconCls:'icon-edit',
                handler:function(){
                    editMoneyTreeRule();
                }
            },{
                text:'删除',
                iconCls:'icon-remove',
                handler:function(){
                    destroyMoneyTreeRule();
                }
            }]

        });


        var purl;
        function newMoneyTreeRule(){
            $('#moneyTreeRuledlg').dialog('open').dialog('setTitle','增加');
            $('#moneyTreeRuleForm').form('clear');
            purl = 'moneyTreeRule/add';
        }
        function editMoneyTreeRule(){
            var row = $('#moneyTreeRuleGrid').datagrid('getSelected');
            if (row){
                $('#moneyTreeRuledlg').dialog('open').dialog('setTitle','修改');
                $('#moneyTreeRuleForm').form('clear');
                $('#moneyTreeRuleForm').form('load',row);
                purl = 'moneyTreeRule/update/'+row.id;
            }
        }
        function saveMoneyTreeRule(){
            $('#moneyTreeRuleForm').form('submit',{
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
                        $('#moneyTreeRuledlg').dialog('close'); // close the dialog
                        $('#moneyTreeRuleGrid').datagrid('reload'); // reload the user data
                    }
                }

            });
        }
        function destroyMoneyTreeRule(){
            var row = $('#moneyTreeRuleGrid').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','确定要删除吗?',function(r){
                    if (r){
                        $.post('moneyTreeRule/del/'+row.id,function(result){
                            if (result.code=='ok'){
                                $('#moneyTreeRuleGrid').datagrid('reload'); // reload the user data
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
        #moneyTreeRuleForm{
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