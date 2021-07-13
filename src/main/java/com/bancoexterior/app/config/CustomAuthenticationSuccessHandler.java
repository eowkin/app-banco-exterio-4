package com.bancoexterior.app.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Controller
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	  private static final int SESSION_TIMEOUT = 180;

	  
	  
	  
	public CustomAuthenticationSuccessHandler() {
		super();
		log.info("CustomAuthenticationSuccessHandler");
	}




	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		log.info("onAuthenticationSuccess");
		HttpSession session = request.getSession();
	    session.setMaxInactiveInterval(180);
	    response.sendRedirect(String.valueOf(request.getContextPath()) + "/inicio");
	}

}
