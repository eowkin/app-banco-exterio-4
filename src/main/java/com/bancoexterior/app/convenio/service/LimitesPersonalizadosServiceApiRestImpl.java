package com.bancoexterior.app.convenio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosRequest;
import com.bancoexterior.app.convenio.dto.LimitesPersonalizadosResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.model.LimitesPersonalizados;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class LimitesPersonalizadosServiceApiRestImpl implements ILimitesPersonalizadosServiceApiRest{

	@Autowired
	private IWSService wsService;
	
	@Autowired 
	private Mapper mapper;
	
	@Value("${des.ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${des.SocketTimeout}")
    private int socketTimeout;
    
    @Value("${des.limitesPersonalizados.urlConsulta}")
    private String urlConsulta;
    
    @Value("${des.limitesPersonalizados.urlActualizar}")
    private String urlActualizar;
    
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio limites personalizados";

    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }

    
	@Override
	public List<LimitesPersonalizados> listaLimitesPersonalizados(
			LimitesPersonalizadosRequest limitesPersonalizadosRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/limitesdivisasclientes/consultas");
		log.info("antes de llamarte WS en listaLimitesPersonalizados");
		retorno = wsService.post(wsrequest);
		
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxListaLimitesPersonalizados(retorno);
			}else {
				throw new CustomException(respuesta4xxListaLimitesPersonalizados(retorno));
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public List<LimitesPersonalizados> respuesta2xxListaLimitesPersonalizados(WSResponse retorno){
		try {
			LimitesPersonalizadosResponse limiteResponse = mapper.jsonToClass(retorno.getBody(), LimitesPersonalizadosResponse.class);
			return limiteResponse.getLimitesPersonalizados();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
        
	}

	public String respuesta4xxListaLimitesPersonalizados(WSResponse retorno){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public LimitesPersonalizados buscarLimitesPersonalizados(LimitesPersonalizadosRequest limitesPersonalizadosRequest)
			throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/limitesdivisasclientes/consultas");
			
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxBuscarLimitesPersonalizados(retorno);
			}else {
				throw new CustomException(respuesta4xxListaLimitesPersonalizados(retorno));
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public LimitesPersonalizados respuesta2xxBuscarLimitesPersonalizados(WSResponse retorno){
		try {
			LimitesPersonalizadosResponse limiteResponse = mapper.jsonToClass(retorno.getBody(), LimitesPersonalizadosResponse.class);
			if(limiteResponse.getResultado().getCodigo().equals("0000")){
	        	return limiteResponse.getLimitesPersonalizados().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
        
	}
	
	@Override
	public String actualizar(LimitesPersonalizadosRequest limitesPersonalizadosRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/limitesdivisasclientes");
		log.info("antes de llamarte WS en actualizar");
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxActualizarCrear(retorno);
			}else {
				throw new CustomException(respuesta4xxActualizarCrear(retorno));
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
	
	public String respuesta4xxActualizarCrear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String crear(LimitesPersonalizadosRequest limitesPersonalizadosRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limitesPersonalizadosRequestJSON;
		limitesPersonalizadosRequestJSON = new Gson().toJson(limitesPersonalizadosRequest);
		wsrequest.setBody(limitesPersonalizadosRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/limitesdivisasclientes");
			
		log.info("antes de llamarte WS en crear");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxActualizarCrear(retorno);
			}else {
				throw new CustomException(respuesta4xxActualizarCrear(retorno));
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

}
