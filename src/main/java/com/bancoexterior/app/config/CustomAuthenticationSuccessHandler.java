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

import com.bancoexterior.app.inicio.service.IAuditoriaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Controller
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	  
	private static final int SESSION_TIMEOUT = 180;

	private IAuditoriaService auditoriaService;   
	  
	  
	public CustomAuthenticationSuccessHandler(IAuditoriaService auditoriaService) {
		super();
		log.info("CustomAuthenticationSuccessHandler");
		this.auditoriaService = auditoriaService;
	}




	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		log.info("onAuthenticationSuccess");
		log.info(authentication.getName());
		log.info(request.getRemoteAddr());
		
		HttpSession session = request.getSession();
	    session.setMaxInactiveInterval(180);
	    //Colocar al finalizar el war
	    //auditoriaService.save(authentication.getName(), "Login", "Iniciar Sesion", "N/A", true, "Inicio de Sesion", request.getRemoteAddr(), 0);
	    response.sendRedirect(String.valueOf(request.getContextPath()) + "/inicio");
	}

}
