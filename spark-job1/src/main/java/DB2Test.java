import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import com.metasoft.kafka.ProducerHelper;

public class DB2Test {
	public static void Run(ProducerHelper helper){
		
		SparkSession spark = SparkSession
				  .builder()
				  .appName("Java Spark SQL basic example")
				  .config("spark.some.config.option", "some-value")
				  .getOrCreate();
		
		// Note: JDBC loading and saving can be achieved via either the load/save or jdbc methods
		// Loading data from a JDBC source
		Dataset<Row> jdbcDF = spark.read()
		  .format("jdbc")
		  .option("url", "jdbc:db2://121.201.24.170:50000/demo")
		  .option("dbtable", "DB2INST1.AS_USER")
		  .option("user", "db2inst1")
		  .option("password", "db2inst1")
		  .load();
		
		jdbcDF.show();
	}
}
