<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <div id="tbanalyse_family">
        &nbsp;开始日期:
        <input id="analyse_family_sdate" class="easyui-datebox" value="${sdate}"/> &nbsp;
        &nbsp;结束日期:
        <input id="analyse_family_edate" class="easyui-datebox" value="${edate}"/> &nbsp;
        <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="javascript:search_family();">查询</a>
    </div>
    <table id='analyseFamily' class="easyui-datagrid" url="analyse/family/list" pagination="false" rownumbers="true" fitColumns="false">
    </table>
    <script type="text/javascript">
        function search_family() {
            var queryParams = {sdate:'${sdate}',edate:'${edate}'};
            if($("#analyse_family_sdate").datebox('getValue')!=""&&$("#analyse_family_edate").datebox('getValue')!=""){
                queryParams.sdate=$("#analyse_family_sdate").datebox('getValue');
                queryParams.edate=$("#analyse_family_edate").datebox('getValue');
            }
            $('#analyseFamily').datagrid({
                queryParams:queryParams
                ,url:'analyse/family/list'
            });
        }

        $('#analyseFamily').datagrid({
			height: "auto"
			,width:800
            ,idField:'id'
            ,method:'GET'
            ,url:'analyse/family/list'
            ,queryParams:{sdate:'${sdate}',edate:'${edate}'}
            ,columns:[[
                {field:'date',title:'日期'}
                ,{field:'name',title:'明星'}
                ,{field:'num1',title:'家族历史用户数'}
                ,{field:'num2',title:'进入家族用户数'}
                ,{field:'num3',title:'进入明星广场用户数'}
                ,{field:'num4',title:'礼物盒统计'}
                ,{field:'num5',title:'摇钱树统计'}
                ,{field:'num6',title:'家族任务统计'}
                ,{field:'num7',title:'家族游戏币捐献统计'}
                ,{field:'num8',title:'家族达人币捐献统计'}
                ,{field:'num9',title:'家族福利领取统计'}
            ]]

        });
    </script>
    </body>
</html>