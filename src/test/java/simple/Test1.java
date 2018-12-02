package simple;

import org.apache.calcite.util.Sources;

import java.io.File;
import java.sql.*;
import java.util.Properties;

public class Test1 {

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

        final String model = "simple/model.json";
        final String sql = "select * from EMPS";

        Connection connection = null;
        Statement statement = null;
        try {
            Properties info = new Properties();

            String jsonPath = Sources.of(new File(Test1.class.getResource("/" + model).getFile())).file().getAbsolutePath();

            info.put("model", jsonPath);
            connection = DriverManager.getConnection("jdbc:calcite:", info);
            statement = connection.createStatement();
            final ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int n = resultSet.getMetaData().getColumnCount();

                System.out.printf("getMetaData.getColumnCount: %d, %d\n",n, resultSet.getInt("EMPNO"));

            }
        } finally {
            close(connection, statement);
        }

    }
}
