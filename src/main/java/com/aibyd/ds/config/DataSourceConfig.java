package com.aibyd.ds.config;

import javax.sql.DataSource;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:db.properties")
public class DataSourceConfig {

	// @Primary
	// @Bean(name = "appsysDataSourceProperties")
	// @Qualifier("appsysDataSourceProperties")
	// @ConfigurationProperties(prefix = "spring.datasource.appsys")
	// public DataSourceProperties appsysDataSourceProperties() {
	// 	return new DataSourceProperties();
	// }

	@Primary
	@Bean(name = "appsysDataSource")
	@Qualifier("appsysDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.appsys")
	public DataSource appsysDataSource() {
		return DruidDataSourceBuilder.create().build();
		// return appsysDataSourceProperties().initializeDataSourceBuilder().build();
	}

    /**	
    @Bean(name = "shopDataSource")
	@Qualifier("shopDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.shop")
	public DataSource shopDataSource() {
		return DataSourceBuilder.create().build();
	}
    */

}
