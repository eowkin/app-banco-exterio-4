package com.bancoexterior.app.convenio.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bancoexterior.app.inicio.model.Grupo;
import com.bancoexterior.app.inicio.model.Menu;
import com.bancoexterior.app.inicio.service.IMenuService;
import com.bancoexterior.app.inicio.service.IGrupoService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {

	@Autowired
	private IMenuService serviceMenu;
	
	@Autowired 
	private IGrupoService serviceGrupo;

	
	
	@GetMapping("/inicio")
	public String mostrarHome(Authentication auth, HttpSession httpSession) {
	    log.info("siempre me llama mostarHome");
	    log.info("[------------Menu por role--------------]");
	    String username = auth.getName();
	    /*
	    
		log.info("username: "+ username);
	    List<Integer> listaInMenu = bucarListaMenuIn(auth);  
	    if(!listaInMenu.isEmpty()) {
			List<Menu> listaMenu = buscarListaMenuMostrar(listaInMenu);
		    if(!listaMenu.isEmpty()) {
		    	if(validarListaMenu(listaMenu)) {
		    		httpSession.setAttribute("listaMenu", listaMenu);
					return "index";
		    	}else {
		    		log.info("[-----no tiene menu asignado-----]");
					return "redirect:/logout";
		    	}
		    	
				
		    }else{
		    	log.info("[-----no tiene menu asignado-----]");
				return "redirect:/logout";
		    }
		}else {
				log.info("[-----no tiene menu asignado-----]");
				return "redirect:/logout";	
		}*/
		
	    return "index"; 
	    
	}
	
	public List<Menu> buscarListaMenuMostrar(List<Integer> listaInMenu){
		List<Menu> listaMenu = serviceMenu.todoMenuRoleIn(listaInMenu);;
		log.info("[-----Si tiene menu asignado-----]");
		
	    if(!listaMenu.isEmpty()) {
	    	
			log.info("[-----------------------]");
			log.info("Sin imprimir hijos");
			for (Menu menu : listaMenu) {
				log.info(menu.getNombre());
				menu.setMenuHijos(buscarHijos(listaMenu, menu.getIdMenu()));
			}
			
			
			return listaMenu;
	    }else{
	    	log.info("[-----no tiene menu asignado-----]");
			return listaMenu;
	    }
	}
	
	
	public List<Menu> buscarHijos(List<Menu> menu, int idPapa){
		List<Menu> menuHijos = new ArrayList<>();
		for (Menu menu2 : menu) {
			if(menu2.getNivel() != 1) {
				if(menu2.getMenuPadre().getIdMenu() == idPapa) {
					menuHijos.add(menu2);
				}
			}
			
		}
		
		return menuHijos;
	}
	
	public List<Integer> bucarListaMenuIn(Authentication auth){
		 List<Integer> listaInMenu = new ArrayList<>(); 
		 List<Menu> listaMenus; 
		 for (GrantedAuthority rol : auth.getAuthorities()) {
				log.info("Grupo: "+ rol.getAuthority());
				Grupo grupo = serviceGrupo.findByNombre(rol.getAuthority());
				log.info("Luego de Buscar Menu");
				log.info("grupo: "+ grupo);
				
				if(grupo != null) {
					log.info("grupo distinto de null ");
					listaMenus = grupo.getMenus();
					log.info("listaMenus.size(): "+ listaMenus.size());
					if(!listaMenus.isEmpty()) {
						for (Menu menu : listaMenus) {
							log.info(menu.getNombre());
							listaInMenu.add(menu.getIdMenu());
						}
					}	
				}
			}
		 
		 return listaInMenu;
	}
	
	public boolean validarListaMenu(List<Menu> listaMenu) {
		boolean validoRaiz = false;
		boolean validoLink = false;
		for (Menu menu : listaMenu) {
			if(menu.getNivel() == 1) {
				validoRaiz = true;
			}
		}
		
		if(validoRaiz)
			for (Menu menu : listaMenu) {
				log.info(menu.getNombre());
				if(menu.getMenuHijos().size() > 0) {
					List<Menu> menuHijos = menu.getMenuHijos();
					for (Menu menu2 : menuHijos) {
						log.info(menu2.getNombre());
						if(menu2.getDireccion() != null) {
							validoLink = true;
						}
						if(menu2.getMenuHijos().size() > 0) {
							List<Menu> menuHijos2 = menu2.getMenuHijos();
							for (Menu menu3 : menuHijos2) {
								log.info(menu3.getNombre());
								if(menu3.getDireccion() != null) {
									validoLink = true;
								}
							}
							
						}
						
					}
					
				}
			}
		
		if(validoRaiz && validoLink)
			return true;
		else
			return false;
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
