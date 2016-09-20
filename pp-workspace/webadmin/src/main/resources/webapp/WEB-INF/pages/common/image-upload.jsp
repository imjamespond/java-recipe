<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.pengpeng.common.helper.CookieManager" %>
<%
String uploadImageUrl = CookieManager.retrieve("uploadImageUrl", request);
if(uploadImageUrl != null){	
	request.setAttribute("uploadImageUrl", uploadImageUrl);
    request.setAttribute("done", new Integer(1));
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>碰碰网-上传图片</title>
<script type="text/javascript" src="assets/js/checkForm.js"></script>
<style>body {margin: 0px;font-size: 9pt;font-style: normal;font-variant: normal;font-weight: normal;line-height: normal;}</style>
<script>  
function upLoad(){  
    var filename = document.imgForm.filedata.value.toUpperCase();	
    if(filename != "") {	
		if(filename.indexOf(".GIF") != -1 || filename.indexOf(".JPG") != -1 || filename.indexOf(".JPEG") != -1 ||
			filename.indexOf(".PNG") != -1 || filename.indexOf(".BMP") != -1) {	
			if(checkForm(form)) {
				form.submit();  
			} else {
				return;
			}			
   	    } else {
		    alert("必须为 .gif, .jpg, .png , .bmp 格式的文件！");
  	        return;
	    }
	}
}

function previewPhoto(url){ 
    try {
        parent.previewPhoto(url, '${param.idx}');
    } catch(err){}
}

function isDone(done){ 	
    if(done == 1){       
        var imgPath = new String("${uploadImageUrl}"); 
        previewPhoto(imgPath);
    }    
}
</script>
</head>
<body style="width:400px; height:20px; overflow:hidden;">
<form name="imgForm" method="post" enctype="multipart/form-data" action="http://uud.pengpeng.com/ppimg">
  <input type="hidden" name="imgResult" value="url">    
  <input type="hidden" name="returnURL" value="http://admin.pengpeng.com/common/image-upload?idx=${param.idx}">    
  <input name="filedata" id="filedata" size="20" type="file">
  <input type="hidden" name="process" value="">
  <input type="button" value="上 传" onClick="upLoad();" style="width:60px;height:20px;padding-top:2px;border:1px #cccccc solid;background-color:#f1f1f1;color:#333333">    
</form>
</body>
</html>
<script>isDone(${done});</script>
<% out.flush(); %>
