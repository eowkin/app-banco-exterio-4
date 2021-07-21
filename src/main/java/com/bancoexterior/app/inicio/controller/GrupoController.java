package com.bancoexterior.app.inicio.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.inicio.dto.GrupoDto;
import com.bancoexterior.app.inicio.model.Grupo;
import com.bancoexterior.app.inicio.model.GruposMenu;
import com.bancoexterior.app.inicio.model.GruposMenuPk;
import com.bancoexterior.app.inicio.model.Menu;
import com.bancoexterior.app.inicio.service.IGrupoService;
import com.bancoexterior.app.inicio.service.IGruposMenuService;
import com.bancoexterior.app.inicio.service.IMenuService;
import com.bancoexterior.app.util.LibreriaUtil;




@Controller
@RequestMapping("/grupos")
public class GrupoController {
	
	private static final Logger LOGGER = LogManager.getLogger(GrupoController.class);

	@Autowired
	private IGrupoService grupoServicio;
	
	@Autowired
	private IMenuService menuServicio;
	
	@Autowired
	private LibreriaUtil libreriaUtil;
	
	@Value("${${app.ambiente}"+".grupos.valorBD}")
    private int valorBD;
	
	@Autowired
	private IGruposMenuService gruposMenuService;
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String MENSAJEOPERACIONEXITOSA = "Operacion Exitosa";
	
	private static final String MENSAJEOPERACIONFALLIDA = "Operacion Fallida";
	
	private static final String REDIRECTINDEX = "redirect:/grupos/index";
	
	private static final String MENSAJECONSULTANOARROJORESULTADOS = "La consulta no arrojo resultado";
	
	private static final String GRUPOCONTROLLERINDEXI = "[==== INICIO Index Grupo Consultas - Controller ====]";
	
	private static final String GRUPOCONTROLLERINDEXF = "[==== FIN Index Grupo Consultas - Controller ====]";
	
	private static final String GRUPOCONTROLLERFORMI = "[==== INICIO Form Grupo - Controller ====]";
	
	private static final String GRUPOCONTROLLERFORMF = "[==== FIN Form Grupo - Controller ====]";
	
	private static final String GRUPOCONTROLLEREDITI = "[==== INICIO Edit Grupo - Controller ====]";
	
	private static final String GRUPOCONTROLLEREDITF = "[==== FIN Edit Grupo - Controller ====]";
	
	private static final String GRUPOCONTROLLERSAVEI = "[==== INICIO Save Grupo - Controller ====]";
	
	private static final String GRUPOCONTROLLERSAVEF = "[==== FIN Save Grupo - Controller ====]";
	
	private static final String GRUPOCONTROLLERGUARDARI = "[==== INICIO Guardar Grupo - Controller ====]";
	
	private static final String GRUPOCONTROLLERGUARDARF = "[==== FIN Guardar Grupo - Controller ====]";
	
	private static final String GRUPOCONTROLLERPERMISOSI = "[==== INICIO Permisos Grupo - Controller ====]";
	
	private static final String GRUPOCONTROLLERPERMISOSF = "[==== FIN Permisos Grupo - Controller ====]";
	
	private static final String GRUPOCONTROLLERGUARDARPERMISOSI = "[==== INICIO Guardar Permisos Grupo - Controller ====]";
	
	private static final String GRUPOCONTROLLERGUARDARPERMISOSF = "[==== FIN Guardar Permisos Grupo - Controller ====]";
	
	private static final String GRUPOCONTROLLERACTIVARI = "[==== INICIO Activar Grupo - Controller ====]";
	
	private static final String GRUPOCONTROLLERACTIVARF = "[==== FIN Activar Grupo - Controller ====]";
	
	
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(GRUPOCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<Grupo> listaGrupos = grupoServicio.findAll();
		model.addAttribute("listaGrupos", listaGrupos);
		LOGGER.info(GRUPOCONTROLLERINDEXF);
		
		return "monitorFinanciero/grupo/listaGrupos";
	}
	
	@GetMapping("/formGrupo")
	public String formGrupo(GrupoDto grupoDto, HttpSession httpSession) {
		LOGGER.info(GRUPOCONTROLLERFORMI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		LOGGER.info(GRUPOCONTROLLERFORMF);
		return "monitorFinanciero/grupo/formGrupo";
	}
	
	@GetMapping("/edit")
	public String editGrupo(@RequestParam("idGrupo") int idGrupo, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(GRUPOCONTROLLEREDITI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		GrupoDto grupoEdit = grupoServicio.findById(idGrupo); 
		if(grupoEdit != null) {
			model.addAttribute("grupoDto", grupoEdit);
			LOGGER.info(GRUPOCONTROLLEREDITF);
			return "monitorFinanciero/grupo/formEditGrupo";
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			LOGGER.info(GRUPOCONTROLLEREDITF);
			return REDIRECTINDEX;
		}
		
	}
	
	@GetMapping("/activar/{idGrupo}")
	public String activarWs(@PathVariable("idGrupo") int idGrupo, Grupo grupo, 
			Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(GRUPOCONTROLLERACTIVARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		
		grupoServicio.updateActivarDesactivarGrupo(true, SecurityContextHolder.getContext().getAuthentication().getName(),idGrupo);
		redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
		
		LOGGER.info(GRUPOCONTROLLERACTIVARF);
		return REDIRECTINDEX;
	}
			
	
	@GetMapping("/desactivar/{idGrupo}")
	public String desactivarWs(@PathVariable("idGrupo") int idGrupo, Grupo grupo, 
			Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(GRUPOCONTROLLERACTIVARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		
		grupoServicio.updateActivarDesactivarGrupo(false, SecurityContextHolder.getContext().getAuthentication().getName(),idGrupo);
		redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
		
		LOGGER.info(GRUPOCONTROLLERACTIVARF);
		return REDIRECTINDEX;
	}
	
	
	@PostMapping("/save")
	public String save(GrupoDto grupoDto, BindingResult result, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(GRUPOCONTROLLERSAVEI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		grupoDto.setNombreGrupo(grupoDto.getNombreGrupo().trim());
		grupoDto.setCodUsuario(SecurityContextHolder.getContext().getAuthentication().getName());
		grupoDto.setFlagActivo(true);
		GrupoDto grupoSave =   grupoServicio.save(grupoDto);
		if(grupoSave != null) {
			redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJEOPERACIONFALLIDA);
		}
		LOGGER.info(GRUPOCONTROLLERSAVEF);
		return REDIRECTINDEX;
	}
	
	@PostMapping("/guardar")
	public String guardar(GrupoDto grupoDto, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(GRUPOCONTROLLERGUARDARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		grupoServicio.updateNombreGrupo(grupoDto.getNombreGrupo(), SecurityContextHolder.getContext().getAuthentication().getName(),grupoDto.getIdGrupo());
		redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
		LOGGER.info(GRUPOCONTROLLERGUARDARF);
		return REDIRECTINDEX;
	}
	
	@GetMapping("/permisos")
	public String permisos(@RequestParam("idGrupo") int idGrupo, GrupoDto grupoDto, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(GRUPOCONTROLLERPERMISOSI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		GrupoDto grupoEdit = grupoServicio.findById(idGrupo); 
		if(grupoEdit != null) {
			LOGGER.info(grupoEdit.getMenus().size());
			List<Menu> listaMenu = menuServicio.findAll();
			//List<Menu> listaMenu = menuServicio.todoMenu();
			for (Menu menu : listaMenu) {
				LOGGER.info(menu.getNombre());
			}
			model.addAttribute("grupoDto", grupoEdit);
			model.addAttribute("listaMenu", listaMenu);
			LOGGER.info(GRUPOCONTROLLERPERMISOSF);
			return "monitorFinanciero/grupo/formGrupoPermisos";
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			LOGGER.info(GRUPOCONTROLLERPERMISOSF);
			return REDIRECTINDEX;
		}
	}
	
	@PostMapping("/guardarPermisos")
	public String guardarPermisos(GrupoDto grupoDto, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		
		LOGGER.info(GRUPOCONTROLLERGUARDARPERMISOSI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		GruposMenu gruposMenu = new GruposMenu();
		GruposMenuPk id = new GruposMenuPk();
		id.setIdGrupoPk(grupoDto.getIdGrupo());
		
		LOGGER.info("_[---------Lista Actual-----------]");
		GrupoDto grupoEdit = grupoServicio.findById(grupoDto.getIdGrupo());
		List<Menu> listaActual = grupoEdit.getMenus();
		List<Menu> listaMenu = grupoDto.getMenus();
		if(listaActual.isEmpty()){
			LOGGER.info("_[---------No tiene Menu Actual-----------]");
			if(listaMenu.isEmpty()){
				LOGGER.info("_[---------No selecciono Menu Lista Selecionada, No hago nada-----------]");
			}else {
				LOGGER.info("_[---------Si selecciono Menu Lista Selecionada, Voy a Incluir-----------]");
				for (Menu menu : listaMenu) {
					id.setIdMenuPk(menu.getIdMenu());
					gruposMenu.setIdPk(id);
					gruposMenu.setCodUsuario(SecurityContextHolder.getContext().getAuthentication().getName());
					gruposMenuService.guardarGrupoMenus(gruposMenu);
				}
			}
			
		}else {
			LOGGER.info("_[---------Si tiene Menu Actual, voy a borrar-----------]");
			
			if(listaMenu.isEmpty()){
				LOGGER.info("_[---------No selecciono Menu Lista Selecionada, Solo borro-----------]");
				for (Menu menu : listaActual) {
					id.setIdMenuPk(menu.getIdMenu());
					gruposMenuService.borrarRealcion(id);
				}
			}else {
				LOGGER.info("_[---------Si selecciono Menu Lista Selecionada, Borro y Incluyo la nueva lista de Permiso-----------]");
				for (Menu menu : listaActual) {
					id.setIdMenuPk(menu.getIdMenu());
					gruposMenuService.borrarRealcion(id);
				}
				
				for (Menu menu : listaMenu) {
					id.setIdMenuPk(menu.getIdMenu());
					gruposMenu.setIdPk(id);
					gruposMenu.setCodUsuario(SecurityContextHolder.getContext().getAuthentication().getName());
					gruposMenuService.guardarGrupoMenus(gruposMenu);
				}
			}
			
			
		}
		
		LOGGER.info(GRUPOCONTROLLERGUARDARPERMISOSF);
		redirectAttributes.addFlashAttribute(MENSAJE, MENSAJEOPERACIONEXITOSA);
		return REDIRECTINDEX;
		
		
		
		
	}
	
	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = "grupo";
		model.addAttribute("arrUri", arrUriP);
	}
	
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dataFormat, false));
	}

}
