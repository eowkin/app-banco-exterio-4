package com.bancoexterior.app.convenio.controller;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.service.IMonedaServiceApiRest;
import com.bancoexterior.app.util.LibreriaUtil;





@Controller
@RequestMapping("/monedas")
public class MonedaController {

	private static final Logger LOGGER = LogManager.getLogger(MonedaController.class);
	
	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest;
	
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${${app.ambiente}"+".canal}")
    private String canal;	

	private static final String URLINDEX = "convenio/moneda/listaMonedas";
	
	private static final String URLFORMMONEDA = "convenio/moneda/formMoneda";
	
	private static final String URLFORMMONEDAEDIT = "convenio/moneda/formMonedaEdit";
	
	private static final String LISTAMONEDAS = "listMonedas";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String REDIRECTINDEX = "redirect:/monedas/index";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String MENSAJECONSULTANOARROJORESULTADOS = "La consulta no arrojo resultado";
	
	private static final String MONEDACONTROLLERINDEXI = "[==== INICIO Index Monedas Consultas - Controller ====]";
	
	private static final String MONEDACONTROLLERINDEXF = "[==== FIN Index Monedas Consultas - Controller ====]";
	
	private static final String MONEDACONTROLLERACTIVARI = "[==== INICIO Activar Moneda - Controller ====]";
	
	private static final String MONEDACONTROLLERACTIVARF = "[==== FIN Activar Moneda - Controller ====]";
	
	private static final String MONEDACONTROLLERDESACTIVARI = "[==== INICIO Desactivar Moneda - Controller ====]";
	
	private static final String MONEDACONTROLLERDESACTIVARF = "[==== FIN Desactivar Moneda - Controller ====]";
	
	private static final String MONEDACONTROLLEREDITARI = "[==== INICIO Editar Moneda Consulta - Controller ====]";
	
	private static final String MONEDACONTROLLEREDITARF = "[==== FIN Editar Moneda Consulta - Controller ====]";
	
	private static final String MONEDACONTROLLERGUARDARI = "[==== INICIO Guardar Moneda - Controller ====]";
	
	private static final String MONEDACONTROLLERGUARDARF = "[==== FIN Guardar Moneda - Controller ====]";
	
	private static final String MONEDACONTROLLERSAVEI = "[==== INICIO Save Moneda - Controller ====]";
	
	private static final String MONEDACONTROLLERSAVEF = "[==== FIN Save Moneda - Controller ====]";
	
	private static final String MONEDACONTROLLERSEARHCODIGOI = "[==== INICIO SearchCodigo Moneda - Controller ====]";
	
	private static final String MONEDACONTROLLERSEARHCODIGOF = "[==== FIN SearchCodigo Moneda - Controller ====]";
	
	private static final String INDEX = "/index";
	
	@GetMapping(INDEX)
	public String indexWs(Model model, RedirectAttributes redirectAttributes) {
		LOGGER.info(MONEDACONTROLLERINDEXI);
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
	public String activarWs(@PathVariable("codMoneda") String codMoneda, Moneda moneda, Model model, RedirectAttributes redirectAttributes) {
		LOGGER.info(MONEDACONTROLLERACTIVARI);
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
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
		}
		LOGGER.info(MONEDACONTROLLERACTIVARF);
		return REDIRECTINDEX;
	}	
	
	@GetMapping("/desactivar/{codMoneda}")
	public String desactivarWs(@PathVariable("codMoneda") String codMoneda, Moneda moneda, Model model, RedirectAttributes redirectAttributes) {
		LOGGER.info(MONEDACONTROLLERDESACTIVARI);
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
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
		}
		LOGGER.info(MONEDACONTROLLERDESACTIVARF);
		return REDIRECTINDEX;
	}
	
	@GetMapping("/edit/{codMoneda}")
	public String editarWs(@PathVariable("codMoneda") String codMoneda, Moneda moneda, Model model, RedirectAttributes redirectAttributes) {
		LOGGER.info(MONEDACONTROLLEREDITARI);
		Moneda monedaEdit = new Moneda();
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda monedaBuscar = new Moneda();
		monedaBuscar.setCodMoneda(codMoneda);
		monedasRequest.setMoneda(monedaBuscar);
		try {
			monedaEdit = monedaServiceApiRest.buscarMoneda(monedasRequest);
			if(monedaEdit != null) {
				model.addAttribute("moneda", monedaEdit);
				LOGGER.info(MONEDACONTROLLEREDITARF);
        		return URLFORMMONEDAEDIT;
			}else {
				redirectAttributes.addFlashAttribute(MENSAJE, MENSAJECONSULTANOARROJORESULTADOS);
				LOGGER.info(MONEDACONTROLLEREDITARF);
				return REDIRECTINDEX;
			}
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX;
		}
	
	}	
	
	@PostMapping("/guardar")
	public String guardarWs(Moneda moneda, BindingResult result,  RedirectAttributes redirectAttributes) {
		
		LOGGER.info(MONEDACONTROLLERGUARDARI);	
		MonedasRequest monedasRequest = getMonedasRequest();
		monedasRequest.setMoneda(moneda);
		
		
		try {
			String respuesta = monedaServiceApiRest.actualizar(monedasRequest);
			LOGGER.info(respuesta);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			LOGGER.info(MONEDACONTROLLERGUARDARF);
			return REDIRECTINDEX;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return URLFORMMONEDAEDIT;
		}
		
		
		
	}
	
	@PostMapping("/save")
	public String saveWs(@Valid  Moneda moneda, BindingResult result, RedirectAttributes redirectAttributes) {
		
		LOGGER.info(MONEDACONTROLLERSAVEI);
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				LOGGER.info("Ocurrio un error: " + error.getDefaultMessage());
			}
		
			return URLFORMMONEDA;
		}
		
		MonedasRequest monedasRequest = getMonedasRequest();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		
		try {
			String respuesta = monedaServiceApiRest.crear(monedasRequest);
			LOGGER.info(respuesta);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			LOGGER.info(MONEDACONTROLLERSAVEF);
			return REDIRECTINDEX;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			result.addError(new ObjectError("codMoneda", e.getMessage()));
			return URLFORMMONEDA;
		}
		
	}
	
	@GetMapping("/formMoneda")
	public String formMoneda(Moneda moneda, Model model) {
		
		return URLFORMMONEDA;
	}	
	
	
	
	@GetMapping("/searchCodigo")
	public String searchCodigo(@ModelAttribute("monedaSearch") Moneda monedaSearch,
			Model model, RedirectAttributes redirectAttributes) {
		LOGGER.info(MONEDACONTROLLERSEARHCODIGOI);
		
		
		
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
