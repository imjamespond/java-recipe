<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
	<meta http-equiv="Cache-Control" content="no-store" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	
	<script type="text/javascript" src="/js/jquery-1.12.4.js"></script><!-- for ie6~8 support -->
	
	<script type="text/javascript" src="/js/meta-global.js"></script>
	<script type="text/javascript" src="/js/meta-jstree.js"></script>
	<script type="text/javascript" src="/js/meta-user.js"></script>
	<script type="text/javascript" src="/js/meta-data.js"></script>
	
	<script type="text/javascript" src="/plugins/jstree/jstree.js"></script>
	<script type="text/javascript" src="/plugins/bootstrap/js/bootstrap.min.js"></script>
	
	<link href="/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="/css/common.css" rel="stylesheet">
	<link href="/images/favicon.ico" rel="icon" type="image/png" />
	<title><sitemesh:write property='title'/></title>
	<sitemesh:write property="head" />
</head>
<body>
	<div class="container-fluid">
		<%@ include file="header.jsp"%>
		<sitemesh:write property="body" />
		<%@ include file="footer.jsp"%>
	</div>
</body>
</html>