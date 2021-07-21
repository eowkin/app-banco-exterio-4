package com.bancoexterior.app.convenio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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




@Service
public class AgenciaServiceApiRestImpl implements IAgenciaServiceApiRest{

	private static final Logger LOGGER = LogManager.getLogger(AgenciaServiceApiRestImpl.class);
	
	@Autowired
	private IWSService wsService;
	
	@Autowired 
	private Mapper mapper;
	
	@Value("${${app.ambiente}"+".ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${${app.ambiente}"+".SocketTimeout}")
    private int socketTimeout;
    
    @Value("${${app.ambiente}"+".agencia.urlConsulta}")
    private String urlConsulta;
    
    @Value("${${app.ambiente}"+".agencia.urlActualizar}")
    private String urlActualizar;
	
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio Agencias";
    
    private static final String AGENCIASERVICELISTAI = "[==== INICIO Lista Agencias Consultas - Service ====]";
	
	private static final String AGENCIASERVICELISTAF = "[==== FIN Lista Agencias Consultas - Service ====]";
	
	private static final String AGENCIASERVICEBUSCARI = "[==== INICIO Buscar Agencia Consultas - Service ====]";
	
	private static final String AGENCIASERVICEBUSCARF = "[==== FIN Buscar Agencia Consultas - Service ====]";
	
	private static final String AGENCIASERVICEACTUALIZARI = "[==== INICIO Actualizar Agencia - Service ====]";
	
	private static final String AGENCIASERVICEACTUALIZARF = "[==== FIN Actualizar Agencia - Service ====]";
	
	private static final String AGENCIASERVICECREARI = "[==== INICIO Crear Agencia - Service ====]";
	
	private static final String AGENCIASERVICECREARF = "[==== FIN Crear Agencia - Service ====]";
    
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }
    
    
	@Override
	public List<Agencia> listaAgencias(AgenciaRequest agenciaRequest) throws CustomException {
		LOGGER.info(AGENCIASERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String agenciaRequestJSON;
		agenciaRequestJSON = new Gson().toJson(agenciaRequest);
		wsrequest.setBody(agenciaRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(AGENCIASERVICELISTAF);
				return respuesta2xxListaAgencias(retorno);
			}else {
				LOGGER.error(respuesta4xxListaAgencias(retorno));
				throw new CustomException(respuesta4xxListaAgencias(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public List<Agencia> respuesta2xxListaAgencias(WSResponse retorno){
		try {
			AgenciaResponse agenciaResponse = mapper.jsonToClass(retorno.getBody(), AgenciaResponse.class);
	        return agenciaResponse.getListaAgencias();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return new ArrayList<>();
		}
        
	}
	
	public String respuesta4xxListaAgencias(WSResponse retorno){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	@Override
	public Agencia buscarAgencia(AgenciaRequest agenciaRequest) throws CustomException {
		LOGGER.info(AGENCIASERVICEBUSCARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String agenciaRequestJSON;
		agenciaRequestJSON = new Gson().toJson(agenciaRequest);
		wsrequest.setBody(agenciaRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(AGENCIASERVICEBUSCARF);
				return respuesta2xxBuscarAgencia(retorno);
			}else {
				LOGGER.error(respuesta4xxListaAgencias(retorno));
				throw new CustomException(respuesta4xxListaAgencias(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public Agencia respuesta2xxBuscarAgencia(WSResponse retorno){
		try {
			AgenciaResponse agenciaResponse = mapper.jsonToClass(retorno.getBody(), AgenciaResponse.class);
	        if(agenciaResponse.getResultado().getCodigo().equals("0000")){
	        	return agenciaResponse.getListaAgencias().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
        
	}
	
	
	@Override
	public String actualizar(AgenciaRequest agenciaRequest) throws CustomException {
		LOGGER.info(AGENCIASERVICEACTUALIZARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String agenciaRequestJSON;
		agenciaRequestJSON = new Gson().toJson(agenciaRequest);
		wsrequest.setBody(agenciaRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(AGENCIASERVICEACTUALIZARF);
				return respuesta2xxActualizarCrear(retorno);
			}else {
				LOGGER.error(respuesta4xxActualizarCrear(retorno));
				throw new CustomException(respuesta4xxActualizarCrear(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public String respuesta2xxActualizarCrear(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		
		
	}
	
	public String respuesta4xxActualizarCrear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();		
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	
	@Override
	public String crear(AgenciaRequest agenciaRequest) throws CustomException {
		LOGGER.info(AGENCIASERVICECREARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String agenciaRequestJSON;
		agenciaRequestJSON = new Gson().toJson(agenciaRequest);
		wsrequest.setBody(agenciaRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(AGENCIASERVICECREARF);
				return respuesta2xxActualizarCrear(retorno);
			}else {
				if (retorno.getStatus() == 422) {
					LOGGER.error(respuesta4xxActualizarCrear(retorno));
					throw new CustomException(respuesta4xxActualizarCrear(retorno));
					
				}else {
					if (retorno.getStatus() == 400 || retorno.getStatus() == 600) {
						LOGGER.error(respuesta4xxListaAgencias(retorno));
						throw new CustomException(respuesta4xxListaAgencias(retorno));
					}
				}
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}

}
