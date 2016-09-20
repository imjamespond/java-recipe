1.if u have this error,add "set WEBPATH=C:\project\jetty\webapp\WEB-INF" to classpath
2014-03-11 14:46:11.302:WARN:oejw.WebAppContext:main: Failed startup of context
o.e.j.w.WebAppContext@1bddfd9{/,file:/C:/project/jetty/webapp/,STARTING}
org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named '
springSecurityFilterChain' is defined

2. There is an error in invoking javac.  A full JDK (not just JRE) is required
set enviroment value and change the name of java.exe in system path
变量名：Path 
变量值：%JAVA_HOME%\bin;%JAVA_HOME%\jre\bin;