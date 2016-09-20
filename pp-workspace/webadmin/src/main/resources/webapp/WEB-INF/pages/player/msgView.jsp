<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="pengpeng taglib" uri="/pengpeng" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>

<script type="text/javascript">


    function sendAll() {
        $.get('message/sendAll', {'msg':$('#msgdlg textarea:first').val()}, function (data) {
            if (data.code == 'ok') {
                $('#msgdlg').dialog('close');
            } else {
                alert(data.code)
            }
        });
    }

    function sendFamily() {
        $.get('message/sendFamily', {'msg':$('#msgdlg2 textarea:first').val(), 'id':$('#msgdlg2 input:first').val()}, function (data) {
            if (data.code == 'ok') {
                $('#msgdlg2').dialog('close');
            } else {
                alert(data.code)
            }
        });
    }
    function sendSingle() {
        $.get('message/sendSingle', {'msg':$('#msgdlg3 textarea:first').val(), 'id':$('#msgdlg3 input:first').val()}, function (data) {
            if (data.code == 'ok') {
                $('#msgdlg3').dialog('close');
            } else {
                alert(data.code)
            }
        });
    }

</script>

<div style=" padding:10px; width:870px;">

    <div id="left" style="width:300px; float:left;">


        <div style="padding-bottom: 5px;">
            全服广播:
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#msgdlg').dialog('open')">click</a>
        </div>

        <div style="padding-bottom: 5px;">
            家族广播:
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#msgdlg2').dialog('open')">click</a>
        </div>

        <div style="padding-bottom: 5px;">
            个人广播:
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#msgdlg3').dialog('open')">click</a>
        </div>
        <!--<table id="status"></table>-->

    </div>
    <!-- left -->


    <div id="msgdlg" class="easyui-dialog" title="全服广播" data-options="
        iconCls: 'icon-save',buttons: '#msgdlg-buttons',modal:true,closed:true
       " style="width:400px;height:200px;padding:10px">
        <form>
            发送资讯:<textarea></textarea>
        </form>
    </div>
    <div id="msgdlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:sendAll()">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#msgdlg').dialog('close')">取消</a>
    </div>
    <!-- dialog3 -->

    <div id="msgdlg2" class="easyui-dialog" title="家族广播" data-options="
      iconCls: 'icon-save',buttons: '#msgdlg-buttons2',modal:true,closed:true
     " style="width:400px;height:200px;padding:10px">
        <form>
            家族id:<input type="text"/>
            <br/>
            发送资讯:<textarea></textarea>
        </form>
    </div>
    <div id="msgdlg-buttons2">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:sendFamily()">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#msgdlg2').dialog('close')">取消</a>
    </div>
    <!--dialog3 -->

    <div id="msgdlg3" class="easyui-dialog" title="玩家广播" data-options="
      iconCls: 'icon-save',buttons: '#msgdlg-buttons3',modal:true,closed:true
     " style="width:400px;height:200px;padding:10px">
        <form>
            玩家uid:<input type="text"/>
            <br/>
            发送资讯:<textarea></textarea>
        </form>
    </div>
    <div id="msgdlg-buttons3">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:sendSingle()">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#msgdlg3').dialog('close')">取消</a>
    </div>
    <!--dialog3 -->


</div>
</body>
</html>