package com.codechiev.spark;

/* SimpleApp.java 
 * mvn package
 * scp -P22001 ./target/hdp-test.jar root@yy:/root/
 * ./spark-2.1.0-bin-hadoop2.7/bin/spark-submit --class "com.codechiev.spark.SimpleApp"  --master local[4] ~/hdp-test.jar
 * */
import org.apache.spark.api.java.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;

public class SimpleApp {
  public static void main(String[] args) {
    String logFile = "./README.md"; // Should be some file on your system,测试该文件中包含a,b出现地次数
    SparkConf conf = new SparkConf().setAppName("Simple Application");
    JavaSparkContext sc = new JavaSparkContext(conf);
    JavaRDD<String> logData = sc.textFile(logFile).cache();

    long numAs = logData.filter(new Function<String, Boolean>() {
      public Boolean call(String s) { return s.contains("a"); }
    }).count();

    long numBs = logData.filter(new Function<String, Boolean>() {
      public Boolean call(String s) { return s.contains("b"); }
    }).count();

    System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);
    
    sc.stop();
  }
}
