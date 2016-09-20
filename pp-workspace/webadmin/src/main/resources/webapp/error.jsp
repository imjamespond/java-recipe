<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2010-9-30
  Time: 17:00:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ page import="java.io.*" %>
<%
/**
 * 本页面专门用于其它JSP页面的错误处理页引用
 * 本页面将在页面产生异常时显示该异常的信息内容和异常类名，
 * 并可通过按“`”键来显示异常的stackTrace。
 *
 * 在Resin服务器中，本errorPage还可以显示出发生异常的页面的出错行号，但对原页面有一个要求：
 * 在JSP开头需要加入一行：
 * request.setAttribute("_SOURCE_PAGE_",this);
 * 一般这一行代码可以通过一个公共的包含页面来完成。
 * 如果不写这一行，将按原来的方式显示异常的StackTrace，不能显示JSP页面的出错行号
 * 注意：此功能仅对Resin服务器有效！
 *
 * 如果要在Web应用中自定义某类异常的具体显示内容，
 * 可以在./exception/目录下建一个<异常类全名>.jsp的errorPage页面，其中根据exception变量显示相应内容。
 * 此页面必须在第一行使用
 * exception = request.getAttribute("exception")
 * 的方式来获取需要处理的异常
 * （注意，该异常类如果是某个类的内部类的话，采用.分隔名称而不使用$，
 * 以防止Linux下不能读取含$字符的文件名。
 * 例如：想让页面产生java.lang.NullPointerException时显示特定的内容，可编写一个
 * ./exception/java.lang.NullPointerException.jsp
 * 来显示，可加上更利于用户理解的图片、色彩等等。
 * 在异常的继承层次上，越具体的处理页面越优先。
 * 比如：./exception/目录下有java.lang.Exception.jsp和java.lang.RuntimeException.jsp两个页面，
 * 当有一个动态异常（如java.lang.NullPointerException）发生时，
 * 将会使用java.lang.RuntimeException.jsp而不会采用java.lang.Exception.jsp。
 *
 * 如果处理某异常的页面发现应该转为处理另一个异常
 * （比如被一些常用异常包装过的原始异常或者是重新包装为另一异常），
 * 假定为 anotherException
 * 可以在扔回此exception之前设置request的“exception”属性，如：
 * request.setAttribute("exception", anotherException);
 * throw exception; //返回errorPage，重新逆归处理anotherException
 *
 * 提示一：如果使用一个./exception/java.lang.Exception.jsp页面，将完全屏蔽本页面的默认显示，
 * 因为所有的异常类都是java.lang.Exception的子类。需要的话，甚至可以写一个java.lang.Error.jsp！
 * 提示二：如果某个处理异常的页面自身出现了异常，则本机制将在后台打印该新异常，
 * 并查找父类异常的处理页面，直至最终到达本页。
 * 提示三：如果处理特定异常的页面不想处理该异常，可再扔出原异常，本页面则照常继续进行
 * 提示四：处理特定异常的页面可使用两个requestScope属性：exception和stackTrace
 */

if(exception == null) exception = (Throwable) request.getAttribute("exception");

if(exception == null) {
    exception = new Throwable("未发生任何异常。（请勿直接访问本页面！）");
    request.setAttribute("_SOURCE_PAGE_",this);
}

//生成stackTrace(字符串)，并尝试让resin自动转换JSP行号
StringWriter sw = new StringWriter();
PrintWriter pw = new PrintWriter(sw,true);
try {
    Object srcPage = request.getAttribute("_SOURCE_PAGE_");
    java.lang.reflect.Method method = srcPage.getClass().getMethod("_caucho_getLineMap",new Class[]{});
    Object lineMap = method.invoke(srcPage,new Object[]{});
    method = lineMap.getClass().getMethod("printStackTrace",new Class[] { Throwable.class, PrintWriter.class });
    method.invoke(lineMap, new Object[]{ exception,pw });

} catch(Exception exx) { //Resin行号转换不成功，直接打stackTrace:
    exception.printStackTrace(pw);
}

String stackTrace = sw.toString();
request.setAttribute("stackTrace",stackTrace);
request.setAttribute("exception",exception);

Class cls = exception.getClass();
Throwable originalException = exception;

while(cls != null && cls != Throwable.class) { //递归检测是否有合适的异常处理页面
    String s = cls.getName();
    //System.out.println("errorPage: checking "+s);
    s = s.replace('$','.'); //避免Linux下不能读取带'$'符号的文件名
    s = "/util/exception/"+s+".jsp";
    if(application.getResource(s) != null) try { //该页面存在
    	%>
        <c:set var="HTML"><%pageContext.include(s);%></c:set>
        <% break;

    } catch(IOException ex) {
    } catch(ServletException ex) {
        Exception cause = (Exception) ex.getRootCause();
        if(ex != exception && cause != exception) {
            try { out.clearBuffer(); } catch(IOException ex2) { ex2.printStackTrace(); }
            System.err.println("处理异常的页面["+s+"]本身发生异常！"+cause);
            cause.printStackTrace();

        } else { //检查是否有异常重定向
            Throwable redirectEx = (Throwable) request.getAttribute("exception");
            if(redirectEx != null && redirectEx != exception) { //该异常页面进行了异常转换处理
                exception = redirectEx;
                cls = redirectEx.getClass();
                continue;
            }
        }
    }
    cls = cls.getSuperclass();
}

//找不到合适的处理页面，采用下面的标准处理：

//如果是用户在未显示完页面时跳转产生的异常，则忽略：
if((exception instanceof java.io.IOException) && (0 <= stackTrace.indexOf("java.net.SocketOutputStream.socketWrtite("))) return;

//如果不是RuntimeException或直接扔出的Exception提示信息，在后台打印日志，以便跟踪
cls = exception.getClass();
if(cls != Exception.class && cls != Throwable.class && !(exception instanceof RuntimeException)) {
    response.setStatus(500);
    System.out.println("EXCEPTION! "+new java.sql.Timestamp(System.currentTimeMillis())+" "
        +request.getAttribute("javax.servlet.forward.request_uri")+"?"+request.getAttribute("javax.servlet.forward.query_string")
        +": "+exception
        +"\nReferer="+request.getHeader("Referer")
        );
    System.err.print(stackTrace);
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>碰碰明星网-操作错误</title>
<link type="text/css" rel="stylesheet" href="http://res.pengpeng.com/css/theme.css" />
<link type="text/css" rel="stylesheet" href="http://res.pengpeng.com/css/home/home.css" />
<link type="text/css" rel="stylesheet" href="http://res.pengpeng.com/css/error.css" />
</head>
<body class="error_bg">
<script type="text/javascript" src="http://res.pengpeng.com/js/mini-head.js"></script>
<div id="error_content">
  <div class="top2"></div>
  <div class="middle">
    <div class="error_name">系统内部错误(<span onclick="document.getElementById('detail').style.display='';" style="cursor:hand;"><font color="blue">点击查看出错信息</font></span>)</div>
	<div class="error_main fork">对不起，由于系统错误，当前操作失败。欢迎向我们客服反馈您遇到的问题！<br />
	<div style=" text-align:center;font-size:12px; font-weight:normal;margin-top:30px;"><a href="http://www.pengpeng.com">跳转到首页</a></div></div>
  </div>
  <div id="detail" style="display:none"><pre><%=stackTrace%></pre></div>
  <div class="bottom"></div>
</div>
<script type="text/javascript" src="http://res.pengpeng.com/js/mini-copyright.js"></script>
</body>
</html>
