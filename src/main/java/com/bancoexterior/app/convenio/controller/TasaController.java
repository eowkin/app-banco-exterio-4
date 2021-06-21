package com.bancoexterior.app.convenio.controller;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.bancoexterior.app.convenio.dto.TasaRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.model.Tasa;
import com.bancoexterior.app.convenio.service.IMonedaServiceApiRest;
import com.bancoexterior.app.convenio.service.ITasaServiceApiRest;
import com.bancoexterior.app.util.LibreriaUtil;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/tasas")
public class TasaController {

	@Autowired
	private ITasaServiceApiRest tasaServiceApiRest; 
	
	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest;
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${des.canal}")
    private String canal;	
	
	
	private static final String URLINDEX = "convenio/tasa/listaTasas";
	
	private static final String URLFORMTASA = "convenio/tasa/formTasa";
	
	private static final String URLFORMTASAEDIT = "convenio/tasa/formTasaEdit";
	
	private static final String LISTATASAS = "listaTasas";
	
	private static final String LISTAMONEDAS = "listaMonedas";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String REDIRECTINDEX = "redirect:/tasas/index";
	
	private static final String MENSAJE = "mensaje";

	private static final String MENSAJENORESULTADO = "Operacion Exitosa.La consulta no arrojo resultado.";
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index listaTasasWs");
		
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
				}
			}
			model.addAttribute(LISTATASAS, listaTasas);
			
		} catch (CustomException e) {
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(LISTATASAS, listaTasas);
			
			
		}
		
		return URLINDEX;
		
	}	
	
	@GetMapping("/edit/{codMonedaOrigen}/{codMonedaDestino}/{tipoOperacion}")
	public String editarWs(@PathVariable("codMonedaOrigen") String codMonedaOrigen, 
			@PathVariable("codMonedaDestino") String codMonedaDestino, @PathVariable("tipoOperacion") Integer tipoOperacion,
			Tasa tasa, Model model, RedirectAttributes redirectAttributes) {
		log.info("editarWs");
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
				model.addAttribute("tasa", tasaEdit);
            	return URLFORMTASAEDIT;
			}else {
				redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				return REDIRECTINDEX;
			}
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX;
		}
		
		
		
	}
	
	
	@PostMapping("/guardar")
	public String guardarWs(Tasa tasa, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		log.info("guardarWs");
		log.info("tasa: "+tasa);
		List<String> listaError = new ArrayList<>();
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
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
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			return REDIRECTINDEX;
		} catch (CustomException e) {
			result.addError(new ObjectError(LISTAERROR, e.getMessage()));
			listaError.add(e.getMessage());
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMTASAEDIT;
		}
		
		
		
	}
	
	@GetMapping("/formTasa")
	public String formTasa(Tasa tasa, Model model, RedirectAttributes redirectAttributes) {
		
		List<Moneda> listaMonedas = new ArrayList<>();
		
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		try {
			listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
			model.addAttribute(LISTAMONEDAS, listaMonedas);
			return URLFORMTASA;
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX;
		}
		
		
		
	}	
	
	@PostMapping("/save")
	public String saveWs(Tasa tasa, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		log.info("save");
		log.info("tasa: "+tasa);
		List<String> listaError = new ArrayList<>();
		List<Moneda> listaMonedas;
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
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
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			return REDIRECTINDEX;
		} catch (CustomException e) {
			try {
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				model.addAttribute(LISTAMONEDAS, listaMonedas);
				result.addError(new ObjectError(LISTAERROR, e.getMessage()));
				listaError.add(e.getMessage());
				model.addAttribute(LISTAERROR, listaError);
				return URLFORMTASA;
			} catch (CustomException e1) {
				log.error("error: "+e1);
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
		String uri = request.getRequestURI();
		log.info("uri: "+uri);
		String[] arrUri = uri.split("/");

		arrUri[0] = "Home";
		model.addAttribute("arrUri", arrUri);
	}
	
	
	
	
}
