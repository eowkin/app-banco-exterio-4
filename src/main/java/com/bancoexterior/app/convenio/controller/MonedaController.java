package com.bancoexterior.app.convenio.controller;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.service.IMonedaServiceApiRest;
import com.bancoexterior.app.inicio.service.IAuditoriaService;
import com.bancoexterior.app.util.LibreriaUtil;





@Controller
@RequestMapping("/monedas")
public class MonedaController {

	private static final Logger LOGGER = LogManager.getLogger(MonedaController.class);
	
	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest;
	
	@Autowired
	private IAuditoriaService auditoriaService;
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${${app.ambiente}"+".canal}")
    private String canal;
	
	@Value("${${app.ambiente}"+".moneda.valorBD}")
    private int valorBD;

	private static final String URLINDEX = "convenio/moneda/listaMonedas";
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String LISTAMONEDAS = "listMonedas";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String REDIRECTINDEX = "redirect:/monedas/index";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";
	
	private static final String MENSAJECONSULTANOARROJORESULTADOS = "La consulta no arrojo resultado";
	
	private static final String MONEDACONTROLLERINDEXI = "[==== INICIO Index Monedas Consultas - Controller ====]";
	
	private static final String MONEDACONTROLLERINDEXF = "[==== FIN Index Monedas Consultas - Controller ====]";
	
	private static final String MONEDACONTROLLERACTIVARI = "[==== INICIO Activar Moneda - Controller ====]";
	
	private static final String MONEDACONTROLLERACTIVARF = "[==== FIN Activar Moneda - Controller ====]";
	
	private static final String MONEDACONTROLLERDESACTIVARI = "[==== INICIO Desactivar Moneda - Controller ====]";
	
	private static final String MONEDACONTROLLERDESACTIVARF = "[==== FIN Desactivar Moneda - Controller ====]";
	
	private static final String MONEDACONTROLLERSEARHCODIGOI = "[==== INICIO SearchCodigo Moneda - Controller ====]";
	
	private static final String MONEDACONTROLLERSEARHCODIGOF = "[==== FIN SearchCodigo Moneda - Controller ====]";
	
	private static final String INDEX = "/index";
	
	@GetMapping(INDEX)
	public String indexWs(Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONEDACONTROLLERINDEXI);
		
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda moneda = new Moneda();
		monedasRequest.setMoneda(moneda);
		List<Moneda> listMonedas = new ArrayList<>();
		try {
			listMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
			for (Moneda moneda2 : listMonedas) {
				if(moneda2.getFechaModificacion() != null) {
					String[] arrOfStr = moneda2.getFechaModificacion().split(" ", 2);
					moneda2.setFechaModificacion(arrOfStr[0]);
				}
			}
			//auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					//"Monedas", "Index", "0000", true, "Operacion Exitosa: Consulta", request.getRemoteAddr());
			model.addAttribute(LISTAMONEDAS, listMonedas);
	    	
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(LISTAMONEDAS, listMonedas);
			
		}
		LOGGER.info(MONEDACONTROLLERINDEXF);
		return URLINDEX; 
	}
	
	@GetMapping("/activar/{codMoneda}")
	public String activarWs(@PathVariable("codMoneda") String codMoneda, Moneda moneda, Model model, RedirectAttributes redirectAttributes,
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONEDACONTROLLERACTIVARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		Moneda monedaEdit = new Moneda();
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda monedaBuscar = new Moneda();
		monedaBuscar.setCodMoneda(codMoneda);
		monedasRequest.setMoneda(monedaBuscar);
		
		try {
			
			monedaEdit = monedaServiceApiRest.buscarMoneda(monedasRequest);
			monedaEdit.setFlagActivo(true);
			monedasRequest.setMoneda(monedaEdit);
			String respuesta = monedaServiceApiRest.actualizar(monedasRequest);
			LOGGER.info(respuesta);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					"Monedas", "activar", "0000", true, respuesta+" Moneda:"+codMoneda, request.getRemoteAddr());
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
		}
		LOGGER.info(MONEDACONTROLLERACTIVARF);
		return REDIRECTINDEX;
	}	
	
	@GetMapping("/desactivar/{codMoneda}")
	public String desactivarWs(@PathVariable("codMoneda") String codMoneda, Moneda moneda, Model model, RedirectAttributes redirectAttributes,
			HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONEDACONTROLLERDESACTIVARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		Moneda monedaEdit = new Moneda();
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda monedaBuscar = new Moneda();
		monedaBuscar.setCodMoneda(codMoneda);
		monedasRequest.setMoneda(monedaBuscar);
		
		try {
			
			monedaEdit = monedaServiceApiRest.buscarMoneda(monedasRequest);
			monedaEdit.setFlagActivo(false);
			monedasRequest.setMoneda(monedaEdit);
			String respuesta = monedaServiceApiRest.actualizar(monedasRequest);
			LOGGER.info(respuesta);
			auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
					"Monedas", "desactivar", "0000", true, respuesta+" Moneda:"+codMoneda, request.getRemoteAddr());
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
		}
		LOGGER.info(MONEDACONTROLLERDESACTIVARF);
		return REDIRECTINDEX;
	}
	
		
	
	
	
	@GetMapping("/searchCodigo")
	public String searchCodigo(@ModelAttribute("monedaSearch") Moneda monedaSearch,
			Model model, RedirectAttributes redirectAttributes, HttpSession httpSession, HttpServletRequest request) {
		LOGGER.info(MONEDACONTROLLERSEARHCODIGOI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda moneda = new Moneda();
		if(!monedaSearch.getCodMoneda().equals("")) {
			moneda.setCodMoneda(monedaSearch.getCodMoneda().toUpperCase());
		}
		monedasRequest.setMoneda(moneda);
		List<Moneda> listMonedas = new ArrayList<>();
		try {
			listMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
			if(!listMonedas.isEmpty()) {
				for (Moneda moneda2 : listMonedas) {
					if(moneda2.getFechaModificacion() != null) {
						String[] arrOfStr = moneda2.getFechaModificacion().split(" ", 2);
						moneda2.setFechaModificacion(arrOfStr[0]);
					}
				}
				if(!monedaSearch.getCodMoneda().equals("")) {
					auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
							"Monedas", "searchCodigo", "0000", true," Moneda:"+monedaSearch.getCodMoneda().toUpperCase(), request.getRemoteAddr());
				}else {
					auditoriaService.save(SecurityContextHolder.getContext().getAuthentication().getName(),
							"Monedas", "searchCodigo", "0000", true," Moneda:", request.getRemoteAddr());
				}
				
				model.addAttribute(LISTAMONEDAS, listMonedas);
			}else {
				
				model.addAttribute(LISTAMONEDAS, listMonedas);
				model.addAttribute(MENSAJE, MENSAJECONSULTANOARROJORESULTADOS);
			}
			
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(LISTAMONEDAS, listMonedas);
			
		}
		LOGGER.info(MONEDACONTROLLERSEARHCODIGOF);
		return URLINDEX;
	}
	
	
	public MonedasRequest getMonedasRequest() {
		MonedasRequest monedasRequest = new MonedasRequest();
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		monedasRequest.setIdUsuario(userName);
		monedasRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		monedasRequest.setCodUsuario(userName);
		monedasRequest.setCanal(canal);
		return monedasRequest;
	}
	
	
	
	
	@ModelAttribute
	public void setGenericos(Model model, HttpServletRequest request) {
		Moneda monedaSearch = new Moneda();
		model.addAttribute("monedaSearch", monedaSearch);
		
		
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = "moneda";
		model.addAttribute("arrUri", arrUriP);
	}
	
	
	
	

	
}
