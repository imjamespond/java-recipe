package simple;

import org.apache.calcite.util.Sources;

import java.io.File;
import java.sql.*;
import java.util.Properties;

@SuppressWarnings("Since15")
public class Test2 {

    public static void close(Connection connection, Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    public static void main(String[] args) throws Exception {

        final String model = "simple/model-jdbc.json";
        final String sql = "select * from \"bc_user\"";
//        final String sql = "select * from \"blockchain_test\".\"bc_user\""; //enclose the name in double-quotes prevent SQL parser convert it to upper-case

        Connection connection = null;
        Statement statement = null;
        try {
            Properties info = new Properties();

            String jsonPath = Sources.of(new File(Test2.class.getResource("/" + model).getFile())).file().getAbsolutePath();

            info.put("model", jsonPath);
            connection = DriverManager.getConnection("jdbc:calcite:", info);
            statement = connection.createStatement();
            final ResultSet resultSet = statement.executeQuery(sql);


//            connection = DriverManager.getConnection("jdbc:calcite:");
//            CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
//            SchemaPlus rootSchema = calciteConnection.getRootSchema();
//            final DataSource ds = JdbcSchema.dataSource(
//                    "jdbc:postgresql://localhost:5432/blockchain_test",
//                    "org.postgresql.Driver",
//                    "postgres",
//                    "mysecretpassword");
//            rootSchema.add("blockchain_test", JdbcSchema.create(rootSchema, "blockchain_test", ds, null, null));
//            statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(sql);


            while (resultSet.next()) {
                int n = resultSet.getMetaData().getColumnCount();

                System.out.printf("getMetaData.getColumnCount: %d, %s\n",n, resultSet.getString("username"));

            }
        } finally {
            close(connection, statement);
        }

    }
}
