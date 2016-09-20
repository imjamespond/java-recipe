<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>
    <div id="tbanalyse_operation">
        &nbsp;开始日期:
        <input id="analyse_operation_sdate" class="easyui-datebox" value="${sdate}"/> &nbsp;
        &nbsp;结束日期:
        <input id="analyse_operation_edate" class="easyui-datebox" value="${edate}"/> &nbsp;
        <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="javascript:search_operation();">查询</a>
    </div>
    <table id='analyseoperation' class="easyui-datagrid" url="analyse/operation/list">
    </table>
    <script type="text/javascript">
        function search_operation() {
            var queryParams = {};
            if($("#analyse_operation_sdate").datebox('getValue')!=""&&$("#analyse_operation_edate").datebox('getValue')!=""){
                queryParams.sdate=$("#analyse_operation_sdate").datebox('getValue');
                queryParams.edate=$("#analyse_operation_edate").datebox('getValue');
            }
            $('#analyseoperation').datagrid({
                height: "auto"
                ,fit:true
//                ,width:1000
                ,idField:'id'
                ,method:'GET'
                ,url:'analyse/operation/list'
                ,queryParams:queryParams
                ,columns:[[
                    {field:'date',title:'日期'}
                    ,{field:'st1',title:'注册量'}
                    ,{field:'st2',title:'登录用户数'}
                    ,{field:'st3',title:'首日登录'}
                    ,{field:'st4',title:'每日收入'}
                    ,{field:'st5',title:'每日充值人数'}
                    ,{field:'st6',title:'首次充值金额'}
                    ,{field:'st7',title:'首次充值人数'}

                    ,{field:'st8',title:'当日总充值率'}
                    ,{field:'st9',title:'首次登陆充值率'}
                    ,{field:'st10',title:'每日总ARPU值'}
                    ,{field:'st11',title:'首次ARPU值'}
                    ,{field:'st12',title:'收入/注册'}
                    ,{field:'st13',title:'新用户 收入占比'}
                    ,{field:'st14',title:'新注册次日留存'}
                ]]
            });
        }


    </script>
    </body>
</html>