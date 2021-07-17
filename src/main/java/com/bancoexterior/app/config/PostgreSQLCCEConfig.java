package com.bancoexterior.app.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bancoexterior.app.seguridad.MiCipher;




//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(entityManagerFactoryRef = "adminEntityManagerFactory", transactionManagerRef = "adminTransactionManager", 
//basePackages = { "com.bancoexterior.app.cce.repository" })
public class PostgreSQLCCEConfig {

	@Autowired
	private Environment env;
	
	private BasicDataSource db = new BasicDataSource();
	
	@Value("${${app.ambiente}"+".seed.monitorfinanciero}")
    private String sconfigDesKey;
	
	@Value("${${app.ambiente}"+".dbCce.user}")
    private String usuario;
    @Value("${${app.ambiente}"+".dbCce.password}")
    private String clave;
    @Value("${${app.ambiente}"+".dbCce.url}")
    private String url;
    @Value("${dbCce.driver}")
    private String driver;
    @Value("${dbCce.initialSize}")
    private String initialSize;
    @Value("${dbCce.testOnBorrow}")
    private String testOnBorrow;
    @Value("${dbCce.testOnReturn}")
    private String testOnReturn;
    @Value("${dbCce.testWhileIdle}")
    private String testWhileIdle;
    @Value("${dbCce.timeBetweenEvictionRunsMillis}")
    private String timeBetweenEvictionRunsMillis;
    @Value("${dbCce.minIdle}")
    private String minIdle;
    @Value("${dbCce.maxTotal}")
    private String maxTotal;
    @Value("${dbCce.maxIdle}")
    private String maxIdle;
    @Value("${dbCce.maxWaitMillis}")
    private String maxWaitMillis;
    @Value("${dbCce.removeAbandonedOnBorrow}")
    private String removeAbandonedOnBorrow;
    @Value("${dbCce.removeAbandonedTimeout}")
    private String removeAbandonedTimeout;
    @Value("${dbCce.logAbandoned}")
    private String logAbandoned;
    @Value("${dbCce.minEvictableIdleTimeMillis}")
    private String minEvictableIdleTimeMillis;
    @Value("${dbCce.defaultAutoCommit}")
    private String defaultAutoCommit;
    @Value("${dbCce.removeAbandonedOnMaintenance}")
    private String removeAbandonedOnMaintenance;
    @Value("${dbCce.validationQuery}")
    private String validationQuery;
    @Value("${dbCce.validationQueryTimeout}")
    private String validationQueryTimeout;
	
	
	
	@Bean(name = "adminDataSource")
	public DataSource adminDatasource() {
		/*DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(env.getProperty("postgre.datasource.url"));
		dataSource.setUsername(env.getProperty("postgre.datasource.username"));
		dataSource.setPassword(env.getProperty("postgre.datasource.password"));
		dataSource.setDriverClassName(env.getProperty("postgre.datasource.driver-class-name"));
		*/
		
		//log.info("sconfigDesKey: "+sconfigDesKey);
    	//log.info("usuarioCCE: "+usuario);
    	//String usuarioDecryptCCE = MiCipher.decrypt(usuario, sconfigDesKey);
    	//log.info("usuarioDecryptCCE: "+usuarioDecryptCCE);
		 db.setUsername(MiCipher.decrypt(usuario.trim(), sconfigDesKey.trim()));
         db.setPassword(MiCipher.decrypt(clave.trim(), sconfigDesKey.trim()));
         db.setUrl(url);
         db.setDriverClassName(driver);
         db.setInitialSize(Integer.parseInt(initialSize));
         db.setTestOnBorrow(Boolean.parseBoolean(testOnBorrow));
         db.setTestOnReturn(Boolean.parseBoolean(testOnReturn));
         db.setTestWhileIdle(Boolean.parseBoolean(testWhileIdle));
         db.setTimeBetweenEvictionRunsMillis(Long.parseLong(timeBetweenEvictionRunsMillis));
         db.setMinIdle(Integer.parseInt(minIdle));
         db.setMaxTotal(Integer.parseInt(maxTotal));
         db.setMaxIdle(Integer.parseInt(maxIdle));
         db.setMaxWaitMillis(Long.parseLong(maxWaitMillis));
         db.setRemoveAbandonedOnBorrow(Boolean.parseBoolean(removeAbandonedOnBorrow));
         db.setRemoveAbandonedTimeout(Integer.parseInt(removeAbandonedTimeout));
         db.setLogAbandoned(Boolean.parseBoolean(logAbandoned));
         db.setMinEvictableIdleTimeMillis(Long.parseLong(minEvictableIdleTimeMillis));
         db.setDefaultAutoCommit(Boolean.parseBoolean(defaultAutoCommit));
         db.setRemoveAbandonedOnMaintenance(Boolean.parseBoolean(removeAbandonedOnMaintenance));
         db.setValidationQuery(validationQuery);
         db.setValidationQueryTimeout(Integer.parseInt(validationQueryTimeout));
		
		
		return db;
	}
	

	@Primary
	@Bean(name = "adminEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(adminDatasource());
		em.setPackagesToScan("com.bancoexterior.app.cce.model");
		
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		
		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("postgre.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.show-sql", env.getProperty("postgre.jpa.show-sql"));
		properties.put("hibernate.dialect", env.getProperty("postgre.jpa.database-platform"));
		
		em.setJpaPropertyMap(properties);
		
		return em;
		
	}
	
	@Primary
	@Bean(name = "adminTransactionManager")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		
		return transactionManager;
	}
}
