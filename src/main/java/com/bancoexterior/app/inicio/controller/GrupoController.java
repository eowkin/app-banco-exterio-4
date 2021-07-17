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
import com.bancoexterior.app.inicio.model.GruposMenu;
import com.bancoexterior.app.inicio.model.GruposMenuPk;
import com.bancoexterior.app.inicio.model.Menu;
import com.bancoexterior.app.inicio.service.IGrupoService;
import com.bancoexterior.app.inicio.service.IGruposMenuService;
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
	
	@Autowired
	private IGruposMenuService gruposMenuService;
	
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
		grupoDto.setFlagActivo(true);
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
		grupoEdit.getMenus().size();
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
		log.info("guardarPermisos");
		log.info("getIdGrupo(): "+grupoDto.getIdGrupo());
		log.info("getNombreGrupo(): "+grupoDto.getNombreGrupo());
		log.info("grupoDto.getCodUsuario(): "+grupoDto.getCodUsuario());
		log.info("getFechaIngreso(): "+grupoDto.getFechaIngreso());
		log.info("grupoDto.getFechaModificacion(): "+grupoDto.getFechaModificacion());
		log.info("grupoDto.getMenus(): "+grupoDto.getMenus());
		
		
		GruposMenu gruposMenu = new GruposMenu();
		GruposMenuPk id = new GruposMenuPk();
		id.setIdGrupoPk(grupoDto.getIdGrupo());
		
		log.info("_[---------Lista Actual-----------]");
		GrupoDto grupoEdit = grupoServicio.findById(grupoDto.getIdGrupo());
		List<Menu> listaActual = grupoEdit.getMenus();
		List<Menu> listaMenu = grupoDto.getMenus();
		if(listaActual.isEmpty()){
			log.info("_[---------No tiene Menu Actual-----------]");
			if(listaMenu.isEmpty()){
				log.info("_[---------No selecciono Menu Lista Selecionada, No hago nada-----------]");
			}else {
				log.info("_[---------Si selecciono Menu Lista Selecionada, Voy a Incluir-----------]");
				for (Menu menu : listaMenu) {
					id.setIdMenuPk(menu.getIdMenu());
					gruposMenu.setIdPk(id);
					gruposMenu.setCodUsuario(SecurityContextHolder.getContext().getAuthentication().getName());
					gruposMenuService.guardarGrupoMenus(gruposMenu);
				}
			}
			
		}else {
			log.info("_[---------Si tiene Menu Actual, voy a borrar-----------]");
			
			for (Menu menu : listaActual) {
				log.info("menu.getIdMenu(): "+menu.getIdMenu());
				log.info("menu.getNombre(): "+menu.getNombre());
				log.info("menu.getDireccion(): "+menu.getDireccion());
				log.info("menu.getNivel(): "+menu.getNivel());
				log.info("menu.getOrden(): "+menu.getOrden());
				
				
			}
			
			
			for (Menu menu : listaActual) {
				id.setIdMenuPk(menu.getIdMenu());
				log.info("id.getIdGrupoPk(): "+id.getIdGrupoPk());
				log.info("id.getIdMenuPk(): "+id.getIdMenuPk());
				gruposMenuService.borrarRealcion(id);
			}
			
			for (Menu menu : listaMenu) {
				id.setIdMenuPk(menu.getIdMenu());
				gruposMenu.setIdPk(id);
				gruposMenu.setCodUsuario(SecurityContextHolder.getContext().getAuthentication().getName());
				gruposMenuService.guardarGrupoMenus(gruposMenu);
			}
		}
		
		
		redirectAttributes.addFlashAttribute(MENSAJE, "Operacion Exitosa");
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
