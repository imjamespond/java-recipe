<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <body>

    <%--<table id='analyseTotal' class="easyui-datagrid" url="analyse/total/list" pagination="false" rownumbers="true" fitColumns="false">--%>
    <table id='analyseTotal' class="easyui-datagrid" pagination="true" rownumbers="true"
           fitColumns="true" toolbar="#tbanalyse_total">
    </table>


    <div id="tbanalyse_total">
        <select id="sel_assistant"> </select>
        &nbsp;开始日期:
        <input id="analyse_total_sdate" class="easyui-datebox" value="${sdate}"/> &nbsp;
        &nbsp;结束日期:
        <input id="analyse_total_edate" class="easyui-datebox" value="${edate}"/> &nbsp;
        <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="javascript:search_total();">查询</a>
    </div>
    <script type="text/javascript">
        $(function(){
            $("#sel_assistant").append("<option value=\""+"\">"+"全部"+"</option>");
            $.get("assistant/family",function(data){
                for(var i=0;i<data.length;i++){
                    $("#sel_assistant").append("<option value=\""+data[i].val+"\">"+data[i].key+"</option>");
                }
            });
        });

        function search_total() {
            var queryParams = {};
//            if($("#analyse_total_sdate").datebox('getValue')!=""&&$("#analyse_total_edate").datebox('getValue')!=""){
//                queryParams.sdate=$("#analyse_total_sdate").datebox('getValue');
//                queryParams.edate=$("#analyse_total_edate").datebox('getValue');
//            }
//            if($("#sel_assistant").val()){
                queryParams.fid=$("#sel_assistant").val();
//            }
            if($("#analyse_total_sdate").datebox('getValue')!=""&&$("#analyse_total_edate").datebox('getValue')!=""){
                queryParams.sdate=$("#analyse_total_sdate").datebox('getValue');
                queryParams.edate=$("#analyse_total_edate").datebox('getValue');
            }else{
                alert("请输入开始日期和结束日期")
                return;
            }

//            $('#analyseTotal').datagrid({
//                queryParams:queryParams
//                ,url:'analyse/total/list'
//            });
            $('#analyseTotal').datagrid({
                height: "auto"
                , fit:true
                ,idField:'id'
                ,method:'GET'
                ,url:'analyse/total/list'
                <%--,queryParams:{sdate:'${sdate}',edate:'${edate}'}--%>
                ,queryParams:queryParams
                ,pagination:"true"
                ,rownumbers:"true"
                ,fitColumns:"true"
                ,frozenColumns:[
                    [
                        {field:'ck', checkbox:true}
                    ]
                ]
                ,columns:[[
                    {field:'date',title:'日期'}
                    ,{field:'num1',title:'游戏历史注册用户数'}
                    ,{field:'num2',title:'游戏当天登录用户数'}
                    ,{field:'string1',title:'用户流失率%'}
                    ,{field:'num4',title:'平均在线(单位:人/小时)'}
                    ,{field:'num5',title:'最高在线(单位:人)'}
                    ,{field:'num6',title:'最低在线(单位:人)'}
                    ,{field:'num7',title:'活跃数'}
                    ,{field:'num8',title:'7天活跃数'}
                    ,{field:'num9',title:'当天游戏积分获取数'}
                ]]
            });
        }



    </script>
    </body>
</html>