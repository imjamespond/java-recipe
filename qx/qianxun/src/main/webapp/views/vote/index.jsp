<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.qianxun.model.Constant" %>
<!DOCTYPE html>
<html>
<head>
<title>游戏投票</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<script type="text/javascript">
var listData = ${listData};
var resultData = ${resultData};
</script>
</head>
<body>
<div id="container" class="Area">
	<div id="vote">
		<div id="leader">
			<a href="<%= Constant.MAIN_HTTP %>">首页</a>
			&gt
			<span>游戏榜投票</span>
		</div>
		<div id="voteArea">
			<div>
				<div class="title">
					<h2>游戏榜投票</h2>
				</div>
				<div class="voteDiv clearfix">
					<div class="voteTabA">
						<ul>
							<li class="active" t="0"><a href="javascript:;">端游</a></li>
							<li t="1"><a href="javascript:;">页游</a></li>
							<li t="2"><a href="javascript:;">手游</a></li>
						</ul>
					</div>
					<div class="voteCon">
						<div class="voteTabB">
							<ul>
								<li class="active" n="0"><a href="javascript:;">花钱如流水</a></li>
								<li>|</li>
								<li n="1"><a href="javascript:;">省钱又好玩</a></li>
								<li>|</li>
								<li n="2"><a href="javascript:;">萌妹纸最多</a></li>
								<li>|</li>
								<li n="3"><a href="javascript:;">平衡性最佳</a></li>
								<li>|</li>
								<li n="4"><a href="javascript:;">操作体验佳</a></li>
								<li>|</li>
								<li n="5"><a href="javascript:;">故事情节佳</a></li>
								<li>|</li>
								<li n="6"><a href="javascript:;">画面音乐佳</a></li>
								<li>|</li>
								<li n="7"><a href="javascript:;">人气最火爆</a></li>
							</ul>
						</div>
						<div class="voteConA">
							<div class="con show" id="con0">
								<div class="voteConL">
									<div class="voteLists">
										<div class="voteList" id="list0">
											<ul></ul>
											<div class="voteListBtn">
												<input type="button" value="确定投票" t="0" />
												<div class="voteMsg">欢迎您继续投票其他榜单！</div>
											</div>
										</div>
									</div>
									<p class="remind">
										<span class="red">投票说明</span>
										：注册用户，每周可投票一次，周日晚上12点后，即可进行新一周的投票。
									</p>
								</div>
								<div class="voteConR">
									<div class="voteResult">
										<div class="titleA txtCenter">
											<p>本周端游投票结果</p>
										</div>
										<div class="rList" id="rList_0">
											<ul></ul>
										</div>
									</div>
								</div>
								<div class="clear"></div>
							</div>
							<div class="con" id="con1">
								<div class="voteConL">
									<div class="voteLists">
										<div class="voteList" id="list1">
											<ul></ul>
											<div class="voteListBtn">
												<input type="button" value="确定投票" t="1" />
												<div class="voteMsg">欢迎您继续投票其他榜单！</div>
											</div>
										</div>
									</div>
									<p class="remind">
										<span class="red">投票说明</span>
										：注册用户，每周可投票一次，周日晚上12点后，即可进行新一周的投票。
									</p>
								</div>
								<div class="voteConR">
									<div class="voteResult">
										<div class="titleA txtCenter">
											<p>本周页游投票结果</p>
										</div>
										<div class="rList" id="rList_1">
											<ul></ul>
										</div>
									</div>
								</div>
								<div class="clear"></div>
							</div>
							<div class="con" id="con2">
								<div class="voteConL">
									<div class="voteLists">
										<div class="voteList" id="list2">
											<ul></ul>
											<div class="voteListBtn">
												<input type="button" value="确定投票" t="2" />
												<div class="voteMsg">欢迎您继续投票其他榜单！</div>
											</div>
										</div>
									</div>
									<p class="remind">
										<span class="red">投票说明</span>
										：注册用户，每周可投票一次，周日晚上12点后，即可进行新一周的投票。
									</p>
								</div>
								<div class="voteConR">
									<div class="voteResult">
										<div class="titleA txtCenter">
											<p>本周手游投票结果</p>
										</div>
										<div class="rList" id="rList_2">
											<ul></ul>
										</div>
									</div>
								</div>
								<div class="clear"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<input type="hidden" id="type" value="0" />
			<input type="hidden" id="rank" value="0" />
			<input type="hidden" id="gid" value="" />
		</div>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function(){
	voteFn.init(listData, resultData);
});
</script>
</body>
</html>