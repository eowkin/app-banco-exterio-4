package com.bancoexterior.app.convenio.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bancoexterior.app.inicio.model.Menu;
import com.bancoexterior.app.inicio.model.Role;
import com.bancoexterior.app.inicio.repository.IRoleRepository;
import com.bancoexterior.app.inicio.service.IMenuService;
import com.bancoexterior.app.inicio.service.IRoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

	@Autowired
	private IMenuService serviceMenu;
	
	@Autowired 
	private IRoleService serviceRole;
	
	@Autowired 
	private IRoleRepository repoRole;
	
	@GetMapping("/inicio")
	public String mostrarHome(Authentication auth, HttpSession httpSession) {
	    log.info("siempre me llama mostarHome");
	    log.info("[------------Menu por role--------------]");
	    List<Menu> listaMenus;
	    /*
	    List<Role> listaRoles = serviceRole.findAll();
		for (Role role : listaRoles) {
		    log.info("nombre: : "+role.getNombre());
		    listaMenus = role.getMenus();
		    if(!listaMenus.isEmpty()) {
				log.info("[-----Si tiene menu asignado-----]");
				for (Menu menu : listaMenus) {
					log.info(menu.getNombre());
					log.info("Si tiene hijos");
					for (Menu menu2 : menu.getMenuHijos()) {
						log.info(menu2.getNombre());
					}
				}
			}else {
					log.info("[-----no tiene menu asignado-----]");
			}
		}*/
		
	    
	    
		//Role role = serviceRole.findByNombre("ROLE_APP-CACTUS");
		Role role = serviceRole.findByNombre("ROLE_SIU");
		String valores="";
		log.info("nombre: : "+role.getNombre());
	    listaMenus = role.getMenus();
	    if(!listaMenus.isEmpty()) {
			log.info("[-----Si tiene menu asignado-----]");
			for (Menu menu : listaMenus) {
				log.info(menu.getNombre());
				valores = menu.getIdMenu().toString();
				log.info("Si tiene hijos");
				for (Menu menu2 : menu.getMenuHijos()) {
					log.info(menu2.getNombre());
				}
			}
		}else {
				log.info("[-----no tiene menu asignado-----]");
		}
		//Role role = repoRole.findByNombre(1);
		//log.info("nombre: : "+role.getNombre());
		//log.info("listaMenu: "+role.getMenus());
	    
		//List<Menu> listaMenu = role.getMenus();
	    List<Menu> listaMenu = serviceMenu.todoMenuRole(valores);
		
	    
	    
	    String username = auth.getName();
		log.info("username: "+ username);
		log.info("[-----------------------]");
		log.info("Sin imprimir hijos");
		for (Menu menu : listaMenu) {
			
			log.info(menu.getNombre());
		}
		
		log.info("[-----------------------]");
		for (Menu menu : listaMenu) {
			log.info(menu.getNombre());
			if(menu.getMenuHijos().size() > 0) {
				log.info("Si tiene hijos");
				for (Menu menu2 : menu.getMenuHijos()) {
					log.info(menu2.getNombre());
				}
				
			}else {
				log.info("No tiene hijos");
			}
		}
		
		for (GrantedAuthority rol : auth.getAuthorities()) {
			log.info("Rol: "+ rol.getAuthority());
		}
		
		
		
		
		
		httpSession.setAttribute("listaMenu", listaMenus);
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
