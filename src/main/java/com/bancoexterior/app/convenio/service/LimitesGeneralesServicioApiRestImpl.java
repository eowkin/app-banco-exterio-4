package com.bancoexterior.app.convenio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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




@Service
public class LimitesGeneralesServicioApiRestImpl implements ILimitesGeneralesServiceApirest{

	private static final Logger LOGGER = LogManager.getLogger(LimitesGeneralesServicioApiRestImpl.class);
	
	@Autowired
	private IWSService wsService;
	
	@Autowired 
	private Mapper mapper;
	
	@Value("${${app.ambiente}"+".ConnectTimeout}")
	private int connectTimeout;
	    
	@Value("${${app.ambiente}"+".SocketTimeout}")
	private int socketTimeout;
	
	@Value("${${app.ambiente}"+".limitesGenerales.urlConsulta}")
	private String urlConsulta;
	    
	@Value("${${app.ambiente}"+".limitesGenerales.urlActualizar}")
	private String urlActualizar;
	
	private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio LimitesGenerales";
	
	private static final String LIMITESGENERALESSERVICELISTAI = "[==== INICIO Lista LimitesGenerales Consultas - Service ====]";
	
	private static final String LIMITESGENERALESSERVICELISTAF = "[==== FIN Lista LimitesGenerales Consultas - Service ====]";
	
	private static final String LIMITESGENERALESSERVICEBUSCARI = "[==== INICIO Buscar LimitesGenerales Consultas - Service ====]";
	
	private static final String LIMITESGENERALESSERVICEBUSCARF = "[==== FIN Buscar LimitesGenerales Consultas - Service ====]";
	
	private static final String LIMITESGENERALESSERVICEACTUALIZARI = "[==== INICIO Actualizar LimitesGenerales - Service ====]";
	
	private static final String LIMITESGENERALESSERVICEACTUALIZARF = "[==== FIN Actualizar LimitesGenerales - Service ====]";
	
	private static final String LIMITESGENERALESSERVICECREARI = "[==== INICIO Crear LimitesGenerales - Service ====]";
	
	private static final String LIMITESGENERALESSERVICECREARF = "[==== FIN Crear LimitesGenerales - Service ====]";
	
	public WSRequest getWSRequest() {
	   WSRequest wsrequest = new WSRequest();
 	   wsrequest.setConnectTimeout(connectTimeout);
	   wsrequest.setContenType("application/json");
	   wsrequest.setSocketTimeout(socketTimeout);
	   return wsrequest;
	}

	
	
	
	@Override
	public List<LimitesGenerales> listaLimitesGenerales(LimiteRequest limiteRequest) throws CustomException {
		LOGGER.info(LIMITESGENERALESSERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limiteRequestJSON;
		limiteRequestJSON = new Gson().toJson(limiteRequest);
		wsrequest.setBody(limiteRequestJSON);
		wsrequest.setUrl(urlConsulta);	
		retorno = wsService.post(wsrequest);
		
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESGENERALESSERVICELISTAF);
				return respuesta2xxListaLimitesGenerales(retorno);
			}else {
				LOGGER.error(respuesta4xxListaLimitesGenerales(retorno));
				throw new CustomException(respuesta4xxListaLimitesGenerales(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public List<LimitesGenerales> respuesta2xxListaLimitesGenerales(WSResponse retorno){
		try {
			LimiteResponse limiteResponse = mapper.jsonToClass(retorno.getBody(), LimiteResponse.class);
			return limiteResponse.getLimites();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return new ArrayList<>();
		}
        
	}
	
	public String respuesta4xxListaLimitesGenerales(WSResponse retorno){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	@Override
	public LimitesGenerales buscarLimitesGenerales(LimiteRequest limiteRequest) throws CustomException {
		LOGGER.info(LIMITESGENERALESSERVICEBUSCARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limiteRequestJSON;
		limiteRequestJSON = new Gson().toJson(limiteRequest);
		wsrequest.setBody(limiteRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESGENERALESSERVICEBUSCARF);
				return respuesta2xxbuscarLimitesGenerales(retorno);
			}else {
				LOGGER.error(respuesta4xxbuscarLimitesGenerales(retorno));
				throw new CustomException(respuesta4xxbuscarLimitesGenerales(retorno));	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
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
			LOGGER.error(e.getMessage());
			return null;
		}
        
        
	}

	public String respuesta4xxbuscarLimitesGenerales(WSResponse retorno){
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	@Override
	public String actualizar(LimiteRequest limiteRequest) throws CustomException {
		LOGGER.info(LIMITESGENERALESSERVICEACTUALIZARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limiteRequestJSON;
		limiteRequestJSON = new Gson().toJson(limiteRequest);
		wsrequest.setBody(limiteRequestJSON);
		wsrequest.setUrl(urlActualizar);	
		retorno = wsService.put(wsrequest);
			if(retorno.isExitoso()) {
				if(retorno.getStatus() == 200) {
					LOGGER.info(LIMITESGENERALESSERVICEACTUALIZARF);
					return respuesta2xxActualizarCrear(retorno);
				}else {
					LOGGER.error(respuesta4xxbuscarLimitesGenerales(retorno));
					throw new CustomException(respuesta4xxbuscarLimitesGenerales(retorno));
				}
			}else {
				LOGGER.error(ERRORMICROCONEXION);
				throw new CustomException(ERRORMICROCONEXION);
			}	
	}

	public String respuesta2xxActualizarCrear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		
	}
	
	
	@Override
	public String crear(LimiteRequest limiteRequest) throws CustomException {
		LOGGER.info(LIMITESGENERALESSERVICECREARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String limiteRequestJSON;
		limiteRequestJSON = new Gson().toJson(limiteRequest);
		wsrequest.setBody(limiteRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(LIMITESGENERALESSERVICECREARF);
				return respuesta2xxActualizarCrear(retorno);
			}else {
				LOGGER.error(respuesta4xxbuscarLimitesGenerales(retorno));
				throw new CustomException(respuesta4xxbuscarLimitesGenerales(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

}
