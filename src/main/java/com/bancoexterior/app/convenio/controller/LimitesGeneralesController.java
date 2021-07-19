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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.convenio.dto.LimiteRequest;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.LimitesGenerales;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.service.ILimitesGeneralesServiceApirest;
import com.bancoexterior.app.convenio.service.IMonedaServiceApiRest;
import com.bancoexterior.app.util.LibreriaUtil;




@Controller
@RequestMapping("/limitesGenerales")
public class LimitesGeneralesController {

	private static final Logger LOGGER = LogManager.getLogger(LimitesGeneralesController.class);
	
	@Autowired
	private ILimitesGeneralesServiceApirest limitesGeneralesServiceApirest;
	
	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest;
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${${app.ambiente}"+".canal}")
    private String canal;
	
	@Value("${${app.ambiente}"+".limitesGenerales.valorBD}")
    private int valorBD;
	
	private static final String URLINDEX = "convenio/limitesGenerales/listaLimitesGenerales";
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String URLFORMLIMITESGENERALES = "convenio/limitesGenerales/formLimitesGenerales";
	
	private static final String URLFORMLIMITESGENERALESEDIT = "convenio/limitesGenerales/formLimitesGeneralesEdit";
	
	private static final String URLFORMLIMITESGENERALESDETALLE = "convenio/limitesGenerales/formLimitesGeneralesDetalle";
	
	private static final String LISTALIMITESGENERALES = "listaLimitesGenerales";
	
	private static final String LIMITESGENERALES = "limitesGenerales";
	
	private static final String LISTAMONEDAS = "listaMonedas";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String REDIRECTINDEX = "redirect:/limitesGenerales/index";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";
	
	private static final String MENSAJENORESULTADO = "La consulta no arrojo resultado.";
	
	private static final String LIMITESGENERALESCONTROLLERINDEXI = "[==== INICIO Index LimitesGenerales Consultas - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLERINDEXF = "[==== FIN Index LimitesGenerales Consultas - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLERACTIVARI = "[==== INICIO Activar LimitesGenerales - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLERACTIVARF = "[==== FIN Activar LimitesGenerales - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLERDESACTIVARI = "[==== INICIO Desactivar LimitesGenerales - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLERDESACTIVARF = "[==== FIN Desactivar LimitesGenerales - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLERDETALLEI = "[==== INICIO Detalle LimitesGenerales Consultas - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLERDETALLEF = "[==== FIN Detalle LimitesGenerales Consultas - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLEREDITARI = "[==== INICIO Editar LimitesGenerales Consultas - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLEREDITARF = "[==== FIN Editar LimitesGenerales Consultas - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLERGUARDARI = "[==== INICIO Guardar LimitesGenerales - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLERGUARDARF = "[==== FIN Guardar LimitesGenerales - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLERFORMI = "[==== INICIO Form LimitesGenerales - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLERFORMF = "[==== FIN Form LimitesGenerales - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLERSAVEI = "[==== INICIO Save LimitesGenerales - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLERSAVEF = "[==== FIN Save LimitesGenerales - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLERSEARCHI = "[==== INICIO Search LimitesGenerales Consultas - Controller ====]";
	
	private static final String LIMITESGENERALESCONTROLLERSEARCHF = "[==== FIN Search LimitesGenerales Consultas - Controller ====]";
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(LIMITESGENERALESCONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		LimiteRequest limiteRequest = getLimiteRequest(); 
		LimitesGenerales limite = new LimitesGenerales();
		limiteRequest.setLimite(limite);
		List<LimitesGenerales> listaLimitesGenerales = new ArrayList<>();
		
		try {
			listaLimitesGenerales = limitesGeneralesServiceApirest.listaLimitesGenerales(limiteRequest);
			for (LimitesGenerales limitesGenerales : listaLimitesGenerales) {
				if(limitesGenerales.getFechaModificacion() != null) {
					String[] arrOfStr = limitesGenerales.getFechaModificacion().split(" ", 2);
					limitesGenerales.setFechaModificacion(arrOfStr[0]);
				}
			}
			model.addAttribute(LISTALIMITESGENERALES, listaLimitesGenerales);
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(LISTALIMITESGENERALES, listaLimitesGenerales);
		}
		LOGGER.info(LIMITESGENERALESCONTROLLERINDEXF);
		return URLINDEX;
	}
	
	@GetMapping("/activar/{codMoneda}/{tipoTransaccion}/{tipoCliente}")
	public String activarWs(@PathVariable("codMoneda") String codMoneda, @PathVariable("tipoTransaccion") String tipoTransaccion,
			@PathVariable("tipoCliente") String tipoCliente, LimitesGenerales limitesGenerales ,Model model, 
			RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(LIMITESGENERALESCONTROLLERACTIVARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		LimitesGenerales limitesGeneralesEdit = new LimitesGenerales();
		LimiteRequest limiteRequest = getLimiteRequest();
		LimitesGenerales limite = new LimitesGenerales();
		limite.setCodMoneda(codMoneda);
		limite.setTipoTransaccion(tipoTransaccion);
		limite.setTipoCliente(tipoCliente);
		limiteRequest.setLimite(limite);
		
		try {
			limitesGeneralesEdit = limitesGeneralesServiceApirest.buscarLimitesGenerales(limiteRequest);
			limitesGeneralesEdit.setFlagActivo(true);
			limiteRequest.setLimite(limitesGeneralesEdit);
			String respuesta = limitesGeneralesServiceApirest.actualizar(limiteRequest);
			LOGGER.info(respuesta);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
		}
		LOGGER.info(LIMITESGENERALESCONTROLLERACTIVARF);
		return REDIRECTINDEX;
	}	
	
	@GetMapping("/desactivar/{codMoneda}/{tipoTransaccion}/{tipoCliente}")
	public String desactivarWs(@PathVariable("codMoneda") String codMoneda, @PathVariable("tipoTransaccion") String tipoTransaccion,
			@PathVariable("tipoCliente") String tipoCliente, LimitesGenerales limitesGenerales ,Model model,
			RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(LIMITESGENERALESCONTROLLERDESACTIVARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		LimitesGenerales limitesGeneralesEdit = new LimitesGenerales();
		LimiteRequest limiteRequest = getLimiteRequest(); 
		LimitesGenerales limite = new LimitesGenerales();
		limite.setCodMoneda(codMoneda);
		limite.setTipoTransaccion(tipoTransaccion);
		limite.setTipoCliente(tipoCliente);
		limiteRequest.setLimite(limite);
		
		try {
			limitesGeneralesEdit = limitesGeneralesServiceApirest.buscarLimitesGenerales(limiteRequest);
			limitesGeneralesEdit.setFlagActivo(false);
			limiteRequest.setLimite(limitesGeneralesEdit);
			String respuesta = limitesGeneralesServiceApirest.actualizar(limiteRequest);
			LOGGER.info(respuesta);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
		}
		LOGGER.info(LIMITESGENERALESCONTROLLERDESACTIVARF);
		return REDIRECTINDEX;
	}
	
	
	@GetMapping("/detalle")
	public String detalleWs1(@RequestParam("codMoneda") String codMoneda, @RequestParam("tipoTransaccion") String tipoTransaccion,
			@RequestParam("tipoCliente") String tipoCliente, LimitesGenerales limitesGenerales ,Model model, 
			RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(LIMITESGENERALESCONTROLLERDETALLEI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		LimitesGenerales limitesGeneralesEdit = new LimitesGenerales();
		LimiteRequest limiteRequest = getLimiteRequest(); 
		LimitesGenerales limite = new LimitesGenerales();
		limite.setCodMoneda(codMoneda);
		limite.setTipoTransaccion(tipoTransaccion);
		limite.setTipoCliente(tipoCliente);
		limiteRequest.setLimite(limite);
		
		try {
			limitesGeneralesEdit = limitesGeneralesServiceApirest.buscarLimitesGenerales(limiteRequest);
			if(limitesGeneralesEdit != null) {
				limitesGeneralesEdit.setMontoMinString(libreriaUtil.formatNumber(limitesGeneralesEdit.getMontoMin()));
				limitesGeneralesEdit.setMontoMaxString(libreriaUtil.formatNumber(limitesGeneralesEdit.getMontoMax()));
				limitesGeneralesEdit.setMontoTopeString(libreriaUtil.formatNumber(limitesGeneralesEdit.getMontoTope()));
				limitesGeneralesEdit.setMontoMensualString(libreriaUtil.formatNumber(limitesGeneralesEdit.getMontoMensual()));
				limitesGeneralesEdit.setMontoDiarioString(libreriaUtil.formatNumber(limitesGeneralesEdit.getMontoDiario()));
				limitesGeneralesEdit.setMontoBancoString(libreriaUtil.formatNumber(limitesGeneralesEdit.getMontoBanco()));
				
				model.addAttribute(LIMITESGENERALES, limitesGeneralesEdit);
				LOGGER.info(LIMITESGENERALESCONTROLLERDETALLEF);
            	return URLFORMLIMITESGENERALESDETALLE;
			}else {
				redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				return REDIRECTINDEX;
			}
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX;
		}
		
	
	}
	
	
	
	
	@GetMapping("/edit/{codMoneda}/{tipoTransaccion}/{tipoCliente}")
	public String editarWs(@PathVariable("codMoneda") String codMoneda, @PathVariable("tipoTransaccion") String tipoTransaccion,
			@PathVariable("tipoCliente") String tipoCliente, LimitesGenerales limitesGenerales ,Model model,
			RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(LIMITESGENERALESCONTROLLEREDITARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		LimitesGenerales limitesGeneralesEdit = new LimitesGenerales();
		LimiteRequest limiteRequest = getLimiteRequest(); 
		LimitesGenerales limite = new LimitesGenerales();
		limite.setCodMoneda(codMoneda);
		limite.setTipoTransaccion(tipoTransaccion);
		limite.setTipoCliente(tipoCliente);
		limiteRequest.setLimite(limite);
		
		try {
			limitesGeneralesEdit = limitesGeneralesServiceApirest.buscarLimitesGenerales(limiteRequest);
			if(limitesGeneralesEdit != null) {
				model.addAttribute(LIMITESGENERALES, limitesGeneralesEdit);
				LOGGER.info(LIMITESGENERALESCONTROLLEREDITARF);
				return URLFORMLIMITESGENERALESEDIT;
			}else {
				redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				return REDIRECTINDEX;
			}
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX;
		}
	}	
	
	@PostMapping("/guardar")
	public String guardarWs(LimitesGenerales limitesGenerales, BindingResult result,
			RedirectAttributes redirectAttributes, Model model, HttpSession httpSession) {
		LOGGER.info(LIMITESGENERALESCONTROLLERGUARDARI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<String> listaError = new ArrayList<>();
		
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				LOGGER.info("Ocurrio un error: " + error.getDefaultMessage());
				if(error.getCode().equals("typeMismatch")) {
					listaError.add("Los valores de los montos debe ser numerico");
				}
			}
			
			
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMLIMITESGENERALESEDIT;
		}
		 
		  if(limitesGenerales.getMontoMax().compareTo(limitesGenerales.getMontoMin()) < 0) { 
			  listaError.add("El monto mínimo no debe ser mayor al monto máximo");
			  model.addAttribute(LISTAERROR, listaError);
			  result.addError(new  ObjectError(LISTAERROR, " El monto mínimo no debe ser mayor al monto máximo"));
			  
			  return URLFORMLIMITESGENERALESEDIT; 
		  }
		 
 
		  if(limitesGenerales.getMontoMensual().compareTo(limitesGenerales.getMontoDiario()) < 0) { 
			  result.addError(new  ObjectError(LISTAERROR, " El monto diario no debe ser mayor al mensual"));
			  listaError.add("El monto diario no debe ser mayor al mensual");
			  model.addAttribute(LISTAERROR, listaError);
			  return URLFORMLIMITESGENERALESEDIT; 
		  }
		
		LimiteRequest limiteRequest = getLimiteRequest(); 
		limiteRequest.setLimite(limitesGenerales);
		
		try {
			
			String respuesta = limitesGeneralesServiceApirest.actualizar(limiteRequest);
			LOGGER.info(respuesta);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			LOGGER.info(LIMITESGENERALESCONTROLLERGUARDARF);
			return REDIRECTINDEX;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			result.addError(new ObjectError(LISTAERROR, e.getMessage()));
			listaError.add(e.getMessage());
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMLIMITESGENERALESEDIT;
		}
		
	
	}
	
	
	@GetMapping("/formLimitesGenerales")
	public String formLimitesGenerales(LimitesGenerales limitesGenerales,  Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		
		LOGGER.info(LIMITESGENERALESCONTROLLERFORMI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		List<Moneda> listaMonedas = new ArrayList<>();
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		try {
			listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
			model.addAttribute(LISTAMONEDAS, listaMonedas);
			LOGGER.info(LIMITESGENERALESCONTROLLERFORMF);
    		return URLFORMLIMITESGENERALES;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX;
		}
	}
	
	@PostMapping("/save")
	public String saveWs(LimitesGenerales limitesGenerales, BindingResult result, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(LIMITESGENERALESCONTROLLERSAVEI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<String> listaError = new ArrayList<>();
		List<Moneda> listaMonedas;
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		try {
			if (result.hasErrors()) {
				for (ObjectError error : result.getAllErrors()) {
					LOGGER.info("Ocurrio un error: " + error.getDefaultMessage());
					if(error.getCode().equals("typeMismatch")) {
						listaError.add("Los valores de los montos debe ser numerico");
					}
				}
				
				
					listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
					model.addAttribute(LISTAMONEDAS, listaMonedas);
					model.addAttribute(LISTAERROR, listaError);
					return URLFORMLIMITESGENERALES;
				
				
			}
			
	 
			  if(limitesGenerales.getMontoMax().compareTo(limitesGenerales.getMontoMin()) < 0) { 
				  
					  	listaError.add("El monto mínimo no debe ser mayor al monto máximo");
						listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
						model.addAttribute(LISTAMONEDAS, listaMonedas);
						model.addAttribute(LISTAERROR, listaError);
						result.addError(new  ObjectError(LISTAERROR, " El monto mínimo no debe ser mayor al monto máximo"));
						return URLFORMLIMITESGENERALES;
					
			  }
			  
			   
			  if(limitesGenerales.getMontoMensual().compareTo(limitesGenerales.getMontoDiario()) < 0) { 
				 
						listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
						model.addAttribute(LISTAMONEDAS, listaMonedas);
						listaError.add("El monto diario no debe ser mayor al mensual");
				        model.addAttribute(LISTAERROR, listaError);
				        result.addError(new  ObjectError(LISTAERROR, " El monto diario no debe ser mayor al mensual"));
				        return URLFORMLIMITESGENERALES;
					
			  }
			
			LimiteRequest limiteRequest = getLimiteRequest(); 
			limitesGenerales.setFlagActivo(true);
			limiteRequest.setLimite(limitesGenerales);
			
		
			String respuesta = limitesGeneralesServiceApirest.crear(limiteRequest);
			LOGGER.info(respuesta);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			LOGGER.info(LIMITESGENERALESCONTROLLERSAVEF);
			return REDIRECTINDEX;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			try {
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				model.addAttribute(LISTAMONEDAS, listaMonedas);
				result.addError(new ObjectError(LISTAERROR, " Codigo :" +e.getMessage()));
				listaError.add(e.getMessage());
				model.addAttribute(LISTAERROR, listaError);
				return URLFORMLIMITESGENERALES;
			} catch (CustomException e1) {
				LOGGER.error("error: "+e1);
				result.addError(new ObjectError(LISTAERROR, " Codigo :" +e1.getMessage()));
				listaError.add(e1.getMessage());
				model.addAttribute(LISTAERROR, listaError);
				return URLFORMLIMITESGENERALES;
			}
			
		}
	}
	
	@GetMapping("/search")
	public String search(@ModelAttribute("limitesGeneralesSearch") LimitesGenerales limitesGeneralesSearch, 
			Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(LIMITESGENERALESCONTROLLERSEARCHI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<LimitesGenerales> listaLimitesGenerales = new ArrayList<>();
		
		LimiteRequest limiteRequest = getLimiteRequest(); 
		LimitesGenerales limite = new LimitesGenerales();
		if(!limitesGeneralesSearch.getCodMoneda().equals(""))
			limite.setCodMoneda(limitesGeneralesSearch.getCodMoneda().toUpperCase());
		limiteRequest.setLimite(limite);
		
		try {
			listaLimitesGenerales = limitesGeneralesServiceApirest.listaLimitesGenerales(limiteRequest);
			if(!listaLimitesGenerales.isEmpty()) {
				for (LimitesGenerales limitesGenerales : listaLimitesGenerales) {
					if(limitesGenerales.getFechaModificacion() != null) {
						String[] arrOfStr = limitesGenerales.getFechaModificacion().split(" ", 2);
						limitesGenerales.setFechaModificacion(arrOfStr[0]);
					}
				}
				model.addAttribute(LISTALIMITESGENERALES, listaLimitesGenerales);
	    		
			}else {
				model.addAttribute(LISTALIMITESGENERALES, listaLimitesGenerales);
				model.addAttribute(MENSAJE, MENSAJENORESULTADO);
				
			}
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(LISTALIMITESGENERALES, listaLimitesGenerales);
			model.addAttribute(MENSAJEERROR, e.getMessage());
			
		}
		
		LOGGER.info(LIMITESGENERALESCONTROLLERSEARCHF);
		return URLINDEX;
	}
	
	
	public LimiteRequest getLimiteRequest() {
		LimiteRequest limiteRequest = new LimiteRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		limiteRequest.setIdUsuario(userName);
		limiteRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		limiteRequest.setCodUsuario(userName);
		limiteRequest.setCanal(canal);
		
		return limiteRequest;
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
		LimitesGenerales limitesGeneralesSearch = new LimitesGenerales();
		model.addAttribute("limitesGeneralesSearch", limitesGeneralesSearch);
		
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = LIMITESGENERALES;
		model.addAttribute("arrUri", arrUriP);
	}
		
	
	
}
