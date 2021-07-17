package com.bancoexterior.app.cce.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bancoexterior.app.cce.dto.AprobacionesConsultasRequest;
import com.bancoexterior.app.cce.dto.AprobacionesConsultasResponse;
import com.bancoexterior.app.cce.dto.AprobacionesRequest;
import com.bancoexterior.app.cce.dto.BancoRequest;
import com.bancoexterior.app.cce.dto.CceTransaccionDto;
import com.bancoexterior.app.cce.dto.FiToFiCustomerCreditTransferRequest;
import com.bancoexterior.app.cce.dto.Sglbtr;
import com.bancoexterior.app.cce.model.BCVLBT;
import com.bancoexterior.app.cce.model.Banco;
import com.bancoexterior.app.cce.model.CceMontoMaximoAproAuto;
import com.bancoexterior.app.cce.model.CceTransaccion;
import com.bancoexterior.app.cce.model.DatosPaginacion;
import com.bancoexterior.app.cce.model.FIToFICstmrCdtTrfInitnDetalle;
import com.bancoexterior.app.cce.model.Filtros;
import com.bancoexterior.app.cce.model.GrpHdrObject;
import com.bancoexterior.app.cce.model.Moneda;
import com.bancoexterior.app.cce.model.ParamIdentificacion;
import com.bancoexterior.app.cce.model.PmtInfObject;
import com.bancoexterior.app.cce.service.IBancoService;
import com.bancoexterior.app.cce.service.IBcvlbtService;
import com.bancoexterior.app.cce.service.ICceMontoMaximoAproAutoService;
import com.bancoexterior.app.cce.service.ICceTransaccionService;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.util.ConsultaExcelExporter;
import com.bancoexterior.app.util.LibreriaUtil;
import com.bancoexterior.app.util.MovimientosExcelExporter;

import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("/ccetransacciones")
public class CceTransaccionController {
	
	private static final Logger LOGGER = LogManager.getLogger(CceTransaccionController.class);
	
	//@Autowired
	private ICceTransaccionService service;
	
	//@Autowired
	private ICceMontoMaximoAproAutoService montoMaximoAproAutoService; 
	
	@Autowired
	private IBancoService bancoService;
	
	@Autowired
	private IBcvlbtService bcvlbtService;
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${${app.ambiente}"+".canal}")
    private String canal;	
	
	@Value("${${app.ambiente}"+".trasacciones.numeroRegistroPage}")
    private int numeroRegistroPage;
	
	@Value("${${app.ambiente}"+".trasacciones.montoTopeMaximoAproAuto}")
    private BigDecimal montoTopeMaximoAproAuto;
	
	private static final String URLFORMCONSULTARMOVIMIENTOSALTOBAJOVALOR = "cce/formConsultarMovimientosAltoBajoValor";
	
	private static final String URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR = "cce/formConsultarOperacionesAprobarAltoBajoValor";
	
	private static final String URLFORMMOVIMIENTOSALTOBAJOVALORDETALLEFECHAS = "cce/formMovimientoAltoBajoValorDetalleFechas";
	
	private static final String URLFORMAPROBARALTOVALORLOTEAUTOMATICO = "cce/formAprobarAltoValorLoteAutomatico";
																				   
	private static final String URLLISTAMOVIMIENTOSCONSULTAALTOBAJOVALORPAGINATE = "cce/listaMovimientosConsultaAltoBajoValorPaginate";
	
	private static final String URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE = "cce/listaOperacionesPorAporbarAltoValorPaginate";
	
	private static final String STRDATEFORMET = "yyyy-MM-dd";
	
	private static final String LISTATRANSACCIONES = "listaTransacciones";
	
	private static final String LISTATRANSACCIONESEXCEL = "listaTransacciones";
	
	private static final String LISTABCVLBTPORAPROBAR = "listaBCVLBTPorAprobar";
	
	private static final String LISTABCVLBTPORAPROBARSELECCION = "listaBCVLBTPorAprobarSeleccion";
	
	private static final String DATOSPAGINACION = "datosPaginacion";
	
	private static final String CODTRANSACCION = "codTransaccion";
	
	private static final String BANCODESTINO = "bancoDestino";
	
	private static final String NUMEROIDENTIFICACION = "numeroIdentificacion";
	
	private static final String FECHADESDE = "fechaDesde";
	
	private static final String FECHAHASTA = "fechaHasta";
	
	private static final String MONTODESDE = "montoDesde";
	
	private static final String MONTOHASTA = "montoHasta";
	
	private static final String BANCOEMISOR = "bancoEmisor";
	
	private static final String NROIDEMISOR = "nroIdEmisor";
	
	private static final String NUMEROAPROBACIONESLOTES = "numeroAprobacionesLotes";
	
	private static final String MONTOAPROBACIONESLOTES = "montoAprobacionesLotes";
	
	private static final String LISTABANCOS = "listaBancos";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String LISTAERRORFECHA = "listaErrorFecha";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String MENSAJEFECHASINVALIDAS = "Los valores de las fechas son invalidos";
	
	private static final String MENSAJEMONTOSINVALIDAS = "Los valores de los montos son invalidos";
	
	private static final String MENSAJENORESULTADO = "Operacion Exitosa.La consulta no arrojo resultado.";
	
	private static final String MENSAJENORESULTADOLOTE = "No se encontraron operaciones Alto Valor Lote que procesar.";
	
	private static final String MENSAJEFUERARANGO = "El monto a consultar esta fuera de rango Alto Valor Lote Automatico.";
	
	private static final String CCETRANSACCIONCONTROLLERFORMCONSULTARMOVIMIENTOSALTOBAJOVALORI = "[==== INICIO FormConsultarMovimientosAltoBajoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERFORMCONSULTARMOVIMIENTOSALTOBAJOVALORF = "[==== FIN FormConsultarMovimientosAltoBajoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERPROCESARCONSULTARMOVIMIENTOSALTOBAJOVALORI = "[==== INICIO ProcesarConsultarMovimientosAltoBajoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERPROCESARCONSULTARMOVIMIENTOSALTOBAJOVALORF = "[==== FIN ProcesarConsultarMovimientosAltoBajoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERCONSULTARMOVIMIENTOSALTOBAJOVALORI = "[==== INICIO ConsultarMovimientosAltoBajoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERCONSULTARMOVIMIENTOSALTOBAJOVALORF = "[==== FIN ConsultarMovimientosAltoBajoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERVERDETALLEMOVIMIENTOSI = "[==== INICIO VerDeatlleMovimientos CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERVERDETALLEMOVIMIENTOSF = "[==== FIN VerDetalleMovimientos CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERFORMAPROBARALTOVALORLOTEAUTOMATICOI = "[==== INICIO FormAprobarAltoValorLoteAutomatico CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERFORMAPROBARALTOVALORLOTEAUTOMATICOF = "[==== FIN FormAprobarAltoValorLoteAutomatico CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERPROCESARAPROBARALTOVALORLOTEAUTOMATICOI = "[==== INICIO ProcesarAprobarAltoValorLoteAutomatico CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERPROCESARAPROBARALTOVALORLOTEAUTOMATICOF = "[==== FIN ProcesarAprobarAltoValorLoteAutomatico CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERFORMCONSULTAROPERACIONESAPROBARALTOVALORI = "[==== INICIO FormConsultarOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERFORMCONSULTAROPERACIONESAPROBARALTOVALORF = "[==== FIN FormConsultarOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERPROCESARCONSULTAROPERACIONESAPROBARALTOVALORI = "[==== INICIO ProcesarConsultarOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERPROCESARCONSULTAROPERACIONESAPROBARALTOVALORF = "[==== FIN ProcesarConsultarOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";
	
	private static final String CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORI = "[==== INICIO ConsultarOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";

	private static final String CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORF = "[==== FIN ConsultarOperacionesAprobarAltoValor CceTransaccion Consultas - Controller ====]";
	
	//procesarConsultaOperacionesAprobarAltoBajoValorPageable
	
	
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
		LOGGER.info(CCETRANSACCIONCONTROLLERFORMCONSULTARMOVIMIENTOSALTOBAJOVALORI);
		
		BancoRequest bancoRequest = getBancoRequest();
		
		try {
			List<Banco> listaBancos  = bancoService.listaBancos(bancoRequest);
			model.addAttribute(LISTABANCOS, listaBancos);
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(LISTAERROR, e.getMessage());
		}
		LOGGER.info(CCETRANSACCIONCONTROLLERFORMCONSULTARMOVIMIENTOSALTOBAJOVALORF);
		return URLFORMCONSULTARMOVIMIENTOSALTOBAJOVALOR;
		
	}
					
				
	@GetMapping("/procesarConsultaMovimientosAltoBajoValorPageable")
	public String procesarConsultaMovimientosAltoBajoValorPageable(CceTransaccionDto cceTransaccionDto, 
			Model model, Pageable page, HttpSession httpSession) {
		LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARCONSULTARMOVIMIENTOSALTOBAJOVALORI);
		
		
		BancoRequest bancoRequest = getBancoRequest();
		
		try {
			
			List<String> listaError = new ArrayList<>();
			Page<CceTransaccion> listaTransacciones;
			
			if(libreriaUtil.isFechaValidaDesdeHasta(cceTransaccionDto.getFechaDesde(), cceTransaccionDto.getFechaHasta())){
				LOGGER.info("hablame mano");	
				listaTransacciones = service.consultaMovimientosConFechas(cceTransaccionDto.getCodTransaccion(), cceTransaccionDto.getBancoDestino(),
					cceTransaccionDto.getNumeroIdentificacion(),cceTransaccionDto.getFechaDesde(), cceTransaccionDto.getFechaHasta(), page);
			
				
				listaTransacciones = convertirLista(listaTransacciones);
				LOGGER.info("Nro de listaTransacciones: "+listaTransacciones.getTotalElements());
				
				List<CceTransaccionDto> listaTransaccionesDto = service.consultaMovimientosConFechas(cceTransaccionDto.getCodTransaccion(),
						cceTransaccionDto.getBancoDestino(), cceTransaccionDto.getNumeroIdentificacion(), cceTransaccionDto.getFechaDesde(), cceTransaccionDto.getFechaHasta());
				LOGGER.info("Nro de listaTransaccionesDto: "+listaTransaccionesDto.size());
				httpSession.setAttribute(LISTATRANSACCIONESEXCEL, listaTransaccionesDto);	
					
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
				LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARCONSULTARMOVIMIENTOSALTOBAJOVALORF);
				return URLLISTAMOVIMIENTOSCONSULTAALTOBAJOVALORPAGINATE;
					
			}else {
				LOGGER.info("fechas invalidas");
				listaError.add(MENSAJEFECHASINVALIDAS);
				model.addAttribute(LISTAERROR, listaError);
				return URLFORMCONSULTARMOVIMIENTOSALTOBAJOVALOR;
			}
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(LISTAERROR, e.getMessage());
			return URLFORMCONSULTARMOVIMIENTOSALTOBAJOVALOR;
		}
		
		
		
	}
	            
	@GetMapping("/consultaMovimientosAltoBajoValorPageable")
	public String consultaMovimientosAltoBajoValorPageable(@RequestParam("codTransaccion") String codTransaccion, 
			@RequestParam("bancoDestino") String bancoDestino, @RequestParam("numeroIdentificacion") String numeroIdentificacion,
			@RequestParam("fechaDesde") String fechaDesde, @RequestParam("fechaHasta") String fechaHasta, 
			Model model, Pageable page) {
		LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTARMOVIMIENTOSALTOBAJOVALORI);
		
		List<String> listaError = new ArrayList<>();
		Page<CceTransaccion> listaTransacciones;
		
		if(libreriaUtil.isFechaValidaDesdeHasta(fechaDesde, fechaHasta)){
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
			LOGGER.info("fechas invalidas");
			listaError.add(MENSAJEFECHASINVALIDAS);
			model.addAttribute(LISTAERROR, listaError);
			
		}
		LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTARMOVIMIENTOSALTOBAJOVALORF);
		return URLLISTAMOVIMIENTOSCONSULTAALTOBAJOVALORPAGINATE;
	}
	
	
	
	
	
	@GetMapping("/detalleMovimiento")
	public String verMovimineto(@RequestParam("endtoendId") String endtoendId,@RequestParam("codTransaccion") String codTransaccion, 
			@RequestParam("bancoDestino") String bancoDestino, @RequestParam("numeroIdentificacion") String numeroIdentificacion,
			@RequestParam("fechaDesde") String fechaDesde, @RequestParam("fechaHasta") String fechaHasta, 
			Model model, Pageable page) {
		LOGGER.info(CCETRANSACCIONCONTROLLERVERDETALLEMOVIMIENTOSI);
		
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
			LOGGER.info(CCETRANSACCIONCONTROLLERVERDETALLEMOVIMIENTOSF);
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
	
	
	@GetMapping("/formAprobarMovimientosAltoValorLoteAutomatico")
	public String formAprobarAltoValorLoteAutomatico(CceTransaccionDto cceTransaccionDto, Model model, HttpSession httpSession) {
		LOGGER.info(CCETRANSACCIONCONTROLLERFORMAPROBARALTOVALORLOTEAUTOMATICOI);
				
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest(); 
		
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
	
		aprobacionesConsultasRequest.setNumeroPagina(1);   
		aprobacionesConsultasRequest.setTamanoPagina(2147483647);
		Filtros filtros = new Filtros();
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
					cceTransaccionDto.setMontoAprobacionesLotes(libreriaUtil.montoAprobacionesLotes(listaBCVLBTPorAprobar));
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
		LOGGER.info(CCETRANSACCIONCONTROLLERFORMAPROBARALTOVALORLOTEAUTOMATICOF);
		return URLFORMAPROBARALTOVALORLOTEAUTOMATICO;
		
	}
	
	@GetMapping("/procesarAprobarAltoValorLoteAutomatico")
	public String procesarAprobarAltoValorLoteAutomatico(CceTransaccionDto cceTransaccionDto, Model model, HttpSession httpSession) {
		LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARAPROBARALTOVALORLOTEAUTOMATICOI);
		List<BCVLBT> listaBCVLBTPorAprobar =(List<BCVLBT>)httpSession.getAttribute(LISTABCVLBTPORAPROBAR);
		
		FiToFiCustomerCreditTransferRequest FiToFiCustomerCreditTransferRequest = new FiToFiCustomerCreditTransferRequest(); 
		
		Sglbtr sglbtr = new Sglbtr();
		FIToFICstmrCdtTrfInitnDetalle fIToFICstmrCdtTrfInitnDetalle = new FIToFICstmrCdtTrfInitnDetalle(); 
		GrpHdrObject grpHdr = new GrpHdrObject();
		Moneda moneda = new Moneda();
		for (BCVLBT bcvlbt : listaBCVLBTPorAprobar) {
			LOGGER.info("bcvlbt: "+bcvlbt);
			
			
			try {
				//creando el ParamIdentificacion de la esctructura
				ParamIdentificacion paramIdentificacion = getParamIdentificacion();
				paramIdentificacion.setCodTransaccion(bcvlbt.getCodTransaccion());
				paramIdentificacion.setBancoReceptor(getBancoReceptor(bcvlbt.getBancoReceptor()).getNbBanco());
				
				//creando el grpHdr de la esctructura
				grpHdr.setMsgId(getMsgId());
				grpHdr.setCreDtTm(libreriaUtil.fechayhora());
				grpHdr.setNbOfTxs(1);
				
				moneda.setCcy(bcvlbt.getCodMoneda());
				moneda.setAmt(bcvlbt.getMonto().doubleValue());
				grpHdr.setCtrlSum(moneda);
				grpHdr.setLclInstrm(libreriaUtil.getProducto(bcvlbt.getProducto()));
				grpHdr.setChannel(libreriaUtil.getChannel());
				
				PmtInfObject pmtInfObject1 = new PmtInfObject();
				pmtInfObject1.setRegId(1);
				pmtInfObject1.setEndToEndId(BANCODESTINO);
				
			} catch (CustomException e) {
				e.printStackTrace();
			}
			
		}
		
		
		
		
		LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARAPROBARALTOVALORLOTEAUTOMATICOF);
		return "/index";
	}	
	
	
	
	@GetMapping("/formConsultaOperacionesAprobarAltoBajoValor")
	public String formConsultaOperacionesAprobarAltoBajoValor(CceTransaccionDto cceTransaccionDto, Model model) {
		LOGGER.info(CCETRANSACCIONCONTROLLERFORMCONSULTAROPERACIONESAPROBARALTOVALORI);
		
		BancoRequest bancoRequest = getBancoRequest();
		
		try {
			List<Banco> listaBancos  = bancoService.listaBancos(bancoRequest);
			model.addAttribute(LISTABANCOS, listaBancos);
		} catch (CustomException e) {
			LOGGER.error(e.getMessage());
			model.addAttribute(LISTAERROR, e.getMessage());
		}
		LOGGER.info(CCETRANSACCIONCONTROLLERFORMCONSULTAROPERACIONESAPROBARALTOVALORF);
		return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
		
	}
	
			 
	@GetMapping("/procesarConsultaOperacionesAprobarAltoBajoValorPageable")
	public String procesarConsultaOperacionesAprobarAltoBajoValorPageable(CceTransaccionDto cceTransaccionDto, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, HttpSession httpSession) {
		LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARCONSULTAROPERACIONESAPROBARALTOVALORI);
		LOGGER.info("llegue hasta aqui 1");
		BancoRequest bancoRequest = getBancoRequest();
		List<String> listaError = new ArrayList<>();
		List<Banco> listaBancos = new ArrayList<>();
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest();
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		try {
			LOGGER.info("llegue hasta aqui 2");
			listaBancos  = bancoService.listaBancos(bancoRequest);
			CceTransaccionDto cceTransaccionDtoMostrar = getTransaccionesMostrar();
			model.addAttribute(NUMEROAPROBACIONESLOTES,cceTransaccionDtoMostrar.getNumeroAprobacionesLotes());
			model.addAttribute(MONTOAPROBACIONESLOTES,libreriaUtil.formatNumber(cceTransaccionDtoMostrar.getMontoAprobacionesLotes()));
			//request.getRemoteAddr()
			aprobacionesConsultasRequest.setNumeroPagina(1);   
			//aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
			aprobacionesConsultasRequest.setTamanoPagina(5);
			Filtros filtros = new Filtros();
			LOGGER.info("llegue hasta aqui 3");
			//filtros.setReferencia(null);
			filtros.setStatus("I");
			if (result.hasErrors()) {
				LOGGER.info("llegue hasta aqui result.hasErrors()");
				for (ObjectError error : result.getAllErrors()) {
					LOGGER.info("Ocurrio un error: " + error.getDefaultMessage());
					if(error.getCode().equals("typeMismatch")) {
						listaError.add("El valor del monto debe ser numerico");
					}
				}
				model.addAttribute(LISTAERROR, listaError);
				model.addAttribute(LISTABANCOS, listaBancos);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
				
				
			if(libreriaUtil.isMontoDesdeMontoHastaDistintoNull(cceTransaccionDto.getMontoDesde(), cceTransaccionDto.getMontoDesde())) {
				LOGGER.info("llegue hasta aqui dsitinto de null");
				LOGGER.info("montoDesde: "+cceTransaccionDto.getMontoDesde());
				LOGGER.info("montoHasta: "+cceTransaccionDto.getMontoHasta());
				if(libreriaUtil.montoSerch(cceTransaccionDto.getMontoDesde()).compareTo(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto()))) < 0) {
					LOGGER.info("entro por fuera rango");
					listaError.add(MENSAJEFUERARANGO);
					model.addAttribute(LISTAERROR, listaError);
					model.addAttribute(LISTABANCOS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
					
				if(cceTransaccionDto.getMontoHasta().compareTo(cceTransaccionDto.getMontoDesde()) < 0) { 
					LOGGER.info("entro monto desde menor que monto hasta");
					listaError.add(MENSAJEMONTOSINVALIDAS);
					model.addAttribute(LISTAERROR, listaError);
					model.addAttribute(LISTABANCOS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
				LOGGER.info("llegue hasta aqui filtra por montos");
					
					
				filtros.setMontoDesde(cceTransaccionDto.getMontoDesde());
				filtros.setMontoHasta(cceTransaccionDto.getMontoHasta());
			}else {
				LOGGER.info("llegue hasta aqui no hay montos");
				filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto())));
				filtros.setMontoHasta(montoTopeMaximoAproAuto);
			}
			if(!cceTransaccionDto.getFechaDesde().equals("") && !cceTransaccionDto.getFechaHasta().equals("")) {
				if(libreriaUtil.isFechaHoraValidaDesdeHasta(cceTransaccionDto.getFechaDesde(), cceTransaccionDto.getFechaHasta())) {
					LOGGER.info("fechaDesdeNueva: "+getFechaHoraDesdeFormato(cceTransaccionDto.getFechaDesde()));
					LOGGER.info("fechaHastaNueva: "+getFechaHoraHastaFormato(cceTransaccionDto.getFechaHasta()));
					filtros.setFechaDesde(getFechaHoraDesdeFormato(cceTransaccionDto.getFechaDesde()));
					filtros.setFechaHasta(getFechaHoraHastaFormato(cceTransaccionDto.getFechaHasta()));
				        
				}
			}
					
			if(!cceTransaccionDto.getBancoDestino().equals(""))
				filtros.setBancoBeneficiario(cceTransaccionDto.getBancoDestino());
					
			if(!cceTransaccionDto.getNumeroIdentificacion().equals(""))
				filtros.setNroIdEmisor(cceTransaccionDto.getNumeroIdentificacion());
					
			aprobacionesConsultasRequest.setFiltros(filtros);
			LOGGER.info("filtros: "+filtros);
			AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest);
					
			if(aprobacionesConsultasResponse != null) {
				listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
				listaBCVLBTPorAprobar = convertirListaBCVLT(listaBCVLBTPorAprobar);
				listaBCVLBTPorAprobar = convertirListaBCVLTSeleccionadosFalse(listaBCVLBTPorAprobar);
				LOGGER.info("antes de guardar en sesion");
				httpSession.setAttribute(LISTABCVLBTPORAPROBARSELECCION, listaBCVLBTPorAprobar);
				LOGGER.info("despues de guardar en sesion");
				BigDecimal montoAprobarOperacionesSeleccionadas = libreriaUtil.montoAprobarOperacionesSeleccionadas(listaBCVLBTPorAprobar);
				datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
				if(listaBCVLBTPorAprobar.isEmpty()) {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				}
				model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
				model.addAttribute(LISTABANCOS, listaBancos);
				model.addAttribute(DATOSPAGINACION,datosPaginacion);
				model.addAttribute(MONTODESDE, cceTransaccionDto.getMontoDesde());
				model.addAttribute(MONTOHASTA, cceTransaccionDto.getMontoHasta());
				model.addAttribute(BANCOEMISOR, cceTransaccionDto.getBancoDestino());
				model.addAttribute(NROIDEMISOR, cceTransaccionDto.getNumeroIdentificacion());
				model.addAttribute(FECHADESDE, cceTransaccionDto.getFechaDesde());
				model.addAttribute(FECHAHASTA, cceTransaccionDto.getFechaHasta());
				model.addAttribute("selecionados", false);
				model.addAttribute("montoAprobarOperacionesSeleccionadas", montoAprobarOperacionesSeleccionadas);
				LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE;
			}else {
				listaError.add(MENSAJENORESULTADO);
				model.addAttribute(LISTAERROR, listaError);
				model.addAttribute(LISTABANCOS, listaBancos);
				model.addAttribute("selecionados", false);
				model.addAttribute("montoAprobarOperacionesSeleccionadas", "0.00");
				LOGGER.info(CCETRANSACCIONCONTROLLERPROCESARCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
					
				
		} catch (CustomException e) {
			e.printStackTrace();
			model.addAttribute(LISTAERROR, e.getMessage());
			model.addAttribute(LISTABANCOS, listaBancos);
			return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
		}
				
		
	}
	
	@GetMapping("/consultaOperacionesAprobarAltoBajoValorPageable")
	public String consultaOperacionesAprobarAltoBajoValorPageable(@RequestParam("montoDesde") BigDecimal montoDesde, @RequestParam("montoHasta") BigDecimal montoHasta, 
			@RequestParam("bancoEmisor") String bancoEmisor, @RequestParam("nroIdEmisor") String nroIdEmisor, @RequestParam("fechaDesde") String fechaDesde,
			@RequestParam("fechaHasta") String fechaHasta, @RequestParam("page") int page, Model model, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORI);
		LOGGER.info("montoDesde: "+montoDesde);
		LOGGER.info("montoHasta: "+montoHasta);
		LOGGER.info("bancoEmisor: "+bancoEmisor);
		LOGGER.info("nroIdEmisor: "+nroIdEmisor);
		LOGGER.info("fechaDesde: "+fechaDesde);
		LOGGER.info("fechaHasta: "+fechaHasta);
		LOGGER.info("page: "+page);
		BancoRequest bancoRequest = getBancoRequest();
		List<String> listaError = new ArrayList<>();
		List<Banco> listaBancos = new ArrayList<>();
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest();
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		try {
			LOGGER.info("llegue hasta aqui 2");
			listaBancos  = bancoService.listaBancos(bancoRequest);
			CceTransaccionDto cceTransaccionDtoMostrar = getTransaccionesMostrar();
			model.addAttribute(NUMEROAPROBACIONESLOTES,cceTransaccionDtoMostrar.getNumeroAprobacionesLotes());
			model.addAttribute(MONTOAPROBACIONESLOTES,libreriaUtil.formatNumber(cceTransaccionDtoMostrar.getMontoAprobacionesLotes()));
			//request.getRemoteAddr()
			aprobacionesConsultasRequest.setNumeroPagina(page);   
			//aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
			aprobacionesConsultasRequest.setTamanoPagina(5);
			Filtros filtros = new Filtros();
			LOGGER.info("llegue hasta aqui 3");
			//filtros.setReferencia(null);
			filtros.setStatus("I");
								
			if(montoDesde != null && montoHasta != null) {
				LOGGER.info("llegue hasta aqui dsitinto de null");
				LOGGER.info("montoDesde: "+montoDesde);
				LOGGER.info("montoHasta: "+montoHasta);
				if(libreriaUtil.montoSerch(montoDesde).compareTo(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto()))) < 0) {
					LOGGER.info("entro por fuera rango");
					listaError.add(MENSAJEFUERARANGO);
					model.addAttribute(LISTAERROR, listaError);
					model.addAttribute(LISTABANCOS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
					
				if(montoHasta.compareTo(montoDesde) < 0) { 
					LOGGER.info("entro monto desde menor que monto hasta");
					listaError.add(MENSAJEMONTOSINVALIDAS);
					model.addAttribute(LISTAERROR, listaError);
					model.addAttribute(LISTABANCOS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
				LOGGER.info("llegue hasta aqui filtra por montos");
				filtros.setMontoDesde(montoDesde);
				filtros.setMontoHasta(montoHasta);
			}else {
				LOGGER.info("llegue hasta aqui no hay montos");
				filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto())));
				filtros.setMontoHasta(montoTopeMaximoAproAuto);
			}
			if(!fechaDesde.equals("") && !fechaHasta.equals("")) {
				if(libreriaUtil.isFechaHoraValidaDesdeHasta(fechaDesde, fechaHasta)) {
					LOGGER.info("fechaDesdeNueva: "+getFechaHoraDesdeFormato(fechaDesde));
					LOGGER.info("fechaHastaNueva: "+getFechaHoraHastaFormato(fechaHasta));
					filtros.setFechaDesde(getFechaHoraDesdeFormato(fechaDesde));
					filtros.setFechaHasta(getFechaHoraHastaFormato(fechaHasta));	
					/*
					String[] arrOfFechaD = fechaDesde.split("T");
					String fechaDesdeT = arrOfFechaD[0];
					String horaDesde = arrOfFechaD[1];
					String[] arrOfFechaH = fechaHasta.split("T");
					String fechaHastaT = arrOfFechaH[0];
					String horaHasta = arrOfFechaH[1];
					fechaDesdeT = getFechaDiaMesAno(fechaDesdeT);
					horaDesde = getHora(horaDesde);
					fechaDesdeT = fechaDesdeT+" "+horaDesde+":00.000000";
					log.info("fechaDesdeT: "+fechaDesdeT);
						
					fechaHastaT = getFechaDiaMesAno(fechaHastaT);
					horaHasta = getHora(horaHasta);
					fechaHastaT = fechaHastaT+" "+horaHasta+":59.000000";
					log.info("fechaHastaT: "+fechaHastaT);
				        
					filtros.setFechaDesde(fechaDesdeT);
					filtros.setFechaHasta(fechaHastaT);*/
				        
				}
			}
					
					
					
			if(!bancoEmisor.equals(""))
				filtros.setBancoBeneficiario(bancoEmisor);
					
			if(!nroIdEmisor.equals(""))
				filtros.setNroIdEmisor(nroIdEmisor);
					
			aprobacionesConsultasRequest.setFiltros(filtros);
			LOGGER.info("filtros: "+filtros);
			AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest);
					
			if(aprobacionesConsultasResponse != null) {
				listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
				listaBCVLBTPorAprobar = convertirListaBCVLT(listaBCVLBTPorAprobar);
				listaBCVLBTPorAprobar = convertirListaBCVLTSeleccionadosFalse(listaBCVLBTPorAprobar);
				httpSession.setAttribute(LISTABCVLBTPORAPROBARSELECCION, listaBCVLBTPorAprobar);
				BigDecimal montoAprobarOperacionesSeleccionadas = libreriaUtil.montoAprobarOperacionesSeleccionadas(listaBCVLBTPorAprobar);
				datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
				if(listaBCVLBTPorAprobar.isEmpty()) {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				}
				model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
				model.addAttribute(LISTABANCOS, listaBancos);
				model.addAttribute(DATOSPAGINACION,datosPaginacion);
				model.addAttribute(MONTODESDE, montoDesde);
				model.addAttribute(MONTOHASTA, montoHasta);
				model.addAttribute(BANCOEMISOR, bancoEmisor);
				model.addAttribute(NROIDEMISOR, nroIdEmisor);
				model.addAttribute(FECHADESDE, fechaDesde);
				model.addAttribute(FECHAHASTA, fechaHasta);
				model.addAttribute("selecionados", false);
				model.addAttribute("montoAprobarOperacionesSeleccionadas", montoAprobarOperacionesSeleccionadas);
				LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE;
			}else {
				listaError.add(MENSAJENORESULTADO);
				model.addAttribute(LISTAERROR, listaError);
				model.addAttribute(LISTABANCOS, listaBancos);
				model.addAttribute("selecionados", false);
				model.addAttribute("montoAprobarOperacionesSeleccionadas", "0.00");
				LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}	
		} catch (CustomException e) {
			e.printStackTrace();
			model.addAttribute(LISTAERROR, e.getMessage());
			model.addAttribute(LISTABANCOS, listaBancos);
			return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
		}
	}
	
	
	
	@GetMapping("/seleccionarTodosAprobarOperaciones")
	public String seleccionarTodosAprobarOperaciones(@RequestParam("montoDesde") BigDecimal montoDesde, @RequestParam("montoHasta") BigDecimal montoHasta, 
			@RequestParam("bancoEmisor") String bancoEmisor, @RequestParam("nroIdEmisor") String nroIdEmisor, @RequestParam("fechaDesde") String fechaDesde,
			@RequestParam("fechaHasta") String fechaHasta, @RequestParam("page") int page, Model model, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info("me llamo you");
		LOGGER.info("montoDesde: "+montoDesde);
		LOGGER.info("montoHasta: "+montoHasta);
		LOGGER.info("bancoEmisor: "+bancoEmisor);
		LOGGER.info("nroIdEmisor: "+nroIdEmisor);
		LOGGER.info("fechaDesde: "+fechaDesde);
		LOGGER.info("fechaHasta: "+fechaHasta);
		LOGGER.info("page: "+page);
		BancoRequest bancoRequest = getBancoRequest();
		List<String> listaError = new ArrayList<>();
		List<Banco> listaBancos = new ArrayList<>();
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest();
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		try {
			LOGGER.info("llegue hasta aqui 2");
			listaBancos  = bancoService.listaBancos(bancoRequest);
			CceTransaccionDto cceTransaccionDtoMostrar = getTransaccionesMostrar();
			model.addAttribute(NUMEROAPROBACIONESLOTES,cceTransaccionDtoMostrar.getNumeroAprobacionesLotes());
			model.addAttribute(MONTOAPROBACIONESLOTES,libreriaUtil.formatNumber(cceTransaccionDtoMostrar.getMontoAprobacionesLotes()));
			//request.getRemoteAddr()
			aprobacionesConsultasRequest.setNumeroPagina(page);   
			//aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
			aprobacionesConsultasRequest.setTamanoPagina(5);
			Filtros filtros = new Filtros();
			LOGGER.info("llegue hasta aqui 3");
			//filtros.setReferencia(null);
			filtros.setStatus("I");
								
			if(montoDesde != null && montoHasta != null) {
				LOGGER.info("llegue hasta aqui dsitinto de null");
				LOGGER.info("montoDesde: "+montoDesde);
				LOGGER.info("montoHasta: "+montoHasta);
				if(libreriaUtil.montoSerch(montoDesde).compareTo(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto()))) < 0) {
					LOGGER.info("entro por fuera rango");
					listaError.add(MENSAJEFUERARANGO);
					model.addAttribute(LISTAERROR, listaError);
					model.addAttribute(LISTABANCOS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
					
				if(montoHasta.compareTo(montoDesde) < 0) { 
					LOGGER.info("entro monto desde menor que monto hasta");
					listaError.add(MENSAJEMONTOSINVALIDAS);
					model.addAttribute(LISTAERROR, listaError);
					model.addAttribute(LISTABANCOS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
				LOGGER.info("llegue hasta aqui filtra por montos");
				filtros.setMontoDesde(montoDesde);
				filtros.setMontoHasta(montoHasta);
			}else {
				LOGGER.info("llegue hasta aqui no hay montos");
				filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto())));
				filtros.setMontoHasta(montoTopeMaximoAproAuto);
			}
			
			if(!fechaDesde.equals("") && !fechaHasta.equals("")) {
				if(libreriaUtil.isFechaHoraValidaDesdeHasta(fechaDesde, fechaHasta)) {
					String[] arrOfFechaD = fechaDesde.split("T");
					String fechaDesdeT = arrOfFechaD[0];
					String horaDesde = arrOfFechaD[1];
					String[] arrOfFechaH = fechaHasta.split("T");
					String fechaHastaT = arrOfFechaH[0];
					String horaHasta = arrOfFechaH[1];
					fechaDesdeT = getFechaDiaMesAno(fechaDesdeT);
					horaDesde = getHora(horaDesde);
					fechaDesdeT = fechaDesdeT+" "+horaDesde+":00.000000";
					LOGGER.info("fechaDesdeT: "+fechaDesdeT);
						
					fechaHastaT = getFechaDiaMesAno(fechaHastaT);
					horaHasta = getHora(horaHasta);
					fechaHastaT = fechaHastaT+" "+horaHasta+":59.000000";
					LOGGER.info("fechaHastaT: "+fechaHastaT);
				        
					filtros.setFechaDesde(fechaDesdeT);
					filtros.setFechaHasta(fechaHastaT);
				        
				}
			}
					
			if(!bancoEmisor.equals(""))
				filtros.setBancoBeneficiario(bancoEmisor);
					
			if(!nroIdEmisor.equals(""))
				filtros.setNroIdEmisor(nroIdEmisor);
					
			aprobacionesConsultasRequest.setFiltros(filtros);
			LOGGER.info("filtros: "+filtros);
			AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest);
					
			if(aprobacionesConsultasResponse != null) {
				listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
				listaBCVLBTPorAprobar = convertirListaBCVLT(listaBCVLBTPorAprobar);
				listaBCVLBTPorAprobar = convertirListaBCVLTSeleccionadosTrue(listaBCVLBTPorAprobar);
				httpSession.setAttribute(LISTABCVLBTPORAPROBARSELECCION, listaBCVLBTPorAprobar);
				BigDecimal montoAprobarOperacionesSeleccionadas = libreriaUtil.montoAprobarOperacionesSeleccionadas(listaBCVLBTPorAprobar);
				datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
				if(listaBCVLBTPorAprobar.isEmpty()) {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				}
				model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
				model.addAttribute(LISTABANCOS, listaBancos);
				model.addAttribute(DATOSPAGINACION,datosPaginacion);
				model.addAttribute(MONTODESDE, montoDesde);
				model.addAttribute(MONTOHASTA, montoHasta);
				model.addAttribute(BANCOEMISOR, bancoEmisor);
				model.addAttribute(NROIDEMISOR, nroIdEmisor);
				model.addAttribute(FECHADESDE, fechaDesde);
				model.addAttribute(FECHAHASTA, fechaHasta);
				model.addAttribute("selecionados", true);
				model.addAttribute("montoAprobarOperacionesSeleccionadas", montoAprobarOperacionesSeleccionadas);
				LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE;
			}else {
				listaError.add(MENSAJENORESULTADO);
				model.addAttribute(LISTAERROR, listaError);
				model.addAttribute(LISTABANCOS, listaBancos);
				model.addAttribute("selecionados", true);
				model.addAttribute("montoAprobarOperacionesSeleccionadas", "0.00");
				LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
							
		} catch (CustomException e) {
			e.printStackTrace();
			model.addAttribute(LISTAERROR, e.getMessage());
			model.addAttribute(LISTABANCOS, listaBancos);
			return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
		}
	}
	
	
	@GetMapping("/deseleccionarTodosAprobarOperaciones")
	public String deseleccionarTodosAprobarOperaciones(@RequestParam("montoDesde") BigDecimal montoDesde, @RequestParam("montoHasta") BigDecimal montoHasta, 
			@RequestParam("bancoEmisor") String bancoEmisor, @RequestParam("nroIdEmisor") String nroIdEmisor, @RequestParam("fechaDesde") String fechaDesde,
			@RequestParam("fechaHasta") String fechaHasta, @RequestParam("page") int page, Model model, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info("me llamo you");
		LOGGER.info("montoDesde: "+montoDesde);
		LOGGER.info("montoHasta: "+montoHasta);
		LOGGER.info("bancoEmisor: "+bancoEmisor);
		LOGGER.info("nroIdEmisor: "+nroIdEmisor);
		LOGGER.info("fechaDesde: "+fechaDesde);
		LOGGER.info("fechaHasta: "+fechaHasta);
		LOGGER.info("page: "+page);
		BancoRequest bancoRequest = getBancoRequest();
		List<String> listaError = new ArrayList<>();
		List<Banco> listaBancos = new ArrayList<>();
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest();
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		try {
			LOGGER.info("llegue hasta aqui 2");
			listaBancos  = bancoService.listaBancos(bancoRequest);
			CceTransaccionDto cceTransaccionDtoMostrar = getTransaccionesMostrar();
			model.addAttribute(NUMEROAPROBACIONESLOTES,cceTransaccionDtoMostrar.getNumeroAprobacionesLotes());
			model.addAttribute(MONTOAPROBACIONESLOTES,libreriaUtil.formatNumber(cceTransaccionDtoMostrar.getMontoAprobacionesLotes()));
			//request.getRemoteAddr()
			aprobacionesConsultasRequest.setNumeroPagina(page);   
			//aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
			aprobacionesConsultasRequest.setTamanoPagina(5);
			Filtros filtros = new Filtros();
			LOGGER.info("llegue hasta aqui 3");
			//filtros.setReferencia(null);
			filtros.setStatus("I");
								
			if(montoDesde != null && montoHasta != null) {
				LOGGER.info("llegue hasta aqui dsitinto de null");
				LOGGER.info("montoDesde: "+montoDesde);
				LOGGER.info("montoHasta: "+montoHasta);
				if(libreriaUtil.montoSerch(montoDesde).compareTo(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto()))) < 0) {
					LOGGER.info("entro por fuera rango");
					listaError.add(MENSAJEFUERARANGO);
					model.addAttribute(LISTAERROR, listaError);
					model.addAttribute(LISTABANCOS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
					
				if(montoHasta.compareTo(montoDesde) < 0) { 
					LOGGER.info("entro monto desde menor que monto hasta");
					listaError.add(MENSAJEMONTOSINVALIDAS);
					model.addAttribute(LISTAERROR, listaError);
					model.addAttribute(LISTABANCOS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
				LOGGER.info("llegue hasta aqui filtra por montos");
				filtros.setMontoDesde(montoDesde);
				filtros.setMontoHasta(montoHasta);
			}else {
				LOGGER.info("llegue hasta aqui no hay montos");
				filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto())));
				filtros.setMontoHasta(montoTopeMaximoAproAuto);
			}
			
			if(!fechaDesde.equals("") && !fechaHasta.equals("")) {
				if(libreriaUtil.isFechaHoraValidaDesdeHasta(fechaDesde, fechaHasta)) {
					String[] arrOfFechaD = fechaDesde.split("T");
					String fechaDesdeT = arrOfFechaD[0];
					String horaDesde = arrOfFechaD[1];
					String[] arrOfFechaH = fechaHasta.split("T");
					String fechaHastaT = arrOfFechaH[0];
					String horaHasta = arrOfFechaH[1];
					fechaDesdeT = getFechaDiaMesAno(fechaDesdeT);
					horaDesde = getHora(horaDesde);
					fechaDesdeT = fechaDesdeT+" "+horaDesde+":00.000000";
					LOGGER.info("fechaDesdeT: "+fechaDesdeT);
						
					fechaHastaT = getFechaDiaMesAno(fechaHastaT);
					horaHasta = getHora(horaHasta);
					fechaHastaT = fechaHastaT+" "+horaHasta+":59.000000";
					LOGGER.info("fechaHastaT: "+fechaHastaT);
				        
					filtros.setFechaDesde(fechaDesdeT);
					filtros.setFechaHasta(fechaHastaT);
				        
				}
			}
					
			if(!bancoEmisor.equals(""))
				filtros.setBancoBeneficiario(bancoEmisor);
					
			if(!nroIdEmisor.equals(""))
				filtros.setNroIdEmisor(nroIdEmisor);
					
			aprobacionesConsultasRequest.setFiltros(filtros);
			LOGGER.info("filtros: "+filtros);
			AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest);
					
			if(aprobacionesConsultasResponse != null) {
				listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
				listaBCVLBTPorAprobar = convertirListaBCVLT(listaBCVLBTPorAprobar);
				listaBCVLBTPorAprobar = convertirListaBCVLTSeleccionadosFalse(listaBCVLBTPorAprobar);
				httpSession.setAttribute(LISTABCVLBTPORAPROBARSELECCION, listaBCVLBTPorAprobar);
				BigDecimal montoAprobarOperacionesSeleccionadas = libreriaUtil.montoAprobarOperacionesSeleccionadas(listaBCVLBTPorAprobar);
				datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
				if(listaBCVLBTPorAprobar.isEmpty()) {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				}
				model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
				model.addAttribute(LISTABANCOS, listaBancos);
				model.addAttribute(DATOSPAGINACION,datosPaginacion);
				model.addAttribute(MONTODESDE, montoDesde);
				model.addAttribute(MONTOHASTA, montoHasta);
				model.addAttribute(BANCOEMISOR, bancoEmisor);
				model.addAttribute(NROIDEMISOR, nroIdEmisor);
				model.addAttribute(FECHADESDE, fechaDesde);
				model.addAttribute(FECHAHASTA, fechaHasta);
				model.addAttribute("selecionados", false);
				model.addAttribute("montoAprobarOperacionesSeleccionadas", montoAprobarOperacionesSeleccionadas);
				LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE;
			}else {
				listaError.add(MENSAJENORESULTADO);
				model.addAttribute(LISTAERROR, listaError);
				model.addAttribute(LISTABANCOS, listaBancos);
				model.addAttribute("selecionados", false);
				model.addAttribute("montoAprobarOperacionesSeleccionadas", "0.00");
				LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
							
		} catch (CustomException e) {
			e.printStackTrace();
			model.addAttribute(LISTAERROR, e.getMessage());
			model.addAttribute(LISTABANCOS, listaBancos);
			return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
		}
	}
	
	@GetMapping("/seleccionarUnaAprobarOperaciones")
	public String seleccionarUnaAprobarOperaciones(@RequestParam("montoDesde") BigDecimal montoDesde, @RequestParam("montoHasta") BigDecimal montoHasta, 
			@RequestParam("bancoEmisor") String bancoEmisor, @RequestParam("nroIdEmisor") String nroIdEmisor, @RequestParam("fechaDesde") String fechaDesde,
			@RequestParam("fechaHasta") String fechaHasta, @RequestParam("page") int page, @RequestParam("referencia") String referencia, Model model, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info("me llamo you");
		LOGGER.info("montoDesde: "+montoDesde);
		LOGGER.info("montoHasta: "+montoHasta);
		LOGGER.info("bancoEmisor: "+bancoEmisor);
		LOGGER.info("nroIdEmisor: "+nroIdEmisor);
		LOGGER.info("fechaDesde: "+fechaDesde);
		LOGGER.info("fechaHasta: "+fechaHasta);
		LOGGER.info("page: "+page);
		LOGGER.info("referencia: "+referencia);
		
		BancoRequest bancoRequest = getBancoRequest();
		List<String> listaError = new ArrayList<>();
		List<Banco> listaBancos = new ArrayList<>();
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest();
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		try {
			LOGGER.info("llegue hasta aqui 2");
			listaBancos  = bancoService.listaBancos(bancoRequest);
			CceTransaccionDto cceTransaccionDtoMostrar = getTransaccionesMostrar();
			model.addAttribute(NUMEROAPROBACIONESLOTES,cceTransaccionDtoMostrar.getNumeroAprobacionesLotes());
			model.addAttribute(MONTOAPROBACIONESLOTES,libreriaUtil.formatNumber(cceTransaccionDtoMostrar.getMontoAprobacionesLotes()));
			//request.getRemoteAddr()
			aprobacionesConsultasRequest.setNumeroPagina(page);   
			//aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
			aprobacionesConsultasRequest.setTamanoPagina(5);
			Filtros filtros = new Filtros();
			LOGGER.info("llegue hasta aqui 3");
			//filtros.setReferencia(null);
			filtros.setStatus("I");
								
			if(montoDesde != null && montoHasta != null) {
				LOGGER.info("llegue hasta aqui dsitinto de null");
				LOGGER.info("montoDesde: "+montoDesde);
				LOGGER.info("montoHasta: "+montoHasta);
				if(libreriaUtil.montoSerch(montoDesde).compareTo(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto()))) < 0) {
					LOGGER.info("entro por fuera rango");
					listaError.add(MENSAJEFUERARANGO);
					model.addAttribute(LISTAERROR, listaError);
					model.addAttribute(LISTABANCOS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
					
				if(montoHasta.compareTo(montoDesde) < 0) { 
					LOGGER.info("entro monto desde menor que monto hasta");
					listaError.add(MENSAJEMONTOSINVALIDAS);
					model.addAttribute(LISTAERROR, listaError);
					model.addAttribute(LISTABANCOS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
				LOGGER.info("llegue hasta aqui filtra por montos");
				filtros.setMontoDesde(montoDesde);
				filtros.setMontoHasta(montoHasta);
			}else {
				LOGGER.info("llegue hasta aqui no hay montos");
				filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto())));
				filtros.setMontoHasta(montoTopeMaximoAproAuto);
			}
			
			if(!fechaDesde.equals("") && !fechaHasta.equals("")) {
				if(libreriaUtil.isFechaHoraValidaDesdeHasta(fechaDesde, fechaHasta)) {
					String[] arrOfFechaD = fechaDesde.split("T");
					String fechaDesdeT = arrOfFechaD[0];
					String horaDesde = arrOfFechaD[1];
					String[] arrOfFechaH = fechaHasta.split("T");
					String fechaHastaT = arrOfFechaH[0];
					String horaHasta = arrOfFechaH[1];
					fechaDesdeT = getFechaDiaMesAno(fechaDesdeT);
					horaDesde = getHora(horaDesde);
					fechaDesdeT = fechaDesdeT+" "+horaDesde+":00.000000";
					LOGGER.info("fechaDesdeT: "+fechaDesdeT);
						
					fechaHastaT = getFechaDiaMesAno(fechaHastaT);
					horaHasta = getHora(horaHasta);
					fechaHastaT = fechaHastaT+" "+horaHasta+":59.000000";
					LOGGER.info("fechaHastaT: "+fechaHastaT);
				        
					filtros.setFechaDesde(fechaDesdeT);
					filtros.setFechaHasta(fechaHastaT);
				        
				}
			}
					
			if(!bancoEmisor.equals(""))
				filtros.setBancoBeneficiario(bancoEmisor);
					
			if(!nroIdEmisor.equals(""))
				filtros.setNroIdEmisor(nroIdEmisor);
					
			aprobacionesConsultasRequest.setFiltros(filtros);
			LOGGER.info("filtros: "+filtros);
			AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest);
					
			if(aprobacionesConsultasResponse != null) {
				listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
				listaBCVLBTPorAprobar = convertirListaBCVLT(listaBCVLBTPorAprobar);
				listaBCVLBTPorAprobar = convertirListaBCVLTSeleccionadosUnaTrue(listaBCVLBTPorAprobar, referencia, httpSession);
				httpSession.setAttribute(LISTABCVLBTPORAPROBARSELECCION, listaBCVLBTPorAprobar);
				BigDecimal montoAprobarOperacionesSeleccionadas = libreriaUtil.montoAprobarOperacionesSeleccionadas(listaBCVLBTPorAprobar);
				datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
				if(listaBCVLBTPorAprobar.isEmpty()) {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				}
				model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
				model.addAttribute(LISTABANCOS, listaBancos);
				model.addAttribute(DATOSPAGINACION,datosPaginacion);
				model.addAttribute(MONTODESDE, montoDesde);
				model.addAttribute(MONTOHASTA, montoHasta);
				model.addAttribute(BANCOEMISOR, bancoEmisor);
				model.addAttribute(NROIDEMISOR, nroIdEmisor);
				model.addAttribute(FECHADESDE, fechaDesde);
				model.addAttribute(FECHAHASTA, fechaHasta);
				model.addAttribute("selecionados", true);
				model.addAttribute("montoAprobarOperacionesSeleccionadas", montoAprobarOperacionesSeleccionadas);
				LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE;
			}else {
				listaError.add(MENSAJENORESULTADO);
				model.addAttribute(LISTAERROR, listaError);
				model.addAttribute(LISTABANCOS, listaBancos);
				model.addAttribute("selecionados", true);
				model.addAttribute("montoAprobarOperacionesSeleccionadas", "0.00");
				LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
							
		} catch (CustomException e) {
			e.printStackTrace();
			model.addAttribute(LISTAERROR, e.getMessage());
			model.addAttribute(LISTABANCOS, listaBancos);
			return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
		}
	}
	
	
	
	@GetMapping("/deseleccionarUnaAprobarOperaciones")
	public String deseleccionarUnaAprobarOperaciones(@RequestParam("montoDesde") BigDecimal montoDesde, @RequestParam("montoHasta") BigDecimal montoHasta, 
			@RequestParam("bancoEmisor") String bancoEmisor, @RequestParam("nroIdEmisor") String nroIdEmisor, @RequestParam("fechaDesde") String fechaDesde,
			@RequestParam("fechaHasta") String fechaHasta, @RequestParam("page") int page, @RequestParam("referencia") String referencia, Model model, HttpServletRequest request, HttpSession httpSession) {
		LOGGER.info("me llamo you");
		LOGGER.info("montoDesde: "+montoDesde);
		LOGGER.info("montoHasta: "+montoHasta);
		LOGGER.info("bancoEmisor: "+bancoEmisor);
		LOGGER.info("nroIdEmisor: "+nroIdEmisor);
		LOGGER.info("fechaDesde: "+fechaDesde);
		LOGGER.info("fechaHasta: "+fechaHasta);
		LOGGER.info("page: "+page);
		LOGGER.info("referencia: "+referencia);
		BancoRequest bancoRequest = getBancoRequest();
		List<String> listaError = new ArrayList<>();
		List<Banco> listaBancos = new ArrayList<>();
		List<BCVLBT> listaBCVLBTPorAprobar = new ArrayList<>();
		DatosPaginacion datosPaginacion = new DatosPaginacion(0,0,0,0);
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest();
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		try {
			LOGGER.info("llegue hasta aqui 2");
			listaBancos  = bancoService.listaBancos(bancoRequest);
			CceTransaccionDto cceTransaccionDtoMostrar = getTransaccionesMostrar();
			model.addAttribute(NUMEROAPROBACIONESLOTES,cceTransaccionDtoMostrar.getNumeroAprobacionesLotes());
			model.addAttribute(MONTOAPROBACIONESLOTES,libreriaUtil.formatNumber(cceTransaccionDtoMostrar.getMontoAprobacionesLotes()));
			//request.getRemoteAddr()
			aprobacionesConsultasRequest.setNumeroPagina(page);   
			//aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
			aprobacionesConsultasRequest.setTamanoPagina(5);
			Filtros filtros = new Filtros();
			LOGGER.info("llegue hasta aqui 3");
			//filtros.setReferencia(null);
			filtros.setStatus("I");
								
			if(montoDesde != null && montoHasta != null) {
				LOGGER.info("llegue hasta aqui dsitinto de null");
				LOGGER.info("montoDesde: "+montoDesde);
				LOGGER.info("montoHasta: "+montoHasta);
				if(libreriaUtil.montoSerch(montoDesde).compareTo(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto()))) < 0) {
					LOGGER.info("entro por fuera rango");
					listaError.add(MENSAJEFUERARANGO);
					model.addAttribute(LISTAERROR, listaError);
					model.addAttribute(LISTABANCOS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
					
				if(montoHasta.compareTo(montoDesde) < 0) { 
					LOGGER.info("entro monto desde menor que monto hasta");
					listaError.add(MENSAJEMONTOSINVALIDAS);
					model.addAttribute(LISTAERROR, listaError);
					model.addAttribute(LISTABANCOS, listaBancos);
					return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
				}
				LOGGER.info("llegue hasta aqui filtra por montos");
				filtros.setMontoDesde(montoDesde);
				filtros.setMontoHasta(montoHasta);
			}else {
				LOGGER.info("llegue hasta aqui no hay montos");
				filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto())));
				filtros.setMontoHasta(montoTopeMaximoAproAuto);
			}
			
			if(!fechaDesde.equals("") && !fechaHasta.equals("")) {
				if(libreriaUtil.isFechaHoraValidaDesdeHasta(fechaDesde, fechaHasta)) {
					String[] arrOfFechaD = fechaDesde.split("T");
					String fechaDesdeT = arrOfFechaD[0];
					String horaDesde = arrOfFechaD[1];
					String[] arrOfFechaH = fechaHasta.split("T");
					String fechaHastaT = arrOfFechaH[0];
					String horaHasta = arrOfFechaH[1];
					fechaDesdeT = getFechaDiaMesAno(fechaDesdeT);
					horaDesde = getHora(horaDesde);
					fechaDesdeT = fechaDesdeT+" "+horaDesde+":00.000000";
					LOGGER.info("fechaDesdeT: "+fechaDesdeT);
						
					fechaHastaT = getFechaDiaMesAno(fechaHastaT);
					horaHasta = getHora(horaHasta);
					fechaHastaT = fechaHastaT+" "+horaHasta+":59.000000";
					LOGGER.info("fechaHastaT: "+fechaHastaT);
				        
					filtros.setFechaDesde(fechaDesdeT);
					filtros.setFechaHasta(fechaHastaT);
				        
				}
			}
					
			if(!bancoEmisor.equals(""))
				filtros.setBancoBeneficiario(bancoEmisor);
					
			if(!nroIdEmisor.equals(""))
				filtros.setNroIdEmisor(nroIdEmisor);
					
			aprobacionesConsultasRequest.setFiltros(filtros);
			LOGGER.info("filtros: "+filtros);
			AprobacionesConsultasResponse aprobacionesConsultasResponse =bcvlbtService.listaTransaccionesPorAporbarAltoValorPaginacion(aprobacionesConsultasRequest);
					
			if(aprobacionesConsultasResponse != null) {
				listaBCVLBTPorAprobar = aprobacionesConsultasResponse.getOperaciones();
				listaBCVLBTPorAprobar = convertirListaBCVLT(listaBCVLBTPorAprobar);
				listaBCVLBTPorAprobar = convertirListaBCVLTSeleccionadosUnaFalse(listaBCVLBTPorAprobar, referencia, httpSession);
				httpSession.setAttribute(LISTABCVLBTPORAPROBARSELECCION, listaBCVLBTPorAprobar);
				BigDecimal montoAprobarOperacionesSeleccionadas = libreriaUtil.montoAprobarOperacionesSeleccionadas(listaBCVLBTPorAprobar);
				datosPaginacion = aprobacionesConsultasResponse.getDatosPaginacion();
				if(listaBCVLBTPorAprobar.isEmpty()) {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				}
				model.addAttribute(LISTABCVLBTPORAPROBAR,listaBCVLBTPorAprobar);
				model.addAttribute(LISTABANCOS, listaBancos);
				model.addAttribute(DATOSPAGINACION,datosPaginacion);
				model.addAttribute(MONTODESDE, montoDesde);
				model.addAttribute(MONTOHASTA, montoHasta);
				model.addAttribute(BANCOEMISOR, bancoEmisor);
				model.addAttribute(NROIDEMISOR, nroIdEmisor);
				model.addAttribute(FECHADESDE, fechaDesde);
				model.addAttribute(FECHAHASTA, fechaHasta);
				model.addAttribute("selecionados", true);
				model.addAttribute("montoAprobarOperacionesSeleccionadas", montoAprobarOperacionesSeleccionadas);
				LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLLISTAOPERACIONESPORAPROBARAALTOVALORPAGINATE;
			}else {
				listaError.add(MENSAJENORESULTADO);
				model.addAttribute(LISTAERROR, listaError);
				model.addAttribute(LISTABANCOS, listaBancos);
				model.addAttribute("selecionados", true);
				model.addAttribute("montoAprobarOperacionesSeleccionadas", "0.00");
				LOGGER.info(CCETRANSACCIONCONTROLLERCONSULTAROPERACIONESAPROBARALTOVALORF);
				return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
			}
							
		} catch (CustomException e) {
			e.printStackTrace();
			model.addAttribute(LISTAERROR, e.getMessage());
			model.addAttribute(LISTABANCOS, listaBancos);
			return URLFORMCONSULTAROPERACIONESAPORBARALTOBAJOVALOR;
		}
	}
	
	
	
	@GetMapping("/exportarExcelMoviminetos")
	public void exportarExcelMoviminetos(HttpServletResponse response, HttpSession httpSession) {
		LOGGER.info("exportarExcelMoviminetos");
		
		List<CceTransaccionDto> listaTransaccionesDto =(List<CceTransaccionDto>)httpSession.getAttribute(LISTATRANSACCIONESEXCEL);
		
		for (CceTransaccionDto cceTransaccionDto : listaTransaccionesDto) {
			//log.info("cceTransaccionDto: "+cceTransaccionDto);
			LOGGER.info("cceTransaccionDto.getCodTransaccion(): "+cceTransaccionDto.getMonto());
			LOGGER.info("monto: "+ cceTransaccionDto.getMonto());
			LOGGER.info("montoformatNumber: "+ libreriaUtil.formatNumber(cceTransaccionDto.getMonto()));
		}
		
		
		
		response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=movimientosconsulta_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        MovimientosExcelExporter excelExporter = new MovimientosExcelExporter(listaTransaccionesDto);
        try {
			excelExporter.export(response);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String getFechaHoraDesdeFormato(String fechaHoraDesde) {
		String[] arrOfFecha = fechaHoraDesde.split("T");
		String fechaDesde = arrOfFecha[0];
		String horaDesde = arrOfFecha[1];
		
		fechaDesde = getFechaDiaMesAno(fechaDesde);
		horaDesde = getHora(horaDesde);
		
		fechaDesde = fechaDesde+" "+horaDesde+":00.000000";
		
		return fechaDesde;
	}
	
	public String getFechaHoraHastaFormato(String fechaHoraHasta) {
		String[] arrOfFecha = fechaHoraHasta.split("T");
		String fechaHasta = arrOfFecha[0];
		String horaHasta = arrOfFecha[1];
		
		fechaHasta = getFechaDiaMesAno(fechaHasta);
		horaHasta = getHora(horaHasta);
		
		fechaHasta = fechaHasta+" "+horaHasta+":59.000000";
		
		return fechaHasta;
	}
	
	public String getFechaDiaMesAno(String fecha) {
		String[] arrOfFecha = fecha.split("-");
		for (String a: arrOfFecha)
			LOGGER.info(a);
		String ano = arrOfFecha[0];
		String mes = arrOfFecha[1];
		String dia = arrOfFecha[2];
		
		return dia+"-"+mes+"-"+ano;
	}
	
	
	public String getHora(String hora) {
		String[] arrOfHora = hora.split(":");
		for (String a: arrOfHora)
			LOGGER.info(a);
		String horaCambio = arrOfHora[0];
		int horaCambioInt = Integer.valueOf(horaCambio).intValue();
		String minutos = arrOfHora[1];
		
		if(horaCambioInt > 12) {
			horaCambioInt = horaCambioInt - 12;
			horaCambio = String.valueOf(horaCambioInt);
		}
		
		
		return horaCambio+":"+minutos;
	}
	
	
	public String getMsgId() {
		String valor = "";
		for (int i = 0; i < 28; i++) {
			valor = valor+"0";
		}
		LOGGER.info(valor);
		return valor;
	}
	
	public CceTransaccionDto getTransaccionesMostrar() throws CustomException{
		
		CceTransaccionDto cceTransaccionDto = new CceTransaccionDto();
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest();
		CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		LOGGER.info("cceMontoMaximoAproAuto: "+cceMontoMaximoAproAuto);
		LOGGER.info("montoTopeMaximoAproAuto: "+montoTopeMaximoAproAuto);
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
					cceTransaccionDto.setMontoAprobacionesLotes(libreriaUtil.montoAprobacionesLotes(listaBCVLBTPorAprobar));
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
	
	public BCVLBT getBCVLBT(Integer referencia) throws CustomException{
		
	
		AprobacionesConsultasRequest aprobacionesConsultasRequest = getAprobacionesConsultasRequest();
		//CceMontoMaximoAproAuto cceMontoMaximoAproAuto = montoMaximoAproAutoService.buscarMontoMaximoAproAutoActual();
		//log.info("cceMontoMaximoAproAuto: "+cceMontoMaximoAproAuto);
		//log.info("montoTopeMaximoAproAuto: "+montoTopeMaximoAproAuto);
		//libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceTransaccionDto.getMonto()))
		
		//request.getRemoteAddr()
		aprobacionesConsultasRequest.setNumeroPagina(1);   
		//aprobacionesConsultasRequest.setTamanoPagina(numeroRegistroPage);
		aprobacionesConsultasRequest.setTamanoPagina(2147483647);
		Filtros filtros = new Filtros();
		filtros.setReferencia(referencia);
		filtros.setStatus("I");
		//filtros.setMontoDesde(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceMontoMaximoAproAuto.getMonto())));
		//filtros.setMontoHasta(montoTopeMaximoAproAuto);
		aprobacionesConsultasRequest.setFiltros(filtros);
		BCVLBT bcvlbt = new BCVLBT();
		try {
			bcvlbt =bcvlbtService.buscarBCVLBT(aprobacionesConsultasRequest);
			return bcvlbt;
			
			
		} catch (CustomException e) {
			e.printStackTrace();
			
			return null;
		}
		
		
		
	}
	
	public Banco getBancoReceptor(String codBanco) throws CustomException{
		
		BancoRequest bancoRequest = getBancoRequest();
		bancoRequest.setCodBanco(codBanco);
		try {
			return bancoService.buscarBanco(bancoRequest);
		} catch (CustomException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Page<CceTransaccion> convertirLista(Page<CceTransaccion> listaTransacciones){
		for (CceTransaccion cceTransaccion : listaTransacciones) {
			//log.info("estadoBcv: "+cceTransaccion.getEstadobcv());
			//log.info("monto: "+ cceTransaccion.getMonto());
			//log.info("montoformatNumber: "+ libreriaUtil.formatNumber(cceTransaccion.getMonto()));
			cceTransaccion.setMontoString(libreriaUtil.formatNumber(cceTransaccion.getMonto()));
		}
	
		return listaTransacciones;
	}
	
	public List<BCVLBT> convertirListaBCVLT(List<BCVLBT> listaTransacciones){
		for (BCVLBT bcvlbt : listaTransacciones) {
			//log.info("monto: "+ bcvlbt.getMonto());
			//log.info("montoformatNumber: "+ libreriaUtil.formatNumber(bcvlbt.getMonto()));
			bcvlbt.setMontoString(libreriaUtil.formatNumber(bcvlbt.getMonto()));
		}
	
		return listaTransacciones;
	}
	
	public List<BCVLBT> convertirListaBCVLTSeleccionadosTrue(List<BCVLBT> listaTransacciones){
		for (BCVLBT bcvlbt : listaTransacciones) {
			bcvlbt.setSeleccionado(true);
		}
	
		return listaTransacciones;
	}
	
	public List<BCVLBT> convertirListaBCVLTSeleccionadosFalse(List<BCVLBT> listaTransacciones){
		for (BCVLBT bcvlbt : listaTransacciones) {
			bcvlbt.setSeleccionado(false);
		}
	
		return listaTransacciones;
	}
	
	public List<BCVLBT> convertirListaBCVLTSeleccionadosUnaTrue(List<BCVLBT> listaTransacciones, String referencia, HttpSession httpSession){
		List<BCVLBT> listaBCVLBTPorAprobarSesion =(List<BCVLBT>)httpSession.getAttribute(LISTABCVLBTPORAPROBARSELECCION);
		int referenciaInt = Integer.valueOf(referencia).intValue();
		for (BCVLBT bcvlbt : listaTransacciones) {
			if(bcvlbt.getReferencia() == referenciaInt) {
				bcvlbt.setSeleccionado(true);
			}else {
				bcvlbt.setSeleccionado(buscarvalorListaBCVLTSeleccionados(referencia, httpSession));
			}
		}
		
		
	
		return listaTransacciones;
	}
	
	
	public boolean buscarvalorListaBCVLTSeleccionados(String referencia, HttpSession httpSession){
		List<BCVLBT> listaBCVLBTPorAprobarSesion =(List<BCVLBT>)httpSession.getAttribute(LISTABCVLBTPORAPROBARSELECCION);
		
		boolean valor = false;
		int referenciaInt = Integer.valueOf(referencia).intValue();
		for (BCVLBT bcvlbt : listaBCVLBTPorAprobarSesion) {
			
			if(bcvlbt.getReferencia() == referenciaInt) {
				LOGGER.info("bcvlbt.getReferencia(): "+bcvlbt.getReferencia());
				LOGGER.info("bcvlbtSesion.isSeleccionado(): "+bcvlbt.isSeleccionado());
				valor = bcvlbt.isSeleccionado();
			}
			
		}
		
		//log.valor
	
		return valor;
	}
	
	
	public List<BCVLBT> convertirListaBCVLTSeleccionadosUnaFalse(List<BCVLBT> listaTransacciones, String referencia, HttpSession httpSession){
		List<BCVLBT> listaBCVLBTPorAprobarSesion =(List<BCVLBT>)httpSession.getAttribute(LISTABCVLBTPORAPROBARSELECCION);
		int referenciaInt = Integer.valueOf(referencia).intValue();
		for (BCVLBT bcvlbt : listaTransacciones) {
			for (BCVLBT bcvlbtSesion : listaBCVLBTPorAprobarSesion) {
				if(bcvlbt.getReferencia() == bcvlbtSesion.getReferencia()) {
					if(bcvlbt.getReferencia() == referenciaInt) {
						bcvlbt.setSeleccionado(false);
					}else {
						bcvlbt.setSeleccionado(bcvlbtSesion.isSeleccionado());
					}
				}else {
					bcvlbt.setSeleccionado(false);
				}
			}
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
	
	public ParamIdentificacion getParamIdentificacion() {
		ParamIdentificacion paramIdentificacion = new ParamIdentificacion();
		paramIdentificacion.setIdSesion(libreriaUtil.obtenerIdSesionCce());
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		paramIdentificacion.setIdUsuario(userName);
		
		return paramIdentificacion;
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
