<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<table id='playerFarmComplimentActionModel' class="easyui-datagrid" pagination="true" rownumbers="true"
       fitColumns="true" toolbar="#tbplayerFarmComplimentActionModel">
</table>


<div id="tbplayerFarmComplimentActionModel">
    &nbsp;Uid:
    <input id="uidplayerFarmComplimentActionModel" class="easyui-validatebox" type="text"/> &nbsp;
    &nbsp;开始日期:
    <input id="dateBeginplayerFarmComplimentActionModel" class="easyui-datebox"/> &nbsp;
    &nbsp;结束日期:
    <input id="dateEndplayerFarmComplimentActionModel" class="easyui-datebox"/> &nbsp;
    <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch()">查询</a>
</div>


<script type="text/javascript">

    function doSearch() {
        var queryParams = {} ;
        if($("#uidplayerFarmComplimentActionModel").val()){
            queryParams.uid=$("#uidplayerFarmComplimentActionModel").val();
        }
        if($("#dateBeginplayerFarmComplimentActionModel").datebox('getValue')!=""&&$("#dateEndplayerFarmComplimentActionModel").datebox('getValue')!=""){
            queryParams.dateBegin=$("#dateBeginplayerFarmComplimentActionModel").datebox('getValue');
            queryParams.dateEnd=$("#dateEndplayerFarmComplimentActionModel").datebox('getValue');
        }
        $('#playerFarmComplimentActionModel').datagrid({
            height:"auto",
            fit:true,
            idField:'id', method:'GET', url:'playerFarmComplimentActionModel/list/', queryParams:queryParams ,pagination:true, rownumbers:"true", fitColumns:"true", frozenColumns:[
                [
                    {field:'ck', checkbox:true}
                ]
            ]
            ,columns:[[                                     {field:'id',title:'id'}
                                                         ,{field:'pid',title:'pid'}  ,
                {field:'name', title:'name'}
                                                         ,{field:'date',title:'date'}
                                                         ,{field:'uid',title:'uid'}
                                                         ,{field:'friendId',title:'friendId'}
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