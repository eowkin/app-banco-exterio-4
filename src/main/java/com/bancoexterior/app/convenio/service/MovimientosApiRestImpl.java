package com.bancoexterior.app.convenio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.AprobarRechazarRequest;
import com.bancoexterior.app.convenio.dto.AprobarRechazarResponse;
import com.bancoexterior.app.convenio.dto.MovimientosRequest;
import com.bancoexterior.app.convenio.dto.MovimientosResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.model.Movimiento;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MovimientosApiRestImpl implements IMovimientosApiRest{

	@Autowired
	private IWSService wsService;
    
    @Autowired 
	private Mapper mapper;
    
    @Value("${${app.ambiente}"+".ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${${app.ambiente}"+".SocketTimeout}")
    private int socketTimeout;
    
    @Value("${${app.ambiente}"+".movimientos.consultarMovimientosPorAprobar}")
    private String urlConsultarMovimientosPorAprobar;
    //https://172.19.148.51:8443/api/des/V1/divisas/consultasmovimientos?sort=codMoneda,desc&sort=tasaCliente,desc&sort=montoDivisa,asc
    
    @Value("${${app.ambiente}"+".movimientos.consultarMovimientosPorAprobarVenta}")
    private String urlConsultarMovimientosPorAprobarVenta;
    //https://172.19.148.51:8443/api/des/V1/divisas/consultasmovimientos?sort=codMoneda,desc&sort=tasaCliente,asc&sort=montoDivisa,desc
    
    @Value("${${app.ambiente}"+".movimientos.consultarMovimientos}")
    private String urlConsultarMovimientos;
    //https://172.19.148.51:8443/api/des/V1/divisas/consultasmovimientos
    
    @Value("${${app.ambiente}"+".movimientos.compra.actualizar}")
    private String urlActualizarMovimientosCompra;
    //https://172.19.148.51:8443/api/des/V1/divisas/aprobacionescompras
    
    @Value("${${app.ambiente}"+".movimientos.venta.actualizar}")
    private String urlActualizarMovimientosVenta;
    //https://172.19.148.51:8443/api/des/V1/divisas/aprobacionesventas
    
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio Movimientos";
    
    private static final String MOVIMIENTOSSERVICECONSULTARPORAPROBARI = "[==== INICIO ConsultarMovimientosPorAprobar Movimientos Consultas - Service ====]";
	
	private static final String MOVIMIENTOSSERVICECONSULTARPORAPROBARF = "[==== FIN ConsultarMovimientosPorAprobar Movimientos Consultas - Service ====]";
	
	private static final String MOVIMIENTOSSERVICECONSULTARPORAPROBARVENTAI = "[==== INICIO ConsultarMovimientosPorAprobarVenta Movimientos Consultas - Service ====]";
	
	private static final String MOVIMIENTOSSERVICECONSULTARPORAPROBARVENTAF = "[==== FIN ConsultarMovimientosPorAprobarVenta Movimientos Consultas - Service ====]";
	
	private static final String MOVIMIENTOSSERVICECONSULTARMOVIMIENTOSI = "[==== INICIO ConsultarMovimientos Movimientos Consultas - Service ====]";
	
	private static final String MOVIMIENTOSSERVICECONSULTARMOVIMIENTOSF = "[==== FIN ConsultarMovimientos Movimientos Consultas - Service ====]";
	
	private static final String MOVIMIENTOSSERVICERECHAZARCOMPRAI = "[==== INICIO RechazarCompra Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICERECHAZARCOMPRAF = "[==== FIN RechazarCompra Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICEAPROBARCOMPRAI = "[==== INICIO AprobarCompra Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICEAPROBARCOMPRAF = "[==== FIN AprobarCompra Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICERECHAZARVENTAI = "[==== INICIO RechazarVenta Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICERECHAZARVENTAF = "[==== FIN RechazarVenta Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICEAPROBARVENTAI = "[==== INICIO AprobarVenta Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICEAPROBARVENTAF = "[==== FIN AprobarVenta Movimientos - Service ====]";
	
	private static final String MOVIMIENTOSSERVICEGETLISTAMOVIMIENTOSI = "[==== INICIO GetListaMvimientos Movimientos Consultas- Service ====]";
	
	private static final String MOVIMIENTOSSERVICEGETLISTAMOVIMIENTOSF = "[==== FIN GetListaMovimientos Movimientos Consultas- Service ====]";
    
    
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }

	
	@Override
	public MovimientosResponse consultarMovimientosPorAprobar(MovimientosRequest movimientosRequest) throws CustomException {
		log.info(MOVIMIENTOSSERVICECONSULTARPORAPROBARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		wsrequest.setUrl(urlConsultarMovimientosPorAprobar);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info(MOVIMIENTOSSERVICECONSULTARPORAPROBARF);
				return respuesta2xxConsultarMovimientosPorAprobar(retorno);
			}else {
				log.error(respuesta4xxConsultarMovimientosPorAprobar(retorno));
				throw new CustomException(respuesta4xxConsultarMovimientosPorAprobar(retorno));
			}
		}else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	
	public MovimientosResponse respuesta2xxConsultarMovimientosPorAprobar(WSResponse retorno) {
		try {
			return mapper.jsonToClass(retorno.getBody(), MovimientosResponse.class);
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
       
	}
	
	public String respuesta4xxConsultarMovimientosPorAprobar(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado() .getDescripcion();
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
	
	@Override
	public MovimientosResponse consultarMovimientosPorAprobarVenta(MovimientosRequest movimientosRequest)
			throws CustomException {
		log.info(MOVIMIENTOSSERVICECONSULTARPORAPROBARVENTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		wsrequest.setUrl(urlConsultarMovimientosPorAprobarVenta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info(MOVIMIENTOSSERVICECONSULTARPORAPROBARVENTAF);
				return respuesta2xxConsultarMovimientosPorAprobar(retorno);
			}else {
				log.error(respuesta4xxConsultarMovimientosPorAprobar(retorno));
				throw new CustomException(respuesta4xxConsultarMovimientosPorAprobar(retorno));
			}
		}else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		
	}
	
	
	
	@Override
	public MovimientosResponse consultarMovimientos(MovimientosRequest movimientosRequest) throws CustomException {
		log.info(MOVIMIENTOSSERVICECONSULTARMOVIMIENTOSI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		wsrequest.setUrl(urlConsultarMovimientos);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info(MOVIMIENTOSSERVICECONSULTARMOVIMIENTOSF);
				return respuesta2xxConsultarMovimientosPorAprobar(retorno);
			}else {
				log.error(respuesta4xxConsultarMovimientosPorAprobar(retorno));
				throw new CustomException(respuesta4xxConsultarMovimientosPorAprobar(retorno));
			}
		}else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	@Override
	public String rechazarCompra(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		log.info(MOVIMIENTOSSERVICERECHAZARCOMPRAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);								 
		wsrequest.setUrl(urlActualizarMovimientosCompra);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info(MOVIMIENTOSSERVICERECHAZARCOMPRAF);
				return respuesta2xxRechazarAprobarCompraVenta(retorno);
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					log.error(respuesta4xxRechazarAprobarCompraVenta(retorno));
					throw new CustomException(respuesta4xxRechazarAprobarCompraVenta(retorno));
				}else {
					if (retorno.getStatus() == 500) {
						log.error(respuesta5xxRechazarAprobarCompraVenta(retorno));
						throw new CustomException(respuesta5xxRechazarAprobarCompraVenta(retorno));
					}
				}
			}
		}else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}

	
	public String respuesta2xxRechazarAprobarCompraVenta(WSResponse retorno) {
		try {
			AprobarRechazarResponse aprobarRechazarResponse = mapper.jsonToClass(retorno.getBody(), AprobarRechazarResponse.class);
			if(aprobarRechazarResponse.getResultado().getCodigo().equals("0000")){
				Resultado resultado = aprobarRechazarResponse.getResultado();
				return resultado.getDescripcion();
            }else {
            	Resultado resultado = aprobarRechazarResponse.getResultado();
            	return resultado.getDescripcion();
            }
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
	public String respuesta4xxRechazarAprobarCompraVenta(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
			
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
	public String respuesta5xxRechazarAprobarCompraVenta(WSResponse retorno) {
		try {
			AprobarRechazarResponse aprobarRechazarResponse = mapper.jsonToClass(retorno.getBody(), AprobarRechazarResponse.class);
			Resultado resultado = aprobarRechazarResponse.getResultado();
			return resultado.getDescripcion();
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
		
	}
	
	@Override
	public String aprobarCompra(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		log.info(MOVIMIENTOSSERVICEAPROBARCOMPRAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);
		wsrequest.setUrl(urlActualizarMovimientosCompra);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info(MOVIMIENTOSSERVICEAPROBARCOMPRAF);
				return respuesta2xxRechazarAprobarCompraVenta(retorno);
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					log.error(respuesta4xxRechazarAprobarCompraVenta(retorno));
					throw new CustomException(respuesta4xxRechazarAprobarCompraVenta(retorno));
				}else {
					if (retorno.getStatus() == 500) {
						log.error(respuesta5xxRechazarAprobarCompraVenta(retorno));
						throw new CustomException(respuesta5xxRechazarAprobarCompraVenta(retorno));
					}
				}
			}
		}else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}

	@Override
	public String rechazarVenta(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		log.info(MOVIMIENTOSSERVICERECHAZARVENTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);
		wsrequest.setUrl(urlActualizarMovimientosVenta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info(MOVIMIENTOSSERVICERECHAZARVENTAF);
				return respuesta2xxRechazarAprobarCompraVenta(retorno);
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					log.error(respuesta4xxRechazarAprobarCompraVenta(retorno));
					throw new CustomException(respuesta4xxRechazarAprobarCompraVenta(retorno));
				}else {
					if (retorno.getStatus() == 500) {
						log.error(respuesta5xxRechazarAprobarCompraVenta(retorno));
						throw new CustomException(respuesta5xxRechazarAprobarCompraVenta(retorno));
					}
				}
			}
		}else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}

	@Override
	public String aprobarVenta(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		log.info(MOVIMIENTOSSERVICEAPROBARVENTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);										 
		wsrequest.setUrl(urlActualizarMovimientosVenta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info(MOVIMIENTOSSERVICEAPROBARVENTAF);
				return respuesta2xxRechazarAprobarCompraVenta(retorno);		 
		     }else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					log.error(respuesta4xxRechazarAprobarCompraVenta(retorno));
					throw new CustomException(respuesta4xxRechazarAprobarCompraVenta(retorno));
				}else {
					if (retorno.getStatus() == 500) {
						log.error(respuesta5xxRechazarAprobarCompraVenta(retorno));
						throw new CustomException(respuesta5xxRechazarAprobarCompraVenta(retorno));
					}
				}
			}
		}else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}

	@Override
	public List<Movimiento> getListaMovimientos(MovimientosRequest movimientosRequest) throws CustomException {
		log.info(MOVIMIENTOSSERVICEGETLISTAMOVIMIENTOSI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		wsrequest.setUrl(urlConsultarMovimientos);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info(MOVIMIENTOSSERVICEGETLISTAMOVIMIENTOSF);
				return respuesta2xxGetListaMovimientos(retorno);
	       	}else {
	       		log.error(respuesta4xxGetListaMovimientos(retorno));
	       		throw new CustomException(respuesta4xxGetListaMovimientos(retorno));
			}
		}else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public List<Movimiento> respuesta2xxGetListaMovimientos(WSResponse retorno){
		try {
			MovimientosResponse movimientosResponse = mapper.jsonToClass(retorno.getBody(), MovimientosResponse.class);
        	return movimientosResponse.getMovimientos();
        	
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ArrayList<>();
		}
       
	}
	
	public String respuesta4xxGetListaMovimientos(WSResponse retorno){
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado() .getDescripcion();
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}

}
