<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<link type="text/css" href="css/Master.css" rel="stylesheet"/>
<script type="text/javascript" src="js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="lang_cfg.js"></script>
</head>
<body>
<% 
if(request.getParameter("page") != null){
%>
<jsp:include page='<%= "right/"+request.getParameter("page") %>' flush="true"></jsp:include>
<%
}
%>
</body>
</html>
