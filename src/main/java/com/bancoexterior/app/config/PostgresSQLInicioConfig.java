package com.bancoexterior.app.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.bancoexterior.app.seguridad.MiCipher;




@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "userEntityManagerFactory", transactionManagerRef = "userTransactionManager", 
	basePackages = { "com.bancoexterior.app.inicio.repository"})
public class PostgresSQLInicioConfig {

	@Autowired
	private Environment env;
	
	private BasicDataSource db = new BasicDataSource();
	
	@Value("${${app.ambiente}"+".seed.monitorfinanciero}")
    private String sconfigDesKey;
	
	@Value("${${app.ambiente}"+".dbIni.user}")
    private String usuario;
    @Value("${${app.ambiente}"+".dbIni.password}")
    private String clave;
    @Value("${${app.ambiente}"+".dbIni.url}")
    private String url;
    @Value("${dbIni.driver}")
    private String driver;
    @Value("${dbIni.initialSize}")
    private String initialSize;
    @Value("${dbIni.testOnBorrow}")
    private String testOnBorrow;
    @Value("${dbIni.testOnReturn}")
    private String testOnReturn;
    @Value("${dbIni.testWhileIdle}")
    private String testWhileIdle;
    @Value("${dbIni.timeBetweenEvictionRunsMillis}")
    private String timeBetweenEvictionRunsMillis;
    @Value("${dbIni.minIdle}")
    private String minIdle;
    @Value("${dbIni.maxTotal}")
    private String maxTotal;
    @Value("${dbIni.maxIdle}")
    private String maxIdle;
    @Value("${dbIni.maxWaitMillis}")
    private String maxWaitMillis;
    @Value("${dbIni.removeAbandonedOnBorrow}")
    private String removeAbandonedOnBorrow;
    @Value("${dbIni.removeAbandonedTimeout}")
    private String removeAbandonedTimeout;
    @Value("${dbIni.logAbandoned}")
    private String logAbandoned;
    @Value("${dbIni.minEvictableIdleTimeMillis}")
    private String minEvictableIdleTimeMillis;
    @Value("${dbIni.defaultAutoCommit}")
    private String defaultAutoCommit;
    @Value("${dbIni.removeAbandonedOnMaintenance}")
    private String removeAbandonedOnMaintenance;
    @Value("${dbIni.validationQuery}")
    private String validationQuery;
    @Value("${dbIni.validationQueryTimeout}")
    private String validationQueryTimeout;
    
    
    @Bean(name = "userDataSource")
	public DataSource userDatasource() {
		
    	
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
    
    
    @Bean(name = "userEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(userDatasource());
		em.setPackagesToScan("com.bancoexterior.app.inicio.model");
		
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		
		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("postgreIni.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.show-sql", env.getProperty("postgreIni.jpa.show-sql"));
		properties.put("hibernate.dialect", env.getProperty("postgreIni.jpa.database-platform"));
		
		em.setJpaPropertyMap(properties);
		
		return em;
		
	}
	
	@Bean(name = "userTransactionManager")
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		
		return transactionManager;
	}
}
