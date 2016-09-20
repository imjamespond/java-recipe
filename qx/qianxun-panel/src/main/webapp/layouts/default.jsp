<%@ page contentType="text/html;charset=UTF-8"%>
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
<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
<script type="text/javascript" src="/scripts/jquery-1.11.0.min.js"></script>
<link type="text/css" rel="stylesheet" href="/styles/bootstrap/css/bootstrap.min.css" />
<script type="text/javascript" src="/styles/bootstrap/js/bootstrap.min.js"></script>
<style type="text/css">
*{font-family: "Helvetica Neue", Helvetica, Arial, "WenQuanYi Micro Hei", "Microsoft Yahei", SimSun, sans-serif;}
</style>
<sitemesh:head />
</head>
<body>
	<div class="container">
		<%@ include file="header.jsp"%>
		<div class="row">
			<%-- <div class="col-xs-3">
				<%@ include file="menu.jsp"%>
			</div> --%>
			<div class="col-xs-12">
				<sitemesh:body />
			</div>
		</div>
		<%@ include file="footer.jsp"%>
	</div>
</body>
<script type="text/javascript">
	$(document).ready(function(){
		$('.dropdown-toggle').dropdown();
	});
</script>
</html>