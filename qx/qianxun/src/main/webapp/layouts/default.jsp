<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.qianxun.model.Constant" %>
<%@ taglib prefix="sitemesh"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="baidu-site-verification" content="ZroqSGMtZW" />
<meta name="Keywords" content="qianxun,qianxunyouxi.com,千寻游戏,千寻游戏网,游戏平台,网页游戏,妹子最喜欢,飞行棋,飞行棋比赛,房主模式" />
<meta name="description" content="千寻游戏网以最专具特色的房主模式,最具特色的网页游戏,为玩家提供娱乐全新体验,成为游戏玩家首要选择的网页游戏平台网站。">
<title><sitemesh:title /></title>
<link rel="shortcut icon" href="<%= Constant.MAIN_HTTP %>/images/fav.gif" type="image/x-icon" />
<link type="text/css" rel="stylesheet" href="<%= Constant.MAIN_HTTP %>/styles/common.css?t=20150806" />
<link type="text/css" rel="stylesheet" href="<%= Constant.MAIN_HTTP %>/styles/default.css?t=20150806" />
<link type="text/css" rel="stylesheet" href="<%= Constant.MAIN_HTTP %>/styles/jquery_dialog_x.css?t=20150806" />

<script src="http://libs.baidu.com/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="<%= Constant.MAIN_HTTP %>/scripts/jquery_dialog_x.js?t=20150806"></script>
<script type="text/javascript" src="<%= Constant.MAIN_HTTP %>/scripts/global.js?t=20150806"></script>
<script type="text/javascript" src="<%= Constant.MAIN_HTTP %>/scripts/qx.js?t=20150806"></script>

<!-- Bootstrap -->
<link type="text/css" rel="stylesheet" href="/css/bootstrap.min.css">
<sitemesh:head />
</head>
<body>
	<div class="wrapper">
		<%@ include file="header.jsp"%>
		<sitemesh:body />
		<%@ include file="footer.jsp"%>
	</div>
</body>
</html>