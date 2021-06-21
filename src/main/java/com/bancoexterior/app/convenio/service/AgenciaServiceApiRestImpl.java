package com.bancoexterior.app.convenio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.AgenciaRequest;
import com.bancoexterior.app.convenio.dto.AgenciaResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.model.Agencia;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AgenciaServiceApiRestImpl implements IAgenciaServiceApiRest{

	@Autowired
	private IWSService wsService;
	
	@Autowired 
	private Mapper mapper;
	
	@Value("${des.ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${des.SocketTimeout}")
    private int socketTimeout;
    
    @Value("${des.agencia.urlConsulta}")
    private String urlConsulta;
    
    @Value("${des.agencia.urlActualizar}")
    private String urlActualizar;
	
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio Agencias";
    
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }
    
    
	@Override
	public List<Agencia> listaAgencias(AgenciaRequest agenciaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String agenciaRequestJSON;
		agenciaRequestJSON = new Gson().toJson(agenciaRequest);
		wsrequest.setBody(agenciaRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/agencias/consultas");								
		log.info("antes de llamarte WS en consultar listaAgencias");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxListaAgencias(retorno);
			}else {
				throw new CustomException(respuesta4xxListaAgencias(retorno));
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public List<Agencia> respuesta2xxListaAgencias(WSResponse retorno){
		try {
			AgenciaResponse agenciaResponse = mapper.jsonToClass(retorno.getBody(), AgenciaResponse.class);
			log.info(agenciaResponse.getResultado().getCodigo());
	        return agenciaResponse.getListaAgencias();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
        
	}
	
	public String respuesta4xxListaAgencias(WSResponse retorno){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public Agencia buscarAgencia(AgenciaRequest agenciaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String agenciaRequestJSON;
		agenciaRequestJSON = new Gson().toJson(agenciaRequest);
		wsrequest.setBody(agenciaRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/agencias/consultas");								
		log.info("antes de llamarte WS en consultar buscarAgencia");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxBuscarAgencia(retorno);
			}else {
				throw new CustomException(respuesta4xxListaAgencias(retorno));
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public Agencia respuesta2xxBuscarAgencia(WSResponse retorno){
		try {
			AgenciaResponse agenciaResponse = mapper.jsonToClass(retorno.getBody(), AgenciaResponse.class);
			log.info(agenciaResponse.getResultado().getCodigo());
	        if(agenciaResponse.getResultado().getCodigo().equals("0000")){
	        	log.info("Respusta codigo 0000 si existe la agencia");
	        	return agenciaResponse.getListaAgencias().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
        
	}
	
	
	@Override
	public String actualizar(AgenciaRequest agenciaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String agenciaRequestJSON;
		agenciaRequestJSON = new Gson().toJson(agenciaRequest);
		wsrequest.setBody(agenciaRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/agencias");								
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
		log.info("Respusta codigo 200 en Actualizar agencia por codigo");
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
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
	public String crear(AgenciaRequest agenciaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String agenciaRequestJSON;
		agenciaRequestJSON = new Gson().toJson(agenciaRequest);
		wsrequest.setBody(agenciaRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/agencias");								
		log.info("antes de llamarte WS en creear");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxActualizarCrear(retorno);
			}else {
				if (retorno.getStatus() == 422) {
					throw new CustomException(respuesta4xxActualizarCrear(retorno));
					
				}else {
					if (retorno.getStatus() == 400 || retorno.getStatus() == 600) {
						throw new CustomException(respuesta4xxListaAgencias(retorno));
					}
				}
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}

}
