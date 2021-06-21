package com.bancoexterior.app.convenio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.LimiteRequest;
import com.bancoexterior.app.convenio.dto.LimiteResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.model.LimitesGenerales;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LimitesGeneralesServicioApiRestImpl implements ILimitesGeneralesServiceApirest{

	@Autowired
	private IWSService wsService;
	
	@Autowired 
	private Mapper mapper;
	
	@Value("${des.ConnectTimeout}")
	private int connectTimeout;
	    
	@Value("${des.SocketTimeout}")
	private int socketTimeout;
	
	@Value("${des.limitesGenerales.urlConsulta}")
	private String urlConsulta;
	    
	@Value("${des.limitesGenerales.urlActualizar}")
	private String urlActualizar;
	
	private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio LimitesGenerales";
	
	
	public WSRequest getWSRequest() {
	   WSRequest wsrequest = new WSRequest();
 	   wsrequest.setConnectTimeout(connectTimeout);
	   wsrequest.setContenType("application/json");
	   wsrequest.setSocketTimeout(socketTimeout);
	   return wsrequest;
	}

	
	
	
	@Override
	public List<LimitesGenerales> listaLimitesGenerales(LimiteRequest limiteRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limiteRequestJSON;
		limiteRequestJSON = new Gson().toJson(limiteRequest);
		wsrequest.setBody(limiteRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/limitesdivisas/consultas");
			
		log.info("antes de llamarte WS en consultar listaLimitesGenerales");
		retorno = wsService.post(wsrequest);
		
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxListaLimitesGenerales(retorno);
			}else {
				throw new CustomException(respuesta4xxListaLimitesGenerales(retorno));
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public List<LimitesGenerales> respuesta2xxListaLimitesGenerales(WSResponse retorno){
		try {
			LimiteResponse limiteResponse = mapper.jsonToClass(retorno.getBody(), LimiteResponse.class);
			return limiteResponse.getLimites();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
        
	}
	
	public String respuesta4xxListaLimitesGenerales(WSResponse retorno){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public LimitesGenerales buscarLimitesGenerales(LimiteRequest limiteRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limiteRequestJSON;
		limiteRequestJSON = new Gson().toJson(limiteRequest);
		wsrequest.setBody(limiteRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/limitesdivisas/consultas");
		log.info("antes de llamarte WS en consultar buscarLimitesGenerales");
		retorno = wsService.post(wsrequest);
		
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxbuscarLimitesGenerales(retorno);
			}else {
				throw new CustomException(respuesta4xxbuscarLimitesGenerales(retorno));	
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public LimitesGenerales respuesta2xxbuscarLimitesGenerales(WSResponse retorno){
		try {
			LimiteResponse limiteResponse = mapper.jsonToClass(retorno.getBody(), LimiteResponse.class);
			if(limiteResponse.getResultado().getCodigo().equals("0000")){
	        	return limiteResponse.getLimites().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
        
        
	}

	public String respuesta4xxbuscarLimitesGenerales(WSResponse retorno){
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String actualizar(LimiteRequest limiteRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limiteRequestJSON;
		limiteRequestJSON = new Gson().toJson(limiteRequest);
		wsrequest.setBody(limiteRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/limitesdivisas");
			
		log.info("antes de llamarte WS en actualizar limitesGenerales");
		retorno = wsService.put(wsrequest);
			if(retorno.isExitoso()) {
				if(retorno.getStatus() == 200) {
					return respuesta2xxActualizarCrear(retorno);
				}else {
					throw new CustomException(respuesta4xxbuscarLimitesGenerales(retorno));
				}
			}else {
				throw new CustomException(ERRORMICROCONEXION);
			}	
	}

	public String respuesta2xxActualizarCrear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	@Override
	public String crear(LimiteRequest limiteRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limiteRequestJSON;
		limiteRequestJSON = new Gson().toJson(limiteRequest);
		wsrequest.setBody(limiteRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/limitesdivisas");
		log.info("antes de llamarte WS en crear");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxActualizarCrear(retorno);
			}else {
				throw new CustomException(respuesta4xxbuscarLimitesGenerales(retorno));
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

}
