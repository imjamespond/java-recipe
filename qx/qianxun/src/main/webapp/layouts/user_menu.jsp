<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="userMenu">
	<ul>
		<li <c:if test="${type eq 'index'}">class="active"</c:if>><a href="/user/index">我的资料</a></li>
		<li <c:if test="${type eq 'avatar'}">class="active"</c:if>><a href="/user/avatar/1">修改头像</a></li>
		<li <c:if test="${type eq 'password'}">class="active"</c:if>><a href="/user/password">修改密码</a></li>
		<li <c:if test="${type eq 'payDetail'}">class="active"</c:if> style="display:none;"><a href="/user/payDetail">我的充值</a></li>
		<li <c:if test="${type eq 'bind'}">class="active"</c:if>><a href="/user/bind">帐号安全</a></li>
		<li <c:if test="${type eq 'fcm'}">class="active"</c:if>><a href="/user/fcm">防沉迷认证</a></li>
		<li <c:if test="${type eq 'integral'}">class="active"</c:if>><a href="/user/integral">个人资产</a></li>
		<%-- <li <c:if test="${type eq 'record'}">class="active"</c:if>><a href="/user/record">比赛记录</a></li> --%>
		<li <c:if test="${type eq 'exchange'}">class="active"</c:if>><a href="/exchange/index">兑换管理</a></li>
		<li <c:if test="${type eq 'message'}">class="active"</c:if> style="display:none;"><a href="/user/message">我的消息</a></li>
	</ul>
</div>