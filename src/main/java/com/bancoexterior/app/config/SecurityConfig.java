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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;




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
		.and().formLogin().loginPage("/login").failureUrl("/login-error").defaultSuccessUrl("/inicio").permitAll()
		.usernameParameter("username")
	    .passwordParameter("password")
	    .successHandler(new CustomAuthenticationSuccessHandler())
	    .and()
	    //.logout().permitAll();
		 .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).clearAuthentication(true)
		 .logoutSuccessUrl("/login").deleteCookies("JSESSIONID")
		 .invalidateHttpSession(true);
		
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
