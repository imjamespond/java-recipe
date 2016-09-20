<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="date" uri="/WEB-INF/copycat.tld"%>
<%@ page import="com.qianxun.model.Constant" %>
<html>
<head>
<title>帖子详情</title>
<link href="<%= Constant.MAIN_HTTP %>/plugins/umeditor/themes/default/css/umeditor.min.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="<%= Constant.MAIN_HTTP %>/plugins/umeditor/umeditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="<%= Constant.MAIN_HTTP %>/plugins/umeditor/umeditor.min.js"></script>
<script type="text/javascript" src="<%= Constant.MAIN_HTTP %>/plugins/umeditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript" src="<%= Constant.MAIN_HTTP %>/scripts/jquery.tmpl.min.js"></script>
</head>
<body>
<div id="container" class="Area bgfff">
	<div id="userLeader">
		<a href="/topic/index">论坛首页</a>
		&gt
		<a href="/topic/${topic.stype}">
			<c:if test="${topic.stype eq 1}">比赛专区</c:if>
			<c:if test="${topic.stype eq 2}">千寻帮助</c:if>
			<c:if test="${topic.stype eq 3}">玩家交流</c:if>
		</a>
		&gt
		<span>${topic.title}</span>
	</div>
	<div id="topicContainer">
		<div>
			<div class="formRow15 overhide">
				<c:if test="${empty session}">
					<a href="javascript:;" jump="/topic/create/${topic.stype}" onclick="toLogin(this)"  class="orgBtn postedBtn left">发帖</a>
					<a href="javascript:;" jump="/topic/show/${topic.id}" onclick="toLogin(this)" class="orgBtn postedBtn left">回复</a>
				</c:if>
				<c:if test="${!empty session}">
					<a href="/topic/create/${topic.stype}" class="orgBtn postedBtn left">发帖</a>
					<a href="#topic_reply" class="orgBtn postedBtn left">回复</a>
				</c:if>
				<tags:pagination page="${page}" url="/topic/show/${topic.id}" />
			</div>
			<div class="topicTable">
				<table>
					<tr>
						<th class="td0">
							<div class="left">查看:<span>${topic.read}</span></div>
							<span class="left">|</span>
							<div class="right">回复:<span>${topic.reply}</span></div>
						</th>
						<th class="td1"><h1 title="${topic.title}">${topic.title}</h1></th>
					</tr>
					<c:if test="${page.number eq 1}">
					<tr>
						<td class="td0">
							<div class="td_l_0">${topic.username}</div>
							<div class="td_l_1">
								
								<img src="${imageURL}/avatar/${topic.uid}-150.jpg"  onerror="this.src='${mainUrl}/images/default.png'">
								
							</div>
						</td>
						<td class="td1">
							<div>
								<div class="td_r_0">
									发表于 <date:format value="${topic.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</div>
								<div class="td_r_1">
									<div class="topicCon">${topic.content}</div>
								</div>
								<div class="td_r_2">
									<c:if test="${empty session}">
										<a href="javascript:;" onclick="toLogin(this)" class="topic_postItemReply">回复</a>
									</c:if>
									<c:if test="${!empty session}">
										<a href="#topic_reply" class="reviewBtn">回复</a>
									</c:if>
								</div>
							</div>
						</td>
					</tr>
					</c:if>
					<c:forEach items="${replyList}" var="reply" varStatus="status">
					<tr rid="${reply.id}">
						<td class="td0">
							<div class="td_l_0">${reply.username}</div>
							<div class="td_l_1">
								<img src="${imageURL}/avatar/${reply.uid}-150.jpg" onerror="this.src='${mainUrl}/images/default.png'">
							</div>
						</td>
						<td class="td1">
							<div>
								<div class="td_r_0">
									发表于 <date:format value="${reply.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
								</div>
								<div class="td_r_1">
									<div class="topicCon">${reply.content}</div>
								</div>
								<div class="td_r_2">
									<a href="javascript:;" class="reviewBtn" onclick="topicFn.review(this,'${reply.id}')">回复</a>
									<div class="reviewList">
										<ul>
											<c:forEach items="${reply.commentList}" var="comment" >
											<li class="overhide" commentid="${comment.id}">
												<div class="reviewL">
													<img src="${imageURL}/avatar/${comment.uid}-80.jpg" alt="">
												</div>
												<div class="reviewR">
													<span class="replyuname">${comment.username}</span>
													：
													<span>${comment.content}</span>
												</div>
												<div class="reviewB">
													<span><date:human value="${comment.createTime}" /></span>
													<a href="javascript:;" onclick="topicFn.replytoreply(this,'${reply.id}','${comment.id}')">回复</a>
												</div>
											</li>
											</c:forEach>
											<li id="def_${reply.id}" class="lastLi"></li>
										</ul>
									</div>
									<div class="reviewArea hidden">
										<textarea class="reviewText" cols="90" rows="6"></textarea>
										<p class="replyError"></p>
										<c:if test="${empty session.uid}">
											<a href="javascript:;" class="orgBtn reviewSubBtn"  onclick="toLogin(this)">回复</a>
										</c:if>
										<c:if test="${!empty session.uid}">
											<a href="javascript:;" class="orgBtn reviewSubBtn" onclick="topicFn.comment('${reply.id}','${topic.id}')">回复</a>
											<input type="hidden" id="comment_to_${reply.id}" value="0">
										</c:if>
									</div>
								</div>
							</div>
						</td>
					</tr>
					</c:forEach>
					<tr id="lastTr"></tr>
				</table>
			</div>
			<div class="blank20"></div>
			<div class="formRow15 overhide">
				<c:if test="${empty session}">
					<a href="javascript:;" jump="/topic/create/${topic.stype}" onclick="toLogin(this)"  class="orgBtn postedBtn left">发帖</a>
					<a href="javascript:;" jump="/topic/show/${topic.id}" onclick="toLogin(this)" class="orgBtn postedBtn left">回复</a>
				</c:if>
				<c:if test="${!empty session}">
					<a href="/topic/create/${topic.stype}" class="orgBtn postedBtn left">发帖</a>
					<a href="#topic_reply" class="orgBtn postedBtn left">回复</a>
				</c:if>
				<tags:pagination page="${page}" url="/topic/show/${topic.id}" />
			</div>
			<div class="topicTable" id="topic_reply">
				<table>
					<tr>
						<td class="td0">
							<div class="td_l_0">${session.username}</div>
							<div class="td_l_1">
								<img src="${imageURL}/avatar/${session.uid}-150.jpg">
							</div>
						</td>
						<td class="td1">
							<div class="reviewEditer">
								<script type="text/plain" id="content" name="content" style="width:700px;height:200px;"></script>
								<div class="blank20"></div>
								<a href="javascript:;" onclick="topicFn.reply()" class="orgBtn postedBtn left">回复</a>
								<div class="blank20"></div>
								<input type="hidden" id="username" value="${session.username}">
								<input type="hidden" id="uid" value="${session.uid}">
								<input type="hidden" id="tid" value="${topic.id}">
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
</div>
<script id="replyTemplate" type="text/x-jquery-tmpl">
<tr rid=\${id}>
	<td class="td0">
		<div class="td_l_0">\${username}</div>
		<div class="td_l_1">
			<img src="${imageURL}/avatar/${session.uid}-150.jpg" alt="">
		</div>
	</td>
	<td class="td1">
		<div>
			<div class="td_r_0">
				发表于 1秒前
			</div>
			<div class="td_r_1">
				<div class="topicCon">{{html content}}</div>
			</div>
			<div class="td_r_2">
				<a href="javascript:;" class="reviewBtn" onclick="topicFn.review(this,'\${id}')">回复</a>
				<div class="reviewList">
					<ul>
						<li id="def_\${id}" class="lastLi"></li>
					</ul>
				</div>
				<div class="reviewArea hidden">
					<textarea class="reviewText" cols="90" rows="6"></textarea>
					<p class="replyError"></p>
					<a href="javascript:;" class="orgBtn reviewSubBtn" onclick="topicFn.comment('\${id}','${topic.id}')">回复</a>
					<input type="hidden" id="comment_to_\${id}" value="0">
				</div>
			</div>
		</div>
	</td>
</tr>
</script>
<script id="commentTemplate" type="text/x-jquery-tmpl">
<li class="overhide" commentid="\${id}">
	<div class="reviewL">
		<img src="${imageURL}/avatar/${session.uid}-80.jpg" alt="">
	</div>
	<div class="reviewR">
		<span class="replyuname">\${username}</span>
		：
		<span>\${content}</span>
	</div>
	<div class="reviewB">
		<span>1秒前</span>
		<a href="javascript:;" onclick="topicFn.replytoreply(this,'\${rid}','\${id}')">回复</a>
	</div>
</li>
</script>
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