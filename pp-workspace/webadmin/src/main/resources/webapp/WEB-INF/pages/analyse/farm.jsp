<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <div id="tbanalyse_farm">
        &nbsp;开始日期:
        <input id="analyse_farm_sdate" class="easyui-datebox" value="${sdate}"/> &nbsp;
        &nbsp;结束日期:
        <input id="analyse_farm_edate" class="easyui-datebox" value="${edate}"/> &nbsp;
        <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="javascript:search_farm();">查询</a>
    </div>
    <table id='analyseFarm' class="easyui-datagrid" url="analyse/farm/list" pagination="false" rownumbers="true" fitColumns="false">
    </table>
    <script type="text/javascript">
        function search_farm() {
            var queryParams = {};
            if($("#analyse_farm_sdate").datebox('getValue')!=""&&$("#analyse_farm_edate").datebox('getValue')!=""){
                queryParams.sdate=$("#analyse_farm_sdate").datebox('getValue');
                queryParams.edate=$("#analyse_farm_edate").datebox('getValue');
            }
            $('#analyseFarm').datagrid({
                height: "auto"
                ,fit:true

                ,idField:'id'
                ,method:'GET'
                ,url:'analyse/farm/list'
                ,queryParams:queryParams
                ,columns:[[
                    {field:'date',title:'日期'}
                    ,{field:'st1',title:'小游戏排行'}
                    ,{field:'st2',title:'明星收集活动'}
                    ,{field:'st3',title:'货船起航'}
                    ,{field:'st4',title:'神秘路人'}
                    ,{field:'st5',title:'领取活跃度'}
                    ,{field:'st6',title:'神秘宝箱'}
                    ,{field:'st7',title:'招财树'}
                    ,{field:'st8',title:'任务奖励'}
                    ,{field:'st10',title:'成就系统'}
                ]]
            });
        }


    </script>
    </body>
</html>