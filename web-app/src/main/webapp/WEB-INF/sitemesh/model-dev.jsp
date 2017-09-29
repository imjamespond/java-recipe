<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.metasoft.model.Constant" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta http-equiv="Cache-Control" content="no-store" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	
	<link rel="stylesheet" href="<%=Constant.ContextPath%>/js/lib/jstree/themes/default/style.min.css" />
	<link rel='stylesheet' href="<%=Constant.ContextPath%>/js/lib/nprogress.css"/>
	<link rel="stylesheet" href="<%=Constant.ContextPath%>/css/data-center.css">
	<link rel="stylesheet" href="<%=Constant.ContextPath%>/css/common.css">
	<link rel="icon" href="<%=Constant.ContextPath%>/image/favicon.ico" type="image/png" />
	<title><sitemesh:write property='title'>Title goes here</sitemesh:write></title>
	<sitemesh:write property='head'/>
</head>
<body>
		<%@ include file="header.jsp"%>
		<sitemesh:write property='body'>Body goes here. Blah blah blah.</sitemesh:write>
		<%@ include file="footer.jsp"%>
</body>
</html>