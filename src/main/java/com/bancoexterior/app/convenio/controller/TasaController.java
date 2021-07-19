package com.bancoexterior.app.convenio.controller;




import java.math.RoundingMode;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.dto.TasaRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.model.Tasa;
import com.bancoexterior.app.convenio.service.IMonedaServiceApiRest;
import com.bancoexterior.app.convenio.service.ITasaServiceApiRest;
import com.bancoexterior.app.util.LibreriaUtil;





@Controller
@RequestMapping("/tasas")
public class TasaController {

	private static final Logger LOGGER = LogManager.getLogger(TasaController.class);
	
	@Autowired
	private ITasaServiceApiRest tasaServiceApiRest; 
	
	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest;
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${${app.ambiente}"+".canal}")
    private String canal;	
	
	@Value("${${app.ambiente}"+".tasa.valorBD}")
    private int valorBD;
	
	private static final String URLINDEX = "convenio/tasa/listaTasas";
	
	private static final String URLNOPERMISO = "error/403";
	
	private static final String URLFORMTASA = "convenio/tasa/formTasa";
	
	private static final String URLFORMTASAEDIT = "convenio/tasa/formTasaEdit";
	
	private static final String LISTATASAS = "listaTasas";
	
	private static final String LISTAMONEDAS = "listaMonedas";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String REDIRECTINDEX = "redirect:/tasas/index";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String NOTIENEPERMISO = "No tiene Permiso";

	private static final String MENSAJECONSULTANOARROJORESULTADOS = "La consulta no arrojo resultado";
	
	private static final String TASACONTROLLERINDEXI = "[==== INICIO Index Tasas Consultas - Controller ====]";
	
	private static final String TASACONTROLLERINDEXF = "[==== FIN Index Tasas Consultas - Controller ====]";
	
	private static final String TASACONTROLLEREDITARI = "[==== INICIO Editar Tasa Consultas - Controller ====]";
	
	private static final String TASACONTROLLEREDITARF = "[==== FIN Editar Tasa Consultas - Controller ====]";
	
	private static final String TASACONTROLLERGUARDARI = "[==== INICIO Guardar Tasa - Controller ====]";
	
	private static final String TASACONTROLLERGUARDARF = "[==== FIN Guardar Tasa - Controller ====]";
	
	private static final String TASACONTROLLERFORMI = "[==== INICIO Form Tasa - Controller ====]";
	
	private static final String TASACONTROLLERFORMF = "[==== FIN Form Tasa - Controller ====]";
	
	private static final String TASACONTROLLERSAVEI = "[==== INICIO Save Tasa - Controller ====]";
	
	private static final String TASACONTROLLERSAVEF = "[==== FIN Save Tasa - Controller ====]";
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(TASACONTROLLERINDEXI);
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		List<Tasa> listaTasas = new ArrayList<>();
		
		TasaRequest tasaRequest = getTasaRequest();
		Tasa tasa = new Tasa();
		tasaRequest.setTasa(tasa);
	
		try {
			listaTasas = tasaServiceApiRest.listaTasas(tasaRequest);
			for (Tasa tasa2 : listaTasas) {
				if(tasa2.getFechaModificacion() != null) {
					String[] arrOfStr = tasa2.getFechaModificacion().split(" ", 2);
					tasa2.setFechaModificacion(arrOfStr[0]);
					tasa2.setMontoTasaCompraString(libreriaUtil.formatNumber(tasa2.getMontoTasaCompra()));
					tasa2.setMontoTasaVentaString(libreriaUtil.formatNumber(tasa2.getMontoTasaVenta()));
				}
			}
			model.addAttribute(LISTATASAS, listaTasas);
			
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(LISTATASAS, listaTasas);
			
			
		}
		LOGGER.info(TASACONTROLLERINDEXF);
		return URLINDEX;
		
	}	
	
	
	
	
	@GetMapping("/edit")
	public String editarWsPrueba(@RequestParam("codMonedaOrigen") String codMonedaOrigen, 
			@RequestParam("codMonedaDestino") String codMonedaDestino, @RequestParam("tipoOperacion") Integer tipoOperacion,
			Tasa tasa, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(TASACONTROLLEREDITARI);
		
		if(!libreriaUtil.isPermisoMenu(httpSession, valorBD)) {
			LOGGER.info(NOTIENEPERMISO);
			return URLNOPERMISO;
		}
		
		Tasa tasaEdit = new Tasa();
		TasaRequest tasaRequest = getTasaRequest();
		Tasa tasaBuscar = new Tasa();
		tasaBuscar.setCodMonedaOrigen(codMonedaOrigen);
		tasaBuscar.setCodMonedaDestino(codMonedaDestino);
		tasaBuscar.setTipoOperacion(tipoOperacion);
		tasaRequest.setTasa(tasaBuscar);
		
		try {
			tasaEdit = tasaServiceApiRest.buscarTasa(tasaRequest);
			if(tasaEdit != null) {
				tasaEdit.setMontoTasaCompra(tasaEdit.getMontoTasaCompra().setScale(2, RoundingMode.HALF_UP));
				tasaEdit.setMontoTasaVenta(tasaEdit.getMontoTasaVenta().setScale(2, RoundingMode.HALF_UP));
				model.addAttribute("tasa", tasaEdit);
				LOGGER.info(TASACONTROLLEREDITARF);
            	return URLFORMTASAEDIT;
			}else {
				redirectAttributes.addFlashAttribute(MENSAJE, MENSAJECONSULTANOARROJORESULTADOS);
				LOGGER.info(TASACONTROLLEREDITARF);
				return REDIRECTINDEX;
			}
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX;
		}
		
		
		
	}
	
	
	@PostMapping("/guardar")
	public String guardarWs(Tasa tasa, BindingResult result, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(TASACONTROLLERGUARDARI);
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
			return URLFORMTASAEDIT;
		}
		
		TasaRequest tasaRequest = getTasaRequest();
		Tasa tasaEdit = new Tasa();
		tasaEdit.setCodMonedaOrigen(tasa.getCodMonedaOrigen());
		tasaEdit.setCodMonedaDestino(tasa.getCodMonedaDestino());
		tasaEdit.setTipoOperacion(tasa.getTipoOperacion());
		tasaEdit.setMontoTasaCompra(tasa.getMontoTasaCompra());
		tasaEdit.setMontoTasaVenta(tasa.getMontoTasaVenta());
		tasaRequest.setTasa(tasaEdit);
		
		try {
			
			String respuesta = tasaServiceApiRest.actualizar(tasaRequest);
			LOGGER.info(respuesta);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			LOGGER.info(TASACONTROLLERGUARDARF);
			return REDIRECTINDEX;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			result.addError(new ObjectError(LISTAERROR, e.getMessage()));
			listaError.add(e.getMessage());
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMTASAEDIT;
		}
		
		
		
	}
	
	@GetMapping("/formTasa")
	public String formTasa(Tasa tasa, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(TASACONTROLLERFORMI);
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
			LOGGER.info(TASACONTROLLERFORMF);
			return URLFORMTASA;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX;
		}
		
		
		
	}	
	
	@PostMapping("/save")
	public String saveWs(Tasa tasa, BindingResult result, Model model, RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(TASACONTROLLERSAVEI);
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
		
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				LOGGER.info("Ocurrio un error: " + error.getDefaultMessage());
				if(error.getCode().equals("typeMismatch")) {
					listaError.add("Los valores de los montos debe ser numerico");
				}
			}
			try {
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				model.addAttribute(LISTAMONEDAS, listaMonedas);
				model.addAttribute(LISTAERROR, listaError);
				return URLFORMTASA;
			} catch (CustomException e) {
				result.addError(new ObjectError(LISTAERROR, e.getMessage()));
				model.addAttribute(LISTAERROR, e.getMessage());
				return URLFORMTASA;
			}
		}
		
		TasaRequest tasaRequest = getTasaRequest();
		tasaRequest.setTasa(tasa);
		
		try {
			String respuesta = tasaServiceApiRest.crear(tasaRequest);
			LOGGER.info(respuesta);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			LOGGER.info(TASACONTROLLERSAVEF);
			return REDIRECTINDEX;
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			try {
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				model.addAttribute(LISTAMONEDAS, listaMonedas);
				result.addError(new ObjectError(LISTAERROR, e.getMessage()));
				listaError.add(e.getMessage());
				model.addAttribute(LISTAERROR, listaError);
				return URLFORMTASA;
			} catch (CustomException e1) {
				LOGGER.error("error: "+e1);
				result.addError(new ObjectError(LISTAERROR,e1.getMessage()));
				listaError.add(e1.getMessage());
				model.addAttribute(LISTAERROR, listaError);
				return URLFORMTASA;
			}
			
		}
		
		
	}	
	
	public TasaRequest getTasaRequest() {
		TasaRequest tasaRequest = new TasaRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		tasaRequest.setIdUsuario(userName);
		tasaRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		tasaRequest.setCodUsuario(userName);
		tasaRequest.setCanal(canal);
		return tasaRequest;
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
		
		String[] arrUriP = new String[2]; 
		arrUriP[0] = "Home";
		arrUriP[1] = "tasa";
		model.addAttribute("arrUri", arrUriP);
	}
	
	
	
	
}
