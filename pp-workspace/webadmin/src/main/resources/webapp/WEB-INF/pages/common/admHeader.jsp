<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ include file="taglib.jsp" %>
<div id="logo">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="361"><img src="<c:url value='/assets/images/admin_02.jpg'/>" width="361" height="58"/></td>
			<td align="right">
				当前用户： <sec:authentication property="name"/>
				<a href="span" class="easyui-menubutton" menu="#user-menu">${SESSION_ADMINUSER.name}</a>

				<a href="/" class="easyui-linkbutton">返回主页</a>
			</td>
			<td width="30" align="right">&nbsp;</td>
		</tr>
	</table>
</div>

<div id="user-menu" style="width:150px;">
	<%--<div iconCls="icon-edit"><a href="showAdminUser.do?id=${SESSION_ADMINUSER.id}">用户信息</a></div>--%>
	<%--<div><a href="showUserPassword.do?id=${SESSION_ADMINUSER.id}">修改密码</a></div>
	<div class="menu-sep"></div>
	<div><a href="/ums/admin-group!execute">部门管理</a></div>
    <div><a href="/ums/admin-user!execute">用户管理</a></div>
    <div><a href="/ums/admin-role!list">角色管理</a></div>
    <div><a href="/ums/sys-log!execute">查看操作日志</a></div>  --%>
    <div><a href="/j_spring_security_logout">安全退出</a></div>

</div>
<div class="boder_bottom_solid"></div>
<br>