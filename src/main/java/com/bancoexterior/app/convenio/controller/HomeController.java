package com.bancoexterior.app.convenio.controller;

import java.util.ArrayList;

import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bancoexterior.app.inicio.model.Grupo;
import com.bancoexterior.app.inicio.model.Menu;
import com.bancoexterior.app.inicio.service.IMenuService;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.inicio.service.IGrupoService;




@Controller
public class HomeController {

	private static final Logger LOGGER = LogManager.getLogger(HomeController.class);
	
	@Autowired
	private IMenuService serviceMenu;
	
	@Autowired 
	private IGrupoService serviceGrupo;

	@Autowired
	private IAuditoriaService auditoriaService;
	
	@GetMapping("/inicio")
	public String mostrarHome(Authentication auth, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info("siempre me llama mostarHome");
		LOGGER.info("[------------Menu por role--------------]");
	    String username = auth.getName();
	    
	    
	    LOGGER.info("username: "+ username);
	    List<Integer> listaInMenu = bucarListaMenuIn(auth);
	    //List<Integer> listaInMenu = bucarListaMenuInPrueba();
	    
	    if(!listaInMenu.isEmpty()) {
			List<Menu> listaMenu = buscarListaMenuMostrar(listaInMenu);
		    if(!listaMenu.isEmpty()) {
		    	if(validarListaMenu(listaMenu)) {
		    		auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(), "Login", "Iniciar Sesion", "N/A", true, "Inicio de Sesion", request.getRemoteAddr());
		    		httpSession.setAttribute("listaMenu", listaMenu);
					return "index";
		    	}else {
		    		LOGGER.info("[-----no tiene menu asignado-----]");
					return "redirect:/logout";
		    	}
		    	
				
		    }else{
		    	LOGGER.info("[-----no tiene menu asignado-----]");
				return "redirect:/logout";
		    }
		}else {
			LOGGER.info("[-----no tiene menu asignado-----]");
			return "redirect:/logout";	
		}  
	    
	    
	    
	    
	   
	    
	   
		
	     
	    
	}
	
	public List<Menu> buscarListaMenuMostrar(List<Integer> listaInMenu){
		List<Menu> listaMenu = serviceMenu.todoMenuRoleIn(listaInMenu);;
		LOGGER.info("[-----Si tiene menu asignado-----]");
		
	    if(!listaMenu.isEmpty()) {
	    	
	    	LOGGER.info("[-----------------------]");
	    	LOGGER.info("Sin imprimir hijos");
			for (Menu menu : listaMenu) {
				LOGGER.info(menu.getNombre());
				menu.setMenuHijos(buscarHijos(listaMenu, menu.getIdMenu()));
			}
			
			
			return listaMenu;
	    }else{
	    	LOGGER.info("[-----no tiene menu asignado-----]");
			return listaMenu;
	    }
	}
	
	
	public List<Menu> buscarListaMenuMostrarPorNombreGrupo(List<String> listaInMenu){
		List<Menu> listaMenu = serviceMenu.todoMenuNombreGrupoIn(listaInMenu);;
		LOGGER.info("[-----Si tiene menu asignado-----]");
		
	    if(!listaMenu.isEmpty()) {
	    	
	    	LOGGER.info("[-----------------------]");
	    	LOGGER.info("Sin imprimir hijos");
			for (Menu menu : listaMenu) {
				LOGGER.info(menu.getNombre());
				menu.setMenuHijos(buscarHijos(listaMenu, menu.getIdMenu()));
			}
			
			
			return listaMenu;
	    }else{
	    	LOGGER.info("[-----no tiene menu asignado-----]");
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
			 LOGGER.info("Grupo: "+ rol.getAuthority());
			 	//Grupo grupo = serviceGrupo.findByNombre(rol.getAuthority());
			 	Grupo grupo = serviceGrupo.findByNombreAndFlagActivo(rol.getAuthority(), true);
				LOGGER.info("Luego de Buscar Menu");
				LOGGER.info("grupo: "+ grupo);
				
				if(grupo != null) {
					LOGGER.info("grupo distinto de null ");
					listaMenus = grupo.getMenus();
					LOGGER.info("listaMenus.size(): "+ listaMenus.size());
					if(!listaMenus.isEmpty()) {
						for (Menu menu : listaMenus) {
							LOGGER.info("añadi a listaInMenu:"+menu.getIdMenu());
							LOGGER.info(menu.getNombre());
							listaInMenu.add(menu.getIdMenu());
						}
					}	
				}
			}
		 
		 return listaInMenu;
	}
	
	public List<Integer> bucarListaMenuInPrueba(){
		 List<Integer> listaInMenu = new ArrayList<>(); 
		 List<Menu> listaMenus;
		 List<String> stringGrupos = new ArrayList<>();
		 stringGrupos.add("app_MF_Mod_GestDivisasConsulta");
		 stringGrupos.add("app_MF_Mod_GestDivisasAprob");
		 stringGrupos.add("app_MF_Mod_GestDivisasSeguridad");
		 stringGrupos.add("app_MF_Mod_GestDivisasParam");
		 
		 
		 
		 for (String string : stringGrupos) {
			 LOGGER.info("Grupo: "+ string);
				//Grupo grupo = serviceGrupo.findByNombre(string);
				Grupo grupo = serviceGrupo.findByNombreAndFlagActivo(string, true);
				LOGGER.info("Luego de Buscar Menu");
				LOGGER.info("grupo: "+ grupo);
				
				if(grupo != null) {
					LOGGER.info("grupo distinto de null ");
					listaMenus = grupo.getMenus();
					LOGGER.info("listaMenus.size(): "+ listaMenus.size());
					if(!listaMenus.isEmpty()) {
						for (Menu menu : listaMenus) {
							LOGGER.info("añadi a listaInMenu:"+menu.getIdMenu());
							LOGGER.info(menu.getNombre());
							listaInMenu.add(menu.getIdMenu());
						}
					}	
				}
			}
		 
		 return listaInMenu;
	}
	
	
	public List<String> bucarListaMenuInNombre(Authentication auth){
		 List<String> listaInMenu = new ArrayList<>(); 
		 List<Menu> listaMenus; 
		 for (GrantedAuthority rol : auth.getAuthorities()) {
			 LOGGER.info("Grupo: "+ rol.getAuthority());
				//Grupo grupo = serviceGrupo.findByNombre(rol.getAuthority());
			 	Grupo grupo = serviceGrupo.findByNombreAndFlagActivo(rol.getAuthority(), true);
				LOGGER.info("Luego de Buscar Menu");
				LOGGER.info("grupo: "+ grupo);
				
				if(grupo != null) {
					LOGGER.info("grupo distinto de null ");
					listaMenus = grupo.getMenus();
					LOGGER.info("listaMenus.size(): "+ listaMenus.size());
					if(!listaMenus.isEmpty()) {
						LOGGER.info("añadi a lista :"+grupo.getNombreGrupo());
						listaInMenu.add(grupo.getNombreGrupo());
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
				LOGGER.info(menu.getNombre());
				if(menu.getMenuHijos().size() > 0) {
					List<Menu> menuHijos = menu.getMenuHijos();
					for (Menu menu2 : menuHijos) {
						LOGGER.info(menu2.getNombre());
						if(menu2.getDireccion() != null) {
							validoLink = true;
						}
						if(menu2.getMenuHijos().size() > 0) {
							List<Menu> menuHijos2 = menu2.getMenuHijos();
							for (Menu menu3 : menuHijos2) {
								LOGGER.info(menu3.getNombre());
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
		LOGGER.info("entre por login");
		return "/login";
	}
	
	@GetMapping("/logout") 
	public String logout(HttpServletRequest request){
		
		LOGGER.info("entre por logout");
		auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(), "Logout", "Fin Sesion", "N/A", true, "Finalizar de Sesion", request.getRemoteAddr());
		
		SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(request, null, null);
		return "redirect:/";
	}

	
	@GetMapping("/login-error")
	public String loginError(Model model) {
		LOGGER.info("Pase por /login-error");
		model.addAttribute("loginError", true);
		return "login";
	}
	
	
}
