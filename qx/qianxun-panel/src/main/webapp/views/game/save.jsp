<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>千寻游戏-创建游戏</title>
</head>
<body>
<div class="wrapper">
	<form action="/game/save" method="post">
		<div>
			<span>游戏名字:</span>
			<input name="gname" id="gname">
		</div>
		<div>
			<span>游戏类型:</span>
			<select name="type" id="type">
				<option value="2">手游</option>
				<option value="1">页游</option>
				<option value="0">端游</option>
			</select>
		</div>
		<button type="submit">提交</button>
	</form>
</div>
<script type="text/javascript">

</script>
</body>
</html>