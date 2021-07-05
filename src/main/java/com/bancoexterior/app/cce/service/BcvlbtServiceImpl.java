package com.bancoexterior.app.cce.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.AprobacionesConsultasRequest;
import com.bancoexterior.app.cce.dto.AprobacionesConsultasResponse;
import com.bancoexterior.app.cce.dto.FiToFiCustomerCreditTransferRequest;
import com.bancoexterior.app.cce.model.BCVLBT;
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
		
	 @Value("${${app.ambiente}"+".ConnectTimeout}")
	 private int connectTimeout;
	    
	 @Value("${${app.ambiente}"+".SocketTimeout}")
	 private int socketTimeout;
	 
	 @Value("${${app.ambiente}"+".transacciones.lbtr.urlConsulta}")
	    private String urlConsulta;
	 
	
	 private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio IBCVLBT";
	 
	 private static final String BCVLBTSERVICELISTALISTATRANSACCIONESPORAPROBARI = "[==== INICIO ListaTransaccionesPorAporbar Bcvlbt Consultas - Service ====]";
		
	 private static final String BCVLBTSERVICELISTALISTATRANSACCIONESPORAPROBARF = "[==== FIN ListaTransaccionesPorAporbar Bcvlbt Consultas - Service ====]";
	 
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
		log.info(BCVLBTSERVICELISTALISTATRANSACCIONESPORAPROBARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String aprobacionesConsultasRequestJSON;
		aprobacionesConsultasRequestJSON = new Gson().toJson(aprobacionesConsultasRequest);
		log.info("aprobacionesConsultasRequestJSON: "+aprobacionesConsultasRequestJSON);
		wsrequest.setBody(aprobacionesConsultasRequestJSON);
		//wsrequest.setUrl("http://172.19.50.104:9001/api/des/V1/lbtr/aprobaciones/consultas");
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				log.info(BCVLBTSERVICELISTALISTATRANSACCIONESPORAPROBARF);
				return respuesta2xxListaTransaccionesPorAporbarAltoValorPaginacion(retorno);
			} else {
				log.error(respuesta4xxListaTransaccionesPorAporbarAltoValorPaginacion(retorno));
				throw new CustomException(respuesta4xxListaTransaccionesPorAporbarAltoValorPaginacion(retorno));
			}
		} else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}
	
	public AprobacionesConsultasResponse respuesta2xxListaTransaccionesPorAporbarAltoValorPaginacion(WSResponse retorno) {
		try {
			AprobacionesConsultasResponse aprobacionesConsultasResponse = mapper.jsonToClass(retorno.getBody(), AprobacionesConsultasResponse.class);
			if(aprobacionesConsultasResponse.getResultado().getCodigo().equals("0000")){
	        	return aprobacionesConsultasResponse;
	        }else {
	        	return null;
	        }
			
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
		
	}
	
	public String respuesta4xxListaTransaccionesPorAporbarAltoValorPaginacion(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return  resultado.getDescripcion();
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}



	@Override
	public void prueba(FiToFiCustomerCreditTransferRequest fiToFiCustomerCreditTransferRequest) throws CustomException {
		// TODO Auto-generated method stub
		
	}



	@Override
	public BCVLBT buscarBCVLBT(AprobacionesConsultasRequest aprobacionesConsultasRequest) throws CustomException {
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
				return respuesta2xxBCVLBT(retorno);
			} else {
				throw new CustomException(respuesta4xxListaTransaccionesPorAporbarAltoValorPaginacion(retorno));
			}
		} else {
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}
	
	
	public BCVLBT respuesta2xxBCVLBT(WSResponse retorno) {
		try {
			AprobacionesConsultasResponse aprobacionesConsultasResponse = mapper.jsonToClass(retorno.getBody(), AprobacionesConsultasResponse.class);	
			
			if(aprobacionesConsultasResponse.getResultado().getCodigo().equals("0000")){
	        	
	        	return aprobacionesConsultasResponse.getOperaciones().get(0);
	        	
	        }else {
	        	return null;
	        }
			
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
		
	}
}
