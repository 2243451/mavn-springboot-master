package com.lunatic.manager.config;

import javax.sql.DataSource;
import javax.sql.DataSource;
import javax.sql.DataSource;




import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@MapperScan(basePackages = {"com.lunatic.manager.second.dao"},sqlSessionTemplateRef = "secondarySessionTemplate")
public class SecondDbBConfig {


	@Bean(name = "secondaryDataSource")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource.secondary")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "secondarySqlSessionFactory")
	@Primary
	public SqlSessionFactory secondarySqlSessionFactory(
			@Qualifier("secondaryDataSource") DataSource dataSource)
			throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		return bean.getObject();
	}

	@Bean(name = "secondaryTransactionManager")
	@Primary
	public DataSourceTransactionManager secondaryTransactionManager(
			@Qualifier("secondaryDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean(name = "secondarySessionTemplate")
	@Primary
	public SqlSessionTemplate secondarySessionTemplate(
			@Qualifier("secondarySqlSessionFactory") SqlSessionFactory sqlSessionFactory)
			throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
