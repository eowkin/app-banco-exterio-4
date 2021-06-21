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
    
	@Value("${des.ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${des.SocketTimeout}")
    private int socketTimeout;
    
    @Value("${des.tasa.urlConsulta}")
    private String urlConsulta;
    
    @Value("${des.tasa.urlActualizar}")
    private String urlActualizar;
    
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio tasa";
	
	public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }
	
	@Override
	public List<Tasa> listaTasas(TasaRequest tasaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String tasaRequestJSON;
		tasaRequestJSON = new Gson().toJson(tasaRequest);
		wsrequest.setBody(tasaRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/tasas/consultas");
		log.info("antes de llamarte WS en consultar la lista de tasas");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxListaTasa(retorno);
			}else {
				throw new CustomException(respuesta4xxListaTasas(retorno));
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public List<Tasa> respuesta2xxListaTasa(WSResponse retorno){
		try {
			TasaResponse tasaResponse = mapper.jsonToClass(retorno.getBody(), TasaResponse.class);
			return tasaResponse.getTasa();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
       
	}
	
	public String respuesta4xxListaTasas(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Tasa buscarTasa(TasaRequest tasaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String tasaRequestJSON;
		tasaRequestJSON = new Gson().toJson(tasaRequest);
		wsrequest.setBody(tasaRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/tasas/consultas");
			
		log.info("antes de llamarte WS en buscarTasa");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxBuscarTasa(retorno);
			}else {
				throw new CustomException(respuesta4xxListaTasas(retorno));
			}
		}else {
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
			e.printStackTrace();
			return null;
		}
        
	}

	@Override
	public String actualizar(TasaRequest tasaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String tasaRequestJSON;
		tasaRequestJSON = new Gson().toJson(tasaRequest);
		wsrequest.setBody(tasaRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/tasas");
		log.info("antes de llamarte WS en actualizar");
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxActualizarCrear(retorno);
			}else {
				throw new CustomException(respuesta4xxActualizar(retorno));
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}	
	}

	
	public String respuesta2xxActualizarCrear(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public String respuesta4xxActualizar(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String crear(TasaRequest tasaRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String tasaRequestJSON;
		tasaRequestJSON = new Gson().toJson(tasaRequest);
		wsrequest.setBody(tasaRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/tasas");
		log.info("antes de llamarte WS en crear");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxActualizarCrear(retorno);
				
			}else {
				if (retorno.getStatus() == 422) {
					throw new CustomException(respuesta422Crear(retorno));
				}else {
					if (retorno.getStatus() == 400 || retorno.getStatus() == 600) {
						throw new CustomException(respuesta400Crear(retorno));
					}
				}	
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
		return null;
	}
	
	public String respuesta422Crear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String respuesta400Crear(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
