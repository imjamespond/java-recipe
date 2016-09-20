<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="navbar navbar-default" role="navigation">
	<div class="navbar-header">
		<a class="navbar-brand" href="/">千寻游戏-后台管理</a>
	</div>
	<div class="navbar-header">
		<ul class="nav navbar-nav">
			<li class="dropdown"><a id="drop1" class="dropdown-toggle"
				data-toggle="dropdown" role="button" href="#">运营管理<b
					class="caret"></b></a>
				<ul class="dropdown-menu" role="menu" aria-labelledby="drop1">
					<li>
						<c:if test="${sessionScope.stage == 1}">
							<a href="/stage/list" class="list-group-item">千寻小公主管理</a>
						</c:if>
					</li>
					<li>
						<a href="/game/list" class="list-group-item">游戏榜单管理</a>
					</li>
					<li>
						<a href="/user/loggerlist" class="list-group-item">日志管理</a>
					</li>	
					<li>
						<a href="/user/signinlist" class="list-group-item">登录管理</a>
					</li>			
					<li>
						<a href="/exchange/list" class="list-group-item">兑换积分管理</a>
					</li>
					<li>
						<a href="/user/list" class="list-group-item">用户管理</a>
					</li>
					<li>
						<a href="/flyuser/list" class="list-group-item">飞行棋玩家管理</a>
					</li>
					<li>
						<a href="/forum/topic_list" class="list-group-item">话题管理</a>
					</li>
					<li>
						<a href="/forum/reply_list" class="list-group-item">回复管理</a>
					</li>
					<li>
						<a href="/forum/comment_list" class="list-group-item">评论管理</a>
					</li>
				</ul>
			</li>
		</ul>
		<ul class="nav navbar-nav">
			<li class="dropdown"><a id="drop2" class="dropdown-toggle"
				data-toggle="dropdown" role="button" href="#">系统管理<b
					class="caret"></b></a>
				<ul class="dropdown-menu" role="menu" aria-labelledby="drop2">
					<li>
						<c:if test="${sessionScope.manager == 1}">
							<a href="/manager/list" class="list-group-item">权限管理</a>
						</c:if>
						<c:if test="${sessionScope.user == 1}">
							<a href="/user/list" class="list-group-item">用户管理</a>
						</c:if>
						</li>
				</ul></li>
		</ul>
	</div>
	<div class="navbar-collapse collapse">
		<ul class="nav navbar-nav navbar-right">
			<li><c:if test="${empty sessionScope.username}">
					<a href="/manager/index">登录</a>
				</c:if> <c:if test="${!empty sessionScope.username}">
					<a href="#">${sessionScope.username}</a>
				</c:if></li>
			<li><c:if test="${!empty sessionScope.username}">
					<a href="/manager/change/${sessionScope.id}">修改密码</a>
				</c:if></li>
			<li><a href="/manager/logout">退出</a></li>
		</ul>
	</div>
</div>
