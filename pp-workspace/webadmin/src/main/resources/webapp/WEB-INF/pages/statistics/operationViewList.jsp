<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<table id='userActionModel' class="easyui-datagrid" pagination="true" rownumbers="true"
       fitColumns="true" toolbar="#tbplayerTaskActionModel">
</table>


<div id="tbplayerTaskActionModel">
    &nbsp;Uid:
    <input id="uidplayerTaskActionModel" class="easyui-validatebox" type="text"/> &nbsp;
    &nbsp;开始日期:
    <input id="dateBeginplayerTaskActionModel" class="easyui-datebox"/> &nbsp;
    &nbsp;结束日期:
    <input id="dateEndplayerTaskActionModel" class="easyui-datebox"/> &nbsp;
    <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch()">查询</a>
</div>


<script type="text/javascript">

    function doSearch() {
        var queryParams = {} ;
        if($("#uidplayerTaskActionModel").val()){
            queryParams.uid=$("#uidplayerTaskActionModel").val();
        }
        if($("#dateBeginplayerTaskActionModel").datebox('getValue')!=""&&$("#dateEndplayerTaskActionModel").datebox('getValue')!=""){
            queryParams.dateBegin=$("#dateBeginplayerTaskActionModel").datebox('getValue');
            queryParams.dateEnd=$("#dateEndplayerTaskActionModel").datebox('getValue');
        }
        $('#userActionModel').datagrid({
            height:"auto",
            fit:true,
            idField:'id', method:'GET', url:'userActionModel/list/', queryParams:queryParams ,pagination:true, rownumbers:"true", fitColumns:"true", frozenColumns:[
                [
                    {field:'ck', checkbox:true}
                ]
            ]
            ,columns:[[                                     {field:'id',title:'id'}
                ,{field:'userName',title:'userName'}
                                                         ,{field:'pid',title:'pid'} ,
                {field:'name', title:'name'}
                                                         ,{field:'date',title:'date'}
                                                         ,{field:'uid',title:'uid'}
                                                         ,{field:'type',title:'type'}
                ,{field:'typeName',title:'typeName'}
                ,{field:'num',title:'num'}
                ,{field:'reason',title:'reason'}
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