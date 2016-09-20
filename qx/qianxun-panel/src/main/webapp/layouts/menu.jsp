<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="list-group">

	<c:if test="${sessionScope.paper == 1}">
		<a href="/paper/list"
			class="list-group-item <c:if test="${index == 'paper'}">active</c:if>">手机报管理</a>
	</c:if>
	<c:if test="${sessionScope.category == 1}">
		<a href="/category/list"
			class="list-group-item <c:if test="${index == 'category'}">active</c:if>">分类管理</a>
	</c:if>
	<c:if test="${sessionScope.application == 1}">
		<a href="/application/list"
			class="list-group-item <c:if test="${index == 'application'}">active</c:if>">应用管理</a>
	</c:if>
	<c:if test="${sessionScope.user == 1}">
		<a href="/user/list"
			class="list-group-item <c:if test="${index == 'user'}">active</c:if>">用户管理</a>
	</c:if>
	<c:if test="${sessionScope.music == 1}">
		<a href="/music/list"
			class="list-group-item <c:if test="${index == 'music'}">active</c:if>">音乐管理</a>
	</c:if>
	<c:if test="${sessionScope.calendar == 1}">
		<a href="/calendar/manage"
			class="list-group-item <c:if test="${index == 'calendar'}">active</c:if>">藏历管理</a>
	</c:if>
	<c:if test="${sessionScope.manager == 1}">
		<a href="/manager/list"
			class="list-group-item <c:if test="${index == 'manager'}">active</c:if>">权限管理</a>
		<a href="/template/list"
			class="list-group-item <c:if test="${index == 'template'}">active</c:if>">模板管理</a>
		<a href="/hotel/list"
			class="list-group-item <c:if test="${index == 'hotel'}">active</c:if>">酒店管理</a>
		<a href="/attraction/list"
			class="list-group-item <c:if test="${index == 'attraction'}">active</c:if>">景点管理</a>
		<a href="/carline/list"
		class="list-group-item <c:if test="${index == 'carline'}">active</c:if>">包车管理</a>
		<a href="/driver/list"
		class="list-group-item <c:if test="${index == 'driver'}">active</c:if>">司机管理</a>
		<a href="/car/list"
		class="list-group-item <c:if test="${index == 'car'}">active</c:if>">汽车管理</a>
		<a href="/flight/list"
		class="list-group-item <c:if test="${index == 'flight'}">active</c:if>">航班管理</a>
		<a href="/trip/list"
		class="list-group-item <c:if test="${index == 'trip'}">active</c:if>">计划管理</a>
		<a href="/group/list"
		class="list-group-item <c:if test="${index == 'group'}">active</c:if>">圈子管理</a>
		<a href="/topic/list"
		class="list-group-item <c:if test="${index == 'topic'}">active</c:if>">话题管理</a>
			<a href="/subject/list"
		class="list-group-item <c:if test="${index == 'subject'}">active</c:if>">专题管理</a>
	</c:if>

</div>
