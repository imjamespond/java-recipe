<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link href="css/top.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script language="javascript" type="text/javascript" src="lang_cfg.js"></script>
</head>

<script>
$(function(){
	// $("#serverLi").load("serverlist1.php");
	// $("#serverLi").change(function(){
		// $.post("serverlist1.php",{"action":"setServer","serverid":$("#serverLi").val()},function(data){
			// alert(data);
		// });
	
	// });
});


window.onload=function(){
//do something
//alert(userbuyitem_log_header);
// document.title	= title1;
// document.getElementById("frameHeader").innerHTML 			= city_index_header;
// document.getElementById("accountFor").innerHTML 				= city_index_account;
// document.getElementById("fieldName1").innerHTML 				= city_index1;
}
</script>
<body>
 <div class="top">
  <ul>
   <li><lable  id='greet1'>Welcome,</lable > <span><? echo $_SESSION['ad_truename'];?></span> <lable  id='greet2'>login this system!</lable > &nbsp;</li>
   <li> <!--<a href="serverlist.php" target="_blank">| 選擇服務器</a><span id="serverName">選擇服務器</span>--><select id="serverLi"></select></li>
   <li><a href="pws.php" target="center">| <lable  id='greet2'>Change password</lable > </a></li>
   <li><a href="quit.php" target="_top" class="out">| <lable  id='greet3'>Quit</lable > </a></li>
  </ul>
 </div>
</body>
</html>