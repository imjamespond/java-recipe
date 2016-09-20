<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <div id="tbanalyse_room">
        &nbsp;开始日期:
        <input id="analyse_room_sdate" class="easyui-datebox" value="${sdate}"/> &nbsp;
        &nbsp;结束日期:
        <input id="analyse_room_edate" class="easyui-datebox" value="${edate}"/> &nbsp;
        <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="javascript:search_room();">查询</a>
    </div>
    <table id='analyseRoom' class="easyui-datagrid" url="analyse/room/list" pagination="false" rownumbers="true" fitColumns="false">
    </table>

    <script type="text/javascript">
        function search_room() {
            var queryParams = {sdate:'${sdate}',edate:'${edate}'};
            if($("#analyse_room_sdate").datebox('getValue')!=""&&$("#analyse_room_edate").datebox('getValue')!=""){
                queryParams.sdate=$("#analyse_room_sdate").datebox('getValue');
                queryParams.edate=$("#analyse_room_edate").datebox('getValue');
            }
            $('#analyseRoom').datagrid({
                queryParams:queryParams
                ,url:'analyse/room/list'
            });
        }

        $('#analyseRoom').datagrid({
			height: "auto"
            ,width:800
            ,idField:'id'
            ,method:'GET'
            ,url:'analyse/room/list'
            ,queryParams:{sdate:'${sdate}',edate:'${edate}'}
            ,columns:[[
                {field:'date',title:'日期'}
                ,{field:'num1',title:'进入房间用户数'}
                ,{field:'num2',title:'换墙纸统计'}
                ,{field:'num3',title:'换地板统计'}
                ,{field:'num4',title:'换门窗统计'}
                ,{field:'num5',title:'换衣柜统计'}
                ,{field:'num6',title:'换床铺统计'}
                ,{field:'num7',title:'换桌椅统计'}
                ,{field:'num8',title:'换柜子统计'}
                ,{field:'num9',title:'换电器统计'}
                ,{field:'num10',title:'换装饰统计'}
                ,{field:'num11',title:'换挂墙统计'}
            ]]
        });
    </script>
    </body>
</html>