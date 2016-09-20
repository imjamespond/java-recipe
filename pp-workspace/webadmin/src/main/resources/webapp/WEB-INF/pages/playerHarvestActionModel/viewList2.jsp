<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<table id='playerHarvestActionModel2' class="easyui-datagrid" pagination="true" rownumbers="true"
       fitColumns="true" toolbar="#tbplayerHarvestActionModel2">
</table>


<div id="tbplayerHarvestActionModel2">
    &nbsp;Uid:
    <input id="uidplayerHarvestActionModel2" class="easyui-validatebox" type="text"/> &nbsp;
    &nbsp;开始日期:
    <input id="dateBeginplayerHarvestActionModel2" class="easyui-datebox"/> &nbsp;
    &nbsp;结束日期:
    <input id="dateEndplayerHarvestActionModel2" class="easyui-datebox"/> &nbsp;
    <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch()">查询</a>
</div>


<script type="text/javascript">

    function doSearch() {
        var queryParams = {} ;
        if($("#uidplayerHarvestActionModel2").val()){
            queryParams.uid=$("#uidplayerHarvestActionModel2").val();
        }
        if($("#dateBeginplayerHarvestActionModel2").datebox('getValue')!=""&&$("#dateEndplayerHarvestActionModel2").datebox('getValue')!=""){
            queryParams.dateBegin=$("#dateBeginplayerHarvestActionModel2").datebox('getValue');
            queryParams.dateEnd=$("#dateEndplayerHarvestActionModel2").datebox('getValue');
        }
        $('#playerHarvestActionModel2').datagrid({
            height:"auto",
            fit:true,
            idField:'id', method:'GET', url:'playerHarvestActionModel/list/10/', queryParams:queryParams ,pagination:true, rownumbers:"true", fitColumns:"true", frozenColumns:[
                [
                    {field:'ck', checkbox:true}
                ]
            ]
            ,columns:[[                                     {field:'id',title:'id'}
                                                         ,{field:'pid',title:'pid'}  ,
                {field:'name', title:'name'}
                                                         ,{field:'date',title:'date'}
                                                         ,{field:'uid',title:'uid'}
                                                         ,{field:'type',title:'type'}
                                                         ,{field:'fieldId',title:'fieldId'}
                                                         ,{field:'ripenNum',title:'ripenNum'}
                                                         ,{field:'plantId',title:'plantId'}
                                                ]]

        });
    }
</script>
<style type="text/css">
    #wordRuleForm {
        margin: 0;
        padding: 10px 30px;
    }

    .ftitle {
        font-size: 14px;
        font-weight: bold;
        padding: 5px 0;
        margin-bottom: 10px;
        border-bottom: 1px solid #ccc;
    }

    .fitem {
        margin-bottom: 5px;
    }

    .fitem label {
        display: inline-block;
        width: 80px;
    }
</style>
</body>
</html>