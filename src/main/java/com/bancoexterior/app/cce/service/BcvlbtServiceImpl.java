package com.bancoexterior.app.cce.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.AprobacionesConsultasRequest;
import com.bancoexterior.app.cce.dto.AprobacionesConsultasResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BcvlbtServiceImpl implements IBcvlbtService{
	@Autowired
	private IWSService wsService;
	
	 @Autowired 
	 private Mapper mapper;
		
	 @Value("${des.ConnectTimeout}")
	 private int connectTimeout;
	    
	 @Value("${des.SocketTimeout}")
	 private int socketTimeout;
	
	 private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio IBCVLBT";
	 
	 public WSRequest getWSRequest() {
	    	WSRequest wsrequest = new WSRequest();
	    	wsrequest.setConnectTimeout(connectTimeout);
			wsrequest.setContenType("application/json");
			wsrequest.setSocketTimeout(socketTimeout);
	    	return wsrequest;
	 }



	@Override
	public AprobacionesConsultasResponse listaTransaccionesPorAporbarAltoValorPaginacion(
			AprobacionesConsultasRequest aprobacionesConsultasRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String aprobacionesConsultasRequestJSON;
		aprobacionesConsultasRequestJSON = new Gson().toJson(aprobacionesConsultasRequest);
		log.info("aprobacionesConsultasRequestJSON: "+aprobacionesConsultasRequestJSON);
		wsrequest.setBody(aprobacionesConsultasRequestJSON);
		wsrequest.setUrl("http://172.19.50.104:9001/api/des/V1/lbtr/aprobaciones/consultas");
		log.info("antes de llamarte WS en listaTransaccionesPorAporbarAltoValorPaginacion");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				return respuesta2xxListaTransaccionesPorAporbarAltoValorPaginacion(retorno);
			} else {
				throw new CustomException(respuesta4xxListaTransaccionesPorAporbarAltoValorPaginacion(retorno));
			}
		} else {
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}
	
	public AprobacionesConsultasResponse respuesta2xxListaTransaccionesPorAporbarAltoValorPaginacion(WSResponse retorno) {
		try {
			AprobacionesConsultasResponse aprobacionesConsultasResponse = mapper.jsonToClass(retorno.getBody(), AprobacionesConsultasResponse.class);
			log.info(aprobacionesConsultasResponse.getResultado().getCodigo());
			if(aprobacionesConsultasResponse.getResultado().getCodigo().equals("0000")){
	        	log.info("Respusta codigo 0000 si de clienteResponse");
	        	return aprobacionesConsultasResponse;
	        }else {
	        	return null;
	        }
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public String respuesta4xxListaTransaccionesPorAporbarAltoValorPaginacion(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return  resultado.getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

}
