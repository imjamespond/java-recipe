<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>创建新活动</title>
<script type="text/javascript">

</script>
</head>
<body>
	<ol class="breadcrumb">
		<li><a href="/stage/list">千寻小公主</a></li>
		<li class="active">创建新活动</li>
	</ol>
	<form:form modelAttribute="applePrize" role="form" action="/stage/save"
		method="post" class="form-horizontal">
		<form:errors path="*" cssClass="alert alert-danger" element="div" />
		<div class="form-group">
			<label class="col-sm-3 control-label"><span class="text-danger">*</span>活动期数</label>
			<div class="col-sm-7">
				<form:input path="prizeid" class="form-control" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label"><span class="text-danger">*</span>活动描述</label>
			<div class="col-sm-7">
				<form:input path="description" class="form-control" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label"><span class="text-danger">*</span>活动开始时间</label>
			<div class="col-sm-7">
				<form:input path="start" class="form-control" />
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-3 control-label"><span class="text-danger">*</span>活动结束时间</label>
			<div class="col-sm-7">
				<form:input path="end" class="form-control" />
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-offset-3 col-sm-7">
				<button type="submit" id="sub" class="btn btn-default">提交</button>
			</div>
		</div>
	</form:form>
</body>
</html>