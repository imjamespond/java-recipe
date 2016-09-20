<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="date" uri="/WEB-INF/copycat.tld"%>
<!DOCTYPE html>
<html>
<head>
<title>千寻论坛</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
</head>
<body>
<div id="container" class="Area bgfff">
	<div id="userLeader">
		<a href="/topic/index">论坛首页</a>
		&gt
		<span>
			<c:if test="${stype eq 1}">比赛专区</c:if>
			<c:if test="${stype eq 2}">千寻帮助</c:if>
			<c:if test="${stype eq 3}">玩家交流</c:if>
		</span>
	</div>
	<div id="topicContainer">
		<div>
			<div class="formRow15 overhide">
				<a href="/topic/create/${stype}" class="orgBtn postedBtn left">发帖</a>
				<tags:pagination page="${page}" url="/topic/${stype}" />
			</div>
			<div class="formRow15 borderA"></div>
			<div class="formRow20 topicList">
				<ul>
					<li class="listHead">
						<div class="col_0">全部主题</div>
						<div class="col_1">作者</div>
						<div class="col_2">回复/查看</div>
						<div class="col_3">最后发表</div>
					</li>
					<c:forEach items="${topList}" var="top" varStatus="status">
						<li>
							<div class="col_0">
								<a href="/topic/show/${top.id}" title="${top.title}">${top.title}</a>
							</div>
							<div class="col_1">
								<p class="p1"><span>${top.username}</span></p>
								<p class="p2 c2"><date:human value="${top.createTime}" /></p>
							</div>
							<div class="col_2">
								<p class="p1 c1">${top.reply}</p>
								<p class="p2 c2">${top.read}</p>
							</div>
							<div class="col_3">
								<p class="p1">${top.lastReply}</p>
								<p class="p2 c2"><date:human value="${top.replyTime}" /></p>
							</div>
						</li>
					</c:forEach>
				</ul>
			</div>
			<div class="formRow30 topicList">
				<ul>
					<li class="listHead">
						<div class="col_0">玩家区</div>
						<div class="col_1">作者</div>
						<div class="col_2">回复/查看</div>
						<div class="col_3">最后发表</div>
					</li>
					<c:forEach items="${topicList}" var="topic" varStatus="status">
						<li>
							<div class="col_0">
								<a href="/topic/show/${topic.id}" title="${topic.title}">${topic.title}</a>
							</div>
							<div class="col_1">
								<p class="p1"><span>${topic.username}</span></p>
								<p class="p2 c2"><date:human value="${topic.createTime}" /></p>
							</div>
							<div class="col_2">
								<p class="p1 c1">${topic.reply}</p>
								<p class="p2 c2">${topic.read}</p>
							</div>
							<div class="col_3">
								<p class="p1">${topic.lastReply}</p>
								<p class="p2 c2"><date:human value="${topic.replyTime}" /></p>
							</div>
						</li>
					</c:forEach>
				</ul>
			</div>
			<div class="formRow30 borderA"></div>
			<div class="formRow15 overhide">
				<a href="/topic/create/${stype}" class="orgBtn postedBtn left">发帖</a>
				<tags:pagination page="${page}" url="/topic/${stype}" />
			</div>
		</div>
	</div>
</div>
</body>
</html>