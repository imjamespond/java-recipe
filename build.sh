
if [ "$1" = "pull" ]; then
  git pull origin
elif [ "$1" = "push" ]; then
  git commit -a -m 'auto commit'
  git push origin master
elif [ "$1" = "wc" ]; then
  cd ./src/main/java
  rm -rf **.class
  rm -rf output
  export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar
  SourceDir=com/codechiev/wc/
  Package=com.codechiev.wc
  hadoop com.sun.tools.javac.Main ${SourceDir}WordCount.java
  jar cf wc.jar ${SourceDir}WordCount*.class
  hadoop jar wc.jar ${Package}.WordCount input output
  cat output/*
elif [ "$1" = "wc2" ]; then
  cd ./src/main/java
  rm -rf **.class
  rm -rf output
  export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar
  SourceDir=com/codechiev/wc2/
  Package=com.codechiev.wc2
  hadoop com.sun.tools.javac.Main ${SourceDir}WordCount2.java
  jar cf wc.jar ${SourceDir}WordCount2*.class
  
  Input=/user/root/input
  Output=/user/output
  hdfs dfs -mkdir -p $Input
  hdfs dfs -rm -r -f $Input/* $Output
  hdfs dfs -put ./input/* $Input
  hdfs dfs -ls $Input
  hadoop jar wc.jar ${Package}.WordCount2 $Input $Output
  hadoop fs -cat $Output/part-r-00000
fi
