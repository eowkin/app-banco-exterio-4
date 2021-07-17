package com.bancoexterior.app.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;


import com.bancoexterior.app.seguridad.MiCipher;





//@Configuration
public class DataSourceConfig {
    
	
	
	@Value("${app.ambiente}")
	private String ambiente;
	
	@Value("${spring.datasource.username}")
	private String usuario;
	
	@Value("${spring.datasource.password}")
	private String password;
		
	@Value("${${app.ambiente}"+".seed.monitorfinanciero}")
    private String sconfigDesKey;
	
	
    @Bean
    public DataSource getDataSource() {
    	
    	
    	
    	
    	
    	String usuarioDecrypt = MiCipher.decrypt(usuario, sconfigDesKey);
    	String passwordDecrypt = MiCipher.decrypt(password, sconfigDesKey);
    	
    	
        
    	DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");	
        dataSourceBuilder.url("jdbc:postgresql://localhost/Convenio1");
        dataSourceBuilder.username(usuarioDecrypt);
        dataSourceBuilder.password(passwordDecrypt);
        //dataSourceBuilder.url("jdbc:postgresql://172.19.148.50:5432/Convenio1");
        //dataSourceBuilder.username(usuarioDecrypt);
        //dataSourceBuilder.password(passwordDecrypt);
        return dataSourceBuilder.build();
    }
}
