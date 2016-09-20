<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
<table id='assistant' class="easyui-datagrid" pagination="true" rownumbers="true"
       fitColumns="true" toolbar="#tb_assistant">
</table>


<div id="tb_assistant">
    <select id="sel_assistant"> </select>
    <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doApplicationSearch()">查询</a>
    &nbsp;
    <a href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true" onclick="approve()">批准</a>
    &nbsp;
    <a href="#" class="easyui-linkbutton" iconCls="icon-no" plain="true" onclick="clean()">清除</a>
    &nbsp;
    <a href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true" onclick="designate()">机选</a>
</div>


<script type="text/javascript">
    $(function(){
        $.get("assistant/family",function(data){
           for(var i=0;i<data.length;i++){
               $("#sel_assistant").append("<option value=\""+data[i].val+"\">"+data[i].key+"</option>");
           }
        });
    });

    function doApplicationSearch() {
        var queryParams = {} ;
        if($("#sel_assistant").val()){
            queryParams.fid=$("#sel_assistant").val();
        }
        $('#assistant').datagrid({
            height:"auto",
            fit:true,
            idField:'pid', method:'GET', url:'assistant/list/',queryParams:queryParams , pagination:true, rownumbers:"true", fitColumns:"true", frozenColumns:[
                [
                    {field:'ck', checkbox:true}
                ]
            ], columns:[
                [
                    {field:'pid', title:'pid'} ,
                    {field:'name', title:'name'} ,
                    {field:'uid', title:'uid'}

                ]
            ]
        });
    }


    function approve(){
        var rows = $('#assistant').datagrid('getSelections');
        if (rows || rows.length > 0) {
            for(var i=0;i<rows.length;i++){  //alert(rows[i].pid);
                $.get("assistant/approve",{"pid":rows[i].pid});
            }
        }
    }

    function clean(){
        $.get("assistant/clean",{"fid":$("#sel_assistant").val()});
    }

    function designate(){
        $.get("assistant/designate",{"fid":$("#sel_assistant").val()});
    }
</script>

</body>
</html>