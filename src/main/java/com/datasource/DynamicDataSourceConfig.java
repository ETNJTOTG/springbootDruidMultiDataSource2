package com.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.atomikos.jdbc.AtomikosDataSourceBean;

/**
 * @title DynamicDataSourceConfig.java
 * @description TODO
 * @time 2019/07/19 08:40:06
 */
@Configuration
@MapperScan(basePackages = "com.mapper", sqlSessionTemplateRef = "sqlSessionTemplate")
public class DynamicDataSourceConfig {
	@Bean
	@ConfigurationProperties("spring.datasource.druid.db1")
	public DataSource druidDataSourceOne() {
		return new DruidXADataSource();
	}

	@Bean
	public DataSource dataSourceOne(DataSource druidDataSourceOne) {
		AtomikosDataSourceBean sourceBean = new AtomikosDataSourceBean();
		sourceBean.setXaDataSource((DruidXADataSource) druidDataSourceOne);
		// 必须为数据源指定唯一标识
		sourceBean.setUniqueResourceName(DataSourceType.ONE);
		return sourceBean;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactoryOne(DataSource dataSourceOne) throws Exception {
		return createSqlSessionFactory(dataSourceOne);
	}

	@Bean
	@ConfigurationProperties("spring.datasource.druid.db2")
	public DataSource druidDataSourceTwo() {
		return new DruidXADataSource();
	}

	@Bean
	public DataSource dataSourceTwo(DataSource druidDataSourceTwo) {
		AtomikosDataSourceBean sourceBean = new AtomikosDataSourceBean();
		sourceBean.setXaDataSource((DruidXADataSource) druidDataSourceTwo);
		sourceBean.setUniqueResourceName(DataSourceType.TWO);
		return sourceBean;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactoryTwo(DataSource dataSourceTwo) throws Exception {
		return createSqlSessionFactory(dataSourceTwo);
	}

	@Bean
	public MySqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactoryOne,
			SqlSessionFactory sqlSessionFactoryTwo) {
		Map<Object, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<Object, SqlSessionFactory>();
		sqlSessionFactoryMap.put(DataSourceType.ONE, sqlSessionFactoryOne);
		sqlSessionFactoryMap.put(DataSourceType.TWO, sqlSessionFactoryTwo);

		MySqlSessionTemplate customSqlSessionTemplate = new MySqlSessionTemplate(sqlSessionFactoryOne);
		customSqlSessionTemplate.setTargetSqlSessionFactories(sqlSessionFactoryMap);
		return customSqlSessionTemplate;
	}

	private SqlSessionFactory createSqlSessionFactory(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean
				.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mappers/*.xml"));
		// 其他可配置项(包括是否打印sql,是否开启驼峰命名等)
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		configuration.setLogImpl(StdOutImpl.class);
		factoryBean.setConfiguration(configuration);
		return factoryBean.getObject();
	}
}
