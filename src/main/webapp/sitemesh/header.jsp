<%@ page language="java" pageEncoding="UTF-8"%>
<div class="col-lg-12 header">
	<h3><a href="/">指标分析系统</a></h3>
	<ul class="nav nav-tabs">
		<li role="presentation" class="active"><a href="/">首页</a></li>
		<li role="presentation"><a href="/metadata/subject">主题实体</a></li>
		<li role="presentation"><a href="/metadata/database">数据源</a></li>
		<li role="presentation"><a href="/metadata/dimension">维度</a></li>
		<li role="presentation"><a href="/metadata/indicator">指标</a></li>
		<li role="presentation"><a href="/metadata/statement">报表查询</a></li>
	</ul>
</div>
<script type="text/javascript">
$(function(){
	var url = GetUrlParts(location.href);
	var navLinks = $(".nav li");
	var i=0;
	var currentPage = url[url.length - 1];
	for(i;i<navLinks.length;i++){
		var pathname = navLinks.eq(i).find('a')[0].pathname;
		console.log(pathname);
		if(url.pathname.indexOf(pathname)==0){
			navLinks.removeClass('active');
			navLinks.eq(i).addClass('active');
		}
	}
});
</script>
