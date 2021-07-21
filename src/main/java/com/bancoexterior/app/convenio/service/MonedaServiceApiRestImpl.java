package com.bancoexterior.app.convenio.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.MonedaResponse;
import com.bancoexterior.app.convenio.dto.MonedasRequest;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.model.Moneda;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;





@Service
public class MonedaServiceApiRestImpl implements IMonedaServiceApiRest{

	private static final Logger LOGGER = LogManager.getLogger(MonedaServiceApiRestImpl.class);	
    
    @Autowired
	private IWSService wsService;
    
    @Autowired 
	private Mapper mapper;
	
    @Value("${${app.ambiente}"+".ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${${app.ambiente}"+".SocketTimeout}")
    private int socketTimeout;
    
    @Value("${${app.ambiente}"+".moneda.urlConsulta}")
    private String urlConsulta;
    
    @Value("${${app.ambiente}"+".moneda.urlActualizar}")
    private String urlActualizar;

    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio Monedas";
    
    private static final String MONEDASERVICELISTAMONEDASI = "[==== INICIO Lista Monedas Consultas - Service ====]";
	
	private static final String MONEDASERVICELISTAMONEDASF = "[==== FIN Lista Monedas Consultas - Service ====]";
	
	private static final String MONEDASERVICEEXISTEMONEDAI = "[==== INICIO Existe Monedas Consultas - Service ====]";
	
	private static final String MONEDASERVICEEXISTEMONEDAF = "[==== FIN Existe Monedas Consultas - Service ====]";
	
	private static final String MONEDASERVICEBUSCARMONEDAI = "[==== INICIO Buscar Monedas Consultas - Service ====]";
	
	private static final String MONEDASERVICEBUSCARMONEDAF = "[==== FIN Buscar Monedas Consultas - Service ====]";
	
	private static final String MONEDASERVICEACTUALIZARMONEDAI = "[==== INICIO Actualizar Monedas - Service ====]";
	
	private static final String MONEDASERVICEACTUALIZARMONEDAF = "[==== FIN Actualizar Monedas - Service ====]";
	
	private static final String MONEDASERVICECREARMONEDAI = "[==== INICIO Crear Monedas - Service ====]";
	
	private static final String MONEDASERVICECREARMONEDAF = "[==== FIN Crear Monedas - Service ====]";
    
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }



	@Override
	public List<Moneda> listaMonedas(MonedasRequest monedasRequest) throws CustomException {
		LOGGER.info(MONEDASERVICELISTAMONEDASI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MONEDASERVICELISTAMONEDASF);
	            return respuesta2xxlistaMonedas(retorno);
			}else {
				LOGGER.error(respuesta4xx(retorno));
				throw new CustomException(respuesta4xx(retorno));	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public List<Moneda> respuesta2xxlistaMonedas(WSResponse retorno){
		try {
			MonedaResponse monedaResponse = mapper.jsonToClass(retorno.getBody(), MonedaResponse.class);
			return monedaResponse.getMonedas();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return new ArrayList<>();
		}
       
	}



	@Override
	public boolean existe(MonedasRequest monedasRequest) throws CustomException {
		LOGGER.info(MONEDASERVICEEXISTEMONEDAI);  
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MONEDASERVICEEXISTEMONEDAF);
				return respuesta2xxExiste(retorno);
			}else {
				LOGGER.error(respuesta4xx(retorno));
				throw new CustomException(respuesta4xx(retorno));	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		
	}

	public boolean respuesta2xxExiste(WSResponse retorno) {
		try {
			MonedaResponse monedaResponse = mapper.jsonToClass(retorno.getBody(), MonedaResponse.class);
			return monedaResponse.getResultado().getCodigo().equals("0000") ? true:false;
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return false;
		}

	}
	
	public String respuesta4xx(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Moneda buscarMoneda(MonedasRequest monedasRequest) throws CustomException {
		LOGGER.info(MONEDASERVICEBUSCARMONEDAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MONEDASERVICEBUSCARMONEDAF);
				return respuest2xxBuscarMoneda(retorno);
			}else {
				LOGGER.error(respuesta4xx(retorno));
				throw new CustomException(respuesta4xx(retorno));	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		
		
	}

	public Moneda respuest2xxBuscarMoneda(WSResponse retorno) {
		try {
			MonedaResponse monedaResponse = mapper.jsonToClass(retorno.getBody(), MonedaResponse.class);
			if(monedaResponse.getResultado().getCodigo().equals("0000")){
	        	return monedaResponse.getMonedas().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
        
	}

	@Override
	public String actualizar(MonedasRequest monedasRequest) throws CustomException{
		LOGGER.info(MONEDASERVICEACTUALIZARMONEDAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MONEDASERVICEACTUALIZARMONEDAF);
				return respuesta2xxActualizarCrear(retorno);
				
			}else {
				LOGGER.error(respuesta4xx(retorno));
				throw new CustomException(respuesta4xx(retorno));	
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
	public String crear(MonedasRequest monedasRequest) throws CustomException {
		LOGGER.info(MONEDASERVICECREARMONEDAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(MONEDASERVICECREARMONEDAF);
				return respuesta2xxActualizarCrear(retorno);
				
			}else {
				LOGGER.error(respuesta4xxCrear(retorno));
				throw new CustomException(respuesta4xxCrear(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public String respuesta4xxCrear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
}
