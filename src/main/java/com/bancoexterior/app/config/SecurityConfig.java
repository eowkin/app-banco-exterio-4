package com.bancoexterior.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${ldap.domain}")
	private String ldapDomain;

	@Value("${ldap.url}")
	private String ldapUrl;
	
	@Value("${ldap.base.dn}")
	private String ldapBaseDn;
	
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("entre 1");
	
		
		http.authorizeRequests()
		.antMatchers("/css/**", "/").permitAll()
		.antMatchers(
				"/vendors/**",
				"/img/**",
				"/js/**",
				"/scss/**",
				"/node_modules/**").permitAll() 
		//.antMatchers("/login*").permitAll()	
		.anyRequest().authenticated()
		.and().formLogin().loginPage("/login").failureUrl("/login-error").defaultSuccessUrl("/index").permitAll()
		.usernameParameter("username")
	    .passwordParameter("password")
	    .defaultSuccessUrl("/index")
		.and().logout().permitAll();
		
	}
	

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {

				auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider(ldapDomain, ldapUrl, ldapBaseDn));
		
		}

		@Bean
		public AuthenticationProvider activeDirectoryLdapAuthenticationProvider(String domain, String url, String rootDn) {
			CustomActiveDirectoryLdapAuthenticationProvider provider = new CustomActiveDirectoryLdapAuthenticationProvider(
					domain, url, rootDn);
			provider.setConvertSubErrorCodesToExceptions(true);
			provider.setUseAuthenticationRequestCredentials(true);
			return provider;
		}

 	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;

	}
}
