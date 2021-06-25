package com.bancoexterior.app.cce.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.bancoexterior.app.cce.dto.BancoRequest;
import com.bancoexterior.app.cce.dto.CceTransaccionDto;
import com.bancoexterior.app.cce.model.Banco;
import com.bancoexterior.app.cce.model.CceTransaccion;
import com.bancoexterior.app.cce.service.IBancoService;
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
	private IBancoService bancoService;
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	private static final String STRDATEFORMET = "yyyy-MM-dd";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String MENSAJEFECHASINVALIDAS = "Los valores de las fechas son invalidos";
	
	private static final String MENSAJENORESULTADO = "Operacion Exitosa.La consulta no arrojo resultado.";
	
	@GetMapping("/listaMovimientosConsultaAltoBajoValor")
	public String index(Model model) {
		
		List<CceTransaccionDto> listaTransacciones = service.consultar();
		model.addAttribute("listaTransacciones", listaTransacciones);   
		return "cce/listaMovimientosConsultaAltoBajoValor";
	}
	
	@GetMapping("/listaMovimientosConsultaAltoBajoValorPaginate")
	public String indexPaginado(Model model, Pageable page) {
		
		Page<CceTransaccion> listaTransacciones = service.consultar(page);
		model.addAttribute("listaTransacciones", listaTransacciones);   
		return "cce/listaMovimientosConsultaAltoBajoValorPaginateTodas";
	}
	
	@GetMapping("/formConsultaMovimientosConsultaAltoBajoValor")
	public String formConsultaMovimientosAltoBajoValor(CceTransaccionDto cceTransaccionDto, Model model) {
		log.info("formConsultaMovimientosAltoBajoValor");
		
		BancoRequest bancoRequest = getBancoRequest();
		//cceTransaccionDto.setFechaDesde(libreriaUtil.obtenerFechaHoy());
		//cceTransaccionDto.setNumeroIdentificacion("hola");
		
		try {
			List<Banco> listaBancos  = bancoService.listaBancos(bancoRequest);
			model.addAttribute("listaBancos", listaBancos);
		} catch (CustomException e) {
			e.printStackTrace();
			model.addAttribute(LISTAERROR, e.getMessage());
		}
		
		return "cce/formConsultarMovimientosAltoBajoValor";
		
	}
					
	
	
				 //procesarConsultaMovimientosAltoBajoValorPageable	
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
		//cceTransaccionDto.setFechaDesde(libreriaUtil.obtenerFechaHoy());
		//cceTransaccionDto.setNumeroIdentificacion("hola");
		
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
					model.addAttribute("listaBancos", listaBancos);
					return "cce/formConsultarMovimientosAltoBajoValor";
				}
				model.addAttribute("listaTransacciones", listaTransacciones);
				model.addAttribute("codTransaccion", cceTransaccionDto.getCodTransaccion());
				model.addAttribute("bancoDestino", cceTransaccionDto.getBancoDestino());
				model.addAttribute("numeroIdentificacion", cceTransaccionDto.getNumeroIdentificacion());
				model.addAttribute("fechaDesde", cceTransaccionDto.getFechaDesde());
				model.addAttribute("fechaHasta", cceTransaccionDto.getFechaHasta());
				return "cce/listaMovimientosConsultaAltoBajoValorPaginate";
					
			}else {
				log.info("fechas invalidas");
				listaError.add(MENSAJEFECHASINVALIDAS);
				model.addAttribute(LISTAERROR, listaError);
				return "cce/formConsultarMovimientosAltoBajoValor";
			}
		} catch (CustomException e) {
			e.printStackTrace();
			model.addAttribute(LISTAERROR, e.getMessage());
			return "cce/formConsultarMovimientosAltoBajoValor";
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
			model.addAttribute("listaTransacciones", listaTransacciones);
			model.addAttribute("codTransaccion", codTransaccion);
			model.addAttribute("bancoDestino", bancoDestino);
			model.addAttribute("numeroIdentificacion", numeroIdentificacion);
			model.addAttribute("fechaDesde", fechaDesde);
			model.addAttribute("fechaHasta", fechaHasta);
			return "cce/listaMovimientosConsultaAltoBajoValorPaginate";
				
		}else {
			log.info("fechas invalidas");
			listaError.add(MENSAJEFECHASINVALIDAS);
			model.addAttribute(LISTAERROR, listaError);
			return "cce/listaMovimientosConsultaAltoBajoValorPaginate";
		}
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
			}
			cceTransaccionDto.setMonto(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceTransaccionDto.getMonto())));
			model.addAttribute("cceTransaccionDto", cceTransaccionDto);
			model.addAttribute("codTransaccion", codTransaccion);
			model.addAttribute("bancoDestino", bancoDestino);
			model.addAttribute("numeroIdentificacion", numeroIdentificacion);
			model.addAttribute("fechaDesde", fechaDesde);
			model.addAttribute("fechaHasta", fechaHasta);
			model.addAttribute("page", page.getPageNumber());
			return "cce/formMovimientoAltoBajoValorDetalleFechas";
		}else {
			Page<CceTransaccion> listaTransacciones;
			listaTransacciones = service.consultaMovimientosConFechas(codTransaccion, bancoDestino, numeroIdentificacion,
					fechaDesde, fechaHasta, page);
			model.addAttribute("listaTransacciones", listaTransacciones);
			model.addAttribute("codTransaccion", codTransaccion);
			model.addAttribute("bancoDestino", bancoDestino);
			model.addAttribute("numeroIdentificacion", numeroIdentificacion);
			model.addAttribute("fechaDesde", fechaDesde);
			model.addAttribute("fechaHasta", fechaHasta);
			return "cce/listaMovimientosConsultaAltoBajoValorPaginate";
		}
		
		
		
	}
	
	
	@GetMapping("/procesarMovimientosPorAprobarAltoValor")
	public String consultaMovimientosPorAprobarAltovalor(Model model, Pageable page) {
		Page<CceTransaccion> listaTransacciones;
		

		listaTransacciones = service.consultaMovimientosConFechas("", "", "", "2021-06-15", "2021-06-18", page);
		
		listaTransacciones = convertirLista(listaTransacciones);
		
		
		
		if(listaTransacciones.isEmpty()) {
			model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
		}
		model.addAttribute("listaTransacciones", listaTransacciones);
		model.addAttribute("codTransaccion", "");
		model.addAttribute("bancoDestino", "");
		model.addAttribute("numeroIdentificacion", "");
		return "cce/listaMovimientosPorAporbarAltoValorPaginate";
	}
	
	@GetMapping("/formAprobarMovimientosAltoValorLoteAutomatico")
	public String formAprobarMovimientosBajoValor(CceTransaccionDto cceTransaccionDto, Model model) {
		log.info("formConsultaMovimientosAltoBajoValor");
		List<Banco> listaBancos = new ArrayList<>();
		BancoRequest bancoRequest = getBancoRequest();
		cceTransaccionDto.setFechaDesde(libreriaUtil.obtenerFechaHoy());
		//cceTransaccionDto.setNumeroIdentificacion("hola");
		/*
		try {
			listaBancos = bancoService.listaBancos(bancoRequest);
			model.addAttribute("cceTransaccionDto", cceTransaccionDto);
		} catch (CustomException e) {
			e.printStackTrace();
			model.addAttribute(LISTAERROR, e.getMessage());
		}*/
		
		return "cce/formAprobarMovimientosBajoValor";
		
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
			//log.info("monto: "+cceTransaccion.getMonto().toString());
			//log.info("monto: "+ libreriaUtil.formatNumber(cceTransaccion.getMonto()));
			//cceTransaccion.setMontoString(libreriaUtil.formatNumber(cceTransaccion.getMonto()));
			//log.info("monto: "+ libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceTransaccion.getMonto())));
			cceTransaccion.setMonto(libreriaUtil.stringToBigDecimal(libreriaUtil.formatNumber(cceTransaccion.getMonto())));
		}
	
		return listaTransacciones;
	}
	
	public BancoRequest getBancoRequest() {
		BancoRequest bancoRequest = new BancoRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		bancoRequest.setIdUsuario(userName);
		bancoRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		return bancoRequest;
	}
	
	
	@ModelAttribute
	public void setGenericos(Model model) {
		CceTransaccionDto cceTransaccionDto = new CceTransaccionDto();
		CceTransaccionDto cceTransaccionDtoSearch = new CceTransaccionDto();
		model.addAttribute("cceTransaccionDto", cceTransaccionDto);
		model.addAttribute("cceTransaccionDtoSearch", cceTransaccionDtoSearch);
		
	}
}
