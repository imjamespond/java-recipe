<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ page import="com.qianxun.model.Constant" %>
<!DOCTYPE html>
<html>
<head>
<title>千寻论坛</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
</head>
<body>
<div id="container" class="Area bgfff">
	<div id="userLeader">
		<span>论坛首页</span>
	</div>
	<div id="topicContainer">
		<div>
			<div class="module">
				<div class="moduleImg">
					<a href="/topic/1"><img src="<%= Constant.MAIN_HTTP %>/images/topic_0.jpg" alt=""></a>
				</div>
				<div class="moduleCon">
					<h1><a href="/topic/1">比赛专区</a></h1>
					<p>来吧，小伙伴们，比赛中的恩恩怨怨，都来这里解决。</p>
					<p>让我们战个痛快！</p>
				</div>
			</div>
			<div class="module">
				<div class="moduleImg">
					<a href="/topic/2"><img src="<%= Constant.MAIN_HTTP %>/images/topic_1.jpg" alt=""></a>
				</div>
				<div class="moduleCon">
					<h1><a href="/topic/2">千寻帮助</a></h1>
					<p>在游戏中有疑问？赶紧戳进这里吧，这里有我们的工作人员和热心玩家都会帮助你解决问题哦。</p>
				</div>
			</div>
			<div class="module">
				<div class="moduleImg">
					<a href="/topic/3"><img src="<%= Constant.MAIN_HTTP %>/images/topic_2.jpg" alt=""></a>
				</div>
				<div class="moduleCon">
					<h1><a href="/topic/3">玩家交流</a></h1>
					<p>你有什么心得体会或者新奇的策略玩法，赶紧来这里和其他小伙伴一起交流分享吧，有分享才有快乐！</p>
				</div>
			</div>
			<div class="module">
				<div class="moduleImg">
					<img src="<%= Constant.MAIN_HTTP %>/images/topic_3.jpg" alt="">
				</div>
				<div class="moduleCon contact">
					<h1>联系我们</h1>
					<p class="pd20_0">请添加我们的客服人员联系方式</p>
					<p>QQ：121035577</p>
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
</div>
</body>
</html>