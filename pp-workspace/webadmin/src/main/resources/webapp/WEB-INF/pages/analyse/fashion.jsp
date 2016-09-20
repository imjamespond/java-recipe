<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <div id="tbanalyse_fashion">
        &nbsp;开始日期:
        <input id="analyse_fashion_sdate" class="easyui-datebox" value="${sdate}"/> &nbsp;
        &nbsp;结束日期:
        <input id="analyse_fashion_edate" class="easyui-datebox" value="${edate}"/> &nbsp;
        <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="javascript:search_fashion();">查询</a>
    </div>

    <table id='analyseFashion' class="easyui-datagrid" url="analyse/fashion/list" pagination="false" rownumbers="true" fitColumns="false">
    </table>

    <script type="text/javascript">
        function search_fashion() {
            var queryParams = {sdate:'${sdate}',edate:'${edate}'};
            if($("#analyse_fashion_sdate").datebox('getValue')!=""&&$("#analyse_fashion_edate").datebox('getValue')!=""){
                queryParams.sdate=$("#analyse_fashion_sdate").datebox('getValue');
                queryParams.edate=$("#analyse_fashion_edate").datebox('getValue');
            }
            $('#analyseFashion').datagrid({
                queryParams:queryParams
                ,url:'analyse/fashion/list'
            });
        }

        $('#analyseFashion').datagrid({
			height: "auto"
            ,width:800
            ,idField:'id'
            ,method:'GET'
            ,url:'analyse/fashion/list'
            ,queryParams:{sdate:'${sdate}',edate:'${edate}'}
            ,columns:[[
                {field:'date',title:'日期'}
                ,{field:'num1',title:'进行换装用户数'}
                ,{field:'num2',title:'换发部统计'}
                ,{field:'num3',title:'换脸部统计'}
                ,{field:'num4',title:'换上装统计'}
                ,{field:'num5',title:'换下装统计'}
                ,{field:'num6',title:'换套装统计'}
                ,{field:'num7',title:'换配饰统计'}
                ,{field:'num8',title:'换背饰统计'}
                ,{field:'num9',title:'换脚底统计'}
            ]]
        });

    </script>
    </body>
</html>