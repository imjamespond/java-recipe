import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import com.metasoft.kafka.ProducerHelper;

public class TextCount {
	public static void Run(ProducerHelper helper){
		String logFile = "/workshop/spark-2.2.0-bin-hadoop2.7/README.md"; // Should be some file on your system
	    SparkConf conf = new SparkConf().setAppName("Simple Application");
	    JavaSparkContext sc = new JavaSparkContext(conf);
	    JavaRDD<String> logData = sc.textFile(logFile).cache();

	    long numAs = logData.filter(new Function<String, Boolean>() {
			private static final long serialVersionUID = 2311867600882491143L;

			public Boolean call(String s) { return s.contains("a"); }
	    }).count();

	    long numBs = logData.filter(new Function<String, Boolean>() { 
			private static final long serialVersionUID = -8751972300970681559L;

			public Boolean call(String s) { return s.contains("b"); }
	    }).count();

	    String rs = "Lines with a: " + numAs + ", lines with b: " + numBs;
	    System.out.println(rs); 
	    helper.setArg(rs); 
	    SimpleApp.producer.produce(helper);
	    
	    sc.stop();
	    sc.close();
	}
}
