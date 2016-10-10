<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="com.metasoft.model.Constant" %>
<script type="text/javascript">
var a = document.createElement('a');
a.href = window.location.href;
var pname 	= "<%=Constant.UNSIGNED_URL%>";
var redirect= "<%=Constant.SIGNIN_URL%>";

alert(a.pathname);//console.log(a.search);
if(pname.indexOf(a.pathname)!=0)
	redirect+=("?referer="+a.pathname);
window.location.href = redirect;
</script>
