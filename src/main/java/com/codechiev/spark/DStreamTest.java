package com.codechiev.spark;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.spark.*;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;
import scala.Tuple2;

/**
 * @author metasoft
 *"./bin/run-example com.codechiev.spark.DStreamTest localhost 9999"
 *./spark-2.1.0-bin-hadoop2.7/bin/spark-submit --class "com.codechiev.spark.DStreamTest"  \
 *  --master local[4] ~/hdp-test.jar localhost 9999
 *nc -lk 9999
 *hello world
 *...
 */
public class DStreamTest {
	public static void main(String[] args) throws InterruptedException {
		// Create a local StreamingContext with two working thread and batch interval of 1 second
		SparkConf conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount");
		JavaStreamingContext jssc = new JavaStreamingContext(conf, Durations.seconds(1));
		
		// Create a DStream that will connect to hostname:port, like localhost:9999
		JavaReceiverInputDStream<String> lines = jssc.socketTextStream("localhost", 9999);
		
		// Split each line into words
		JavaDStream<String> words = lines.flatMap(
		  new FlatMapFunction<String, String>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Iterator<String> call(String x) {
				return Arrays.asList(x.split(" ")).iterator();
			}
		});
		
		// Count each word in each batch
		JavaPairDStream<String, Integer> pairs = words.mapToPair(
		  new PairFunction<String, String, Integer>() {
			private static final long serialVersionUID = 1L;
			@Override public Tuple2<String, Integer> call(String s) {
		      return new Tuple2<>(s, 1);
		    }
		});
		JavaPairDStream<String, Integer> wordCounts = pairs.reduceByKey(
		  new Function2<Integer, Integer, Integer>() {
			private static final long serialVersionUID = 1L;
			@Override public Integer call(Integer i1, Integer i2) {
			  return i1 + i2;
			}
		});

		// Print the first ten elements of each RDD generated in this DStream to the console
		wordCounts.print();
		
		jssc.start();              // Start the computation
		jssc.awaitTermination();   // Wait for the computation to terminate
	}
	
}
