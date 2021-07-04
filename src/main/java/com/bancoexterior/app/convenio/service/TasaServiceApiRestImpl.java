package com.bancoexterior.app.convenio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.TasaRequest;
import com.bancoexterior.app.convenio.dto.TasaResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.model.Tasa;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TasaServiceApiRestImpl implements ITasaServiceApiRest{

	@Autowired
	private IWSService wsService;
	
	@Autowired 
	private Mapper mapper;
    
	@Value("${${app.ambiente}"+".ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${${app.ambiente}"+".SocketTimeout}")
    private int socketTimeout;
    
    @Value("${${app.ambiente}"+".tasa.urlConsulta}")
    private String urlConsulta;
    
    @Value("${${app.ambiente}"+".tasa.urlActualizar}")
    private String urlActualizar;
    
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio tasa";
    
    private static final String TASASERVICELISTATASASI = "[==== INICIO Lista Tasas Consultas - Service ====]";
	
	private static final String TASASERVICELISTATASASF = "[==== FIN Lista Tasas Consultas - Service ====]";
	
	private static final String TASASERVICEBUSACRTASAI = "[==== INICIO Buscar Tasas Consultas - Service ====]";
	
	private static final String TASASERVICEBUSCARTASAF = "[==== FIN Buscar Tasas Consultas - Service ====]";
	
	private static final String TASASERVICEACTUALIZARTASAI = "[==== INICIO Actualizar Tasas - Service ====]";
	
	private static final String TASASERVICEACTUALIZARTASAF = "[==== FIN Actualizar Tasas - Service ====]";
	
	private static final String TASASERVICECREARTASAI = "[==== INICIO Crear Tasas - Service ====]";
	
	private static final String TASASERVICECREARTASAF = "[==== FIN Crear Tasas - Service ====]";
	
	public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }
	
	@Override
	public List<Tasa> listaTasas(TasaRequest tasaRequest) throws CustomException {
		log.info(TASASERVICELISTATASASI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String tasaRequestJSON;
		tasaRequestJSON = new Gson().toJson(tasaRequest);
		wsrequest.setBody(tasaRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info(TASASERVICELISTATASASF);
				return respuesta2xxListaTasa(retorno);
			}else {
				log.error(respuesta4xxListaTasas(retorno));
				throw new CustomException(respuesta4xxListaTasas(retorno));
			}
		}else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public List<Tasa> respuesta2xxListaTasa(WSResponse retorno){
		try {
			TasaResponse tasaResponse = mapper.jsonToClass(retorno.getBody(), TasaResponse.class);
			return tasaResponse.getTasa();
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ArrayList<>();
		}
       
	}
	
	public String respuesta4xxListaTasas(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
			
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Tasa buscarTasa(TasaRequest tasaRequest) throws CustomException {
		log.info(TASASERVICEBUSACRTASAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String tasaRequestJSON;
		tasaRequestJSON = new Gson().toJson(tasaRequest);
		wsrequest.setBody(tasaRequestJSON);
		wsrequest.setUrl(urlConsulta);	
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info(TASASERVICEBUSCARTASAF);
				return respuesta2xxBuscarTasa(retorno);
			}else {
				log.error(respuesta4xxListaTasas(retorno));
				throw new CustomException(respuesta4xxListaTasas(retorno));
			}
		}else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}

	}

	public Tasa respuesta2xxBuscarTasa(WSResponse retorno) {
		try {
			TasaResponse tasaResponse = mapper.jsonToClass(retorno.getBody(), TasaResponse.class);
			if(tasaResponse.getResultado().getCodigo().equals("0000")){
	        	return tasaResponse.getTasa().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
        
	}

	@Override
	public String actualizar(TasaRequest tasaRequest) throws CustomException {
		log.info(TASASERVICEACTUALIZARTASAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String tasaRequestJSON;
		tasaRequestJSON = new Gson().toJson(tasaRequest);
		wsrequest.setBody(tasaRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info(TASASERVICEACTUALIZARTASAF);
				return respuesta2xxActualizarCrear(retorno);
			}else {
				log.error(respuesta4xxActualizar(retorno));
				throw new CustomException(respuesta4xxActualizar(retorno));
			}
		}else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}	
	}

	
	public String respuesta2xxActualizarCrear(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
		
		
	}
	
	public String respuesta4xxActualizar(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String crear(TasaRequest tasaRequest) throws CustomException {
		log.info(TASASERVICECREARTASAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String tasaRequestJSON;
		tasaRequestJSON = new Gson().toJson(tasaRequest);
		wsrequest.setBody(tasaRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info(TASASERVICECREARTASAF);
				return respuesta2xxActualizarCrear(retorno);
				
			}else {
				if (retorno.getStatus() == 422) {
					log.error(respuesta422Crear(retorno));
					throw new CustomException(respuesta422Crear(retorno));
				}else {
					if (retorno.getStatus() == 400 || retorno.getStatus() == 600) {
						log.error(respuesta400Crear(retorno));
						throw new CustomException(respuesta400Crear(retorno));
					}
				}	
			}
		}else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}
	
	public String respuesta422Crear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
	public String respuesta400Crear(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}

}
