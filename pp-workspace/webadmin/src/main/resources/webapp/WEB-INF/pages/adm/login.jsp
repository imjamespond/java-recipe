<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>碰碰明星网 -- 后台管理系统</title>
<script type="text/javascript" src="<c:url value='/webapp/assets/js/jquery.js'/>"></script>
<script type="text/javascript" src="<c:url value='/webapp/assets/js/checkForm.js'/>"></script>
<link href="<c:url value='/webapp/assets/css/admin.css'/>" rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/javascript">
$(document).ready(function(){
	document.loginForm.username.focus();
});
</script>
</head>
<body>
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" align="center">
  <tr>
    <td height="500" style="background:url(<c:url value='/webapp/assets/images/admin_a.jpg'/>) repeat-x top">
	<table width="464" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td height="302" style="background:url(<c:url value='/webapp/assets/images/admin_b.gif'/>) no-repeat top">
		<table width="300" border="0" align="center" cellpadding="8" cellspacing="0">
          <form name="loginForm" action="login!login" method="post" onsubmit="return checkForm(this);">
          <tr>
            <td align="right">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td align="right"><strong>用户名：</strong></td>
            <td><label><input type="text" name="username" title="请输入用户名！~!" style="border:1px solid #999999; width:180px; height:20px; line-height:20px;">&nbsp;<font color="red">*</font></label></td>
          </tr>
          <tr>
            <td align="right"><strong>密&nbsp;&nbsp;码：</strong></td>
            <td><label><input type="password" name="password" title="请输入密码！~!" style="border:1px solid #999999; width:180px; height:20px; line-height:20px;">&nbsp;<font color="red">*</font></label></td>
          </tr>
		  <tr>
		    <td align="right"><strong>验证码：</strong></td>
		    <td><label><input type="text" name="validateCode" style="border:1px solid #999999; width:110px; height:20px; line-height:20px;">&nbsp;<img alt="点击可换一张" src="/servlet/verifyImage.do" onclick="javascript:this.src=this.src+'?1;'"></label></td>
		  </tr>
          <tr>            
            <td align="center" colspan="2"><label><input type="submit" value="登 录">&nbsp;&nbsp;&nbsp;&nbsp;<input type="reset" value="取 消"></label></td>
          </tr>
          </form>
        </table>
		</td>
      </tr>
    </table>
	</td>
  </tr>
</table>
</body>
</html>