<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.qianxun.util.RandomUtils" %>
<%@ page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<html> 
<head>
<title>千寻游戏</title>
<link rel="stylesheet" type="text/css" href="/fly/history/history.css" />
<script type="text/javascript" src="/fly/history/history.js"></script>
<script type="text/javascript" src="/fly/swfobject.js"></script>
<script type="text/javascript" src="/fly/swf_version.js?<%= RandomUtils.nextInt(999999) %>"></script>
<style type="text/css" media="screen"> 
    html, body  { height:100%; }
    body { margin:0; padding:0; overflow:auto; text-align:center; 
           background-color: #01254a; }   
    object:focus { outline:none; }
    #flashContent { display:none; }
</style>
<script type="text/javascript">
	function refresh(){ 
	 	window.location.reload();  
	}     
	function getLocalPath()
	{
       return "/xzfly/";
	}
	function getCookie(name){
	    var arg = name + "=";
	    var alen = arg.length;
	    var clen = document.cookie.length;
	    var i = 0;
	    while (i < clen){
	        var j = i + alen;
	        if (document.cookie.substring(i, j) == arg)
	            return getCookieVal (j);
	        i = document.cookie.indexOf(" ", i) + 1;
	        if (i == 0) break;
	    }
	    return "";
	}

	function getCookieVal(offset){
	    var endstr = document.cookie.indexOf (";", offset);
	    if (endstr == -1)
	     endstr = document.cookie.length;
	    return unescape(document.cookie.substring(offset, endstr));
	}
    var swfVersionStr = "11.1.0";
    var xiSwfUrlStr = "playerProductInstall.swf";
    var flashvars = {};
    var params = {};
    params.quality = "high";
    params.bgcolor = "#01254a";
    params.allowscriptaccess = "sameDomain";
    params.allowfullscreen = "true";
    var attributes = {};
    attributes.id = "Game";
    attributes.name = "Game";
    attributes.align = "middle";
    swfobject.embedSWF(
        version.getPath() + "?v=" + version.getVersion(), "flashContent", 
        "1280", "768", 
        swfVersionStr, xiSwfUrlStr, 
        flashvars, params, attributes);
    swfobject.createCSS("#flashContent", "display:block;text-align:left;");
</script>
</head>
<body>
<div class="wrapper">
    <div id="flashContent">
        <p>
            To view this page ensure that Adobe Flash Player version 
            11.1.0 or greater is installed. 
        </p>
        <script type="text/javascript"> 
            var pageHost = ((document.location.protocol == "https:") ? "https://" : "http://"); 
            document.write("<a href='http://www.adobe.com/go/getflashplayer'><img src='" 
                            + pageHost + "www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /></a>" ); 
        </script> 
    </div>   
</div>
<script type="text/javascript">
$(document).ready(function(){
	version.setVersion();
});
function getVersion(){
	return version.getVersion();
}
function refresh(){
	window.location.reload();
}
</script>
</body>
</html>