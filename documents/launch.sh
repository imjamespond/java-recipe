#export JAVA_HOME=/opt/jdk1.8.0_101
#export PATH=$PATH:$JAVA_HOME/bin
export M2_HOME=/home/dp/deploy/apache-maven-3.3.9
export PATH=$PATH:$M2_HOME/bin
export TOMCAT_HOME=/home/dp/deploy/apache-tomcat-8.0.35
export CATALINA_HOME=$TOMCAT_HOME
#export JAVA_OPTS="$JAVA_OPTS -Djava.security.egd=file:/dev/./urandom"

WebApp=webapps
App=data-center

func_compile()
{
	rm -Rf target
	mvn compile war:war
	mv target/$App.war $TOMCAT_HOME/$WebApp/data-center.war
	rm -Rf $TOMCAT_HOME/$WebApp/data-center
}

func_start()
{
	func_shut_tomcat
	$TOMCAT_HOME/bin/startup.sh
}

func_log()
{
	tail -f $TOMCAT_HOME/logs/catalina.out
}

func_shut_tomcat()
{
	$TOMCAT_HOME/bin/shutdown.sh
}

if [ "$1" = "start" ]; then
	func_start
elif [ "$1" = "rebuild" ]; then
	func_compile
elif [ "$1" = "log" ]; then
	func_log
elif [ "$1" = "shut" ]; then
	func_shut_tomcat
fi