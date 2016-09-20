<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.qianxun.model.Constant" %>
<div id="header">
	<div class="Area" id="headerWrapper">
		<div id="logo" class="logo">
			<h1><a href="<%= Constant.MAIN_HTTP %>">千寻游戏</a></h1>
		</div>
		<div id="welcome" class="right">
			<span>欢迎你，${session.username}</span>
		</div>
		<div class="clear"></div>
	</div>
</div>