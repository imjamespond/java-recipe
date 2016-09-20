package test;

import java.io.File;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Consistency;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;

public class HBaseQueryTest {
	public static void main(String[] args) throws Exception {
		String path = new File(".").getCanonicalPath();
		// File workaround = new File(".");
		System.getProperties().put("hadoop.home.dir", path);
		new File("./bin").mkdirs();
		new File("./bin/winutils.exe").createNewFile();

		Configuration conf = HBaseConfiguration.create();
		Connection connection = ConnectionFactory.createConnection(conf);
		Get get = new Get("row1".getBytes());
		get.addColumn("cf".getBytes(), "a".getBytes());

		Table table = connection.getTable(TableName.valueOf("comment"));
		try {
			Result rs = table.get(get);
		} finally {
			table.close();
			connection.close();
		}

	}

}
