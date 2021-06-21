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

import com.bancoexterior.app.convenio.dto.AgenciaRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Agencia;
import com.bancoexterior.app.convenio.service.IAgenciaServiceApiRest;
import com.bancoexterior.app.util.LibreriaUtil;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/agencias")
public class AgenciaController {

	@Autowired 
	private IAgenciaServiceApiRest agenciaServiceApiRest; 
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${des.canal}")
    private String canal;	
	
	private static final String URLINDEX = "convenio/agencia/listaAgencias";
	
	private static final String URLFORMAGENCIA = "convenio/agencia/formAgencia";
	
	private static final String URLFORMAGENCIABUSCAR = "convenio/agencia/formBuscarAgencia";
	
	private static final String URLFORMAGENCIAEDIT = "convenio/agencia/formAgenciaEdit";
	
	private static final String LISTAAGENCIAS = "listaAgencias";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String REDIRECTINDEX = "redirect:/agencias/index";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String MENSAJENORESULTADO = "Operacion Exitosa.La consulta no arrojo resultado.";
	
	@GetMapping("/index")
	public String index(Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index agenciasWs");
		
		List<Agencia> listaAgencias = new ArrayList<>();
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		Agencia agencia = new Agencia();
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		try {
			listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest);
			
			for (Agencia agencia2 : listaAgencias) {
				if(agencia2.getFechaModificacion() != null) {
					String[] arrOfStr = agencia2.getFechaModificacion().split(" ", 2);
					agencia2.setFechaModificacion(arrOfStr[0]);
				}
			}
			
			model.addAttribute(LISTAAGENCIAS, listaAgencias);
		} catch (CustomException e) {
			model.addAttribute(MENSAJEERROR, e.getMessage());
		}
		
		return URLINDEX;
	}	
	
	@GetMapping("/activar/{codAgencia}")
	public String activarWs(@PathVariable("codAgencia") String codAgencia, Model model,
			RedirectAttributes redirectAttributes) {
		log.info("activarWs");
	
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		Agencia agencia = new Agencia();
		agencia.setCodAgencia(codAgencia);
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		try {
			agenciaEdit = agenciaServiceApiRest.buscarAgencia(agenciaRequest);
			agenciaEdit.setFlagActivo(true);
			agenciaRequest.setAgencia(agenciaEdit);
			String respuesta = agenciaServiceApiRest.actualizar(agenciaRequest);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
		}
		
		return REDIRECTINDEX;
	}	
	
	
	@GetMapping("/desactivar/{codAgencia}")
	public String desactivarWs(@PathVariable("codAgencia") String codAgencia, Model model,
			RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		Agencia agencia = new Agencia();
		agencia.setCodAgencia(codAgencia);
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		try {
			agenciaEdit = agenciaServiceApiRest.buscarAgencia(agenciaRequest);
			agenciaEdit.setFlagActivo(false);
			agenciaRequest.setAgencia(agenciaEdit);
			String respuesta = agenciaServiceApiRest.actualizar(agenciaRequest);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
		}
		
		return REDIRECTINDEX;
	}
	
	
	@GetMapping("/edit/{codAgencia}")
	public String editarWs(@PathVariable("codAgencia") String codAgencia, 
			Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("editarWs");
		
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(true);
		agenciaBuscar.setCodAgencia(agencia.getCodAgencia());
		agenciaRequest.setAgencia(agenciaBuscar);
		
		try {
			agenciaEdit = agenciaServiceApiRest.buscarAgencia(agenciaRequest);
			if(agenciaEdit != null) {
				model.addAttribute("agencia", agenciaEdit);
				return URLFORMAGENCIAEDIT;
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
	public String guardarWs(Agencia agencia, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		log.info("guardarWs");
		log.info("agencia: "+agencia);
		
		
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		agencia.setFlagActivo(true);
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
	
		
		try {
			
			String respuesta = agenciaServiceApiRest.actualizar(agenciaRequest);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			return REDIRECTINDEX;
		} catch (CustomException e) {
			result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
			return URLFORMAGENCIAEDIT;
		}
		
	}
	
	
	@GetMapping("/formBuscarAgencia")
	public String fromBuscarAgencia(Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("fromBuscarAgencia");
		
		List<Agencia> listaAgencias = new ArrayList<>();
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(false);
		agenciaRequest.setAgencia(agenciaBuscar);
		
		try {
			listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest);
			model.addAttribute(LISTAAGENCIAS, listaAgencias);
			return URLFORMAGENCIABUSCAR;
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX;
		}
		
	}
	
	@GetMapping("/formAgencia")
	public String fromAgencia(Agencia agencia,  Model model) {
		
		return URLFORMAGENCIA;
	}
	
	@GetMapping("/searchCrear")
	public String searchCrear(Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a searchCrear clientesPersonalizadosWs");
		log.info(agencia.getCodAgencia());
		
		List<Agencia> listaAgencias = new ArrayList<>();
		Agencia agenciaEdit = new Agencia(); 
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(false);
		agenciaBuscar.setCodAgencia(agencia.getCodAgencia());
		agenciaRequest.setAgencia(agenciaBuscar);
		
		
		try {
			agenciaEdit = agenciaServiceApiRest.buscarAgencia(agenciaRequest);
			if(agenciaEdit != null) {
				model.addAttribute("agencia", agenciaEdit);
				return URLFORMAGENCIA;
			}else {
				Agencia agenciaBuscarCargar = new Agencia();
				agenciaBuscarCargar.setFlagDivisa(false);
				agenciaRequest.setAgencia(agenciaBuscarCargar);
				listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest);
				model.addAttribute(LISTAAGENCIAS, listaAgencias);
				model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				return URLFORMAGENCIABUSCAR;
			}
			
			
		} catch (CustomException e) {
			Agencia agenciaBuscarCargar = new Agencia();
			agenciaBuscarCargar.setFlagDivisa(false);
			agenciaRequest.setAgencia(agenciaBuscarCargar);
			try {
				listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest);
				model.addAttribute(LISTAAGENCIAS, listaAgencias);
				model.addAttribute(MENSAJEERROR, e.getMessage());
				return URLFORMAGENCIABUSCAR;
			} catch (CustomException e1) {
				model.addAttribute(MENSAJEERROR, e1.getMessage());
				return URLFORMAGENCIABUSCAR;
			}
			
		}
		
		
	}	
	
	
	@PostMapping("/save")
	public String saveWs(Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("saveWs");
		log.info("agencia: "+agencia);
		
		
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		agencia.setFlagActivo(true);
		agencia.setFlagDivisa(true);
		agenciaRequest.setAgencia(agencia);
		
		
		
		try {
			String respuesta = agenciaServiceApiRest.crear(agenciaRequest);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR,e.getMessage());
		}
		
		return REDIRECTINDEX;
	}	
	
	
	@GetMapping("/search")
	public String search(@ModelAttribute("agenciaSearch") Agencia agenciaSearch,
			Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a search agenciasWs");
		log.info(agenciaSearch.getCodAgencia());
		
		List<Agencia> listaAgencias = new ArrayList<>();
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(true);
		if(!agenciaSearch.getCodAgencia().equals("")){
			agenciaBuscar.setCodAgencia(agenciaSearch.getCodAgencia());
		}	
		agenciaRequest.setAgencia(agenciaBuscar);
		
		try {
			listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest);
			if(!listaAgencias.isEmpty()) {
				for (Agencia agencia2 : listaAgencias) {
					if(agencia2.getFechaModificacion() != null) {
						String[] arrOfStr = agencia2.getFechaModificacion().split(" ", 2);
						agencia2.setFechaModificacion(arrOfStr[0]);
					}
				}
				model.addAttribute(LISTAAGENCIAS, listaAgencias);
				
			}else {
				model.addAttribute(LISTAAGENCIAS, listaAgencias);
				model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
			}
		} catch (CustomException e) {
			model.addAttribute(LISTAAGENCIAS, listaAgencias);
			model.addAttribute(MENSAJEERROR, e.getMessage());
		}
		return URLINDEX;
	}	
	
	
	@GetMapping("/searchNombre")
	public String searchNombre(@ModelAttribute("agenciaSearch") Agencia agenciaSearch,
			Agencia agencia, Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a search agenciasWs");
		log.info(agenciaSearch.getCodAgencia());
		
		List<Agencia> listaAgencias = new ArrayList<>();
		AgenciaRequest agenciaRequest = getAgenciaRequest();
		Agencia agenciaBuscar = new Agencia();
		agenciaBuscar.setFlagDivisa(true);
		if(!agenciaSearch.getNombreAgencia().equals("")){
			agenciaBuscar.setNombreAgencia(agenciaSearch.getNombreAgencia());
		}	
		agenciaRequest.setAgencia(agenciaBuscar);
		
		try {
			listaAgencias = agenciaServiceApiRest.listaAgencias(agenciaRequest);
			log.info("lista: "+listaAgencias.isEmpty());
			if(!listaAgencias.isEmpty()) {
				for (Agencia agencia2 : listaAgencias) {
					if(agencia2.getFechaModificacion() != null) {
						String[] arrOfStr = agencia2.getFechaModificacion().split(" ", 2);
						agencia2.setFechaModificacion(arrOfStr[0]);
					}
				}
				model.addAttribute(LISTAAGENCIAS, listaAgencias);
			}else {
				model.addAttribute(LISTAAGENCIAS, listaAgencias);
				model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
			}
		} catch (CustomException e) {
			log.error("error: "+e);
			model.addAttribute(LISTAAGENCIAS, listaAgencias);
			model.addAttribute(MENSAJEERROR, e.getMessage());
			
		}
		
		return URLINDEX;
		
		
		
		
		
	}
		
	public AgenciaRequest getAgenciaRequest() {
		AgenciaRequest agenciaRequest = new AgenciaRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		agenciaRequest.setIdUsuario(userName);
		agenciaRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		agenciaRequest.setCodUsuario(userName);
		agenciaRequest.setCanal(canal);
		return agenciaRequest;
	}
	
	@ModelAttribute
	public void setGenericos(Model model, HttpServletRequest request) {
		Agencia agenciaSearch = new Agencia();
		model.addAttribute("agenciaSearch", agenciaSearch);
		String uri = request.getRequestURI();
		log.info("uri: "+uri);
		String[] arrUri = uri.split("/");

		arrUri[0] = "Home";
		model.addAttribute("arrUri", arrUri);
	}
}
