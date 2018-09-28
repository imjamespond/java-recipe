ulimit -Sn 4096
#export JAVA_HOME=/web/jdk1.7.0_71
#export PATH=$PATH:/web/jdk1.7.0_71/bin

APP_JAR=empire-server-1.0.jar 
APP_PATH=.

function_kill()
{
#kill -9 $(ps -ef | grep 'flying-server' |grep -v 'grep' | awk '{printf $2}')
PID=`cat pid`
echo "killing pid ${PID}"
kill -USR2 $PID
while [[ ( -d /proc/$PID ) && ( -z `grep zombie /proc/$PID/status` ) ]]; do
    sleep 1
done
}

function_start()
{
java -server -d64 -Xmx512M -Xms256M -Xmn100M -Xss256K -Dgm=1 -D:file.encoding=UTF-8 -jar $APP_JAR 
#>/dev/null 2>&1 &
echo $! > pid
echo $!
}

if [ "$1" = "start" ]; then
 if [ -f pid ]; then
    echo "pid found! killing the process"
    function_kill
 fi
 function_start
elif [ "$1" = "shut" ]; then
 if [ -f pid ]; then
    echo "pid found! killing the process"
    function_kill
 fi
fi


