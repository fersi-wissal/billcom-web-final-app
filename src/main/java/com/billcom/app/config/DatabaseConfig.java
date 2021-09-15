package com.billcom.app.config;

import com.zaxxer.hikari.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.*;
import javax.sql.DataSource;

@Configuration


public class DatabaseConfig {
 @Value("${spring.datasource.url}")
 private String Billcom_app;
 @Bean
 public DataSource dataSource() {
 HikariConfig config = new HikariConfig();
 config.setJdbcUrl(Billcom_app);
 return new HikariDataSource(config);
 }
}

