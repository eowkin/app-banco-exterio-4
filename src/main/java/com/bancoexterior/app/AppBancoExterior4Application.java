package com.bancoexterior.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource( value = "file:"+"${${app.ambiente}"+".seed.ruta}", ignoreResourceNotFound = false)
public class AppBancoExterior4Application extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AppBancoExterior4Application.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AppBancoExterior4Application.class, args);
	}

}
