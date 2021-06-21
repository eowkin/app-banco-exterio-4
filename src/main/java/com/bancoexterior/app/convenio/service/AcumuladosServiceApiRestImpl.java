package com.bancoexterior.app.convenio.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.AcumuladoCompraVentaResponse;
import com.bancoexterior.app.convenio.dto.AcumuladoRequest;
import com.bancoexterior.app.convenio.dto.AcumuladoResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class AcumuladosServiceApiRestImpl implements IAcumuladosServiceApiRest{

	@Autowired
	private IWSService wsService;
    
    @Autowired 
	private Mapper mapper;
	
    @Value("${des.ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${des.SocketTimeout}")
    private int socketTimeout;
    
    @Value("${des.acumulados.urlConsulta}")
    private String urlConsulta;
    
    String errorMicroCOnexion = "No hubo conexion con el micreoservicio acumulados";
    
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }
    
	@Override
	public String consultarAcumulados(AcumuladoRequest acumuladoRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		String acumuladoRequestJSON;
		acumuladoRequestJSON = new Gson().toJson(acumuladoRequest);
		log.info("acumuladoRequestJSON: "+acumuladoRequestJSON);
		
		wsrequest.setBody(acumuladoRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/montosacumulados");
			
		log.info("antes de llamarte WS en consultarAcumulados");
		WSResponse retorno = wsService.post(wsrequest);
		return null;
	}

	@Override
	public AcumuladoResponse consultarAcumuladosDiariosBanco(AcumuladoRequest acumuladoRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String acumuladoRequestJSON;
		acumuladoRequestJSON = new Gson().toJson(acumuladoRequest);
		wsrequest.setBody(acumuladoRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/montosacumulados");
			
		log.info("antes de llamarte WS en consultarAcumuladosDiariosBanco");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxConsultarAcumuladosDiariosBanco(retorno);
			}else {
				throw new CustomException(respuesta4xxConsultarAcumuladosDiariosBanco(retorno));
			}
		}else {
			throw new CustomException(errorMicroCOnexion);
		}
	}
	
	public AcumuladoResponse respuesta2xxConsultarAcumuladosDiariosBanco(WSResponse retorno) {
		 try {
			 AcumuladoResponse acumuladoResponse = mapper.jsonToClass(retorno.getBody(), AcumuladoResponse.class);
         	 log.info(acumuladoResponse.getResultado().getCodigo());
	         return acumuladoResponse;
		} catch (IOException e) {
			e.printStackTrace();
			return null;	
		}
	}
	
	public String respuesta4xxConsultarAcumuladosDiariosBanco(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado() .getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public AcumuladoCompraVentaResponse consultarAcumuladosCompraVenta(AcumuladoRequest acumuladoRequest)
			throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String acumuladoRequestJSON;
		acumuladoRequestJSON = new Gson().toJson(acumuladoRequest);
		wsrequest.setBody(acumuladoRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/montosacumulados");
			
		log.info("antes de llamarte WS en consultarAcumuladosCompraVenta");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxconsultarConsultarAcumuladosCompraVenta(retorno);
	        }else {
				throw new CustomException(respuesta4xxConsultarAcumuladosCompraVenta(retorno));
			}
		}else {
			throw new CustomException(errorMicroCOnexion);
		}
		
	}
	
	public AcumuladoCompraVentaResponse respuesta2xxconsultarConsultarAcumuladosCompraVenta(WSResponse retorno) {
		try {
			AcumuladoCompraVentaResponse acumuladoCompraVentaResponse = mapper.jsonToClass(retorno.getBody(), AcumuladoCompraVentaResponse.class);
        	log.info(acumuladoCompraVentaResponse.getResultado().getCodigo());
            return acumuladoCompraVentaResponse;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String  respuesta4xxConsultarAcumuladosCompraVenta(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
