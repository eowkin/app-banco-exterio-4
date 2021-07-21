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
	
	private static final String HOMECONTROLLERMOSTRARI = "[==== INICIO Mostrar Home - Controller ====]";
	
	private static final String HOMECONTROLLERMOSTRARF = "[==== FIN Mostrar Home - Controller ====]";
	
	private static final String HOMEFUNCIONBUSCARLISTAMENUINI = "[==== INICIO Funcion Buscar Lista Menu In Home - Controller ====]";
	
	private static final String HOMEFUNCIONBUSCARLISTAMENUINF = "[==== FIN Funcion Buscar Lista Menu In Home - Controller ====]";
	
	private static final String HOMEFUNCIONBUSCARLISTAMENUMOSTRARI = "[==== INICIO Funcion Buscar Lista Menu Mostrar Home - Controller ====]";
	
	private static final String HOMEFUNCIONBUSCARLISTAMENUMOSTRARF = "[==== FIN Funcion Buscar Lista Menu Mostrar Home - Controller ====]";
	
	private static final String HOMEFUNCIONVALIDARLISTAMENUI = "[==== INICIO Funcion Validar Lista Menu Home - Controller ====]";
	
	private static final String HOMEFUNCIONVALIDARLISTAMENUF = "[==== FIN Funcion Validar Lista Menu Home - Controller ====]";
	
	private static final String MENUNOASIGNADO = "[==== No tiene menu asigando ====]";
	
	private static final String URLINDEX = "index";
	
	private static final String URLREDIRECTLOGOUT = "redirect:/logout";
	
	
	@GetMapping("/inicio")
	public String mostrarHome(Authentication auth, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(HOMECONTROLLERMOSTRARI);
		
	    List<Integer> listaInMenu = bucarListaMenuIn(auth);
	    
	    
	    if(!listaInMenu.isEmpty()) {
			List<Menu> listaMenu = buscarListaMenuMostrar(listaInMenu);
		    if(!listaMenu.isEmpty()) {
		    	if(validarListaMenu(listaMenu)) {
		    		auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(), "Login", "Iniciar Sesion", "N/A", true, "Inicio de Sesion", request.getRemoteAddr());
		    		httpSession.setAttribute("listaMenu", listaMenu);
		    		LOGGER.info(HOMECONTROLLERMOSTRARF);
					return URLINDEX;
		    	}else {
		    		LOGGER.info(MENUNOASIGNADO);
		    		LOGGER.info(HOMECONTROLLERMOSTRARF);
					return URLREDIRECTLOGOUT;
		    	}
		    	
				
		    }else{
		    	LOGGER.info(MENUNOASIGNADO);
		    	LOGGER.info(HOMECONTROLLERMOSTRARF);
				return URLREDIRECTLOGOUT;
		    }
		}else {
			LOGGER.info(MENUNOASIGNADO);
			LOGGER.info(HOMECONTROLLERMOSTRARF);
			return URLREDIRECTLOGOUT;	
		}  
	    
	    
	    
	    
	   
	    
	   
		
	     
	    
	}
	
	public List<Menu> buscarListaMenuMostrar(List<Integer> listaInMenu){
		LOGGER.info(HOMEFUNCIONBUSCARLISTAMENUMOSTRARI);
		List<Menu> listaMenu = serviceMenu.todoMenuRoleIn(listaInMenu);;
		
		
	    if(!listaMenu.isEmpty()) {
	    	LOGGER.info("[-----Si tiene menu asignado-----]");
			for (Menu menu : listaMenu) {
				LOGGER.info(menu.getNombre());
				menu.setMenuHijos(buscarHijos(listaMenu, menu.getIdMenu()));
			}
			
			LOGGER.info(HOMEFUNCIONBUSCARLISTAMENUMOSTRARF);
			return listaMenu;
	    }else{
	    	LOGGER.info("[-----No tiene menu asignado-----]");
	    	LOGGER.info(HOMEFUNCIONBUSCARLISTAMENUMOSTRARF);
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
		LOGGER.info(HOMEFUNCIONBUSCARLISTAMENUINI); 
		List<Integer> listaInMenu = new ArrayList<>(); 
		 List<Menu> listaMenus; 
		 for (GrantedAuthority rol : auth.getAuthorities()) {
			 	Grupo grupo = serviceGrupo.findByNombreAndFlagActivo(rol.getAuthority(), true);
				if(grupo != null) {
					listaMenus = grupo.getMenus();
					if(!listaMenus.isEmpty()) {
						for (Menu menu : listaMenus) {
							listaInMenu.add(menu.getIdMenu());
						}
					}	
				}
			}
		 LOGGER.info(HOMEFUNCIONBUSCARLISTAMENUINF);
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
			
				
				Grupo grupo = serviceGrupo.findByNombreAndFlagActivo(string, true);
								
				if(grupo != null) {
					listaMenus = grupo.getMenus();
					if(!listaMenus.isEmpty()) {
						for (Menu menu : listaMenus) {
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
			  	Grupo grupo = serviceGrupo.findByNombreAndFlagActivo(rol.getAuthority(), true);
				
				if(grupo != null) {
					listaMenus = grupo.getMenus();
					if(!listaMenus.isEmpty()) {
						listaInMenu.add(grupo.getNombreGrupo());
					}	
				}
			}
		 
		 return listaInMenu;
	}
	
	public boolean validarListaMenu(List<Menu> listaMenu) {
		LOGGER.info(HOMEFUNCIONVALIDARLISTAMENUI);
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
		LOGGER.info(HOMEFUNCIONVALIDARLISTAMENUF);
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
