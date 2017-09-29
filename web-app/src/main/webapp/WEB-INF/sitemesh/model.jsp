<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.metasoft.model.Constant" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta http-equiv="Cache-Control" content="no-store" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<script charset="UTF-8" src="<%=Constant.DOMAIN_NAME%>/js/sha1.js"></script>
	<script charset="UTF-8" src="<%=Constant.DOMAIN_NAME%>/plugin/jquery-1.12.4.js"></script><!-- for ie6~8 support -->
	<!-- <script charset="UTF-8" src="<%=Constant.DOMAIN_NAME%>/plugin/jquery-ui-1.12.1/jquery-ui.min.js"></script>-->
	<script charset="UTF-8" src="<%=Constant.DOMAIN_NAME%>/plugin/vue.js"></script>
	<script charset="UTF-8" src="<%=Constant.DOMAIN_NAME%>/plugin/vue-router.min.js"></script>
	<script charset="UTF-8" src="<%=Constant.DOMAIN_NAME%>/plugin/jstree/jstree.js"></script>
	<script charset="UTF-8" src="<%=Constant.DOMAIN_NAME%>/plugin/bootstrap/js/bootstrap.min.js"></script>
	<script charset="UTF-8" src="<%=Constant.DOMAIN_NAME%>/plugin/lodash.min.js"></script>
	<script src="<%=Constant.DOMAIN_NAME%>/plugin/nprogress.js"></script>
	
	<script charset="UTF-8" src="<%=Constant.DOMAIN_NAME%>/script/vues.js"></script>
	<script charset="UTF-8" src="<%=Constant.DOMAIN_NAME%>/script/entrances.js"></script>
	
	<!-- <link rel="stylesheet" href="<%=Constant.DOMAIN_NAME%>/plugin/jquery-ui-1.12.1/jquery-ui.min.css"> -->
	<link rel="stylesheet" href="<%=Constant.DOMAIN_NAME%>/plugin/jstree/themes/default/style.min.css" />
	<link rel='stylesheet' href="<%=Constant.DOMAIN_NAME%>/plugin/nprogress.css"/>
	<link rel="stylesheet" href="<%=Constant.DOMAIN_NAME%>/css/data-center.css">
	<link rel="stylesheet" href="<%=Constant.DOMAIN_NAME%>/css/common.css">
	<link rel="icon" href="<%=Constant.DOMAIN_NAME%>/image/favicon.ico" type="image/png" />
	<title><sitemesh:title /></title>
	<sitemesh:head />
</head>
<body>
		<%@ include file="header.jsp"%>
		<sitemesh:body />
		<%@ include file="footer.jsp"%>
</body>
</html>