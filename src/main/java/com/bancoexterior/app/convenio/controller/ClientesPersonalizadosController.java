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

import com.bancoexterior.app.convenio.dto.ClienteDatosBasicoRequest;
import com.bancoexterior.app.convenio.dto.ClienteRequest;
import com.bancoexterior.app.convenio.dto.ClienteResponse;
import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosRequest;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.bancoexterior.app.convenio.model.DatosClientes;
import com.bancoexterior.app.convenio.model.DatosPaginacion;
import com.bancoexterior.app.convenio.model.LimitesPersonalizados;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.service.IClientePersonalizadoServiceApiRest;
import com.bancoexterior.app.convenio.service.ILimitesPersonalizadosServiceApiRest;
import com.bancoexterior.app.convenio.service.IMonedaServiceApiRest;
import com.bancoexterior.app.util.LibreriaUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/clientesPersonalizados")
public class ClientesPersonalizadosController {

	@Autowired
	private IClientePersonalizadoServiceApiRest clientePersonalizadoServiceApiRest;

	@Autowired
	private ILimitesPersonalizadosServiceApiRest limitesPersonalizadosServiceApiRest;
	
	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest;
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${des.canal}")
    private String canal;	
	
	@Value("${des.clientesPersonalizados.numeroRegistroPage}")
    private int numeroRegistroPage;
	
	private static final String URLINDEX = "convenio/clientesPersonalizados/listaClientesPersonalizados";
	
	private static final String URLINDEXLIMITESPERSONALIZADOS = "convenio/clientesPersonalizados/listaLimitesPersonalizados";
	
	private static final String URLFORMCLIENTESPERSONALIZADOS = "convenio/clientesPersonalizados/formClientesPersonalizados";
	
	private static final String URLFORMCLIENTESPERSONALIZADOSBUSCAR = "convenio/clientesPersonalizados/formClientesPersonalizadosBuscar";
	
	private static final String URLFORMCLIENTEPERSONALIZADOEDIT = "convenio/clientesPersonalizados/formClientesPersonalizadosEdit";
	
	private static final String URLFORMLIMITEPERSONALIZADOEDIT = "convenio/clientesPersonalizados/formLimitesPersonalizadosEdit";
	
	private static final String URLFORMLIMITEPERSONALIZADO = "convenio/clientesPersonalizados/formLimitesPersonalizados";
	
	private static final String LISTACLIENTESPERSONALIZADOS = "listaClientesPersonalizados";
	
	private static final String LISTAMONEDAS = "listaMonedas";
	
	private static final String DATOSPAGINACION = "datosPaginacion";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String REDIRECTINDEX = "redirect:/clientesPersonalizados/index/";
	
	private static final String REDIRECTINDEXLIMITESPERSONALIZADOS = "redirect:/clientesPersonalizados/verLimites/";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String MENSAJENORESULTADO = "Operacion Exitosa.La consulta no arrojo resultado.";
	
	@GetMapping("/index/{page}")
	public String index(@PathVariable("page") int page,Model model, RedirectAttributes redirectAttributes) {
		log.info("si me llamo a index clientesPersonalizadosWs");
		
		

		ClienteRequest clienteRequest = getClienteRequest();
		clienteRequest.setNumeroPagina(page);
		clienteRequest.setTamanoPagina(numeroRegistroPage);
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		clienteRequest.setCliente(clientesPersonalizados);
		List<ClientesPersonalizados> listaClientesPersonalizados = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		try {
			
			ClienteResponse clienteResponse = clientePersonalizadoServiceApiRest.listaClientesPaginacion(clienteRequest);
			
			
			if(clienteResponse != null) {
				listaClientesPersonalizados = clienteResponse.getListaClientes();
				for (ClientesPersonalizados clientesPersonalizados2 : listaClientesPersonalizados) {
					if(clientesPersonalizados2.getFechaModificacion() != null) {
						String[] arrOfStr = clientesPersonalizados2.getFechaModificacion().split(" ", 2);
						clientesPersonalizados2.setFechaModificacion(arrOfStr[0]);
					}
				}
				datosPaginacion = clienteResponse.getDatosPaginacion();
				model.addAttribute(LISTACLIENTESPERSONALIZADOS, listaClientesPersonalizados);
				model.addAttribute(DATOSPAGINACION, datosPaginacion);
				
			}else {
				datosPaginacion.setTotalPaginas(0);
				model.addAttribute(LISTACLIENTESPERSONALIZADOS, listaClientesPersonalizados);
				model.addAttribute(DATOSPAGINACION, datosPaginacion);
				
			}
			
			
		} catch (CustomException e) {
			model.addAttribute(DATOSPAGINACION, datosPaginacion);
			model.addAttribute(MENSAJEERROR, e.getMessage());
			
		}
		
		return URLINDEX;
		
	}	
	
	@GetMapping("/activar/{codigoIbs}/{page}")
	public String activarWs(@PathVariable("codigoIbs") String codigoIbs, @PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		

		ClientesPersonalizados clientesPersonalizadosEdit = new ClientesPersonalizados();

		ClienteRequest clienteRequest = getClienteRequest();
		clienteRequest.setNumeroPagina(1);
		clienteRequest.setTamanoPagina(numeroRegistroPage);
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		clientesPersonalizados.setCodigoIbs(codigoIbs);
		clienteRequest.setCliente(clientesPersonalizados);
		
		try {
			clientesPersonalizadosEdit = clientePersonalizadoServiceApiRest.buscarClientesPersonalizados(clienteRequest);
			clientesPersonalizadosEdit.setFlagActivo(true);
			clienteRequest.setCliente(clientesPersonalizadosEdit);
			String respuesta = clientePersonalizadoServiceApiRest.actualizar(clienteRequest);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			return REDIRECTINDEX+page;
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX+page;
		}
		
	}
	
	@GetMapping("/desactivar/{codigoIbs}/{page}")
	public String desactivarWs(@PathVariable("codigoIbs") String codigoIbs, @PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes) {
		log.info("desactivarWs");
		ClientesPersonalizados clientesPersonalizadosEdit = new ClientesPersonalizados();

		ClienteRequest clienteRequest = getClienteRequest();
		clienteRequest.setNumeroPagina(1);
		clienteRequest.setTamanoPagina(numeroRegistroPage);
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		clientesPersonalizados.setCodigoIbs(codigoIbs);
		clienteRequest.setCliente(clientesPersonalizados);
		
		try {
			clientesPersonalizadosEdit = clientePersonalizadoServiceApiRest.buscarClientesPersonalizados(clienteRequest);
			clientesPersonalizadosEdit.setFlagActivo(false);
			clienteRequest.setCliente(clientesPersonalizadosEdit);
			String respuesta = clientePersonalizadoServiceApiRest.actualizar(clienteRequest);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			return REDIRECTINDEX+page;
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX+page;
		}
		
	}
	
	@GetMapping("/edit/{codigoIbs}")
	public String editarWs(@PathVariable("codigoIbs") String codigoIbs, 
			ClientesPersonalizados clientesPersonalizados, Model model, RedirectAttributes redirectAttributes) {
		log.info("editarWs");

		ClientesPersonalizados clientesPersonalizadosEdit = new ClientesPersonalizados();

		ClienteRequest clienteRequest = getClienteRequest();
		ClientesPersonalizados clientesPersonalizadosBuscar = new ClientesPersonalizados();
		clientesPersonalizadosBuscar.setCodigoIbs(codigoIbs);
		clienteRequest.setCliente(clientesPersonalizadosBuscar);
		
		try {
			clientesPersonalizadosEdit = clientePersonalizadoServiceApiRest.buscarClientesPersonalizados(clienteRequest);
			if(clientesPersonalizadosEdit != null) {
				model.addAttribute("clientesPersonalizados", clientesPersonalizadosEdit);
				return URLFORMCLIENTEPERSONALIZADOEDIT;
			}else {
				redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				return REDIRECTINDEX+1;
			}
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX+1;
		}
	}
	
	@GetMapping("/verLimites/{codigoIbs}")
	public String verLimitesWs(@PathVariable("codigoIbs") String codigoIbs, 
			ClientesPersonalizados clientesPersonalizados, Model model, RedirectAttributes redirectAttributes) {
		log.info("verLimitesWs");
		List<LimitesPersonalizados> listaLimitesPersonalizados = new ArrayList<>();
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = getLimitesPersonalizadosRequest();
		LimitesPersonalizados limite = new LimitesPersonalizados();
		limite.setCodigoIbs(codigoIbs);
		limitesPersonalizadosRequest.setLimiteCliente(limite);
		
		ClientesPersonalizados clientesPersonalizadosEdit = new ClientesPersonalizados();

		ClienteRequest clienteRequest = getClienteRequest();
		clienteRequest.setNumeroPagina(1);
		clienteRequest.setTamanoPagina(numeroRegistroPage);
		ClientesPersonalizados clientesPersonalizadosBuscar = new ClientesPersonalizados();
		clientesPersonalizadosBuscar.setCodigoIbs(codigoIbs);
		clienteRequest.setCliente(clientesPersonalizadosBuscar);
			
		try {
			
			clientesPersonalizadosEdit = clientePersonalizadoServiceApiRest.buscarClientesPersonalizados(clienteRequest);
			if(clientesPersonalizadosEdit != null) {
				listaLimitesPersonalizados = limitesPersonalizadosServiceApiRest.listaLimitesPersonalizados(limitesPersonalizadosRequest);
				if(!listaLimitesPersonalizados.isEmpty()) {
					
					for (LimitesPersonalizados limitesPersonalizados : listaLimitesPersonalizados) {
						if(limitesPersonalizados.getFechaModificacion() != null) {
							String[] arrOfStr = limitesPersonalizados.getFechaModificacion().split(" ", 2);
							limitesPersonalizados.setFechaModificacion(arrOfStr[0]);
						}
					}
					model.addAttribute("listaLimitesPersonalizados", listaLimitesPersonalizados);
					model.addAttribute("codigoIbs", codigoIbs);
		    		return URLINDEXLIMITESPERSONALIZADOS;
				}else {
					model.addAttribute("listaLimitesPersonalizados", listaLimitesPersonalizados);
					model.addAttribute("codigoIbs", codigoIbs);
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
					return URLINDEXLIMITESPERSONALIZADOS;
				}
			}else {
				redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				return  REDIRECTINDEX+1;
			}		
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX+1;
		}
	}
	
	
	@GetMapping("/editLimiteCliente/{codigoIbs}/{codMoneda}/{tipoTransaccion}")
	public String editarLimiteClienteWs(@PathVariable("codigoIbs") String codigoIbs, @PathVariable("codMoneda") String codMoneda, 
			@PathVariable("tipoTransaccion") String tipoTransaccion,LimitesPersonalizados limitesPersonalizados,Model model, RedirectAttributes redirectAttributes) {
		log.info("editarLimiteClienteWs");
		log.info(codigoIbs);
		log.info(codMoneda);
		log.info(tipoTransaccion);
		
		LimitesPersonalizados limitesPersonalizadosEdit = new LimitesPersonalizados();
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = getLimitesPersonalizadosRequest();
		LimitesPersonalizados limitesP = new LimitesPersonalizados();
		limitesP.setCodigoIbs(codigoIbs);
		limitesP.setCodMoneda(codMoneda);
		limitesP.setTipoTransaccion(tipoTransaccion);
		limitesPersonalizadosRequest.setLimiteCliente(limitesP);
	
		try {
			limitesPersonalizadosEdit = limitesPersonalizadosServiceApiRest.buscarLimitesPersonalizados(limitesPersonalizadosRequest);
			if(limitesPersonalizadosEdit != null) {
				model.addAttribute("limitesPersonalizados", limitesPersonalizadosEdit);
				return URLFORMLIMITEPERSONALIZADOEDIT;
			}else {
				redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				return REDIRECTINDEX+1;
			}
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX+1;
		}
		
		
	}
	
	
	@PostMapping("/guardarLimiteCliente")
	public String guardarLimiteClienteWs(LimitesPersonalizados limitesPersonalizados, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {
		log.info("guardarWs");
		List<String> listaError = new ArrayList<>();
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				if(error.getCode().equals("typeMismatch")) {
					listaError.add("Los valores de los montos debe ser numerico");
				}
				log.info("getObjectName(): "+error.getObjectName());
				log.info("Ocurrio un error: " + error.getDefaultMessage());
			}
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMLIMITEPERSONALIZADOEDIT;
		}
		
		  if(limitesPersonalizados.getMontoMax().compareTo(limitesPersonalizados.getMontoMin()) < 0) { 
			  listaError.add("El monto mínimo no debe ser mayor al monto máximo");
			  model.addAttribute(LISTAERROR, listaError);
			  result.addError(new  ObjectError(LISTAERROR, " El monto mínimo no debe ser mayor al monto máximo"));
			  
			  return URLFORMLIMITEPERSONALIZADOEDIT;
		  }
		 
		 
		  if(limitesPersonalizados.getMontoMensual().compareTo(limitesPersonalizados.getMontoDiario()) < 0) { 
			  result.addError(new  ObjectError(LISTAERROR, " El monto diario no debe ser mayor al mensual"));
			  listaError.add("El monto diario no debe ser mayor al mensual");
			  model.addAttribute(LISTAERROR, listaError);
			  return URLFORMLIMITEPERSONALIZADOEDIT;
		  }
		
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = getLimitesPersonalizadosRequest();
		limitesPersonalizadosRequest.setLimiteCliente(limitesPersonalizados);
		
		try {
			
			String respuesta = limitesPersonalizadosServiceApiRest.actualizar(limitesPersonalizadosRequest);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			return REDIRECTINDEXLIMITESPERSONALIZADOS+limitesPersonalizados.getCodigoIbs();
		} catch (CustomException e) {
			result.addError(new ObjectError(LISTAERROR,e.getMessage()));
			listaError.add(e.getMessage());
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMLIMITEPERSONALIZADOEDIT;
		}
	
		
	}
	
	
	
	@GetMapping("/formLimiteClientePersonalizado/{codigoIbs}")
	public String formLimiteClientePersonalizado(@PathVariable("codigoIbs") String codigoIbs,LimitesPersonalizados limitesPersonalizados,  Model model, RedirectAttributes redirectAttributes) {
		
		log.info("formLimiteClientePersonalizado");
		log.info("codigoIbs: " + codigoIbs);
		List<Moneda> listaMonedas = new ArrayList<>();
		ClientesPersonalizados clientesPersonalizadosEdit = new ClientesPersonalizados();

		ClienteRequest clienteRequest = getClienteRequest();
		clienteRequest.setNumeroPagina(1);
		clienteRequest.setTamanoPagina(numeroRegistroPage);
		ClientesPersonalizados clientesPersonalizadosBuscar = new ClientesPersonalizados();
		clientesPersonalizadosBuscar.setCodigoIbs(codigoIbs);
		clienteRequest.setCliente(clientesPersonalizadosBuscar);
		
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
			
		try {
			
			clientesPersonalizadosEdit = clientePersonalizadoServiceApiRest.buscarClientesPersonalizados(clienteRequest);
			if(clientesPersonalizadosEdit != null) {
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				limitesPersonalizados.setCodigoIbs(codigoIbs);
				model.addAttribute(LISTAMONEDAS, listaMonedas);
	    		return URLFORMLIMITEPERSONALIZADO;
			}else {
				redirectAttributes.addFlashAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				return REDIRECTINDEX+1;
			}
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEX+1;
		}	
	}
	
	@PostMapping("/saveLimiteCliente")
	public String saveLimiteClienteWs(LimitesPersonalizados limitesPersonalizados, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		log.info("saveLimiteClienteWs");

		
		List<Moneda> listaMonedas;
		List<String> listaError = new ArrayList<>();
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		try {
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
				if(error.getCode().equals("typeMismatch")) {
					listaError.add("Los valores de los montos debe ser numerico");
				}
			}
			
		
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				model.addAttribute(LISTAMONEDAS, listaMonedas);
				model.addAttribute(LISTAERROR, listaError);
	    		return URLFORMLIMITEPERSONALIZADO;
			
			
		}
		 
		  if(limitesPersonalizados.getMontoMax().compareTo(limitesPersonalizados.getMontoMin()) < 0) { 
			  
				  	listaError.add("El monto mínimo no debe ser mayor al monto máximo");
					listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
					model.addAttribute(LISTAMONEDAS, listaMonedas);
					model.addAttribute(LISTAERROR, listaError);
					result.addError(new  ObjectError(LISTAERROR, " El monto mínimo no debe ser mayor al monto máximo"));
					return URLFORMLIMITEPERSONALIZADO;
				
		  }
		  
		  if(limitesPersonalizados.getMontoMensual().compareTo(limitesPersonalizados.getMontoDiario()) < 0) {
			  
					listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
					model.addAttribute(LISTAMONEDAS, listaMonedas);
					listaError.add("El monto diario no debe ser mayor al mensual");
			        model.addAttribute(LISTAERROR, listaError);
			        result.addError(new  ObjectError(LISTAERROR, " El monto diario no debe ser mayor al mensual"));
		    		return URLFORMLIMITEPERSONALIZADO;
				
		  }
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = getLimitesPersonalizadosRequest();
		limitesPersonalizados.setFlagActivo(true);
		limitesPersonalizadosRequest.setLimiteCliente(limitesPersonalizados);
		
		
			String respuesta = limitesPersonalizadosServiceApiRest.crear(limitesPersonalizadosRequest);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			return REDIRECTINDEXLIMITESPERSONALIZADOS+limitesPersonalizados.getCodigoIbs();
		} catch (CustomException e) {
			try {
				listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
				model.addAttribute(LISTAMONEDAS, listaMonedas);
				result.addError(new ObjectError("codMoneda", " Codigo :" +e.getMessage()));
				listaError.add(e.getMessage());
				model.addAttribute(LISTAERROR, listaError);
	    		return URLFORMLIMITEPERSONALIZADO;
			} catch (CustomException e1) {
				result.addError(new ObjectError("codMoneda", " Codigo :" +e1.getMessage()));
				listaError.add(e1.getMessage());
				model.addAttribute(LISTAERROR, listaError);
				return URLFORMLIMITEPERSONALIZADO;
			}
		
		}
	}	
	
	
	@GetMapping("/activarLimiteCliente/{codigoIbs}/{codMoneda}/{tipoTransaccion}")
	public String activarLimiteClienteWs(@PathVariable("codigoIbs") String codigoIbs, @PathVariable("codMoneda") String codMoneda, 
			@PathVariable("tipoTransaccion") String tipoTransaccion,LimitesPersonalizados limitesPersonalizados,Model model, RedirectAttributes redirectAttributes) {
		log.info("activarLimiteClienteWs");
		log.info(codigoIbs);
		log.info(codMoneda);
		log.info(tipoTransaccion);
		
		LimitesPersonalizados limitesPersonalizadosEdit = new LimitesPersonalizados();
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = getLimitesPersonalizadosRequest();
		LimitesPersonalizados limitesP = new LimitesPersonalizados();
		limitesP.setCodigoIbs(codigoIbs);
		limitesP.setCodMoneda(codMoneda);
		limitesP.setTipoTransaccion(tipoTransaccion);
		limitesPersonalizadosRequest.setLimiteCliente(limitesP);
		
		try {
			limitesPersonalizadosEdit = limitesPersonalizadosServiceApiRest.buscarLimitesPersonalizados(limitesPersonalizadosRequest);
			limitesPersonalizadosEdit.setFlagActivo(true);
			limitesPersonalizadosRequest.setLimiteCliente(limitesPersonalizadosEdit);
			String respuesta = limitesPersonalizadosServiceApiRest.actualizar(limitesPersonalizadosRequest);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			return REDIRECTINDEXLIMITESPERSONALIZADOS+codigoIbs;
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEXLIMITESPERSONALIZADOS+codigoIbs;
		}
	
		
	}
	
	@GetMapping("/desactivarLimiteCliente/{codigoIbs}/{codMoneda}/{tipoTransaccion}")
	public String desactivarLimiteClienteWs(@PathVariable("codigoIbs") String codigoIbs, @PathVariable("codMoneda") String codMoneda, 
			@PathVariable("tipoTransaccion") String tipoTransaccion,LimitesPersonalizados limitesPersonalizados,Model model, RedirectAttributes redirectAttributes) {
		log.info("activarWs");
		log.info(codigoIbs);
		log.info(codMoneda);
		log.info(tipoTransaccion);
		
		LimitesPersonalizados limitesPersonalizadosEdit = new LimitesPersonalizados();
		
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = getLimitesPersonalizadosRequest();
		LimitesPersonalizados limitesP = new LimitesPersonalizados();
		limitesP.setCodigoIbs(codigoIbs);
		limitesP.setCodMoneda(codMoneda);
		limitesP.setTipoTransaccion(tipoTransaccion);
		limitesPersonalizadosRequest.setLimiteCliente(limitesP);
		
		try {
			limitesPersonalizadosEdit = limitesPersonalizadosServiceApiRest.buscarLimitesPersonalizados(limitesPersonalizadosRequest);
			limitesPersonalizadosEdit.setFlagActivo(false);
			limitesPersonalizadosRequest.setLimiteCliente(limitesPersonalizadosEdit);
			String respuesta = limitesPersonalizadosServiceApiRest.actualizar(limitesPersonalizadosRequest);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			return REDIRECTINDEXLIMITESPERSONALIZADOS+codigoIbs;
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return REDIRECTINDEXLIMITESPERSONALIZADOS+codigoIbs;
		}
	
		
	}
	
	@GetMapping("/search")
	public String search(
			@ModelAttribute("clientesPersonalizadosSearch") ClientesPersonalizados clientesPersonalizadosSearch,
			Model model, RedirectAttributes redirectAttributes) {
		log.info(clientesPersonalizadosSearch.getCodigoIbs());

		

		ClienteRequest clienteRequest = getClienteRequest();
		clienteRequest.setNumeroPagina(1);
		clienteRequest.setTamanoPagina(numeroRegistroPage);
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		if (!clientesPersonalizadosSearch.getCodigoIbs().equals(""))
			clientesPersonalizados.setCodigoIbs(clientesPersonalizadosSearch.getCodigoIbs());
		clienteRequest.setCliente(clientesPersonalizados);
		List<ClientesPersonalizados> listaClientesPersonalizados = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		
		try {
			ClienteResponse clienteResponse = clientePersonalizadoServiceApiRest.listaClientesPaginacion(clienteRequest);
			
			
			if(clienteResponse != null) {
				listaClientesPersonalizados = clienteResponse.getListaClientes();
				if(!listaClientesPersonalizados.isEmpty()) {
					for (ClientesPersonalizados clientesPersonalizados2 : listaClientesPersonalizados) {
						if(clientesPersonalizados2.getFechaModificacion() != null) {
							String[] arrOfStr = clientesPersonalizados2.getFechaModificacion().split(" ", 2);
							clientesPersonalizados2.setFechaModificacion(arrOfStr[0]);
						}
					}
					datosPaginacion = clienteResponse.getDatosPaginacion();
					log.info("datosPaginacion: "+datosPaginacion);
					model.addAttribute(LISTACLIENTESPERSONALIZADOS, listaClientesPersonalizados);
					model.addAttribute(DATOSPAGINACION, datosPaginacion);
				}else {
					datosPaginacion.setTotalPaginas(0);
					model.addAttribute(LISTACLIENTESPERSONALIZADOS, listaClientesPersonalizados);
					model.addAttribute(DATOSPAGINACION, datosPaginacion);
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				}
				
				
			}else {
				datosPaginacion.setTotalPaginas(0);
				model.addAttribute(LISTACLIENTESPERSONALIZADOS, listaClientesPersonalizados);
				model.addAttribute(DATOSPAGINACION, datosPaginacion);
				model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
			}
			
		} catch (CustomException e) {
			datosPaginacion.setTotalPaginas(0);
			model.addAttribute(LISTACLIENTESPERSONALIZADOS, listaClientesPersonalizados);
			model.addAttribute(DATOSPAGINACION, datosPaginacion);
			model.addAttribute(MENSAJEERROR, e.getMessage());
		}
		
		return URLINDEX;
	}	
	
	
	@GetMapping("/searchNroIdCliente")
	public String searchNroIdCliente(
			@ModelAttribute("clientesPersonalizadosSearch") ClientesPersonalizados clientesPersonalizadosSearch,
			Model model, RedirectAttributes redirectAttributes) {
		log.info(clientesPersonalizadosSearch.getCodigoIbs());

	
		ClienteRequest clienteRequest = getClienteRequest();
		clienteRequest.setNumeroPagina(1);
		clienteRequest.setTamanoPagina(numeroRegistroPage);
		ClientesPersonalizados clientesPersonalizados = new ClientesPersonalizados();
		if (!clientesPersonalizadosSearch.getNroIdCliente().equals(""))
			clientesPersonalizados.setNroIdCliente(clientesPersonalizadosSearch.getNroIdCliente());
		clienteRequest.setCliente(clientesPersonalizados);
		
		List<ClientesPersonalizados> listaClientesPersonalizados = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		
		try {
			ClienteResponse clienteResponse = clientePersonalizadoServiceApiRest.listaClientesPaginacion(clienteRequest);
			
			
			if(clienteResponse != null) {
				listaClientesPersonalizados = clienteResponse.getListaClientes();
				if(!listaClientesPersonalizados.isEmpty()) {
					for (ClientesPersonalizados clientesPersonalizados2 : listaClientesPersonalizados) {
						if(clientesPersonalizados2.getFechaModificacion() != null) {
							String[] arrOfStr = clientesPersonalizados2.getFechaModificacion().split(" ", 2);
							clientesPersonalizados2.setFechaModificacion(arrOfStr[0]);
						}
					}
					datosPaginacion = clienteResponse.getDatosPaginacion();
					model.addAttribute(LISTACLIENTESPERSONALIZADOS, listaClientesPersonalizados);
					model.addAttribute(DATOSPAGINACION, datosPaginacion);
					
				}else {
					datosPaginacion.setTotalPaginas(0);
					model.addAttribute(LISTACLIENTESPERSONALIZADOS, listaClientesPersonalizados);
					model.addAttribute(DATOSPAGINACION, datosPaginacion);
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
					
				}
				
				
			}else {
				datosPaginacion.setTotalPaginas(0);
				model.addAttribute(LISTACLIENTESPERSONALIZADOS, listaClientesPersonalizados);
				model.addAttribute(DATOSPAGINACION, datosPaginacion);
				model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
			
			}
			
			
			
			
		} catch (CustomException e) {
			datosPaginacion.setTotalPaginas(0);
			model.addAttribute(LISTACLIENTESPERSONALIZADOS, listaClientesPersonalizados);
			model.addAttribute(DATOSPAGINACION, datosPaginacion);
			model.addAttribute(MENSAJEERROR, e.getMessage());
			
		}
		
		return URLINDEX;
	}
	
	
	@GetMapping("/searchCrear")
	public String searchCrear(ClientesPersonalizados clientesPersonalizados,
			Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		log.info("searchCrear");
		

		 
		
		ClienteDatosBasicoRequest clienteDatosBasicoRequest = getClienteDatosBasicoRequest();
		clienteDatosBasicoRequest.setIp(request.getRemoteAddr());
		if(!clientesPersonalizados.getCodigoIbs().equals(""))
			clienteDatosBasicoRequest.setCodigoIbs(clientesPersonalizados.getCodigoIbs());
		
		if(!clientesPersonalizados.getNroIdCliente().equals(""))
			clienteDatosBasicoRequest.setNroIdCliente(clientesPersonalizados.getNroIdCliente());
		
		
		
		try {
			DatosClientes datosClientes = clientePersonalizadoServiceApiRest.buscarDatosBasicos(clienteDatosBasicoRequest);
			
			if(datosClientes != null) {
				clientesPersonalizados.setCodigoIbs(datosClientes.getCodIbs());
				clientesPersonalizados.setNroIdCliente(datosClientes.getNroIdCliente());
				clientesPersonalizados.setNombreRif(datosClientes.getNombreLegal());
				model.addAttribute("clientesPersonalizados", clientesPersonalizados);
				return URLFORMCLIENTESPERSONALIZADOS;
			}else {
				model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				return URLFORMCLIENTESPERSONALIZADOSBUSCAR;
			}
			
			
		} catch (CustomException e) {
			log.error("error: "+e);
			model.addAttribute(MENSAJEERROR, e.getMessage());
			return URLFORMCLIENTESPERSONALIZADOSBUSCAR;
		}
		
	}	

	@PostMapping("/guardar")
	public String guardarWs(ClientesPersonalizados clientesPersonalizados, Model model, RedirectAttributes redirectAttributes) {
		log.info("saveWs");
		log.info("clientesPersonalizados: "+clientesPersonalizados);
		
		ClienteRequest clienteRequest = getClienteRequest();
		clientesPersonalizados.setFlagActivo(true);
		clienteRequest.setCliente(clientesPersonalizados);
		
		try {
			String respuesta = clientePersonalizadoServiceApiRest.actualizar(clienteRequest);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			return "redirect:/clientesPersonalizados/index";
			
		} catch (CustomException e) {
			model.addAttribute(MENSAJEERROR,e.getMessage());
			return URLFORMLIMITEPERSONALIZADOEDIT;
		}
	}
	
	
	@PostMapping("/save")
	public String saveWs(ClientesPersonalizados clientesPersonalizados, Model model, RedirectAttributes redirectAttributes) {
		log.info("saveWs");
		log.info("clientesPersonalizados: "+clientesPersonalizados);
		
		ClienteRequest clienteRequest = getClienteRequest();
		clientesPersonalizados.setFlagActivo(true);
		clienteRequest.setCliente(clientesPersonalizados);
		
		try {
			String respuesta = clientePersonalizadoServiceApiRest.crear(clienteRequest);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta+ " Puede crear limites Personalizados al cliiente nuevo");
			return "redirect:/clientesPersonalizados/formLimiteClientePersonalizado/"+clientesPersonalizados.getCodigoIbs();
			
		} catch (CustomException e) {
			model.addAttribute(MENSAJEERROR,e.getMessage());
			return URLFORMCLIENTESPERSONALIZADOS;
		}
		
		
		
	}
	
	
	@GetMapping("/formClientePersonalizado")
	public String formClientePersonalizado(Model model) {

		return URLFORMCLIENTESPERSONALIZADOS;
	}
	
	
	@GetMapping("/formClientePersonalizadoBuscar")
	public String formClientePersonalizadoBuscar(ClientesPersonalizados clientesPersonalizados) {

		return URLFORMCLIENTESPERSONALIZADOSBUSCAR;
	}
	
	public ClienteRequest getClienteRequest() {
		ClienteRequest clienteRequest = new ClienteRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		clienteRequest.setIdUsuario(userName);
		clienteRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		clienteRequest.setCodUsuario(userName);
		clienteRequest.setCanal(canal);
		return clienteRequest;
	}
	
	public ClienteDatosBasicoRequest getClienteDatosBasicoRequest() {
		ClienteDatosBasicoRequest clienteDatosBasicoRequest = new ClienteDatosBasicoRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		clienteDatosBasicoRequest.setIdUsuario(userName);
		clienteDatosBasicoRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		clienteDatosBasicoRequest.setCodUsuario(userName);
		clienteDatosBasicoRequest.setCanal(canal);
		return clienteDatosBasicoRequest;
	}
	
	public LimitesPersonalizadosRequest getLimitesPersonalizadosRequest() {
		LimitesPersonalizadosRequest limitesPersonalizadosRequest = new LimitesPersonalizadosRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		limitesPersonalizadosRequest.setIdUsuario(userName);
		limitesPersonalizadosRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		limitesPersonalizadosRequest.setCodUsuario(userName);
		limitesPersonalizadosRequest.setCanal(canal);
		return limitesPersonalizadosRequest;
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
		ClientesPersonalizados clientesPersonalizadosSearch = new ClientesPersonalizados();
		model.addAttribute("clientesPersonalizadosSearch", clientesPersonalizadosSearch);
		String uri = request.getRequestURI();
		log.info("uri: "+uri);
		String[] arrUri = uri.split("/");

		arrUri[0] = "Home";
		model.addAttribute("arrUri", arrUri);
	}
}
