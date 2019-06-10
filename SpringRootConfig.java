package com.hiveag.geepy.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan({"com.hiveag.geepy.dao","com.hiveag.geepy.service"})
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 30*60)
@EnableTransactionManagement
public class SpringRootConfig {
	
	@Bean
	public DataSource dataSource() {
		
		String url = "jdbc:postgresql://geepy.c12zdwrasdfaykqx.us-west-2.rds.amazonaws.com:5432/geepybike";
		String username = "geadsfasdepybike";
		String password = "g33pyBisdfaksdfa3f%";
		final DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
		dataSource.setDriverClassName("org.postgresql.Driver");
		
		return dataSource;
	}
	
	
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		
		final LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(new String[] {"com.hiveag.geepy.pojo"});
		sessionFactory.setHibernateProperties(hibernateProperties());
		
		return sessionFactory;
	}
	
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		
		final HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		
		return txManager;
	}
	
	final Properties hibernateProperties() {
		
		final Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		hibernateProperties.setProperty("hibernate.show_sql", "false");
		
		return hibernateProperties;
	}
	
	@Bean
    public JedisConnectionFactory connectionFactory() {
		
		JedisConnectionFactory jedisConnectionFactory =  new JedisConnectionFactory(); 
		
		jedisConnectionFactory.setHostName("34.17.206.74");
		jedisConnectionFactory.setPort(637);
		/*jedisConnectionFactory.setPassword("");*/
		
		/*jedisConnectionFactory.setHostName("deadpool.l5lydx.0001.usw2.cache.amazonaws.com");
		jedisConnectionFactory.setPort(637);*/
		
		return jedisConnectionFactory;
	}
	
	/*@Bean
	public static ConfigureRedisAction configureRedisAction() {
	    return ConfigureRedisAction.NO_OP;
	}*/


}
