<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<table id='playerOlineInfoModel' class="easyui-datagrid" pagination="true" rownumbers="true"
       fitColumns="true" toolbar="#tbplayerOlineInfoModel">
</table>


<div id="tbplayerOlineInfoModel">
    &nbsp;Uid:
    <input id="uidplayerOlineInfoModel" class="easyui-validatebox" type="text"/> &nbsp;
    &nbsp;开始日期:
    <input id="dateBeginplayerOlineInfoModel" class="easyui-datebox"/> &nbsp;
    &nbsp;结束日期:
    <input id="dateEndplayerOlineInfoModel" class="easyui-datebox"/> &nbsp;
    <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doSearch()">查询</a>
</div>


<script type="text/javascript">

    function doSearch() {
        var queryParams = {} ;
        if($("#uidplayerOlineInfoModel").val()){
            queryParams.uid=$("#uidplayerOlineInfoModel").val();
        }
        if($("#dateBeginplayerOlineInfoModel").datebox('getValue')!=""&&$("#dateEndplayerOlineInfoModel").datebox('getValue')!=""){
            queryParams.dateBegin=$("#dateBeginplayerOlineInfoModel").datebox('getValue');
            queryParams.dateEnd=$("#dateEndplayerOlineInfoModel").datebox('getValue');
        }
        $('#playerOlineInfoModel').datagrid({
            height:"auto",
            fit:true,
            idField:'id', method:'GET', url:'playerOlineInfoModel/list/', queryParams:queryParams ,pagination:true, rownumbers:"true", fitColumns:"true", frozenColumns:[
                [
                    {field:'ck', checkbox:true}
                ]
            ]
            ,columns:[[                                     {field:'id',title:'id'}
                                                         ,{field:'maxNum',title:'maxNum'}
                                                         ,{field:'minNum',title:'minNum'}
                                                         ,{field:'maxTime',title:'maxTime'}
                                                         ,{field:'minTime',title:'minTime'}
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