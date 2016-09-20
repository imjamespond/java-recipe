<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../common/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>

<c:if test="${uid!=null}">
    ID: 用户名： 家族： 数量：
</c:if>
<table id='VideoModel' class="easyui-datagrid" pagination="true" rownumbers="true"
       fitColumns="true" toolbar="#tbVideoModel">
</table>


<div id="tbVideoModel">
    <%--&nbsp;Uid:--%>
    <%--<input id="uidVideoModel" class="easyui-validatebox" type="text"/> &nbsp;--%>
    <%--&nbsp;开始日期:--%>
    <%--<input id="dateBeginVideoModel" class="easyui-datebox"/> &nbsp;--%>
    <%--&nbsp;结束日期:--%>
    <%--<input id="dateEndVideoModel" class="easyui-datebox"/> &nbsp;--%>
    <%--<a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doPlayerVideoSearch()">查询</a>--%>
    <select id="sel_assistant"> </select>
    <a href="#" class="easyui-linkbutton" iconCls="icon-search" plain="true" onclick="doFamilyVideoSearch()">查询</a>

</div>


<script type="text/javascript">
    $(function(){
        $.get("assistant/family",function(data){
            for(var i=0;i<data.length;i++){
                $("#sel_assistant").append("<option value=\""+data[i].val+"\">"+data[i].key+"</option>");
            }
        });
    });

    function doFamilyVideoSearch() {
        var queryParams = {} ;
        if($("#sel_assistant").val()){
            queryParams.fid=$("#sel_assistant").val();
        }
        $('#VideoModel').datagrid({
            height:"auto",
            fit:true,
            idField:'id', method:'GET', url:'video/viewFamilyVideo', queryParams:queryParams ,pagination:true, rownumbers:"true", fitColumns:"true", frozenColumns:[
                [
                    {field:'ck', checkbox:true}
                ]
            ]
            ,columns:[[
                {field:'uid',title:'玩家网站Id'}
                ,{field:'name',title:'玩家名字'}
                ,{field:'num',title:'天线当前数量'}
                ,{field:'allNum',title:'家族总量'}
            ]]

        });
    }
    function doPlayerVideoSearch() {
//        alert("aaaaaaa");
//        if($("#uidVideoModel").val()){
//            var reg = /[^\d]+/;
//            if(reg.test($("#uidVideoModel").val())) {
//                alert("玩家id,请输入数字");
//                return;
//            }
//        }
//        alert("bbbbbbbbbbbb");
//        location.href = '/video/findUserVideoNum/u/'+$('#uidVideoModel').val()
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