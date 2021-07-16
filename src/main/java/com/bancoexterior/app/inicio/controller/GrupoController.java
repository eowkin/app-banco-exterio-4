package com.bancoexterior.app.inicio.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.inicio.dto.GrupoDto;
import com.bancoexterior.app.inicio.model.Grupo;
import com.bancoexterior.app.inicio.model.Menu;
import com.bancoexterior.app.inicio.service.IGrupoService;
import com.bancoexterior.app.inicio.service.IMenuService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/grupos")
public class GrupoController {

	@Autowired
	private IGrupoService grupoServicio;
	
	@Autowired
	private IMenuService menuServicio;
	
	
	private static final String MENSAJE = "mensaje";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String REDIRECTINDEX = "redirect:/grupos/index";
	
	private static final String MENSAJECONSULTANOARROJORESULTADOS = "La consulta no arrojo resultado";
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes) {
		
		List<Grupo> listaGrupos = grupoServicio.findAll();
		model.addAttribute("listaGrupos", listaGrupos);
		
		
		return "monitorFinanciero/grupo/listaGrupos";
	}
	
	@GetMapping("/formGrupo")
	public String formGrupo(GrupoDto grupoDto) {
		log.info("formGrupo");
		return "monitorFinanciero/grupo/formGrupo";
	}
	
	@GetMapping("/edit")
	public String editGrupo(@RequestParam("idGrupo") int idGrupo, Model model, RedirectAttributes redirectAttributes) {
		log.info("editGrupo");
		
		GrupoDto grupoEdit = grupoServicio.findById(idGrupo); 
		if(grupoEdit != null) {
			log.info("grupoEditId :" +grupoEdit.getIdGrupo());
			log.info("grupoEditNombre :" +grupoEdit.getNombreGrupo());
			log.info("grupoEditCodUsuario :" +grupoEdit.getCodUsuario());
			log.info("grupoEditFechaIngreso :" +grupoEdit.getFechaIngreso());
			log.info("grupoEdit :" +grupoEdit.getFechaModificacion());
			model.addAttribute("grupoDto", grupoEdit);
			return "monitorFinanciero/grupo/formEditGrupo";
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			return REDIRECTINDEX;
		}
		
	}
	
	
	@PostMapping("/save")
	public String save(GrupoDto grupoDto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		log.info("grupo: "+grupoDto.getNombreGrupo());
		grupoDto.setCodUsuario(SecurityContextHolder.getContext().getAuthentication().getName());
		GrupoDto grupoSave =   grupoServicio.save(grupoDto);
		if(grupoSave != null) {
			redirectAttributes.addFlashAttribute(MENSAJE, "Operacion Exitosa");
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, "Operacion Fallida");
		}
		return REDIRECTINDEX;
	}
	
	@PostMapping("/guardar")
	public String guardar(GrupoDto grupoDto, Model model, RedirectAttributes redirectAttributes) {
		log.info("getNombreGrupo(): "+grupoDto.getNombreGrupo());
		log.info("getIdGrupo(): "+grupoDto.getIdGrupo());
		log.info("getFechaIngreso(): "+grupoDto.getFechaIngreso());
		
		grupoDto.setCodUsuario(SecurityContextHolder.getContext().getAuthentication().getName());
		GrupoDto grupoSave =   grupoServicio.save(grupoDto);
		if(grupoSave != null) {
			redirectAttributes.addFlashAttribute(MENSAJE, "Operacion Exitosa");
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, "Operacion Fallida");
		}
		return REDIRECTINDEX;
	}
	
	@GetMapping("/permisos")
	public String permisos(@RequestParam("idGrupo") int idGrupo, GrupoDto grupoDto, Model model, RedirectAttributes redirectAttributes) {
		
		log.info("permisos");
		
		GrupoDto grupoEdit = grupoServicio.findById(idGrupo); 
		List<Menu> listaMenu = menuServicio.findAll();
		for (Menu menu : listaMenu) {
			log.info("menu: "+menu.getNombre());
		}
		
		if(grupoEdit != null) {
			log.info("grupoEditId :" +grupoEdit.getIdGrupo());
			log.info("grupoEditNombre :" +grupoEdit.getNombreGrupo());
			log.info("grupoEditCodUsuario :" +grupoEdit.getCodUsuario());
			log.info("grupoEditFechaIngreso :" +grupoEdit.getFechaIngreso());
			log.info("grupoEdit.getFechaModificacion() :" +grupoEdit.getFechaModificacion());
			model.addAttribute("grupoDto", grupoEdit);
			model.addAttribute("listaMenu", listaMenu);
			
			return "monitorFinanciero/grupo/formGrupoPermisos";
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJECONSULTANOARROJORESULTADOS);
			return REDIRECTINDEX;
		}
	}
	
	@PostMapping("/guardarPermisos")
	public String guardarPermisos(GrupoDto grupoDto, Model model, RedirectAttributes redirectAttributes) {
		
		log.info("getIdGrupo(): "+grupoDto.getIdGrupo());
		log.info("getNombreGrupo(): "+grupoDto.getNombreGrupo());
		log.info("grupoDto.getCodUsuario(): "+grupoDto.getCodUsuario());
		log.info("getFechaIngreso(): "+grupoDto.getFechaIngreso());
		log.info("grupoDto.getFechaModificacion(): "+grupoDto.getFechaModificacion());
		log.info("grupoDto.getMenus(): "+grupoDto.getMenus());
		
		List<Menu> listaMenu = grupoDto.getMenus();
		for (Menu menu : listaMenu) {
			log.info("menuid: "+menu.getNombre());
			log.info("menuNombre: "+menu.getNombre());
			log.info("menuNivel: "+menu.getNivel());
			log.info("menuNivel: "+menu.getNivel());
		}
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("userName: "+userName);
		grupoDto.setCodUsuario(userName);
		log.info("[---------------------Antes del Save------------------ ]");
		log.info("getIdGrupo(): "+grupoDto.getIdGrupo());
		log.info("getNombreGrupo(): "+grupoDto.getNombreGrupo());
		log.info("grupoDto.getCodUsuario(): "+grupoDto.getCodUsuario());
		log.info("getFechaIngreso(): "+grupoDto.getFechaIngreso());
		log.info("grupoDto.getFechaModificacion(): "+grupoDto.getFechaModificacion());
		log.info("grupoDto.getMenus(): "+grupoDto.getMenus());
		GrupoDto grupoSave =   grupoServicio.save(grupoDto);
		if(grupoSave != null) {
			redirectAttributes.addFlashAttribute(MENSAJE, "Operacion Exitosa");
		}else {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, "Operacion Fallida");
		}
		return REDIRECTINDEX;
	}
	
	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = "grupo";
		model.addAttribute("arrUri", arrUriP);
	}
	
	
	/*
	 * @InitBinder public void initBinder1(WebDataBinder binder) { SimpleDateFormat
	 * dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	 * 
	 * binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat,
	 * true)); }
	 */
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy");
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dataFormat, false));
	}

}
