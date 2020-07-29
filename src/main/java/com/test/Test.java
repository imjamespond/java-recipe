package com.test;

import java.sql.*;

public class Test {
    public static void main(String[] args) throws SQLException {
        // URL parameters
        String url = "jdbc:presto://192.168.0.254:8080/mysql/testdb?user=root";
        Connection connection = DriverManager.getConnection(url);

        PreparedStatement ps = connection.prepareStatement("select * from foobar");
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            long id = rs.getLong(1);
            String name = rs.getString(2);
            System.out.printf("id: %d, name: %s\n", id, name);
        }
    }
}
