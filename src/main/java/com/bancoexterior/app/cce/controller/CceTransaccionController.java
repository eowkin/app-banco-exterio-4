package com.bancoexterior.app.cce.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bancoexterior.app.cce.dto.AprobacionesConsultasRequest;
import com.bancoexterior.app.cce.dto.AprobacionesConsultasResponse;
import com.bancoexterior.app.cce.dto.AprobacionesRequest;
import com.bancoexterior.app.cce.dto.BancoRequest;
import com.bancoexterior.app.cce.dto.CceTransaccionDto;
import com.bancoexterior.app.cce.model.BCVLBT;
import com.bancoexterior.app.cce.model.Banco;
import com.bancoexterior.app.cce.model.CceMontoMaximoAproAuto;
import com.bancoexterior.app.cce.model.CceTransaccion;
import com.bancoexterior.app.cce.model.DatosPaginacion;
import com.bancoexterior.app.cce.model.Filtros;
import com.bancoexterior.app.cce.service.IBancoService;
import com.bancoexterior.app.cce.service.IBcvlbtService;
import com.bancoexterior.app.cce.service.ICceMontoMaximoAproAutoService;
import com.bancoexterior.app.cce.service.ICceTransaccionService;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.util.LibreriaUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/ccetransacciones")
public class CceTransaccionController {

	@Autowired
	private ICceTransaccionService service;
	
	@Autowired
	private ICceMontoMaximoAproAutoService montoMaximoAproAutoService; 
	
	@Autowired
	private IBancoService bancoService;
	
	@Autowired
	private IBcvlbtService bcvlbtService;
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${des.canal}")
    private String canal;	
	
	@Value("${des.trasacciones.numeroRegistroPage}")
    private int numeroRegistroPage;
	
	@Value("${des.trasacciones.montoTopeMaximoAproAuto}")
    private BigDecimal montoTopeMaximoAproAuto;
	
	private static final String URLFORMCONSULTARMOVIMIENTOSALTOBAJOVALOR = "cce/formConsultarMovimientosAltoBajoValor";
	
	private static final String URLFORMMOVIMIENTOSALTOBAJOVALORDETALLEFECHAS = "cce/formMovimientoAltoBajoValorDetalleFechas";
	
	private static final String URLFORMAPROBARALTOVALORLOTEAUTOMATICO = "cce/formAprobarAltoValorLoteAutomatico";
																				   
	private static final String URLLISTAMOVIMIENTOSCONSULTAALTOBAJOVALORPAGINATE = "cce/listaMovimientosConsultaAltoBajoValorPaginate";
	
	private static final String URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE = "cce/listaOperacionesPorAporbarAltoValorPaginate";
	
	private static final String STRDATEFORMET = "yyyy-MM-dd";
	
	private static final String LISTATRANSACCIONES = "listaTransacciones";
	
	private static final String LISTABCVLBTPORAPROBAR = "listaBCVLBTPorAprobar";
	
	private static final String DATOSPAGINACION = "datosPaginacion";
	
	private static final String CODTRANSACCION = "codTransaccion";
	
	private static final String BANCODESTINO = "bancoDestino";
	
	private static final String NUMEROIDENTIFICACION = "numeroIdentificacion";
	
	private static final String FECHADESDE = "fechaDesde";
	
	private static final String FECHAHASTA = "fechaHasta";
	
	private static final String NUMEROAPROBACIONESLOTES = "numeroAprobacionesLotes";
	
	private static final String MONTOAPROBACIONESLOTES = "montoAprobacionesLotes";
	
	private static final String LISTABANCOS = "listaBancos";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String MENSAJEFECHASINVALIDAS = "Los valores de las fechas son invalidos";
	
	private static final String MENSAJENORESULTADO = "Operacion Exitosa.La consulta no arrojo resultado.";
	
	private static final String MENSAJENORESULTADOLOTE = "No se encontraron operaciones Alto Valor Lote que procesar.";
	
	private static final String MENSAJEFUERARANGO = "El monto a consultar esta fuera de rango Alto Valor Lote Automatico.";
	
	@GetMapping("/listaMovimientosConsultaAltoBajoValor")
	public String index(Model model) {
		
		List<CceTransaccionDto> listaTransacciones = service.consultar();
		model.addAttribute(LISTATRANSACCIONES, listaTransacciones);   
		return "cce/listaMovimientosConsultaAltoBajoValor";
	}
	
	@GetMapping("/listaMovimientosConsultaAltoBajoValorPaginate")
	public String indexPaginado(Model model, Pageable page) {
		
		Page<CceTransaccion> listaTransacciones = service.consultar(page);
		model.addAttribute(LISTATRANSACCIONES, listaTransacciones);   
		return "cce/listaMovimientosConsultaAltoBajoValorPaginateTodas";
	}
	
	@GetMapping("/formConsultaMovimientosConsultaAltoBajoValor")
	public String formConsultaMovimientosAltoBajoValor(CceTransaccionDto cceTransaccionDto, Model model) {
		log.info("formConsultaMovimientosAltoBajoValor");
		
		BancoRequest bancoRequest = getBancoRequest();
		
		try {
			List<Banco> listaBancos  = bancoService.listaBancos(bancoRequest);
			model.addAttribute(LISTABANCOS, listaBancos);
		} catch (CustomException e) {
			e.printStackTrace();
			model.addAttribute(LISTAERROR, e.getMessage());
		}
		
		return URLFORMCONSULTARMOVIMIENTOSALTOBAJOVALOR;
		
	}
					
	
	@GetMapping("/procesarConsultaMovimientosAltoBajoValorPageable")
	public String procesarConsultaMovimientosAltoBajoValorPageable(CceTransaccionDto cceTransaccionDto, 
			Model model, Pageable page) {
		log.info("procesarConsultaMovimientosAltoBajoValor");
		log.info("fechaDesde: "+cceTransaccionDto.getFechaDesde());
		log.info("fechaHasta: "+cceTransaccionDto.getFechaHasta());
		log.info("codTransaccion: "+cceTransaccionDto.getCodTransaccion());
		log.info("bancoDestino: "+cceTransaccionDto.getBancoDestino());
		log.info("numeroIdentificacion: "+cceTransaccionDto.getNumeroIdentificacion());
		
		
		BancoRequest bancoRequest = getBancoRequest();
		
		try {
			
			List<String> listaError = new ArrayList<>();
			Page<CceTransaccion> listaTransacciones;
			
			if(isFechaValidaDesdeHasta(cceTransaccionDto.getFechaDesde(), cceTransaccionDto.getFechaHasta())){
					
				listaTransacciones = service.consultaMovimientosConFechas(cceTransaccionDto.getCodTransaccion(), cceTransaccionDto.getBancoDestino(),
					cceTransaccionDto.getNumeroIdentificacion(),cceTransaccionDto.getFechaDesde(), cceTransaccionDto.getFechaHasta(), page);
					
				listaTransacciones = convertirLista(listaTransacciones);
					
					
					
				if(listaTransacciones.isEmpty()) {
					model.addAttribute(LISTAERROR, MENSAJENORESULTADO);
					List<Banco> listaBancos  = bancoService.listaBancos(bancoRequest);
					model.addAttribute(LISTABANCOS, listaBancos);
					return URLFORMCONSULTARMOVIMIENTOSALTOBAJOVALOR;
				}
				model.addAttribute(LISTATRANSACCIONES, listaTransacciones);
				model.addAttribute(CODTRANSACCION, cceTransaccionDto.getCodTransaccion());
				model.addAttribute(BANCODESTINO, cceTransaccionDto.getBancoDestino());
				model.addAttribute(NUMEROIDENTIFICACION, cceTransaccionDto.getNumeroIdentificacion());
				model.addAttribute(FECHADESDE, cceTransaccionDto.getFechaDesde());
				model.addAttribute(FECHAHASTA, cceTransaccionDto.getFechaHasta());
				return URLLISTAMOVIMIENTOSCONSULTAALTOBAJOVALORPAGINATE;
					
			}else {
				log.info("fechas invalidas");
				listaError.add(MENSAJEFECHASINVALIDAS);
				model.addAttribute(LISTAERROR, listaError);
				return URLFORMCONSULTARMOVIMIENTOSALTOBAJOVALOR;
			}
		} catch (CustomException e) {
			e.printStackTrace();
			model.addAttribute(LISTAERROR, e.getMessage());
			return URLFORMCONSULTARMOVIMIENTOSALTOBAJOVALOR;
		}
		
		
		
	}
	            //consultaMovimientosAltoBajoValorPageable
	@GetMapping("/consultaMovimientosAltoBajoValorPageable")
	public String consultaMovimientosAltoBajoValorPageable(@RequestParam("codTransaccion") String codTransaccion, 
			@RequestParam("bancoDestino") String bancoDestino, @RequestParam("numeroIdentificacion") String numeroIdentificacion,
			@RequestParam("fechaDesde") String fechaDesde, @RequestParam("fechaHasta") String fechaHasta, 
			Model model, Pageable page) {
		log.info("procesarConsultaMovimientosAltoBajoValor");
		log.info("fechaDesde: "+fechaDesde);
		log.info("fechaHasta: "+fechaHasta);
		log.info("codTransaccion: "+codTransaccion);
		log.info("bancoDestino: "+bancoDestino);
		log.info("numeroIdentificacion: "+numeroIdentificacion);
		
		List<String> listaError = new ArrayList<>();
		Page<CceTransaccion> listaTransacciones;
		
		if(isFechaValidaDesdeHasta(fechaDesde, fechaHasta)){
			listaTransacciones = service.consultaMovimientosConFechas(codTransaccion, bancoDestino, numeroIdentificacion,
							                                          fechaDesde, fechaHasta, page);
			listaTransacciones = convertirLista(listaTransacciones);
			if(listaTransacciones.isEmpty()) {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
			}
			model.addAttribute(LISTATRANSACCIONES, listaTransacciones);
			model.addAttribute(CODTRANSACCION, codTransaccion);
			model.addAttribute(BANCODESTINO, bancoDestino);
			model.addAttribute(NUMEROIDENTIFICACION, numeroIdentificacion);
			model.addAttribute(FECHADESDE, fechaDesde);
			model.addAttribute(FECHAHASTA, fechaHasta);
			
				
		}else {
			log.info("fechas invalidas");
			listaError.add(MENSAJEFECHASINVALIDAS);
			model.addAttribute(LISTAERROR, listaError);
			
		}
		return URLLISTAMOVIMIENTOSCONSULTAALTOBAJOVALORPAGINATE;
	}
	
	
	
	
	
	@GetMapping("/detalleMovimiento")
	public String verMovimineto(@RequestParam("endtoendId") String endtoendId,@RequestParam("codTransaccion") String codTransaccion, 
			@RequestParam("bancoDestino") String bancoDestino, @RequestParam("numeroIdentificacion") String numeroIdentificacion,
			@RequestParam("fechaDesde") String fechaDesde, @RequestParam("fechaHasta") String fechaHasta, 
			Model model, Pageable page) {
		log.info(endtoendId);
		
		CceTransaccionDto cceTransaccionDto = service.findByEndtoendId(endtoendId);
		if(cceTransaccionDto != null) {
			if(cceTransaccionDto.getCodTransaccion().equals("5724") || cceTransaccionDto.getCodTransaccion().equals("5728")) {
				String cuentaOrigen = cceTransaccionDto.getCuentaOrigen();
				String cuentaDestino = cceTransaccionDto.getCuentaDestino();
				cceTransaccionDto.setCuentaOrigen(cuentaDestino);
				cceTransaccionDto.setCuentaDestino(cuentaOrigen);
				String numeroIdentificacionCce = cceTransaccionDto.getNumeroIdentificacion();
				String numeroIdentificacionDestinoCce = cceTransaccionDto.getNumeroIdentificacionDestino();
				cceTransaccionDto.setNumeroIdentificacion(numeroIdentificacionDestinoCce);
				cceTransaccionDto.setNumeroIdentificacionDestino(numeroIdentificacionCce);
				String beneficiarioOrigen = cceTransaccionDto.getBeneficiarioOrigen();
				String beneficiarioDestino = cceTransaccionDto.getBeneficiarioDestino();
				cceTransaccionDto.setBeneficiarioOrigen(beneficiarioDestino);
				cceTransaccionDto.setBeneficiarioDestino(beneficiarioOrigen);
			}
			
			cceTransaccionDto.setNombreTransaccion(nombreTransaccion(cceTransaccionDto.getCodTransaccion())+"-"+cceTransaccionDto.getCodTransaccion());
			cceTransaccionDto.setNombreEstadoBcv(nombreEstadoBcv(cceTransaccionDto.getEstadobcv()));
			cceTransaccionDto.setMonto(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceTransaccionDto.getMonto())));
			model.addAttribute("cceTransaccionDto", cceTransaccionDto);
			model.addAttribute(CODTRANSACCION, codTransaccion);
			model.addAttribute(BANCODESTINO, bancoDestino);
			model.addAttribute(NUMEROIDENTIFICACION, numeroIdentificacion);
			model.addAttribute(FECHADESDE, fechaDesde);
			model.addAttribute(FECHAHASTA, fechaHasta);
			model.addAttribute("page", page.getPageNumber());
			return URLFORMMOVIMIENTOSALTOBAJOVALORDETALLEFECHAS;
		}else {
			Page<CceTransaccion> listaTransacciones;
			listaTransacciones = service.consultaMovimientosConFechas(codTransaccion, bancoDestino, numeroIdentificacion,
					fechaDesde, fechaHasta, page);
			listaTransacciones = convertirLista(listaTransacciones);
			model.addAttribute(LISTATRANSACCIONES, listaTransacciones);
			model.addAttribute(CODTRANSACCION, codTransaccion);
			model.addAttribute(BANCODESTINO, bancoDestino);
			model.addAttribute(NUMEROIDENTIFICACION, numeroIdentificacion);
			model.addAttribute(FECHADESDE, fechaDesde);
			model.addAttribute(FECHAHASTA, fechaHasta);
			return URLLISTAMOVIMIENTOSCONSULTAALTOBAJOVALORPAGINATE;
			 
		}
		
		
		
	}
	
	
	@GetMapping("/procesarMovimientosPorAprobarAltoValor/{page}")
	public String consultaMovimientosPorAprobarAltovalor(@PathVariable("page") int page, Model model, HttpServletRequest request) {
		
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest(); 
		
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		//request.getRemoteAddr()
		aprobacionesConsultasRequest.setNumeroPagina(page);   
		//aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
		aprobacionesConsultasRequest.setTamanoPagina(5);
		Filtros filtros = new Filtros();
		//filtros.setReferencia(null);
		filtros.setStatus("I");
		filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto())));
		filtros.setMontoHasta(montoTopeMaximoAproAuto);
		
		
		
		aprobacionesConsultasRequest.setFiltros(filtros);
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		try {
			AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest);
			CceTransaccionDto cceTransaccionDto = getTransaccionesMostrar();
			model.addAttribute(NUMEROAPROBACIONESLOTES,cceTransaccionDto.getNumeroAprobacionesLotes());
			model.addAttribute(MONTOAPROBACIONESLOTES,cceTransaccionDto.getMontoAprobacionesLotes());
			if(aprobacionesConsultasResponse != null) {
				listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
				datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
				model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
				model.addAttribute(DATOSPAGINACION,datosPaginacion);
			}else {
				model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
				model.addAttribute(DATOSPAGINACION,datosPaginacion);
			}
			
		} catch (CustomException e) {
			e.printStackTrace();
			model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
			model.addAttribute(DATOSPAGINACION,datosPaginacion);
			model.addAttribute(MENSAJEERROR,e.getMessage());
		}
		
		
		return URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE;
	}
	
	
	@GetMapping("/formAprobarMovimientosAltoValorLoteAutomatico")
	public String formAprobarAltoValorLoteAutomatico(CceTransaccionDto cceTransaccionDto, Model model, HttpSession httpSession) {
		log.info("formAprobarAltoValorLoteAutomatico");
				
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest(); 
		
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
	
		//request.getRemoteAddr()
		aprobacionesConsultasRequest.setNumeroPagina(1);   
		aprobacionesConsultasRequest.setTamanoPagina(2147483647);
		Filtros filtros = new Filtros();
		//filtros.setReferencia(null);
		filtros.setStatus("I");
		filtros.setMontoDesde(new BigDecimal(0));
		filtros.setMontoHasta(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto())));
		
		
		
		aprobacionesConsultasRequest.setFiltros(filtros);
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		try {
			AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest);
			
			if(aprobacionesConsultasResponse != null) {
				listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
				if(listaBCVLBTPorAprobar.isEmpty()) {
					cceTransaccionDto.setNumeroAprobacionesLotes(0);
					cceTransaccionDto.setMontoAprobacionesLotes(new BigDecimal("0.00"));
					model.addAttribute(LISTAERROR, MENSAJENORESULTADOLOTE);
				}else {
					httpSession.setAttribute(LISTABCVLBTPORAPROBAR, listaBCVLBTPorAprobar);
					cceTransaccionDto.setNumeroAprobacionesLotes(listaBCVLBTPorAprobar.size());
					cceTransaccionDto.setMontoAprobacionesLotes(montoAprobacionesLotes(listaBCVLBTPorAprobar));
				}
				
			}else {
				cceTransaccionDto.setNumeroAprobacionesLotes(0);
				cceTransaccionDto.setMontoAprobacionesLotes(new BigDecimal("0.00"));
				model.addAttribute(LISTAERROR, MENSAJENORESULTADOLOTE);
			}
			
		} catch (CustomException e) {
			e.printStackTrace();
			cceTransaccionDto.setNumeroAprobacionesLotes(0);
			cceTransaccionDto.setMontoAprobacionesLotes(new BigDecimal("0.00"));
			model.addAttribute(LISTAERROR, e.getMessage());
		}
		return URLFORMAPROBARALTOVALORLOTEAUTOMATICO;
		
	}
	
	@GetMapping("/procesarAprobarAltoValorLoteAutomatico")
	public String procesarAprobarAltoValorLoteAutomatico(CceTransaccionDto cceTransaccionDto, Model model, HttpSession httpSession) {
		log.info("procesarAprobarAltoValorLoteAutomatico");
		List<BCVLBT> listaBCVLBTPorAprobar =(List<BCVLBT>)httpSession.getAttribute(LISTABCVLBTPORAPROBAR);
		for (BCVLBT bcvlbt : listaBCVLBTPorAprobar) {
			log.info("bcvlbt: "+bcvlbt);
		}
		
		return "/index";
	}	
	
	
	@GetMapping("/searchMonto")
	public String searchMonto(@ModelAttribute("cceTransaccionDtoSearch") CceTransaccionDto cceTransaccionDtoSearch,
			BindingResult result, Model model) {
		log.info("monto: " + cceTransaccionDtoSearch.getMonto());
		List<String> listaError = new ArrayList<>();
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest();
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		
		try {
			if (result.hasErrors()) {
				for (ObjectError error : result.getAllErrors()) {
					log.info("Ocurrio un error: " + error.getDefaultMessage());
					if(error.getCode().equals("typeMismatch")) {
						listaError.add("El valor del monto debe ser numerico");
					}
				}
				model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				model.addAttribute(LISTAERROR, listaError);
				model.addAttribute("listaBCVLBTPorAprobar",listaBCVLBTPorAprobar);
				model.addAttribute("datosPaginacion",datosPaginacion);
				return "cce/listaOperacionesPorAporbarAltoValorPaginate";
			}
			//request.getRemoteAddr()
			aprobacionesConsultasRequest.setNumeroPagina(1);   
			//aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
			aprobacionesConsultasRequest.setTamanoPagina(5);
			Filtros filtros = new Filtros();
			//filtros.setReferencia(null);
			filtros.setStatus("I");
			BigDecimal montoSerch = montoSerch(cceTransaccionDtoSearch.getMonto());
			CceTransaccionDto cceTransaccionDto = getTransaccionesMostrar();
			model.addAttribute(NUMEROAPROBACIONESLOTES,cceTransaccionDto.getNumeroAprobacionesLotes());
			model.addAttribute(MONTOAPROBACIONESLOTES,cceTransaccionDto.getMontoAprobacionesLotes());
			if(montoSerch.compareTo(BigDecimal.ZERO) == 0) { 
				filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto())));
				filtros.setMontoHasta(montoTopeMaximoAproAuto);

				aprobacionesConsultasRequest.setFiltros(filtros);
				AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest);
				
				if(aprobacionesConsultasResponse != null) {
					listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
					datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
					if(listaBCVLBTPorAprobar.isEmpty()) {
						model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
					}
					model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
					model.addAttribute(DATOSPAGINACION,datosPaginacion);
					model.addAttribute("monto", montoSerch);
					
				}else {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
					model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
					model.addAttribute(DATOSPAGINACION,datosPaginacion);
					
				}
				return "cce/listaOperacionesPorAporbarAltoValorPaginate";
			}else {
				if(montoSerch(cceTransaccionDtoSearch.getMonto()).compareTo(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto()))) < 0) {
					log.info("entro por fuera rango");
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
					listaError.add(MENSAJEFUERARANGO);
					model.addAttribute(LISTAERROR, listaError);
					model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
					model.addAttribute(DATOSPAGINACION,datosPaginacion);
					return "cce/listaOperacionesPorAporbarAltoValorPaginate";
				}
				filtros.setMontoDesde(cceTransaccionDtoSearch.getMonto());
				filtros.setMontoHasta(cceTransaccionDtoSearch.getMonto());
				aprobacionesConsultasRequest.setFiltros(filtros);
				AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest);
				
				if(aprobacionesConsultasResponse != null) {
					listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
					datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
					if(listaBCVLBTPorAprobar.isEmpty()) {
						model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
					}
					model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
					model.addAttribute(DATOSPAGINACION,datosPaginacion);
					model.addAttribute("monto", montoSerch);
					
				}else {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
					model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
					model.addAttribute(DATOSPAGINACION,datosPaginacion);
				}
				return "cce/listaOperacionesPorAporbarAltoValorPaginateSearhMonto";
			}
			
			
			
		} catch (CustomException e) {
			e.printStackTrace();
			model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
			model.addAttribute(DATOSPAGINACION,datosPaginacion);
			model.addAttribute(MENSAJEERROR,e.getMessage());
			return "cce/listaOperacionesPorAporbarAltoValorPaginate";
		}
		
		
	}
	
	
	@GetMapping("/searchOredenante")
	public String searchOredenante(@ModelAttribute("cceTransaccionDtoSearch") CceTransaccionDto cceTransaccionDtoSearch,
			 Model model) {
		log.info("ordenante: " + cceTransaccionDtoSearch.getNumeroIdentificacion());
		List<String> listaError = new ArrayList<>();
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest();
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		
		try {
			CceTransaccionDto cceTransaccionDto = getTransaccionesMostrar();
			log.info("cceTransaccionDtoMostar: "+cceTransaccionDto.getNumeroAprobacionesLotes() +"-"+cceTransaccionDto.getMontoAprobacionesLotes());
			model.addAttribute(NUMEROAPROBACIONESLOTES,cceTransaccionDto.getNumeroAprobacionesLotes());
			model.addAttribute(MONTOAPROBACIONESLOTES,cceTransaccionDto.getMontoAprobacionesLotes());
			//request.getRemoteAddr()
			aprobacionesConsultasRequest.setNumeroPagina(1);   
			//aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
			aprobacionesConsultasRequest.setTamanoPagina(5);
			Filtros filtros = new Filtros();
			//filtros.setReferencia(null);
			filtros.setStatus("I");
			filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto())));
			filtros.setMontoHasta(montoTopeMaximoAproAuto);
			filtros.setNroIdEmisor(cceTransaccionDtoSearch.getNumeroIdentificacion());
			
			
			aprobacionesConsultasRequest.setFiltros(filtros);
			AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest);
			log.info("aprobacionesConsultasResponse "+aprobacionesConsultasResponse);
			if(aprobacionesConsultasResponse != null) {
				listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
				datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
				if(listaBCVLBTPorAprobar.isEmpty()) {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				}
				model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
				model.addAttribute(DATOSPAGINACION,datosPaginacion);
				model.addAttribute("ordenante", cceTransaccionDtoSearch.getNumeroIdentificacion());
				if(cceTransaccionDtoSearch.getNumeroIdentificacion().equals("")) {
					log.info("es blanco"+cceTransaccionDtoSearch.getNumeroIdentificacion());
					return "cce/listaOperacionesPorAporbarAltoValorPaginate";
				}
				return "cce/listaOperacionesPorAporbarAltoValorPaginateSearhOrdenante";
			}else {
				model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
				model.addAttribute(DATOSPAGINACION,datosPaginacion);
				model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				return "cce/listaOperacionesPorAporbarAltoValorPaginateSearhOrdenante";
			}
		} catch (CustomException e) {
			e.printStackTrace();
			model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
			model.addAttribute(DATOSPAGINACION,datosPaginacion);
			model.addAttribute(MENSAJEERROR,e.getMessage());
			return "cce/listaOperacionesPorAporbarAltoValorPaginate";
		}
		
		
	}
	
	@GetMapping("/procesarMovimientosPorAprobarAltoValorSearhMonto")
	public String consultaMovimientosPorAprobarAltovalorSearhMonto(@RequestParam("monto") BigDecimal monto, 
			@RequestParam("page") int page, Model model, HttpServletRequest request) {
		
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest(); 
		
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		log.info("cceMontoMaximoAproAuto: "+cceMontoMaximoAproAuto);
		log.info("montoTopeMaximoAproAuto: "+montoTopeMaximoAproAuto);
		//libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceTransaccionDto.getMonto()))
		
		//request.getRemoteAddr()
		aprobacionesConsultasRequest.setNumeroPagina(page);   
		//aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
		aprobacionesConsultasRequest.setTamanoPagina(5);
		Filtros filtros = new Filtros();
		//filtros.setReferencia(null);
		filtros.setStatus("I");
		filtros.setMontoDesde(monto);
		filtros.setMontoHasta(monto);
		
		
		
		aprobacionesConsultasRequest.setFiltros(filtros);
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		try {
			AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest);
			CceTransaccionDto cceTransaccionDto = getTransaccionesMostrar();
			log.info("cceTransaccionDtoMostar: "+cceTransaccionDto.getNumeroAprobacionesLotes() +"-"+cceTransaccionDto.getMontoAprobacionesLotes());
			model.addAttribute(NUMEROAPROBACIONESLOTES,cceTransaccionDto.getNumeroAprobacionesLotes());
			model.addAttribute(MONTOAPROBACIONESLOTES,cceTransaccionDto.getMontoAprobacionesLotes());
			if(aprobacionesConsultasResponse != null) {
				listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
				datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
				model.addAttribute("listaBCVLBTPorAprobar",listaBCVLBTPorAprobar);
				model.addAttribute("datosPaginacion",datosPaginacion);
				model.addAttribute("monto", monto);
			}else {
				model.addAttribute("listaBCVLBTPorAprobar",listaBCVLBTPorAprobar);
				model.addAttribute("datosPaginacion",datosPaginacion);
			}
			
		} catch (CustomException e) {
			e.printStackTrace();
			model.addAttribute("listaBCVLBTPorAprobar",listaBCVLBTPorAprobar);
			model.addAttribute("datosPaginacion",datosPaginacion);
			model.addAttribute("mensajeError",e.getMessage());
		}
		
		
		return "cce/listaOperacionesPorAporbarAltoValorPaginateSearhMonto";
	}
	
	
	
	
	public BigDecimal montoSerch(BigDecimal numero) {
		if(numero != null) {
			return libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(numero));
		}
		return new BigDecimal("0.00");
	}
	
	public BigDecimal montoAprobacionesLotes(List<BCVLBT> listaBCVLBTPorAprobarLotes) {
		
		BigDecimal montoAprobacionesLotes = new BigDecimal(0.00);
		
		for (BCVLBT bcvlbt : listaBCVLBTPorAprobarLotes) {
			log.info("montoLote: "+bcvlbt.getMonto());
			montoAprobacionesLotes = montoAprobacionesLotes.add(bcvlbt.getMonto());
		}
		
		return montoAprobacionesLotes;
	}
	
	public CceTransaccionDto getTransaccionesMostrar() throws CustomException{
		
		CceTransaccionDto cceTransaccionDto = new CceTransaccionDto();
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest();
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		log.info("cceMontoMaximoAproAuto: "+cceMontoMaximoAproAuto);
		log.info("montoTopeMaximoAproAuto: "+montoTopeMaximoAproAuto);
		//libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceTransaccionDto.getMonto()))
		
		//request.getRemoteAddr()
		aprobacionesConsultasRequest.setNumeroPagina(1);   
		//aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
		aprobacionesConsultasRequest.setTamanoPagina(2147483647);
		Filtros filtros = new Filtros();
		//filtros.setReferencia(null);
		filtros.setStatus("I");
		filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto())));
		filtros.setMontoHasta(montoTopeMaximoAproAuto);
		
		
		
		aprobacionesConsultasRequest.setFiltros(filtros);
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		try {
			AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest);
			
			if(aprobacionesConsultasResponse != null) {
				listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
				if(listaBCVLBTPorAprobar.isEmpty()) {
					cceTransaccionDto.setNumeroAprobacionesLotes(0);
					cceTransaccionDto.setMontoAprobacionesLotes(new BigDecimal("0.00"));
					
				}else {
					
					cceTransaccionDto.setNumeroAprobacionesLotes(listaBCVLBTPorAprobar.size());
					cceTransaccionDto.setMontoAprobacionesLotes(montoAprobacionesLotes(listaBCVLBTPorAprobar));
				}
				
			}else {
				cceTransaccionDto.setNumeroAprobacionesLotes(0);
				cceTransaccionDto.setMontoAprobacionesLotes(new BigDecimal("0.00"));
				
			}
			
		} catch (CustomException e) {
			e.printStackTrace();
			cceTransaccionDto.setNumeroAprobacionesLotes(0);
			cceTransaccionDto.setMontoAprobacionesLotes(new BigDecimal("0.00"));
			
		}
		return cceTransaccionDto;
		
		
	}
	
	
	
	public boolean isFechaValidaDesdeHasta(String fechaDesde, String fechaHasta) {
		
		SimpleDateFormat formato = new SimpleDateFormat(STRDATEFORMET);
		
        try {
        	
        	
        	Date fechaDate1 = formato.parse(fechaDesde);
        	Date fechaDate2 = formato.parse(fechaHasta);
        	
        	if ( fechaDate2.before(fechaDate1) ){
        	    log.info("La fechaHasta es menor que la fechaDesde");
        		return false;
        	}else{
        	     if ( fechaDate1.before(fechaDate2) ){
        	    	 log.info("La fechaDesde es menor que la fechaHasta");
        	    	 return true;
        	     }else{
        	    	 log.info("La fechaDesde es igual que la fechaHasta");
        	    	 return true;
        	     } 
        	}
        } 
        catch (ParseException ex) 
        {
        	log.error(ex.getMessage());
        }
        
        return false;
	}
	
	public Page<CceTransaccion> convertirLista(Page<CceTransaccion> listaTransacciones){
		for (CceTransaccion cceTransaccion : listaTransacciones) {
			log.info("estadoBcv: "+cceTransaccion.getEstadobcv());
			log.info("monto: "+ cceTransaccion.getMonto());
			log.info("montoformatNumber: "+ libreriaUtil.formatNumber(cceTransaccion.getMonto()));
			log.info("montostringToBigDecimal: "+ libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceTransaccion.getMonto())));
			cceTransaccion.setMonto(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceTransaccion.getMonto())));
		}
	
		return listaTransacciones;
	}
	
	public String nombreTransaccion(String codTransaccion) {
		String nombreTransaccion="";
		if(codTransaccion.equals("5724")) {
			nombreTransaccion = "Credito Inmediato Recibido";
		}else {
			if(codTransaccion.equals("5723")) {
				nombreTransaccion = "Credito Inmediato Enviado";
			}else {
				if(codTransaccion.equals("5728")) {
					nombreTransaccion = "Alto valor Recibido";
				}else {
					nombreTransaccion = "Alto Valor Enviado";
				}
			}
		}
		
		return nombreTransaccion;
	}
	
	public String nombreEstadoBcv(String estadobcv) {
		String nombreEstadoBcv="";
		
		if(estadobcv == null) {
			nombreEstadoBcv = "Incompleta";
		}else {
			if(estadobcv.equals("ACCP")) {
				nombreEstadoBcv = "Aprobada";
			}else {
				nombreEstadoBcv = "Rechazada";
			}
		}
		
	
		return nombreEstadoBcv;
	}
	
	
	public BancoRequest getBancoRequest() {
		BancoRequest bancoRequest = new BancoRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		bancoRequest.setIdUsuario(userName);
		bancoRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		return bancoRequest;
	}
	
	
	public AprobacionesRequest getAprobacionesRequest() {
		AprobacionesRequest aprobacionesRequest = new AprobacionesRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		aprobacionesRequest.setIdUsuario(userName);
		aprobacionesRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		aprobacionesRequest.setIdCanal(canal);
		return aprobacionesRequest;
	}
	
	public AprobacionesConsultasRequest getAprobacionesConsultasRequest() {
		AprobacionesConsultasRequest aprobacionesConsultasRequest = new AprobacionesConsultasRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		aprobacionesConsultasRequest.setIdUsuario(userName);
		aprobacionesConsultasRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		aprobacionesConsultasRequest.setIdCanal(canal);
		return aprobacionesConsultasRequest;
	}
	
	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		CceTransaccionDto cceTransaccionDto = new CceTransaccionDto();
		CceTransaccionDto cceTransaccionDtoSearch = new CceTransaccionDto();
		model.addAttribute("cceTransaccionDto", cceTransaccionDto);
		model.addAttribute("cceTransaccionDtoSearch", cceTransaccionDtoSearch);
		
	}
}
