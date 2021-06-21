package com.bancoexterior.app.convenio.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MonedaServiceApiRestImpl implements IMonedaServiceApiRest{

		
    
    @Autowired
	private IWSService wsService;
    
    @Autowired 
	private Mapper mapper;
	
    @Value("${des.ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${des.SocketTimeout}")
    private int socketTimeout;
    
    @Value("${des.moneda.urlConsulta}")
    private String urlConsulta;
    
    @Value("${des.moneda.urlActualizar}")
    private String urlActualizar;

    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio Monedas";
    
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }



	@Override
	public List<Moneda> listaMonedas(MonedasRequest monedasRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		log.info("monedasRequestJSON: "+monedasRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/monedas/consultas");
		log.info("urlConsulta: "+urlConsulta);
		log.info("antes de llamarte WS en consultar lista moneda");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
	            return respuesta2xxlistaMonedas(retorno);
			}else {
				throw new CustomException(respuesta4xx(retorno));	
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public List<Moneda> respuesta2xxlistaMonedas(WSResponse retorno){
		try {
			MonedaResponse monedaResponse = mapper.jsonToClass(retorno.getBody(), MonedaResponse.class);
			return monedaResponse.getMonedas();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
       
	}



	@Override
	public boolean existe(MonedasRequest monedasRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/monedas/consultas");
		log.info("antes de llamarte WS en consultar existe moneda");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxExiste(retorno);
			}else {
				throw new CustomException(respuesta4xx(retorno));	
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
		
	}

	public boolean respuesta2xxExiste(WSResponse retorno) {
		try {
			MonedaResponse monedaResponse = mapper.jsonToClass(retorno.getBody(), MonedaResponse.class);
			return monedaResponse.getResultado().getCodigo().equals("0000") ? true:false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}
	
	public String respuesta4xx(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Moneda buscarMoneda(MonedasRequest monedasRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/monedas/consultas");
		log.info("antes de llamarte WS en buscarMoneda");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuest2xxBuscarMoneda(retorno);
			}else {
				throw new CustomException(respuesta4xx(retorno));	
			}
		}else {
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
			e.printStackTrace();
			return null;
		}
        
	}

	@Override
	public String actualizar(MonedasRequest monedasRequest) throws CustomException{
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/monedas");
		log.info("antes de llamarte WS en actualizarWs");
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxActualizarCrear(retorno);
				
			}else {
				throw new CustomException(respuesta4xx(retorno));	
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
	public String crear(MonedasRequest monedasRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String monedasRequestJSON;
		monedasRequestJSON = new Gson().toJson(monedasRequest);
		wsrequest.setBody(monedasRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/parametros/monedas");
		log.info("antes de llamarte WS en crearWs");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxActualizarCrear(retorno);
				
			}else {
				throw new CustomException(respuesta4xxCrear(retorno));
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public String respuesta4xxCrear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
