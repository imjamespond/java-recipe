<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<table id='vipInfoModel' class="easyui-datagrid" pagination="true" rownumbers="true"
       fitColumns="true" toolbar="#tbvipInfoModel">
</table>


<div id="tbvipInfoModel">
    &nbsp;Uid:
    <input id="uidvipInfoModel" class="easyui-validatebox" type="text"/> &nbsp;
    &nbsp;开始日期:
    <input id="dateBeginvipInfoModel" class="easyui-datebox"/> &nbsp;
    &nbsp;结束日期:
    <input id="dateEndvipInfoModel" class="easyui-datebox"/> &nbsp;
    <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doVipSearch()">查询</a>
</div>


<script type="text/javascript">

    function doVipSearch() {
        var queryParams = {} ;
        if($("#uidvipInfoModel").val()){
            queryParams.uid=$("#uidvipInfoModel").val();
            var reg = /[^\d]+/;
            if(reg.test($("#uidvipInfoModel").val())){
                alert("玩家id,请输入数字");
                return;
            }
        }

        if($("#dateBeginvipInfoModel").datebox('getValue')!=""&&$("#dateEndvipInfoModel").datebox('getValue')!=""){
            queryParams.dateBegin=$("#dateBeginvipInfoModel").datebox('getValue');
            queryParams.dateEnd=$("#dateEndvipInfoModel").datebox('getValue');
        }else{
            alert("请输入开始日期和结束日期")
            return;
        }
        $('#vipInfoModel').datagrid({
            height:"auto",
            fit:true,
            idField:'id', method:'GET', url:'rechage/vipRechage', queryParams:queryParams ,pagination:true, rownumbers:"true", fitColumns:"true", frozenColumns:[
                [
                    {field:'ck', checkbox:true}
                ]
            ]
            ,columns:[[
                {field:'id',title:'订单号'}
                ,{field:'uid',title:'玩家网站Id'}
                ,{field:'name',title:'玩家名字'}
                ,{field:'pid',title:'玩家游戏Id'}
                ,{field:'date',title:'充值日期'}
                ,{field:'amount',title:'充值几个月'}
                ,{field:'all',title:'总月份'}
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