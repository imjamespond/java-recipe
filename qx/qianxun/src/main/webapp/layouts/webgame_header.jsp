<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="
com.qianxun.model.Constant
" %>
<div class="webgame_headerInner">
	<div class="webgame_nav">
		<a href="<%= Constant.MAIN_HTTP %>">回到首页</a>
	</div>
	<div class="webgame_userInfo">
		<span>欢迎您，${session.username}</span>
	</div>
</div>