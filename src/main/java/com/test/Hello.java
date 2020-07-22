package com.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
@RestController
public class Hello {
    //static String uuid = UUID.randomUUID().toString();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @PostConstruct
    void init() {
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS `testdb`");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS `testdb`.`foobar` (" +
                "`id` bigint DEFAULT NULL, " +
                "`name` varchar(200) DEFAULT NULL" +
                ")");
        jdbcTemplate.execute("INSERT INTO `testdb`.`foobar` (`id`, `name`) VALUES ("+
                System.currentTimeMillis()+", " +
                "'"+ UUID.randomUUID().toString()+"'" +
                ");");
    }

    @RequestMapping("/")
    public String home() throws UnknownHostException {
        return "Hello Docker World, from: " + InetAddress.getLocalHost().getHostName();
    }

    @RequestMapping("/test-db")
    public List<Map<String, Object>> testDb() throws UnknownHostException {
        return jdbcTemplate.queryForList("SELECT * FROM `testdb`.`foobar` LIMIT 1000;\n");
    }

    public static void main(String[] args) {
        SpringApplication.run(Hello.class, args);
    }
}