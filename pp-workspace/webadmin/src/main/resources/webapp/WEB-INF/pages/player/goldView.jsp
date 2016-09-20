<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="pengpeng taglib" uri="/pengpeng" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>

<script type="text/javascript">


    function getInfo() {
        $.post('gold', function (data) {
            $('#golddlg input:first').val(data.gold);
            $('#golddlg textarea:first').val(data.uids);
            $('#golddlg').dialog('open');
        });


    }

    function save() {
        $.get('gold/save', {'gold':$('#golddlg input:first').val(),'uids':$('#golddlg textarea:first').val()}, function (data) {
            if (data.code == 'ok') {
                $('#golddlg').dialog('close');
            } else {
                alert(data.code)
            }
        });
    }

</script>

<div style=" padding:10px; width:870px;">

    <div id="left" style="width:300px; float:left;">


        <div style="padding-bottom: 5px;">
            登陆送达人币:
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="getInfo()">click</a>
        </div>


        <!--<table id="status"></table>-->

    </div>
    <!-- left -->


    <div id="golddlg" class="easyui-dialog" title="登陆送达人币" data-options="
        iconCls: 'icon-save',buttons: '#golddlg-buttons',modal:true,closed:true
       " style="width:400px;height:200px;padding:10px">
        <form>
            达人币:<input type="text"/>
            <br/>
            玩家uid:<textarea cols="10"></textarea>
        </form>
    </div>
    <div id="golddlg-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:save()">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#golddlg').dialog('close')">取消</a>
    </div>
    <!-- dialog3 -->




</div>
</body>
</html>