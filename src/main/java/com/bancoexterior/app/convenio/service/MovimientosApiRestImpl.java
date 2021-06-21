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
    
    @Value("${des.ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${des.SocketTimeout}")
    private int socketTimeout;
    
    @Value("${des.movimientos.consultarMovimientosPorAprobar}")
    private String urlConsultarMovimientosPorAprobar;
    
    @Value("${des.movimientos.consultarMovimientosPorAprobarVenta}")
    private String urlConsultarMovimientosPorAprobarVenta;
    
    @Value("${des.movimientos.consultarMovimientos}")
    private String urlConsultarMovimientos;
    
    @Value("${des.movimientos.compra.actualizar}")
    private String urlActualizarMovimientosCompra;
    
    @Value("${des.movimientos.venta.actualizar}")
    private String urlActualizarMovimientosVenta;
    
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio Movimientos";
    
    
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }

	
	@Override
	public MovimientosResponse consultarMovimientosPorAprobar(MovimientosRequest movimientosRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		//wsrequest.setUrl(urlConsultarMovimientosPorAprobar);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/consultasmovimientos?sort=codMoneda,desc&sort=tasaCliente,desc&sort=montoDivisa,asc");
		
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxConsultarMovimientosPorAprobar(retorno);
			}else {
				throw new CustomException(respuesta4xxConsultarMovimientosPorAprobar(retorno));
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	
	public MovimientosResponse respuesta2xxConsultarMovimientosPorAprobar(WSResponse retorno) {
		try {
			return mapper.jsonToClass(retorno.getBody(), MovimientosResponse.class);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
       
	}
	
	public String respuesta4xxConsultarMovimientosPorAprobar(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado() .getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public MovimientosResponse consultarMovimientosPorAprobarVenta(MovimientosRequest movimientosRequest)
			throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		//wsrequest.setUrl(urlConsultarMovimientosPorAprobarVenta);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/consultasmovimientos?sort=codMoneda,desc&sort=tasaCliente,asc&sort=montoDivisa,desc");
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxConsultarMovimientosPorAprobar(retorno);
			}else {
				throw new CustomException(respuesta4xxConsultarMovimientosPorAprobar(retorno));
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
		
	}
	
	
	
	@Override
	public MovimientosResponse consultarMovimientos(MovimientosRequest movimientosRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		//wsrequest.setUrl(urlConsultarMovimientos);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/consultasmovimientos");
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxConsultarMovimientosPorAprobar(retorno);
			}else {
				throw new CustomException(respuesta4xxConsultarMovimientosPorAprobar(retorno));
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	@Override
	public String rechazarCompra(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);								 
		//wsrequest.setUrl(urlActualizarMovimientosCompra);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/aprobacionescompras");
		log.info("antes de llamarte WS en rechazarCompraSolicitud");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxRechazarAprobarCompraVenta(retorno);
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					throw new CustomException(respuesta4xxRechazarAprobarCompraVenta(retorno));
				}else {
					if (retorno.getStatus() == 500) {
						throw new CustomException(respuesta5xxRechazarAprobarCompraVenta(retorno));
					}
				}
			}
		}else {
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
			e.printStackTrace();
			return null;
		}
	}
	
	public String respuesta4xxRechazarAprobarCompraVenta(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String respuesta5xxRechazarAprobarCompraVenta(WSResponse retorno) {
		try {
			AprobarRechazarResponse aprobarRechazarResponse = mapper.jsonToClass(retorno.getBody(), AprobarRechazarResponse.class);
			Resultado resultado = aprobarRechazarResponse.getResultado();
			return resultado.getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	@Override
	public String aprobarCompra(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);
		//wsrequest.setUrl(urlActualizarMovimientosCompra);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/aprobacionescompras");
		log.info("antes de llamarte WS en aprobarCompraSolicitud");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxRechazarAprobarCompraVenta(retorno);
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					throw new CustomException(respuesta4xxRechazarAprobarCompraVenta(retorno));
				}else {
					if (retorno.getStatus() == 500) {
						throw new CustomException(respuesta5xxRechazarAprobarCompraVenta(retorno));
					}
				}
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}

	@Override
	public String rechazarVenta(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);
		//wsrequest.setUrl(urlActualizarMovimientosVenta);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/aprobacionesventas");
		log.info("antes de llamarte WS en rechazarVentaSolicitud");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxRechazarAprobarCompraVenta(retorno);
			}else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					throw new CustomException(respuesta4xxRechazarAprobarCompraVenta(retorno));
				}else {
					if (retorno.getStatus() == 500) {
						throw new CustomException(respuesta5xxRechazarAprobarCompraVenta(retorno));
					}
				}
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}

	@Override
	public String aprobarVenta(AprobarRechazarRequest aprobarRechazarRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String aprobarRechazarRequestJSON;
		aprobarRechazarRequestJSON = new Gson().toJson(aprobarRechazarRequest);
		wsrequest.setBody(aprobarRechazarRequestJSON);										 
		//wsrequest.setUrl(urlActualizarMovimientosVenta);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/aprobacionesventas");
		log.info("antes de llamarte WS en aprobarVentaSolicitud");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxRechazarAprobarCompraVenta(retorno);		 
		     }else {
				if (retorno.getStatus() == 422 || retorno.getStatus() == 400) {
					throw new CustomException(respuesta4xxRechazarAprobarCompraVenta(retorno));
				}else {
					if (retorno.getStatus() == 500) {
						throw new CustomException(respuesta5xxRechazarAprobarCompraVenta(retorno));
					}
				}
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}

	@Override
	public List<Movimiento> getListaMovimientos(MovimientosRequest movimientosRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String movimientosRequestJSON;
		movimientosRequestJSON = new Gson().toJson(movimientosRequest);
		wsrequest.setBody(movimientosRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/consultasmovimientos");
		log.info("antes de llamarte WS buscarListaMovimientos");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxGetListaMovimientos(retorno);
	       	}else {
	       		throw new CustomException(respuesta4xxGetListaMovimientos(retorno));
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public List<Movimiento> respuesta2xxGetListaMovimientos(WSResponse retorno){
		try {
			MovimientosResponse movimientosResponse = mapper.jsonToClass(retorno.getBody(), MovimientosResponse.class);
        	return movimientosResponse.getMovimientos();
        	
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
       
	}
	
	public String respuesta4xxGetListaMovimientos(WSResponse retorno){
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado() .getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
