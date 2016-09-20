<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>

    <div id="tbanalyse_lottery">
        &nbsp;开始日期:
        <input id="analyse_lottery_sdate" class="easyui-datebox" value="${sdate}"/> &nbsp;
        &nbsp;结束日期:
        <input id="analyse_lottery_edate" class="easyui-datebox" value="${edate}"/> &nbsp;
        <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="javascript:search__lottery();">查询</a>
    </div>

    <table id='analyseLottery' class="easyui-datagrid" url="analyse/lottery/list" pagination="false" rownumbers="true" fitColumns="false">
    </table>

    <script type="text/javascript">
        function search__lottery() {
            var queryParams = {sdate:'${sdate}',edate:'${edate}'};
            if($("#analyse_lottery_sdate").datebox('getValue')!=""&&$("#analyse_lottery_edate").datebox('getValue')!=""){
                queryParams.sdate=$("#analyse_lottery_sdate").datebox('getValue');
                queryParams.edate=$("#analyse_lottery_edate").datebox('getValue');
            }
            $('#analyseLottery').datagrid({
                queryParams:queryParams
                ,url:'analyse/lottery/list'
            });
        }

        $('#analyseLottery').datagrid({
			height: 'auto'
            ,width:800
            ,idField:'id'
            ,method:'GET'
            ,url:'analyse/lottery/list'
            ,queryParams:{sdate:'${sdate}',edate:'${edate}'}
            ,columns:[[
                {field:'date',title:'日期'}
                ,{field:'num1',title:'游戏币5000'}
                ,{field:'num2',title:'游戏币20000'}
                ,{field:'num3',title:'游戏币150000'}
                ,{field:'num4',title:'游戏币200000'}
                ,{field:'num5',title:'音乐旋律100个'}
                ,{field:'num6',title:'音乐先锋幼苗5个'}
                ,{field:'num7',title:'音乐先锋幼苗40个'}
                ,{field:'num8',title:'音乐先锋幼苗100个'}
                ,{field:'num9',title:'音乐先锋100个'}
                ,{field:'num10',title:'樱木花道'}
                ,{field:'num10',title:'紫蓝'}
                ,{field:'num10',title:'犬夜叉'}
                ,{field:'num10',title:'成熟干练'}
            ]]
        });

    </script>
    </body>
</html>