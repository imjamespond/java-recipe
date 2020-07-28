package com.test;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.shardingsphere.api.config.sharding.KeyGeneratorConfiguration;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class ShardingApp {
    public static void main(String[] args) throws SQLException {
        // Configure actual data sources
        Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();

        // Configure the first data source, must include db in url
        HikariDataSource dataSource1 = new HikariDataSource();
        dataSource1.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource1.setJdbcUrl("jdbc:mysql://k8s:3306/test?autoReconnect=true&useSSL=false");
        dataSource1.setUsername("root");
        dataSource1.setPassword("9090");
        dataSourceMap.put("ds0", dataSource1);

        // Configure the second data source
        HikariDataSource dataSource2 = new HikariDataSource();
        dataSource2.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource2.setJdbcUrl("jdbc:mysql://k8s:3307/test?autoReconnect=true&useSSL=false");
        dataSource2.setUsername("root");
        dataSource2.setPassword("9090");
        dataSourceMap.put("ds1", dataSource2);

        // Configure user table rule
        TableRuleConfiguration userTableRuleConfig = new TableRuleConfiguration("sht_user", "ds${0..1}.user${1..2}");
        userTableRuleConfig.setKeyGeneratorConfig(new KeyGeneratorConfiguration("SNOWFLAKE","id"));

        // Configure sharding rule
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(userTableRuleConfig);

        // Configure database sharding algorithm
        shardingRuleConfig.setDefaultDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("id", "ds${id % 3 % 2}"));
        // Configure table sharding algorithm
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("id", new PreciseShardingAlgorithm<Long>() {
            public String doSharding(final Collection<String> tableNames, final PreciseShardingValue<Long> shardingValue) {
                for (String each : tableNames) {
                    /*if (each.endsWith(shardingValue.getValue() % 2 + "")) {
                        return each;
                    }*/


                    if ((shardingValue.getValue() % 2 == 0) && each.endsWith("1")) {
                        System.out.println(each);
                        return each;
                    }
                    if ((shardingValue.getValue() % 2 == 1) && each.endsWith("2")) {
                        System.out.println(each);
                        return each;
                    }

                }
                throw new UnsupportedOperationException();
            }
        }));

        //CREATE TABLE `user1` (
        //	`id` BIGINT(20) NOT NULL DEFAULT '0',
        //	`name` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
        //	PRIMARY KEY (`id`) USING BTREE
        //)
        //COLLATE='utf8mb4_general_ci'
        //ENGINE=InnoDB
        //;

        // Create ShardingSphereDataSource
        DataSource dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfig, new Properties());

        /*{
            Connection conn = dataSource.getConnection();
            Statement ps = conn.createStatement();
            ps.execute("INSERT into sht_user (`name`) values('foo'),('bar')", Statement.RETURN_GENERATED_KEYS);
        }*/

        {
            TransactionTypeHolder.set(TransactionType.XA); // Support TransactionType.LOCAL, TransactionType.XA, TransactionType.BASE
            Connection conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps1 = conn.prepareStatement("UPDATE sht_user SET name = ?");
            ps1.setObject(1, "bar333");
            ps1.executeUpdate();
            /*PreparedStatement ps2 = conn.prepareStatement("UPDATE sht_user SET name = ?");
            ps2.setObject(1, null);//assignment 不能有sharding column(StandardShardingStrategy)
            ps2.executeUpdate();*/
            conn.commit();
        }

        String sql = "SELECT t1.* FROM sht_user t1 order by t1.id desc limit ?";
        Connection conn = dataSource.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, 3);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            long id = rs.getLong(1);
            String name = rs.getString(2);
            System.out.printf("id: %d, name: %s\n", id, name);
        }
    }
}
