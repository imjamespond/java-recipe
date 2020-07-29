import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.Properties;


public class Test {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        Class.forName("org.apache.calcite.jdbc.Driver");
        Properties info = new Properties();
        Connection connection = DriverManager.getConnection("jdbc:calcite:", info);// use calcite jdbc driver
        CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
        SchemaPlus rootSchema = calciteConnection.getRootSchema();

        Class.forName("com.mysql.cj.jdbc.Driver");
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://192.168.0.247:32495/testdb?autoReconnect=true&useSSL=false");
        dataSource.setUsername("root");
        dataSource.setPassword("dataSharing");
        Schema schema = JdbcSchema.create(rootSchema, "hr", dataSource,null, null);
        rootSchema.add("hr", schema);
        Statement statement = calciteConnection.createStatement();
        ResultSet resultSet = statement.executeQuery("select f.* from \"hr\".\"foobar\" as f");

        while(resultSet.next()){
            long id = resultSet.getLong(1);
            String name = resultSet.getString(2);
            System.out.printf("id: %d, name: %s\n", id, name);
        }
        resultSet.close();
        statement.close();
        connection.close();
    }
}
