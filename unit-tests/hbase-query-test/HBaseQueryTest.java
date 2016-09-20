package com.codechiev;

import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseQueryTest {
	public static void main(String[] args) throws Exception {

		Configuration conf = HBaseConfiguration.create();
		Connection connection = ConnectionFactory.createConnection(conf);
		Table table = connection.getTable(TableName.valueOf("comment"));
		
		Put put = new Put(Bytes.toBytes("key001"));
		put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("a"), Bytes.toBytes("value1"));
		put.addColumn(Bytes.toBytes("cf"), Bytes.toBytes("a2"), Bytes.toBytes("value2"));
		table.put(put);
		
		Get get = new Get(Bytes.toBytes("key001"));
		get.addColumn("cf".getBytes(), "a".getBytes());
	
		try {
			Result rs = table.get(get);
			String row = Bytes.toString(rs.getRow());
			System.out.println(row); 
	        NavigableMap<byte[], NavigableMap<byte[],NavigableMap<Long,byte[]>>> map = rs.getMap();
	        for (Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> navigableMapEntry : map.entrySet()) {
	            String family = Bytes.toString(navigableMapEntry.getKey());
	            System.out.println("\t" + family);
	            NavigableMap<byte[], NavigableMap<Long, byte[]>> familyContents = navigableMapEntry.getValue();
	            for (Map.Entry<byte[], NavigableMap<Long, byte[]>> mapEntry : familyContents.entrySet()) {
	                String qualifier = Bytes.toString(mapEntry.getKey());
	                System.out.println("\t\t" + qualifier);
	                NavigableMap<Long, byte[]> qualifierContents = mapEntry.getValue();
	                for (Map.Entry<Long, byte[]> entry : qualifierContents.entrySet()) {
	                    Long timestamp = entry.getKey();
	                    String value = Bytes.toString(entry.getValue());
	                    System.out.printf("\t\t\t%s, %d\n", value, timestamp);
	                }
	            }
	        }
		} finally {
			table.close();
			connection.close();
		}

	}

}
