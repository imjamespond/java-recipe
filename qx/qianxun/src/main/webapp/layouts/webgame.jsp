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
<script type="text/javascript" src="http://libs.baidu.com/jquery/1.10.2/jquery.min.js"></script>
<sitemesh:head />
</head>
<body>
	<div class="wrapper">
		<!--<div class="webgame_header">
			<%@ include file="webgame_header.jsp"%>
		</div>-->
		<sitemesh:body />
	</div>
</body>
</html>