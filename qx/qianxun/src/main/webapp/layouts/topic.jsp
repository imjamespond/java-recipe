<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.qianxun.model.Constant" %>
<%@ taglib prefix="sitemesh"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title><sitemesh:title /></title>
<link rel="shortcut icon" href="<%= Constant.MAIN_HTTP %>/images/fav.gif" type="image/x-icon" />
<link type="text/css" href="<%= Constant.MAIN_HTTP %>/styles/common.css?t=20150806" rel="stylesheet">
<link type="text/css" href="<%= Constant.MAIN_HTTP %>/styles/topic.css?t=20150806" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%= Constant.MAIN_HTTP %>/styles/jquery_dialog_x.css?t=20150806" />
<script type="text/javascript" src="http://libs.baidu.com/jquery/1.10.2/jquery.min.js"></script>
<script type="text/javascript" src="<%= Constant.MAIN_HTTP %>/scripts/jquery_dialog_x.js?t=20150806"></script>
<script type="text/javascript" src="<%= Constant.MAIN_HTTP %>/scripts/global.js?t=20150806"></script>
<script type="text/javascript" src="<%= Constant.MAIN_HTTP %>/scripts/qx.js?t=20150806"></script>
<script type="text/javascript" src="<%= Constant.MAIN_HTTP %>/scripts/sha512.js"></script>
<sitemesh:head />
</head>
<body>
	<div id="topic" class="wrapper">
		<%@ include file="topic_header.jsp"%>
		<sitemesh:body />
		<%@ include file="footer.jsp"%>
	</div>
</body>
</html>