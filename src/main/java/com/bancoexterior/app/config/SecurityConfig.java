package com.bancoexterior.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		
		
		http.authorizeRequests()
		.antMatchers("/css/**", "/").permitAll()
		.antMatchers(
				"/vendors/**",
				"/img/**",
				"/js/**",
				"/scss/**",
				"/node_modules/**").permitAll() 
		.antMatchers("/monedas/**").hasAnyAuthority("ROLE_USER") 
		.antMatchers("/tasas/**").hasAnyAuthority("ROLE_USER")
		.anyRequest().authenticated()
		.and().formLogin().loginPage("/login").failureUrl("/login-error").defaultSuccessUrl("/").permitAll()
		.and().logout().permitAll();
			
		
	}
	
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		auth
        .inMemoryAuthentication()
        .withUser("user").password(passwordEncoder().encode("password")).roles("USER")
        .and()
        .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
		
	}
	
	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
