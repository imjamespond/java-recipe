<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="pengpeng taglib" uri="/pengpeng" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<body>

<script type="text/javascript">


    function change() {
        if($('#newpwd').val().indexOf($('#confirm').val())){
               alert("请确认密码!");
        }
        $.get('password/change', {'prepwd':$('#prepwd').val(), 'newpwd':$('#newpwd').val()}, function (data) {
            if (data.code == 'ok') {
                $('#password').dialog('close');
            } else {
                alert(data.code)
            }
        });
    }

</script>

<div style=" padding:10px; width:870px;">

    <div id="left" style="width:300px; float:left;">


        <div style="padding-bottom: 5px;">
            修改密码:
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#password').dialog('open')">click</a>
        </div>

    </div>
    <!-- left -->


    <div id="password" class="easyui-dialog" title="修改密码" data-options="
        iconCls: 'icon-save',buttons: '#password-buttons',modal:true,closed:true
       " style="width:400px;height:200px;padding:10px">
        <form>
            原密码:<input id="prepwd" type="text"/>
            <br/>
            新密码:<input id="newpwd" type="text"/>
            <br/>
            确认密码:<input id="confirm" type="text"/>
        </form>
    </div>
    <div id="password-buttons">
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:change()">确定</a>
        <a href="javascript:void(0)" class="easyui-linkbutton" onclick="javascript:$('#password').dialog('close')">取消</a>
    </div>
    <!-- dialog3 -->



</div>
</body>
</html>