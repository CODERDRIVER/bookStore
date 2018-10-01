package com.lyears.projects.bookstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


/**
 * @author fzm
 * @date 2018/2/25
 **/
@Configuration
@ComponentScan
@PropertySource(value = "classpath:db.properties")
public class JpaConfig {

    private final Environment environment;

    @Autowired
    public JpaConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @ConfigurationProperties(prefix="jdbc")
    public DataSource dataSource() {
        return new DriverManagerDataSource();
    }
}
