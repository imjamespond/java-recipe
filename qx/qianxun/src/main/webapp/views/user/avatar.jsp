<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="com.qianxun.model.Constant" %>
<html>
<head>
<title>修改头像</title>
<link rel="stylesheet" type="text/css" href="<%= Constant.MAIN_HTTP %>/styles/imgareaselect-default.css" />
<script type="text/javascript" src="<%= Constant.MAIN_HTTP %>/scripts/jquery.imgareaselect.pack.js"></script>
</head>
<body>
	<div class="userTitle">
		<h2>修改头像</h2>
	</div>
	<div class="userCon">
		<div class="user_myAvatar">
			<c:if test="${success}">
				<!--<center class="user_myImg formRow10">
					<img src="${imageURL}/avatar/${user.id}-150.jpg?t=<%= System.currentTimeMillis() %>>">
				</center>-->
				<div class="prc_imgUpSuccess">
					<span>恭喜您上传头像成功！<!-- <a href="/user/index">点击查看</a> --></span>
				</div>
				<script type="text/javascript">
				$(document).ready(function(){
					window.location.replace("/user/index/1");
				});
				</script>
			</c:if>
			<c:if test="${!success}">
				<c:if test="${!empty message}"><div class="errorMsg">上传失败：${message}</div></c:if>
				<div class="l">
					<p class="tp">原始图片</p>
					<div class="frame" style="width: ${width}px; height: ${height}px;">
						<img src="${tempURL}/avatar/${name}" id="photo" class="img-rounded" style="width: ${width}px;height: ${height}px;"/>
					</div>
					<form:form action="/user/avatar" method="post" enctype="multipart/form-data" id="form1">
						<form:errors path="*" cssClass="errors" element="div" />
						<a href="javascript:;" class="orgBtn" id="uploadBtn">选择文件</a>
						<input type="file" id="uploadImage" tabindex="3" size="3" name="avatar" class="filePrew"/>
					</form:form>
					<p>仅支持尺寸大于300*300分辨率且大小不超过2M的JPG图片</p>
				</div>
				<div class="r">
					<p class="tp">头像预览</p>
					<div class="frame">
						<div id="preview">
							<img src="${tempURL}/avatar/${name}" class="img-rounded"/>
						</div>
						<form:form action="/user/reduce" method="post">
							<input type="hidden" name="x" id="x1" value="50" />
							<input type="hidden" name="y" id="y1" value="50" />
							<input type="hidden" name="length" value="200" id="w" />
							<input type="hidden" name="name" value="${name}" />
							<c:if test="${name != 'default.jpg'}">
								<p class="instructions" align="center">
									<button type="submit" class="orgBtn avatarOk">确定</button>
								</p>
							</c:if>
						</form:form>
					</div>
				</div>
			</c:if>
		</div>
	</div>
<script type="text/javascript">
$(document).ready(function () {
	var picH = ${height},
		picW = ${width},
		sideLength = picH > picW?4*picW/5:4*picH/5,
		x_1 = (picW - sideLength)/2,
		y_1 = (picH - sideLength)/2,
		x_2 = x_1 + sideLength,
		y_2 = y_1 + sideLength;
	var photo = $('#photo');
	var width = x_2 - x_1;
	var height = y_2 - y_1;
	var st = {
		x1: x_1,
		y1: y_1,
		x2: x_2,
		y2: y_2,
		width: width,
		height: height
	};
	preview(photo, st);
	photo.imgAreaSelect({ aspectRatio: '1:1', handles: true,
        fadeSpeed: 200, onSelectChange: preview,x1: x_1, y1: y_1, x2: x_2, y2: y_2 });

    $("#uploadImage").change(function () {
    	var dia = $("<div class='smallDia'>图片上传中，请稍后<span id='ell'>&emsp;&emsp;&emsp;</span></div>");
    	var bg = $("<div class='singleBg'></div>");
    	var winW = $(window).width();
    	var winH = $(window).height();
    	bg.height($(document).height());
        $("body").append(bg, dia);
    	var diaW = dia.width();
    	var diaH = dia.height();
    	var l = (winW - diaW)/2;
    	var t = (winH - diaH)/2;
    	dia.css({left: l, top: t});
    	var n = 1;
    	setInterval(function(){
    		var str = "";
    		for(i=0;i<3;i++){
    			var s = i<n?"。":"&emsp;";
    			str += s;
    		}
			n++;
			if(n>3){
				n = 1;
				$("#ell").html("。&emsp;&emsp;");
			}
    		$("#ell").html(str);
    	}, 500);
        $("#form1").submit();
    });
});
//个人中心修改头像预览
function preview(img, selection) {
    if (!selection.width || !selection.height)
        return;
    
    var scaleX = 200 / selection.width;
    var scaleY = 200 / selection.height;

    $('#preview img').css({
        width: Math.round(scaleX * ${width}),
        height: Math.round(scaleY * ${height}),
        marginLeft: -Math.round(scaleX * selection.x1),
        marginTop: -Math.round(scaleY * selection.y1)
    });

    $('#x1').val(selection.x1);
    $('#y1').val(selection.y1);
    $('#w').val(selection.width);  
}
</script>
</body>
</html>