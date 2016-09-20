<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>发帖</title>
<link href="/plugins/umeditor/themes/default/css/umeditor.min.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="/plugins/umeditor/umeditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="/plugins/umeditor/umeditor.min.js"></script>
<script type="text/javascript" src="/plugins/umeditor/lang/zh-cn/zh-cn.js"></script>
</head>
<body>
<div id="container" class="Area bgfff">
	<div id="userLeader">
		<a href="/topic/index">论坛首页</a>
		&gt
		<a href="/topic/${stype}">
			<c:if test="${stype eq 1}">比赛专区</c:if>
			<c:if test="${stype eq 2}">千寻帮助</c:if>
			<c:if test="${stype eq 3}">玩家交流</c:if>
		</a>
		&gt
		<span>发帖</span>
	</div>
	<div id="topicContainer">
		<div class="postedDiv overhide">
			<h1>发表帖子</h1>
			<div>
				<label for="title">标题：</label>
				<input type="text" id="title"/>
				<span id="titleRemind">标题不超过50个字符</span>
				<div id="titleError" class="red"></div>
			</div>
			<div>
				<script type="text/plain" id="content" name="content" style="width: 920px;height: 280px;"></script>
				<div id="contentError" class="red"></div>
				<a href="javascript:;" class="orgBtn postedBtn left" onclick="topicFn.create_topic('${stype}')">提交</a>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	var ue = UM.getEditor('content', {
	    toolbar: [
				' image','emotion'
	    ],
	    imageUrl: '/topic/upload',
	    imagePath : index
	});
});
</script>
</body>
</html>