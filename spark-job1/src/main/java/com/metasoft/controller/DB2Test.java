package com.metasoft.controller;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DB2Test {
    @RequestMapping("/db-test")
    public void run() {

        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark SQL basic example")
                .config("spark.some.config.option", "some-value")
                .getOrCreate();

        // Note: JDBC loading and saving can be achieved via either the load/save or jdbc methods
        // Loading data from a JDBC source
        Dataset<Row> jdbcDF = spark.read()
                .format("jdbc")
                .option("driver", "org.postgresql.Driver")
                .option("url", "jdbc:postgresql://192.168.0.254:5432/sddb")
                .option("dbtable", "sd_user")
                .option("user", "postgres")
                .option("password", "")
                .load();
        jdbcDF.show();
    }
}
