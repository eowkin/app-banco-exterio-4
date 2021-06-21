package com.bancoexterior.app.convenio.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.bancoexterior.app.convenio.dto.AcumuladoCompraVentaResponse;
import com.bancoexterior.app.convenio.dto.AcumuladoRequest;
import com.bancoexterior.app.convenio.dto.AcumuladoResponse;
import com.bancoexterior.app.convenio.dto.AprobarRechazarRequest;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.dto.MovimientosRequest;
import com.bancoexterior.app.convenio.dto.MovimientosResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.model.Compra;
import com.bancoexterior.app.convenio.model.DatosConsulta;
import com.bancoexterior.app.convenio.model.DatosPaginacion;
import com.bancoexterior.app.convenio.model.Fechas;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.model.Movimiento;
import com.bancoexterior.app.convenio.model.Venta;
import com.bancoexterior.app.convenio.service.IAcumuladosServiceApiRest;
import com.bancoexterior.app.convenio.service.IMonedaServiceApiRest;
import com.bancoexterior.app.convenio.service.IMovimientosApiRest;
import com.bancoexterior.app.util.ConsultaExcelExporter;
import com.bancoexterior.app.util.LibreriaUtil;


import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/solicitudes")
public class SolicitudController {
		
	@Autowired
	private IMovimientosApiRest movimientosApiRest;
	
	@Autowired
	private IAcumuladosServiceApiRest acumuladosServiceApiRest;
	
	@Autowired
	private IMonedaServiceApiRest monedaServiceApiRest;
	
	@Autowired
	private LibreriaUtil libreriaUtil; 
	
	@Value("${des.canal}")
    private String canal;
	
	@Value("${des.movimientos.numeroRegistroPage}")
    private int numeroRegistroPage;
	
	private static final String VENTAACUMULADOUSD = "ventaAcumuladoUSD";
	
	private static final String VENTAACUMULADOEUR = "ventaAcumuladoEUR";
	
	private static final String MONTOBSTOTALVENTA = "montoBsTotalVenta";
	
	private static final String COMPRAACUMULADOUSD = "compraAcumuladoUSD";
	
	private static final String COMPRAACUMULADOEUR = "compraAcumuladoEUR";
	
	private static final String MONTOBSTOTALCOMPRA = "montoBsTotalCompra";
	
	private static final String VENTAPORAPROBARUSD = "ventaPorAprobarUSD";
	
	private static final String VENTAPORAPROBAREUR = "ventaPorAprobarEUR";
	
	private static final String MONTOBSTOTALPORAPROBARVENTA = "montoBsTotalPorAprobarVenta";
	
	private static final String COMPRAPORAPROBARUSD = "compraPorAprobarUSD";
	
	private static final String COMPRAPORAPROBAREUR = "compraPorAprobarEUR";
	
	private static final String MONTOBSTOTALPORAPROBARCOMPRA = "montoBsTotalPorAprobarCompra";
	
	private static final String DATOSPAGINACIONVENTA = "datosPaginacionVenta";
	
	private static final String LISTAMOVIMIENTOSVENTA = "listaMovimientosVenta";
	
	private static final String DATOSPAGINACIONCOMPRA = "datosPaginacionCompra";
	
	private static final String LISTAMOVIMIENTOSCOMPRA = "listaMovimientosCompra";
	
	private static final String LISTAMONEDAS = "listaMonedas";
	
	private static final String URLLISTAMOVIMIENTOSPORAPROBARVENTA = "convenio/solicitudes/listaSolicitudesMovimientosPorAprobarVenta";
	
	private static final String URLLISTAMOVIMIENTOSVENTA = "convenio/solicitudes/listaSolicitudesMovimientosVenta";
	
	private static final String URLLISTAMOVIMIENTOSVENTASEARCHESTATUS =  "convenio/solicitudes/listaSolicitudesMovimientosVentaSearchEstatus";
	
	private static final String URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS =  "convenio/solicitudes/listaSolicitudesMovimientosCompraSearchEstatus";
	
	private static final String URLLISTAMOVIMIENTOSCOMPRA = "convenio/solicitudes/listaSolicitudesMovimientosCompra";
	
	private static final String URLFORMSOLICITUD = "convenio/solicitudes/formSolicitud";
	
	private static final String URLFORMSOLICITUDVENTA = "convenio/solicitudes/formSolicitudVenta";
	
	private static final String URLFORMSOLICITUDGENERARREPORTE = "convenio/solicitudes/formSolicitudGenerarReporte";
	
	private static final String PAGINAACTUAL = "paginaActual";
	
	private static final String ESTATUS = "estatus";
	
	private static final String FECHADESDE = "fechaDesde";
	
	private static final String FECHAHASTA = "fechaHasta";
	
	private static final String STRDATEFORMET = "yyyy-MM-dd";
	
	private static final String MENSAJE = "mensaje";
	
	private static final String MENSAJEVENTA = "mensajeVenta";
	
	private static final String MENSAJEERROR = "mensajeError";
	
	private static final String MENSAJEERRORVENTA = "mensajeErrorVenta";
	
	private static final String MENSAJEERRORCOMPRA = "mensajeErrorCompra";
	
	private static final String MENSAJENORESULTADO = "Operacion Exitosa.La consulta no arrojo resultado.";
	
	private static final String MENSAJEFECHASINVALIDAS = "Los valores de las fechas son invalidos";
	
	private static final String LISTAERROR = "listaError";
	
	private static final String REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA = "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarCompra/";
	
	private static final String REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS = "redirect:/solicitudes/listaSolicitudesMovimientosPorAprobarVentas/";
	
	private static final String REDIRECTINICIO = "redirect:/";
	
	@GetMapping("/listaSolicitudesMovimientosPorAprobarVentas/{page}")
	public String consultaMovimientoPorAprobarVenta(@PathVariable("page") int page,Model model) {
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion(0,0,0,0);
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion(0,0,0,0);
		
		Venta ventaAcumuladoUSD = new Venta();
		Venta ventaAcumuladoEUR = new Venta();
		BigDecimal montoBsTotalVenta = new BigDecimal("0.00");
		Compra compraAcumuladoUSD = new Compra();
		Compra compraAcumuladoEUR = new Compra();
		BigDecimal montoBsTotalCompra = new BigDecimal("0.00");
		Venta ventaPorAprobarUSD = new Venta();
		Venta ventaPorAprobarEUR = new Venta();
		BigDecimal montoBsTotalPorAprobarVenta = new BigDecimal("0.00");
		Compra compraPorAprobarUSD = new Compra();
		Compra compraPorAprobarEUR = new Compra();
		BigDecimal montoBsTotalPorAprobarCompra = new BigDecimal("0.00");
		try {
			ventaAcumuladoUSD = acumuladosVenta("USD");
			model.addAttribute(VENTAACUMULADOUSD, evaluarBigDecimalVenta(ventaAcumuladoUSD));
			
			ventaAcumuladoEUR = acumuladosVenta("EUR");
			model.addAttribute(VENTAACUMULADOEUR, evaluarBigDecimalVenta(ventaAcumuladoEUR));
			
			montoBsTotalVenta =  ventaAcumuladoEUR.getMontoBs().add(ventaAcumuladoUSD.getMontoBs());
			model.addAttribute(MONTOBSTOTALVENTA, evaluarBigDecimal(montoBsTotalVenta));
			
			compraAcumuladoUSD = acumuladosCompra("USD");
			model.addAttribute(COMPRAACUMULADOUSD, evaluarBigDecimalCompra(compraAcumuladoUSD)); 
			
			compraAcumuladoEUR = acumuladosCompra("EUR");
			model.addAttribute(COMPRAACUMULADOEUR, evaluarBigDecimalCompra(compraAcumuladoEUR));
			
			montoBsTotalCompra =  compraAcumuladoEUR.getMontoBs().add(compraAcumuladoUSD.getMontoBs());
			model.addAttribute(MONTOBSTOTALCOMPRA, evaluarBigDecimal(montoBsTotalCompra));
			
			ventaPorAprobarUSD = acumuladosPorAprobarVenta("USD");
			model.addAttribute(VENTAPORAPROBARUSD,  evaluarBigDecimalVenta(ventaPorAprobarUSD));
			
			ventaPorAprobarEUR = acumuladosPorAprobarVenta("EUR");
			model.addAttribute(VENTAPORAPROBAREUR, evaluarBigDecimalVenta(ventaPorAprobarEUR)); 
			
			montoBsTotalPorAprobarVenta =  ventaPorAprobarEUR.getMontoBs().add(ventaPorAprobarUSD.getMontoBs());
			model.addAttribute(MONTOBSTOTALPORAPROBARVENTA, evaluarBigDecimal(montoBsTotalPorAprobarVenta)); 
			
			compraPorAprobarUSD = acumuladosPorAprobarCompra("USD");
			model.addAttribute(COMPRAPORAPROBARUSD, evaluarBigDecimalCompra(compraPorAprobarUSD)); 
			
			compraPorAprobarEUR = acumuladosPorAprobarCompra("EUR");
			model.addAttribute(COMPRAPORAPROBAREUR, evaluarBigDecimalCompra(compraPorAprobarEUR));
			log.info("compraPorAprobarEUR: "+compraPorAprobarEUR);
			
			montoBsTotalPorAprobarCompra =  compraPorAprobarEUR.getMontoBs().add(compraPorAprobarUSD.getMontoBs());
			model.addAttribute(MONTOBSTOTALPORAPROBARCOMPRA, evaluarBigDecimal(montoBsTotalPorAprobarCompra));
			
			movimientosRequest.setNumeroPagina(page);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			filtrosVenta.setEstatus(0);
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientosPorAprobarVenta(movimientosRequest);
			if(responseVenta != null) {
				
				listaMovimientosVenta = responseVenta.getMovimientos();
				if(!listaMovimientosVenta.isEmpty()) {
					datosPaginacionVenta = responseVenta.getDatosPaginacion();
				}
				
				model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
				model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
				
				movimientosRequest.setNumeroPagina(1);
				movimientosRequest.setTamanoPagina(numeroRegistroPage);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				filtrosCompra.setEstatus(0);
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientosPorAprobar(movimientosRequest);
				
				if(responseCompra != null) {
					
					listaMovimientosCompra = responseCompra.getMovimientos();
					if(!listaMovimientosCompra.isEmpty()) {
						datosPaginacionCompra = responseCompra.getDatosPaginacion();
					}
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					
					return URLLISTAMOVIMIENTOSPORAPROBARVENTA;
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					return URLLISTAMOVIMIENTOSPORAPROBARVENTA;
				}
				
			}else {
				return REDIRECTINICIO;
			}
		} catch (CustomException e) {
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
			model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
			model.addAttribute(MENSAJEERRORVENTA, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
			model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
			ventaAcumuladoUSD.setMonto(new BigDecimal("0.00"));
			model.addAttribute(VENTAACUMULADOUSD, ventaAcumuladoUSD);
			ventaAcumuladoEUR.setMonto(new BigDecimal("0.00"));
			model.addAttribute(VENTAACUMULADOEUR, ventaAcumuladoEUR);
			model.addAttribute(MONTOBSTOTALVENTA, montoBsTotalVenta);
			compraAcumuladoUSD.setMonto(new BigDecimal("0.00"));
			model.addAttribute(COMPRAACUMULADOUSD, compraAcumuladoUSD);
			compraAcumuladoEUR.setMonto(new BigDecimal("0.00"));
			model.addAttribute(COMPRAACUMULADOEUR, compraAcumuladoEUR);
			model.addAttribute(MONTOBSTOTALCOMPRA, montoBsTotalCompra);
			ventaPorAprobarUSD.setMonto(new BigDecimal("0.00"));
			model.addAttribute(VENTAPORAPROBARUSD, ventaPorAprobarUSD);
			ventaPorAprobarEUR.setMonto(new BigDecimal("0.00"));
			model.addAttribute(VENTAPORAPROBAREUR, ventaPorAprobarEUR);
			model.addAttribute(MONTOBSTOTALPORAPROBARVENTA, montoBsTotalPorAprobarVenta);
			compraPorAprobarUSD.setMonto(new BigDecimal("0.00"));
			model.addAttribute(COMPRAPORAPROBARUSD, compraPorAprobarUSD);
			compraPorAprobarEUR.setMonto(new BigDecimal("0.00"));
			model.addAttribute(COMPRAPORAPROBAREUR, compraPorAprobarEUR);
			model.addAttribute(MONTOBSTOTALPORAPROBARCOMPRA, montoBsTotalPorAprobarCompra);
			return URLLISTAMOVIMIENTOSPORAPROBARVENTA;
		}
	}
	
	
	
	
	@GetMapping("/listaSolicitudesMovimientosPorAprobarCompra/{page}")
	public String consultaMovimientoPorAprobarCompra(@PathVariable("page") int page,Model model) {
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion(0,0,0,0);
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion(0,0,0,0);
		
		Venta ventaAcumuladoUSD = new Venta();
		Venta ventaAcumuladoEUR = new Venta();
		BigDecimal montoBsTotalVenta = new BigDecimal("0.00");
		Compra compraAcumuladoUSD = new Compra();
		Compra compraAcumuladoEUR = new Compra();
		BigDecimal montoBsTotalCompra = new BigDecimal("0.00");
		Venta ventaPorAprobarUSD = new Venta();
		Venta ventaPorAprobarEUR = new Venta();
		BigDecimal montoBsTotalPorAprobarVenta = new BigDecimal("0.00");
		Compra compraPorAprobarUSD = new Compra();
		Compra compraPorAprobarEUR = new Compra();
		BigDecimal montoBsTotalPorAprobarCompra = new BigDecimal("0.00");
		try {
			ventaAcumuladoUSD = acumuladosVenta("USD");
			model.addAttribute(VENTAACUMULADOUSD, evaluarBigDecimalVenta(ventaAcumuladoUSD));
			
			ventaAcumuladoEUR = acumuladosVenta("EUR");
			model.addAttribute(VENTAACUMULADOEUR, evaluarBigDecimalVenta(ventaAcumuladoEUR));
			
			montoBsTotalVenta =  ventaAcumuladoEUR.getMontoBs().add(ventaAcumuladoUSD.getMontoBs());
			model.addAttribute(MONTOBSTOTALVENTA, evaluarBigDecimal(montoBsTotalVenta));
			
			compraAcumuladoUSD = acumuladosCompra("USD");
			model.addAttribute(COMPRAACUMULADOUSD, evaluarBigDecimalCompra(compraAcumuladoUSD)); 
			
			compraAcumuladoEUR = acumuladosCompra("EUR");
			model.addAttribute(COMPRAACUMULADOEUR, evaluarBigDecimalCompra(compraAcumuladoEUR));
			
			montoBsTotalCompra =  compraAcumuladoEUR.getMontoBs().add(compraAcumuladoUSD.getMontoBs());
			model.addAttribute(MONTOBSTOTALCOMPRA, evaluarBigDecimal(montoBsTotalCompra));
			
			ventaPorAprobarUSD = acumuladosPorAprobarVenta("USD");
			model.addAttribute(VENTAPORAPROBARUSD,  evaluarBigDecimalVenta(ventaPorAprobarUSD));
			
			ventaPorAprobarEUR = acumuladosPorAprobarVenta("EUR");
			model.addAttribute(VENTAPORAPROBAREUR, evaluarBigDecimalVenta(ventaPorAprobarEUR)); 
			
			montoBsTotalPorAprobarVenta =  ventaPorAprobarEUR.getMontoBs().add(ventaPorAprobarUSD.getMontoBs());
			model.addAttribute(MONTOBSTOTALPORAPROBARVENTA, evaluarBigDecimal(montoBsTotalPorAprobarVenta)); 
			
			compraPorAprobarUSD = acumuladosPorAprobarCompra("USD");
			model.addAttribute(COMPRAPORAPROBARUSD, evaluarBigDecimalCompra(compraPorAprobarUSD)); 
			
			compraPorAprobarEUR = acumuladosPorAprobarCompra("EUR");
			model.addAttribute(COMPRAPORAPROBAREUR, evaluarBigDecimalCompra(compraPorAprobarEUR));
			log.info("compraPorAprobarEUR: "+compraPorAprobarEUR);
			
			montoBsTotalPorAprobarCompra =  compraPorAprobarEUR.getMontoBs().add(compraPorAprobarUSD.getMontoBs());
			model.addAttribute(MONTOBSTOTALPORAPROBARCOMPRA, evaluarBigDecimal(montoBsTotalPorAprobarCompra));
			
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			filtrosVenta.setEstatus(0);
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientosPorAprobarVenta(movimientosRequest);
			if(responseVenta != null) {
				
				listaMovimientosVenta = responseVenta.getMovimientos();
				if(!listaMovimientosVenta.isEmpty()) {
					datosPaginacionVenta = responseVenta.getDatosPaginacion();
				}
				model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
				model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
				
				movimientosRequest.setNumeroPagina(page);
				movimientosRequest.setTamanoPagina(numeroRegistroPage);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				filtrosCompra.setEstatus(0);
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientosPorAprobar(movimientosRequest);
				
				if(responseCompra != null) {
					
					listaMovimientosCompra = responseCompra.getMovimientos();
					if(!listaMovimientosCompra.isEmpty()) {
						datosPaginacionCompra = responseCompra.getDatosPaginacion();
					}
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					
					return URLLISTAMOVIMIENTOSPORAPROBARVENTA;
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					return URLLISTAMOVIMIENTOSPORAPROBARVENTA;
				}
				
			}else {
				return REDIRECTINICIO;
			}
		} catch (CustomException e) {
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
			model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
			model.addAttribute(MENSAJEERRORVENTA, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
			model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
			ventaAcumuladoUSD.setMonto(new BigDecimal("0.00"));
			model.addAttribute(VENTAACUMULADOUSD, ventaAcumuladoUSD);
			ventaAcumuladoEUR.setMonto(new BigDecimal("0.00"));
			model.addAttribute(VENTAACUMULADOEUR, ventaAcumuladoEUR);
			model.addAttribute(MONTOBSTOTALVENTA, montoBsTotalVenta);
			compraAcumuladoUSD.setMonto(new BigDecimal("0.00"));
			model.addAttribute(COMPRAACUMULADOUSD, compraAcumuladoUSD);
			compraAcumuladoEUR.setMonto(new BigDecimal("0.00"));
			model.addAttribute(COMPRAACUMULADOEUR, compraAcumuladoEUR);
			model.addAttribute(MONTOBSTOTALCOMPRA, montoBsTotalCompra);
			ventaPorAprobarUSD.setMonto(new BigDecimal("0.00"));
			model.addAttribute(VENTAPORAPROBARUSD, ventaPorAprobarUSD);
			ventaPorAprobarEUR.setMonto(new BigDecimal("0.00"));
			model.addAttribute(VENTAPORAPROBAREUR, ventaPorAprobarEUR);
			model.addAttribute(MONTOBSTOTALPORAPROBARVENTA, montoBsTotalPorAprobarVenta);
			compraPorAprobarUSD.setMonto(new BigDecimal("0.00"));
			model.addAttribute(COMPRAPORAPROBARUSD, compraPorAprobarUSD);
			compraPorAprobarEUR.setMonto(new BigDecimal("0.00"));
			model.addAttribute(COMPRAPORAPROBAREUR, compraPorAprobarEUR);
			model.addAttribute(MONTOBSTOTALPORAPROBARCOMPRA, montoBsTotalPorAprobarCompra);
			return URLLISTAMOVIMIENTOSPORAPROBARVENTA;
		}
	}
	
	
	
	@GetMapping("/listaSolicitudesMovimientosVentas/{page}")
	public String consultaMovimientoVenta(@PathVariable("page") int page,Model model) {
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion(0,0,0,0);
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion(0,0,0,0);
		try {
			movimientosRequest.setNumeroPagina(page);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				
				listaMovimientosVenta = responseVenta.getMovimientos();
				if(!listaMovimientosVenta.isEmpty()) {
					datosPaginacionVenta = responseVenta.getDatosPaginacion();
				}else {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				}
				model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
				model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
				
				movimientosRequest.setNumeroPagina(1);
				movimientosRequest.setTamanoPagina(numeroRegistroPage);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientos(movimientosRequest);
				
				if(responseCompra != null) {
					
					listaMovimientosCompra = responseCompra.getMovimientos();
					if(!listaMovimientosCompra.isEmpty()) {
						datosPaginacionCompra = responseCompra.getDatosPaginacion();
					}else {
						model.addAttribute(MENSAJEERRORCOMPRA, MENSAJENORESULTADO);
					}
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					
					return URLLISTAMOVIMIENTOSVENTA;
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					return URLLISTAMOVIMIENTOSVENTA;
				}
				
			}else {
				return REDIRECTINICIO;
			}
		} catch (CustomException e) {
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
			model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
			model.addAttribute(MENSAJEERRORCOMPRA, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
			model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
			return URLLISTAMOVIMIENTOSVENTA;
		}
	}
	
	
	@GetMapping("/listaSolicitudesMovimientosCompras/{page}")
	public String consultaMovimientoCompra(@PathVariable("page") int page,Model model) {
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion(0,0,0,0);
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion(0,0,0,0);
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				
				listaMovimientosVenta = responseVenta.getMovimientos();
				if(!listaMovimientosVenta.isEmpty()) {
					datosPaginacionVenta = responseVenta.getDatosPaginacion();
				}else {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				}
				model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
				model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
				
				movimientosRequest.setNumeroPagina(page);
				movimientosRequest.setTamanoPagina(numeroRegistroPage);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientos(movimientosRequest);
				
				if(responseCompra != null) {
					
					listaMovimientosCompra = responseCompra.getMovimientos();
					if(!listaMovimientosCompra.isEmpty()) {
						datosPaginacionCompra = responseCompra.getDatosPaginacion();
					}else {
						model.addAttribute(MENSAJEERRORCOMPRA, MENSAJENORESULTADO);
					}
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					
					return URLLISTAMOVIMIENTOSCOMPRA;
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					return URLLISTAMOVIMIENTOSCOMPRA;
				}
				
			}else {
				return REDIRECTINICIO;
			}
		} catch (CustomException e) {
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
			model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
			model.addAttribute(MENSAJEERRORCOMPRA, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
			model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
			return URLLISTAMOVIMIENTOSCOMPRA;
		}
	}
	
	@GetMapping("/procesarCompra/{codOperacion}/{page}")
	public String procesarCompra(@PathVariable("codOperacion") String codOperacion, @PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, Movimiento movimiento ) {
		log.info("procesarCompra");
		
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		
		Movimiento movimientoProcesar = new Movimiento();
		
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtros = new Movimiento();
			filtros.setCodOperacion(codOperacion);
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			
			if(response.getResultado().getCodigo().equals("0000")) {
				movimientoProcesar = response.getMovimientos().get(0);
				movimientoProcesar.setPaginaActual(page);
				model.addAttribute(PAGINAACTUAL, page);
				model.addAttribute("movimiento", movimientoProcesar);
				return URLFORMSOLICITUD;
			}else {
				String mensajeError = response.getResultado().getDescripcion();
				redirectAttributes.addFlashAttribute(MENSAJEERROR, mensajeError);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+page;
			}	
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR,e.getMessage());
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+page;
		}
	}
	
	@PostMapping("/guardarProcesarCompra")
	public String guardarProcesarCompra(Movimiento movimiento, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		List<String> listaError = new ArrayList<>();
		model.addAttribute(PAGINAACTUAL, movimiento.getPaginaActual());
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
				if(error.getCode().equals("typeMismatch")) {
					listaError.add("Los valores de los montos tasa debe ser numerico");
				}
			}
			
			
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMSOLICITUD;
		}
		
		if(!isFechaValida(movimiento.getFecha())) {
			result.addError(new ObjectError(LISTAERROR, " La fecha liquidacion es invalida"));
			
			listaError.add("La fecha liquidacion es invalida");
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMSOLICITUD;
		}
		
		AprobarRechazarRequest aprobarRechazarRequest = getAprobarRechazarRequest();
		aprobarRechazarRequest.setIp(request.getRemoteAddr());
		aprobarRechazarRequest.setOrigen("01");
		aprobarRechazarRequest.setCodSolicitud(movimiento.getCodOperacion());
		aprobarRechazarRequest.setTipoPacto(movimiento.getTipoPacto());
		aprobarRechazarRequest.setTasa(movimiento.getTasaOperacion());
		aprobarRechazarRequest.setFechaLiquidacion(movimiento.getFecha());
		aprobarRechazarRequest.setEstatus(1);
		
		try {
			String respuesta = movimientosApiRest.aprobarCompra(aprobarRechazarRequest);
			redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+movimiento.getPaginaActual();
			
		} catch (CustomException e) {
			result.addError(new ObjectError(LISTAERROR, e.getMessage()));
			listaError.add(e.getMessage());
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMSOLICITUD;
			
		}
	}
	
	@GetMapping("/procesarVenta/{codOperacion}/{page}")
	public String procesarVenta(@PathVariable("codOperacion") String codOperacion, @PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, Movimiento movimiento ) {
		log.info("procesarVenta");
		
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		Movimiento movimientoProcesar = new Movimiento();
		
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtros = new Movimiento();
			filtros.setTipoTransaccion("V");
			filtros.setCodOperacion(codOperacion);
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			
			if(response.getResultado().getCodigo().equals("0000")) {
				movimientoProcesar = response.getMovimientos().get(0);
				movimientoProcesar.setPaginaActual(page);
				model.addAttribute(PAGINAACTUAL, page);
				model.addAttribute("movimiento", movimientoProcesar);
				return URLFORMSOLICITUDVENTA;
			}else {
				String mensajeError = response.getResultado().getCodigo() + " " + response.getResultado().getDescripcion();
				redirectAttributes.addFlashAttribute(MENSAJEERRORVENTA, mensajeError);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+page;
			}
			
			
			
			
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERRORVENTA,e.getMessage());
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+page;
		}
	}
	
	
	@PostMapping("/guardarProcesarVenta")
	public String guardarProcesarVenta(Movimiento movimiento, BindingResult result, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {

		List<String> listaError = new ArrayList<>();
		model.addAttribute(PAGINAACTUAL, movimiento.getPaginaActual());
		if (result.hasErrors()) {
			for (ObjectError error : result.getAllErrors()) {
				log.info("Ocurrio un error: " + error.getDefaultMessage());
				if(error.getCode().equals("typeMismatch")) {
					listaError.add("Los valores de los montos tasa debe ser numerico");
				}
			}
			
			
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMSOLICITUDVENTA;
		}
		
		if(!isFechaValida(movimiento.getFecha())) {
			result.addError(new ObjectError(LISTAERROR, " La fecha liquidacion es invalida"));
			listaError.add("La fecha liquidacion es invalida");
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMSOLICITUDVENTA;
		}
		
		AprobarRechazarRequest aprobarRechazarRequest = getAprobarRechazarRequest();
		aprobarRechazarRequest.setIp(request.getRemoteAddr());
		aprobarRechazarRequest.setOrigen("01");
		aprobarRechazarRequest.setCodSolicitud(movimiento.getCodOperacion());
		aprobarRechazarRequest.setTipoPacto(movimiento.getTipoPacto());
		aprobarRechazarRequest.setTasa(movimiento.getTasaOperacion());
		aprobarRechazarRequest.setFechaLiquidacion(movimiento.getFecha());
		aprobarRechazarRequest.setEstatus(1);
		
		try {
			String respuesta = movimientosApiRest.aprobarVenta(aprobarRechazarRequest);
			redirectAttributes.addFlashAttribute(MENSAJEVENTA, respuesta);
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+movimiento.getPaginaActual();
			
		} catch (CustomException e) {
			result.addError(new ObjectError(LISTAERROR, e.getMessage()));
			listaError.add(e.getMessage());
			model.addAttribute(LISTAERROR, listaError);
			return URLFORMSOLICITUDVENTA;
		}
	}
	
	
	@GetMapping("/rechazarCompra/{codOperacion}/{page}")
	public String rechazarCompra(@PathVariable("codOperacion") String codOperacion, 
			@PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request ) {
		log.info("rechazarCompra");
		
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		Movimiento movimientoProcesar = new Movimiento();
		
		
		
		try {
			
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtros = new Movimiento();
			filtros.setCodOperacion(codOperacion);
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			
			
			if(response.getResultado().getCodigo().equals("0000")) {
				movimientoProcesar = response.getMovimientos().get(0);
				movimientoProcesar.setPaginaActual(page);
				model.addAttribute(PAGINAACTUAL, page);
				
				AprobarRechazarRequest aprobarRechazarRequest = getAprobarRechazarRequest();
				aprobarRechazarRequest.setIp(request.getRemoteAddr());
				aprobarRechazarRequest.setOrigen("01");
				aprobarRechazarRequest.setCodSolicitud(codOperacion);
				aprobarRechazarRequest.setTipoPacto(movimientoProcesar.getTipoPacto());
				aprobarRechazarRequest.setTasa(movimientoProcesar.getTasaCliente());
				aprobarRechazarRequest.setFechaLiquidacion(fecha(new Date()));
				aprobarRechazarRequest.setEstatus(2);
				
				String respuesta = movimientosApiRest.rechazarCompra(aprobarRechazarRequest);
				redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+page;
				
				
			}else {
				String mensajeError = response.getResultado().getCodigo() + " " + response.getResultado().getDescripcion();
				redirectAttributes.addFlashAttribute(MENSAJEERROR, mensajeError);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+page;
			}
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR,e.getMessage());
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+page;
		}
	}
	
	@GetMapping("/aprobarCompra/{codOperacion}/{page}")
	public String aprobarCompra(@PathVariable("codOperacion") String codOperacion, 
			@PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request ) {
		log.info("aprobarCompra");
		
		
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		Movimiento movimientoProcesar = new Movimiento();
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtros = new Movimiento();
			filtros.setCodOperacion(codOperacion);
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			
			if(response.getResultado().getCodigo().equals("0000")) {
				movimientoProcesar = response.getMovimientos().get(0);
				movimientoProcesar.setPaginaActual(page);
				model.addAttribute(PAGINAACTUAL, page);
				
				
				AprobarRechazarRequest aprobarRechazarRequest = getAprobarRechazarRequest();
				aprobarRechazarRequest.setIp(request.getRemoteAddr());
				aprobarRechazarRequest.setOrigen("01");
				aprobarRechazarRequest.setCodSolicitud(codOperacion);
				aprobarRechazarRequest.setTipoPacto(movimientoProcesar.getTipoPacto());
				aprobarRechazarRequest.setTasa(movimientoProcesar.getTasaCliente());
				aprobarRechazarRequest.setFechaLiquidacion(fecha(new Date()));
				aprobarRechazarRequest.setEstatus(1);
				
				String respuesta = movimientosApiRest.aprobarCompra(aprobarRechazarRequest);
				redirectAttributes.addFlashAttribute(MENSAJE, respuesta);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+page;
				
				
			}else {
				String mensajeError = response.getResultado().getDescripcion();
				redirectAttributes.addFlashAttribute(MENSAJEERROR, mensajeError);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+page;
			}
			
			
			
			
			
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR,e.getMessage());
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARCOMPRA+page;
		}
	}
	
	@GetMapping("/rechazarVenta/{codOperacion}/{page}")
	public String rechazarVenta(@PathVariable("codOperacion") String codOperacion, 
			@PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request ) {
		log.info("rechazarVenta");
		
		
		
		
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		Movimiento movimientoProcesar = new Movimiento();
		
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtros = new Movimiento();
			filtros.setCodOperacion(codOperacion);
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			
			
			if(response.getResultado().getCodigo().equals("0000")) {
				movimientoProcesar = response.getMovimientos().get(0);
				movimientoProcesar.setPaginaActual(page);
				model.addAttribute(PAGINAACTUAL, page);
				
				AprobarRechazarRequest aprobarRechazarRequest = getAprobarRechazarRequest();
				aprobarRechazarRequest.setIp(request.getRemoteAddr());
				aprobarRechazarRequest.setOrigen("01");
				aprobarRechazarRequest.setCodSolicitud(codOperacion);
				aprobarRechazarRequest.setTipoPacto(movimientoProcesar.getTipoPacto());
				aprobarRechazarRequest.setTasa(movimientoProcesar.getTasaCliente());
				aprobarRechazarRequest.setFechaLiquidacion(fecha(new Date()));
				aprobarRechazarRequest.setEstatus(2);
				
				
				String respuesta = movimientosApiRest.rechazarVenta(aprobarRechazarRequest);
				redirectAttributes.addFlashAttribute(MENSAJEVENTA, respuesta);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+page;
				
				
			}else {
				String mensajeError = response.getResultado().getDescripcion();
				redirectAttributes.addFlashAttribute(MENSAJEERROR, mensajeError);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+page;
			}
			
			
			
			
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERRORVENTA,e.getMessage());
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+page;
		}
	}
	
	@GetMapping("/aprobarVenta/{codOperacion}/{page}")
	public String aprobarVenta(@PathVariable("codOperacion") String codOperacion, 
			@PathVariable("page") int page, Model model,
			RedirectAttributes redirectAttributes, HttpServletRequest request ) {
		log.info("aprobarVenta");
		
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		Movimiento movimientoProcesar = new Movimiento();
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtros = new Movimiento();
			filtros.setCodOperacion(codOperacion);
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			
			
			if(response.getResultado().getCodigo().equals("0000")) {
				movimientoProcesar = response.getMovimientos().get(0);
				movimientoProcesar.setPaginaActual(page);
				model.addAttribute(PAGINAACTUAL, page);
				
				AprobarRechazarRequest aprobarRechazarRequest = getAprobarRechazarRequest();
				aprobarRechazarRequest.setIp(request.getRemoteAddr());
				aprobarRechazarRequest.setOrigen("01");
				aprobarRechazarRequest.setCodSolicitud(codOperacion);
				aprobarRechazarRequest.setTipoPacto(movimientoProcesar.getTipoPacto());
				aprobarRechazarRequest.setTasa(movimientoProcesar.getTasaCliente());
				aprobarRechazarRequest.setFechaLiquidacion(fecha(new Date()));
				aprobarRechazarRequest.setEstatus(1);
				
				
				String respuesta = movimientosApiRest.aprobarVenta(aprobarRechazarRequest);
				redirectAttributes.addFlashAttribute(MENSAJEVENTA, respuesta);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+page;
				
				
			}else {
				String mensajeError = response.getResultado().getCodigo() + " " + response.getResultado().getDescripcion();
				redirectAttributes.addFlashAttribute(MENSAJEERRORVENTA, mensajeError);
				return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+page;
			}
			
			
			
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERRORVENTA,e.getMessage());
			return REDIRECTLISTAMOVIMIENTOSPORAPROBARVENTAS+page;
		}
	}
	
	
	
	
	
	@GetMapping("/precesarConsulta/export/excel")
    public String procesarExportToExcelConsulta(Movimiento movimiento, BindingResult result, Model model, 
    		RedirectAttributes redirectAttributes, HttpServletResponse response){
		log.info("exportToExcelConsulta");
        
		List<String> listaError = new ArrayList<>();
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		List<Movimiento> listaMovimientos = new ArrayList<>();
		
		try {
			
			
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(2147483647);
			Movimiento filtros = new Movimiento();
			if(!movimiento.getCodMoneda().equals(""))
				filtros.setCodMoneda(movimiento.getCodMoneda());
			if(!movimiento.getTipoTransaccion().equals(""))
			    filtros.setTipoTransaccion(movimiento.getTipoTransaccion());
			if(movimiento.getEstatus() != -1)
				filtros.setEstatus(movimiento.getEstatus());
			if(!movimiento.getTipoCliente().equals(""))
				filtros.setTipoCliente(movimiento.getTipoCliente());
			
			
			if(!movimiento.getFechas().getFechaDesde().equals("") && !movimiento.getFechas().getFechaHasta().equals("")) {
				if(isFechaValidaDesdeHasta(movimiento.getFechas().getFechaDesde(), movimiento.getFechas().getFechaHasta())) {
					Fechas fechas = new Fechas();
					fechas.setFechaDesde(movimiento.getFechas().getFechaDesde());
					fechas.setFechaHasta(movimiento.getFechas().getFechaHasta());
					filtros.setFechas(fechas);
				}else {
					MonedasRequest monedasRequest = getMonedasRequest();
					Moneda moneda = new Moneda();
					moneda.setFlagActivo(true);
					monedasRequest.setMoneda(moneda);
					model.addAttribute(LISTAMONEDAS, monedaServiceApiRest.listaMonedas(monedasRequest));
					result.addError(new ObjectError(LISTAERROR, MENSAJEFECHASINVALIDAS));
					listaError.add(MENSAJEFECHASINVALIDAS);
					model.addAttribute(LISTAERROR, listaError);
					
				}
			}
			
			
			
			movimientosRequest.setFiltros(filtros);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				listaMovimientos = responseVenta.getMovimientos();
				if(!listaMovimientos.isEmpty()) {
					exportToExcelConsulta(listaMovimientos, response);
				}else {
					MonedasRequest monedasRequest = getMonedasRequest();
					Moneda moneda = new Moneda();
					moneda.setFlagActivo(true);
					monedasRequest.setMoneda(moneda);
					model.addAttribute(LISTAERROR, monedaServiceApiRest.listaMonedas(monedasRequest));
					result.addError(new ObjectError(LISTAERROR, MENSAJENORESULTADO));
					model.addAttribute(LISTAERROR, MENSAJENORESULTADO);
				}
			}else {
				result.addError(new ObjectError(LISTAERROR, MENSAJENORESULTADO));
				model.addAttribute(LISTAERROR, MENSAJENORESULTADO);
			}
		} catch (CustomException e) {
			result.addError(new ObjectError(LISTAERROR,e.getMessage()));
			 listaError.add(e.getMessage());
			 model.addAttribute(LISTAERROR, listaError);			
		}
		
		return URLFORMSOLICITUDGENERARREPORTE;
    }
	
	
    public void exportToExcelConsulta(List<Movimiento> listaMovimientos, HttpServletResponse response) {
		log.info("exportToExcelConsulta");
        
		
		
				response.setContentType("application/octet-stream");
		        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		        String currentDateTime = dateFormatter.format(new Date());
		         
		        String headerKey = "Content-Disposition";
		        String headerValue = "attachment; filename=movimientosconsulta_" + currentDateTime + ".xlsx";
		        response.setHeader(headerKey, headerValue);
		        ConsultaExcelExporter excelExporter = new ConsultaExcelExporter(listaMovimientos); 
		        try {
					excelExporter.export(response);
					response.getOutputStream().flush();
					 response.getOutputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		
    }
	
    
	
	
	
	public Venta acumuladosVenta(String codMoneda) throws CustomException  {
		AcumuladoRequest acumuladoRequest = getAcumuladoRequest();
		acumuladoRequest.setTipoAcumulado("2");
		DatosConsulta datosConsulta = new DatosConsulta();
		datosConsulta.setFechaDesde(fecha(new Date()));
		datosConsulta.setFechaHasta(fecha(new Date()));
		acumuladoRequest.setDatosConsulta(datosConsulta);
		List<Venta> listaVenta;
		Venta ventaRes = new Venta("","", new BigDecimal("0.00"), new BigDecimal("0.00"));
		
		
		
		AcumuladoCompraVentaResponse acumuladoCompraVentaResponse = acumuladosServiceApiRest.consultarAcumuladosCompraVenta(acumuladoRequest);
		if(acumuladoCompraVentaResponse.getResultado().getCodigo().equals("0000")) {
			listaVenta = acumuladoCompraVentaResponse.getAcumuladosCompraVenta().getVenta();
			for (Venta venta : listaVenta) {
				if(venta.getCodMoneda().equals(codMoneda)) {
					ventaRes = venta;
				}
			}
			
			return ventaRes;
		}else {
			return ventaRes;
		}
		
		
		
		
		
	}
	
	
	
	public Compra acumuladosCompra(String codMoneda) throws CustomException  {
		AcumuladoRequest acumuladoRequest = getAcumuladoRequest();
		acumuladoRequest.setTipoAcumulado("2");
		DatosConsulta datosConsulta = new DatosConsulta();
		datosConsulta.setFechaDesde(fecha(new Date()));
		datosConsulta.setFechaHasta(fecha(new Date()));
		acumuladoRequest.setDatosConsulta(datosConsulta);
		List<Compra> listaCompra;
		Compra compraRes = new Compra("","", new BigDecimal("0.00"), new BigDecimal("0.00"));
		AcumuladoCompraVentaResponse acumuladoCompraVentaResponse = acumuladosServiceApiRest.consultarAcumuladosCompraVenta(acumuladoRequest);
		if(acumuladoCompraVentaResponse.getResultado().getCodigo().equals("0000")) {
			listaCompra = acumuladoCompraVentaResponse.getAcumuladosCompraVenta().getCompra();
			for (Compra compra : listaCompra) {
				if(compra.getCodMoneda().equals(codMoneda)) {
					compraRes = compra;
				}
			}
			
			return compraRes;
		}else {
			return compraRes;
		}
		
	}
	
	
	
	
	
	public Venta acumuladosPorAprobarVenta(String codMoneda) throws CustomException  {
		AcumuladoRequest acumuladoRequest = getAcumuladoRequest();
		acumuladoRequest.setTipoAcumulado("3");
		DatosConsulta datosConsulta = new DatosConsulta();
		datosConsulta.setFechaDesde("2021-01-01");
		datosConsulta.setFechaHasta(fecha(new Date()));
		acumuladoRequest.setDatosConsulta(datosConsulta);
		List<Venta> listaPorAprobarVenta;
		Venta ventaPorAprobarRes = new Venta("","", new BigDecimal("0.00"), new BigDecimal("0.00"));
		AcumuladoResponse acumuladoResponse = acumuladosServiceApiRest.consultarAcumuladosDiariosBanco(acumuladoRequest);
		if(acumuladoResponse.getResultado().getCodigo().equals("0000")) {
			listaPorAprobarVenta = acumuladoResponse.getAcumuladosPorAprobar().getVenta();
			for (Venta venta : listaPorAprobarVenta) {
				if(venta.getCodMoneda().equals(codMoneda)) {
					ventaPorAprobarRes = venta;
				}
			}
			
			return ventaPorAprobarRes;
		}else {
			return ventaPorAprobarRes;
		}
	}
	
	
	
	public Compra acumuladosPorAprobarCompra(String codMoneda) throws CustomException  {
		AcumuladoRequest acumuladoRequest = getAcumuladoRequest();
		acumuladoRequest.setTipoAcumulado("3");
		DatosConsulta datosConsulta = new DatosConsulta();
		datosConsulta.setFechaDesde("2021-01-01");
		datosConsulta.setFechaHasta(fecha(new Date()));
		acumuladoRequest.setDatosConsulta(datosConsulta);
		List<Compra> listaPorAprobarCompra;
		Compra compraPorAprobarRes = new Compra("","", new BigDecimal("0.00"), new BigDecimal("0.00"));
		AcumuladoResponse acumuladoResponse = acumuladosServiceApiRest.consultarAcumuladosDiariosBanco(acumuladoRequest);
		if(acumuladoResponse.getResultado().getCodigo().equals("0000")) {
			listaPorAprobarCompra = acumuladoResponse.getAcumuladosPorAprobar().getCompra();
			for (Compra compra : listaPorAprobarCompra) {
				if(compra.getCodMoneda().equals(codMoneda)) {
					compraPorAprobarRes = compra;
				}
			}
			
			return compraPorAprobarRes;
		}else {
			return compraPorAprobarRes;
		}
		
	}
	
	
	
	
	public MovimientosRequest getMovimientosRequest() {
		MovimientosRequest movimientosRequest = new MovimientosRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		movimientosRequest.setIdUsuario(userName);
		movimientosRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		movimientosRequest.setUsuario(userName);
		movimientosRequest.setCanal(canal);
		return movimientosRequest;
	}
	
	public AprobarRechazarRequest getAprobarRechazarRequest() {
		AprobarRechazarRequest aprobarRechazarRequest = new AprobarRechazarRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		aprobarRechazarRequest.setIdUsuario(userName);
		aprobarRechazarRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		aprobarRechazarRequest.setCodUsuario(userName);
		aprobarRechazarRequest.setCanal(canal);
		return aprobarRechazarRequest;
	}
	
	public AcumuladoRequest getAcumuladoRequest() {
		AcumuladoRequest acumuladoRequest = new AcumuladoRequest();
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		acumuladoRequest.setIdUsuario(userName);
		acumuladoRequest.setIdSesion(libreriaUtil.obtenerIdSesion());
		acumuladoRequest.setCanal(canal);
		return acumuladoRequest;
		
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
		Movimiento movimientoSearch = new Movimiento();
		model.addAttribute("movimientoSearch", movimientoSearch);
		log.info("se ejecuta setGenericos");
		String uri = request.getRequestURI();
		log.info("uri: "+uri);
		String[] arrUri = uri.split("/");

		arrUri[0] = "Home";
		model.addAttribute("arrUri", arrUri);
		
	}
	
	
	@GetMapping("/searchEstatus")
	public String searchEstatus(@ModelAttribute("movimientoSearch") Movimiento movimientoSearch, 
			Model model, RedirectAttributes redirectAttributes) {
		
		log.info("searchEstatus");
		
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion(0,0,0,0);
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion(0,0,0,0);
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			filtrosVenta.setEstatus(movimientoSearch.getEstatus());
			

			if(isFechaValidaDesdeHasta(movimientoSearch.getFechas().getFechaDesde(), movimientoSearch.getFechas().getFechaHasta())) {
					Fechas fechas = new Fechas();
					fechas.setFechaDesde(movimientoSearch.getFechas().getFechaDesde());
					fechas.setFechaHasta(movimientoSearch.getFechas().getFechaHasta());
					filtrosVenta.setFechas(fechas);
			}else {
					model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
					model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
					model.addAttribute(MENSAJEERROR, MENSAJEFECHASINVALIDAS);
					movimientosRequest.setNumeroPagina(1);
					movimientosRequest.setTamanoPagina(numeroRegistroPage);
					Movimiento filtrosCompra = new Movimiento();
					filtrosCompra.setTipoTransaccion("C");
					movimientosRequest.setFiltros(filtrosCompra);
					MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientos(movimientosRequest);
					
					if(responseCompra != null) {
						listaMovimientosCompra = responseCompra.getMovimientos();
						if(!listaMovimientosCompra.isEmpty()) {
							datosPaginacionCompra = responseCompra.getDatosPaginacion();
						}else {
							model.addAttribute(MENSAJEERRORCOMPRA, MENSAJENORESULTADO);
						}
						
						model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
						model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
						model.addAttribute(ESTATUS, movimientoSearch.getEstatus());
						return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
						
						
						
					}else {
						datosPaginacionCompra.setTotalPaginas(0);
						model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
						model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
						return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
					}
			}
			
			
			
			
			
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				listaMovimientosVenta = responseVenta.getMovimientos();
				if(!listaMovimientosVenta.isEmpty()) {
					datosPaginacionVenta = responseVenta.getDatosPaginacion();
				}else {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);
				}
					
				
				model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
				model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
				model.addAttribute(ESTATUS, movimientoSearch.getEstatus());
				model.addAttribute(FECHADESDE, movimientoSearch.getFechas().getFechaDesde());
				model.addAttribute(FECHAHASTA, movimientoSearch.getFechas().getFechaHasta());
				
				movimientosRequest.setNumeroPagina(1);
				movimientosRequest.setTamanoPagina(numeroRegistroPage);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientos(movimientosRequest);
				
				if(responseCompra != null) {
					listaMovimientosCompra = responseCompra.getMovimientos();
					if(!listaMovimientosCompra.isEmpty()) {
						datosPaginacionCompra = responseCompra.getDatosPaginacion();
					}else {
						model.addAttribute(MENSAJEERRORCOMPRA, MENSAJENORESULTADO);
					}
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
				}
				
			}else {
				return REDIRECTINICIO;
			}
		} catch (CustomException e) {
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
			model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
			model.addAttribute(MENSAJEERRORCOMPRA, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
			model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
			return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
		}
		
	}
	
	@GetMapping("/listaSolicitudesMovimientosVentas/{page}/{estatus}/{fechaDesde}/{fechaHasta}")
	public String consultaMovimientoVentaSearchEstatus(@PathVariable("page") int page,@PathVariable("estatus") int estatus, 
			@PathVariable("fechaDesde") String fechaDesde, @PathVariable("fechaHasta") String fechaHasta,Model model) {

		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion(0,0,0,0);
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion(0,0,0,0);
		try {
			movimientosRequest.setNumeroPagina(page);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			filtrosVenta.setEstatus(estatus);
			
			if(isFechaValidaDesdeHasta(fechaDesde, fechaHasta)) {
				Fechas fechas = new Fechas();
				fechas.setFechaDesde(fechaDesde);
				fechas.setFechaHasta(fechaHasta);
				filtrosVenta.setFechas(fechas);
			}else {
				model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
				model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
				model.addAttribute(MENSAJEERROR, "Los valores de las fechas son invalidos");
				
				movimientosRequest.setNumeroPagina(1);
				movimientosRequest.setTamanoPagina(numeroRegistroPage);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientos(movimientosRequest);
				
				if(responseCompra != null) {
					listaMovimientosCompra = responseCompra.getMovimientos();
					if(!listaMovimientosCompra.isEmpty()) {
						datosPaginacionCompra = responseCompra.getDatosPaginacion();
					}else {
						model.addAttribute(MENSAJEERRORCOMPRA, MENSAJENORESULTADO);
					}
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
				}
			}
			
			
			
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				
				
				listaMovimientosVenta = responseVenta.getMovimientos();
				if(!listaMovimientosVenta.isEmpty()) {
					datosPaginacionVenta = responseVenta.getDatosPaginacion();
				}else {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);	
				}
					
				model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
				model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
				model.addAttribute(ESTATUS, estatus);
				model.addAttribute(FECHADESDE, fechaDesde);
				model.addAttribute(FECHAHASTA, fechaHasta);
				
				movimientosRequest.setNumeroPagina(1);
				movimientosRequest.setTamanoPagina(numeroRegistroPage);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientos(movimientosRequest);
				
				if(responseCompra != null) {
					
					listaMovimientosCompra = responseCompra.getMovimientos();
					if(!listaMovimientosCompra.isEmpty()) {
						datosPaginacionCompra = responseCompra.getDatosPaginacion();
					}else {
						model.addAttribute(MENSAJEERRORCOMPRA, MENSAJENORESULTADO);
					}
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					
					return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
				}
				
			}else {
				return REDIRECTINICIO;
			}
		} catch (CustomException e) {
			model.addAttribute(MENSAJEERROR, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
			model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
			model.addAttribute(MENSAJEERRORCOMPRA, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
			model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
			return URLLISTAMOVIMIENTOSVENTASEARCHESTATUS;
		}
	}
	
	@GetMapping("/searchEstatusCompra")
	public String searchEstatusCompra(@ModelAttribute("movimientoSearch") Movimiento movimientoSearch, 
			Model model, RedirectAttributes redirectAttributes) {
		
		log.info("searchEstatusCompra");
		
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion(0,0,0,0);
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion(0,0,0,0);
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				
				listaMovimientosVenta = responseVenta.getMovimientos();
				if(!listaMovimientosVenta.isEmpty()) {
					datosPaginacionVenta = responseVenta.getDatosPaginacion();
				}else {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);	
				}
				model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
				model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
				
				movimientosRequest.setNumeroPagina(1);
				movimientosRequest.setTamanoPagina(numeroRegistroPage);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				filtrosCompra.setEstatus(movimientoSearch.getEstatus());
				
				
				if(isFechaValidaDesdeHasta(movimientoSearch.getFechas().getFechaDesde(), movimientoSearch.getFechas().getFechaHasta())) {
					Fechas fechas = new Fechas();
					fechas.setFechaDesde(movimientoSearch.getFechas().getFechaDesde());
					fechas.setFechaHasta(movimientoSearch.getFechas().getFechaHasta());
					filtrosCompra.setFechas(fechas);
				}else {
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(MENSAJEERRORCOMPRA, "Los valores de las fechas son invalidos");
					return URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS;
				}
				
				
				
				
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientos(movimientosRequest);
				
				if(responseCompra != null) {
					
					listaMovimientosCompra = responseCompra.getMovimientos();
					if(!listaMovimientosCompra.isEmpty()) {
						datosPaginacionCompra = responseCompra.getDatosPaginacion();
					}else {
						model.addAttribute(MENSAJEERRORCOMPRA, MENSAJENORESULTADO);
					}
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					model.addAttribute(ESTATUS, movimientoSearch.getEstatus());
					model.addAttribute(FECHADESDE, movimientoSearch.getFechas().getFechaDesde());
					model.addAttribute(FECHAHASTA, movimientoSearch.getFechas().getFechaHasta());
					return URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS;
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					return URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS;
				}
				
			}else {
				return REDIRECTINICIO;
			}
		} catch (CustomException e) {
			model.addAttribute(MENSAJEERRORCOMPRA, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
			model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
			model.addAttribute(MENSAJEERRORCOMPRA, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
			model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
			return URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS;
		}
		
	}
	
	@GetMapping("/listaSolicitudesMovimientosCompras/{page}/{estatus}/{fechaDesde}/{fechaHasta}")
	public String consultaMovimientoCompraSearchEstatus(@PathVariable("page") int page,@PathVariable("estatus") int estatus,
			@PathVariable("fechaDesde") String fechaDesde, @PathVariable("fechaHasta") String fechaHasta,Model model) {
		
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		
		
		List<Movimiento> listaMovimientosVenta = new ArrayList<>();
		DatosPaginacion datosPaginacionVenta = new DatosPaginacion(0,0,0,0);
		
		List<Movimiento> listaMovimientosCompra = new ArrayList<>();
		DatosPaginacion datosPaginacionCompra = new DatosPaginacion(0,0,0,0);
		try {
			movimientosRequest.setNumeroPagina(1);
			movimientosRequest.setTamanoPagina(numeroRegistroPage);
			Movimiento filtrosVenta = new Movimiento();
			filtrosVenta.setTipoTransaccion("V");
			movimientosRequest.setFiltros(filtrosVenta);
			MovimientosResponse responseVenta = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(responseVenta != null) {
				
				listaMovimientosVenta = responseVenta.getMovimientos();
				if(!listaMovimientosVenta.isEmpty()) {
					datosPaginacionVenta = responseVenta.getDatosPaginacion();
				}else {
					model.addAttribute(MENSAJEERROR, MENSAJENORESULTADO);	
				}
				model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
				model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
				
				movimientosRequest.setNumeroPagina(page);
				movimientosRequest.setTamanoPagina(numeroRegistroPage);
				Movimiento filtrosCompra = new Movimiento();
				filtrosCompra.setTipoTransaccion("C");
				filtrosCompra.setEstatus(estatus);
				
				
				if(isFechaValidaDesdeHasta(fechaDesde, fechaHasta)) {
					Fechas fechas = new Fechas();
					fechas.setFechaDesde(fechaDesde);
					fechas.setFechaHasta(fechaHasta);
					filtrosCompra.setFechas(fechas);
				}else {
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					model.addAttribute(MENSAJEERRORCOMPRA, "Los valores de las fechas son invalidos");
					return URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS;
				}
				
				movimientosRequest.setFiltros(filtrosCompra);
				MovimientosResponse responseCompra = movimientosApiRest.consultarMovimientos(movimientosRequest);
				
				if(responseCompra != null) {
					
					listaMovimientosCompra = responseCompra.getMovimientos();
					if(!listaMovimientosCompra.isEmpty()) {
						datosPaginacionCompra = responseCompra.getDatosPaginacion();
					}else {
						model.addAttribute(MENSAJEERRORCOMPRA, MENSAJENORESULTADO);
					}
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					model.addAttribute(ESTATUS, estatus);
					return URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS;
					
					
					
				}else {
					datosPaginacionCompra.setTotalPaginas(0);
					model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
					model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
					return URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS;
				}
				
			}else {
				return REDIRECTINICIO;
			}
		} catch (CustomException e) {
			log.error(e.getMessage());
			model.addAttribute(MENSAJEERRORCOMPRA, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSVENTA, listaMovimientosVenta);
			model.addAttribute(DATOSPAGINACIONVENTA, datosPaginacionVenta);
			model.addAttribute(MENSAJEERRORCOMPRA, e.getMessage());
			model.addAttribute(LISTAMOVIMIENTOSCOMPRA, listaMovimientosCompra);
			model.addAttribute(DATOSPAGINACIONCOMPRA, datosPaginacionCompra);
			return URLLISTAMOVIMIENTOSCOMPRASEARCHESTATUS;
		}
	}
	
	@GetMapping("/formSolicitudGenerarReporte")
	public String formSolicitudGenerarReporte(Movimiento movimiento, Model model, RedirectAttributes redirectAttributes) {
		
		List<Moneda> listaMonedas = new ArrayList<>();
		
		MonedasRequest monedasRequest = getMonedasRequest();
		Moneda moneda = new Moneda();
		moneda.setFlagActivo(true);
		monedasRequest.setMoneda(moneda);
		
		try {
			listaMonedas = monedaServiceApiRest.listaMonedas(monedasRequest);
			model.addAttribute(LISTAMONEDAS, listaMonedas);
			return URLFORMSOLICITUDGENERARREPORTE;
		} catch (CustomException e) {
			redirectAttributes.addFlashAttribute(MENSAJEERROR, e.getMessage());
			return "redirect:/solicitudes/listaSolicitudesMovimientosVentas/1";
		}
		
	}
	
	
	
	public String fecha(Date fecha) {
		
		String strDateFormat = STRDATEFORMET;   
        SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat); 
        
        return objSDF.format(fecha);
	}
	
	
	public boolean isFechaValida(String fechaLiquidacion) {
		
		SimpleDateFormat formato = new SimpleDateFormat(STRDATEFORMET);
		
        try {
        	
        	
        	Date fechaDate1 = formato.parse(fechaLiquidacion);
        	Date fechaDate2 = formato.parse(fecha(new Date()));
        	
        	if ( fechaDate1.before(fechaDate2) ){
        	    log.info("La fechaLiquidacion es menor que la actual");
        		return false;
        	}else{
        	     if ( fechaDate2.before(fechaDate1) ){
        	    	 log.info("La fechaActual es menor que la fechaLiquidacion");
        	    	 return true;
        	     }else{
        	    	 log.info("La fechaActual es igual que la fechaLiquidacion");
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
	
	@GetMapping("/listaSolicitudesMovimientosVentas")
	public String consultaMovimientoVenta1(Model model) {
		MovimientosRequest movimientosRequest = getMovimientosRequest();
		movimientosRequest.setNumeroPagina(1);
		movimientosRequest.setTamanoPagina(numeroRegistroPage);
		Movimiento filtros = new Movimiento();
		filtros.setTipoTransaccion("V");
		movimientosRequest.setFiltros(filtros);
		
		try {
			MovimientosResponse response = movimientosApiRest.consultarMovimientos(movimientosRequest);
			if(response != null) {
				DatosPaginacion datosPaginacion = response.getDatosPaginacion();
				List<Movimiento> listaMovimientos = response.getMovimientos();
				model.addAttribute("listaMovimientos", listaMovimientos);
				model.addAttribute("datosPaginacion", datosPaginacion);
				return URLLISTAMOVIMIENTOSVENTA;
			}else {
				return REDIRECTINICIO;
			}
		} catch (CustomException e) {
			log.error(e.getMessage());
			return REDIRECTINICIO;
		}
	}
	
	
	public Venta evaluarBigDecimalVenta(Venta ventaAcumulado) {
		if(ventaAcumulado.getMonto().compareTo(BigDecimal.ZERO) == 0) { 			
			ventaAcumulado.setMonto(new BigDecimal("0.00"));
		}
		return ventaAcumulado;
	}
	
	public Compra evaluarBigDecimalCompra(Compra compraAcumulado) {
		if(compraAcumulado.getMonto().compareTo(BigDecimal.ZERO) == 0) { 			
			compraAcumulado.setMonto(new BigDecimal("0.00"));
		}
		return compraAcumulado;
	}
	
	public BigDecimal evaluarBigDecimal(BigDecimal valor) {
		if(valor.compareTo(BigDecimal.ZERO) == 0) { 			
			return new BigDecimal("0.00");
		}else {
			return valor;
		}
	}

}
