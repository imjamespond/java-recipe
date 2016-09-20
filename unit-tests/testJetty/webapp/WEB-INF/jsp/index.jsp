<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="./common/taglib.jsp" %>
<%@ page
	import="java.util.Map,java.util.Map.Entry,java.util.HashMap,java.util.Iterator,java.util.List"%>

<%
	out.println("Hello guys!");
%>
<p>welcome back <sec:authentication property="name"/></p>
<p>${var1}</p>
<sec:authorize ifAnyGranted="ROLE_GM,ROLE_ADMIN">
<p>ROLE_ADMIN supposed to see</p>
</sec:authorize>
<sec:authorize ifAnyGranted="ROLE_GM">
<p>ROLE_ADMIN not supposed to see</p>
</sec:authorize>
<p><a href="/j_spring_security_logout">Log Out</a></p>  

${numUsers} user(s) are logged in! 