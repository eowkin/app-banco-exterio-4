package com.bancoexterior.app.convenio.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

	@GetMapping("/")
	public String mostarHome(Authentication auth) {
		
		log.info("auth: "+auth);
		if(auth != null ) {
			String username = auth.getName();
			log.info("username: "+username);
			for (GrantedAuthority rol : auth.getAuthorities()) {
				log.info("Rol: "+ rol.getAuthority());
			}
		}else {
			return "redirect:login";
		}
		
		
		return "index";
	}

	
	@GetMapping("/index")
	public String userIndex() {
		
		return "index";
	}
	
	
	@GetMapping("/login")
	public String login() {
		log.info("entre por login");
		return "/login";
	}
	
	@GetMapping("/logout") 
	public String logout(HttpServletRequest request){
		
		log.info("entre por logout");
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/";
	}

	
	@GetMapping("/login-error")
	public String loginError(Model model) {
		log.info("Pase por /login-error");
		model.addAttribute("loginError", true);
		return "login";
	}
}
