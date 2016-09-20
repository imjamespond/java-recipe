@echo off
setlocal EnableDelayedExpansion

set WEBPATH=C:\project\jetty\webapp\WEB-INF
set classpath=.\target\game\conf;%WEBPATH%;
for %%i in (.\target\game\lib\*.jar) do (
 echo %%i
 set classpath=!classpath!%%i;
)
for %%i in (!WEBPATH!\lib\*.jar) do (
 echo %%i
 set classpath=!classpath!%%i;
)
set classpath=!classpath!
echo !classpath!

java com.james.jetty.HttpServer -Dfile.encoding=UTF-8 -classpath !classpath! -cp .\target\james-jetty-1.0.jar
pause