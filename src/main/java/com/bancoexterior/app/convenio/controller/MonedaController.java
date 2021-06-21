package com.bancoexterior.app.convenio.controller;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/monedas")
public class MonedaController {

	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest;
	
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${des.canal}")
    private String canal;	

	private static final String URLINDEX = "convenio/moneda/listaMonedas";
	
	private static final String URLFORMMONEDA = "convenio/moneda/formMoneda";
	
	private static final String URLFORMMONEDAEDIT = "convenio/moneda/formMonedaEdit";
	
	private static final String LISTAMONEDAS = "listMonedas";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String REDIRECTINDEX = "redirect:/monedas/index";
	
	private static final String MENSAJE = "mensaje";
	
	@GetMapping("/index")
	public String indexWs(Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index listaMonedasWs");
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
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(LISTAMONEDAS, listMonedas);
			
		}
		
		return URLINDEX; 
	}
	
	@GetMapping("/activar/{codMoneda}")
	public String activarWs(@PathVariable("codMoneda") String codMoneda, Moneda moneda, Model model, RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		log.info(codMoneda);
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
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
		}
		return REDIRECTINDEX;
	}	
	
	@GetMapping("/desactivar/{codMoneda}")
	public String desactivarWs(@PathVariable("codMoneda") String codMoneda, Moneda moneda, Model model, RedirectAttributes redirectAttributes) {
		log.info("desactivarWs");
		log.info(codMoneda);
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
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
		}
		return REDIRECTINDEX;
	}
	
	@GetMapping("/edit/{codMoneda}")
	public String editarWs(@PathVariable("codMoneda") String codMoneda, Moneda moneda, Model model, RedirectAttributes redirectAttributes) {
		log.info("editarWs");
		log.info(codMoneda);
		Moneda monedaEdit = new Moneda();
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda monedaBuscar = new Moneda();
		monedaBuscar.setCodMoneda(codMoneda);
		monedasRequest.setMoneda(monedaBuscar);
		try {
			monedaEdit = monedaServiceApiRest.buscarMoneda(monedasRequest);
			if(monedaEdit != null) {
				model.addAttribute("moneda", monedaEdit);
        		return URLFORMMONEDAEDIT;
			}else {
				redirectAttributes.addFlashAttribute(MENSAJEERROR, "Operacion Exitosa.La consulta no arrojo resultado.");
				return REDIRECTINDEX;
			}
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX;
		}
	
	}	
	
	@PostMapping("/guardar")
	public String guardarWs(Moneda moneda, BindingResult result,  RedirectAttributes redirectAttributes) {
		
		log.info("guardar");
		log.info("moneda: "+moneda);
			
		MonedasRequest monedasRequest = getMonedasRequest();
		monedasRequest.setMoneda(moneda);
		
		
		try {
			String respuesta = monedaServiceApiRest.actualizar(monedasRequest);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			return REDIRECTINDEX;
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return URLFORMMONEDAEDIT;
		}
		
		
		
	}
	
	@PostMapping("/save")
	public String saveWs(@Valid  Moneda moneda, BindingResult result, RedirectAttributes redirectAttributes) {
		
		log.info("saveWs");
		log.info("moneda: "+moneda);
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
			}
		
			return URLFORMMONEDA;
		}
		
		MonedasRequest monedasRequest = getMonedasRequest();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		
		try {
			String respuesta = monedaServiceApiRest.crear(monedasRequest);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			return REDIRECTINDEX;
		} catch (CustomException e) {
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
		log.info("si me llamo a searchCodigo");
		
		
		
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
				model.addAttribute(MENSAJEERROR, "Operacion Exitosa.La consulta no arrojo resultado.");
			}
			
			
		} catch (CustomException e) {
			log.error("error: "+e);
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(LISTAMONEDAS, listMonedas);
			
		}
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
		String uri = request.getRequestURI();
		log.info("uri: "+uri);
		String[] arrUri = uri.split("/");

		arrUri[0] = "Home";
		model.addAttribute("arrUri", arrUri);
		
		
		
	}
	
	
	
	

	
}
