package com.bancoexterior.app.convenio.service;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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





@Service
public class AcumuladosServiceApiRestImpl implements IAcumuladosServiceApiRest{

	private static final Logger LOGGER = LogManager.getLogger(AcumuladosServiceApiRestImpl.class);
	
	@Autowired
	private IWSService wsService;
    
    @Autowired 
	private Mapper mapper;
	
    @Value("${${app.ambiente}"+".ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${${app.ambiente}"+".SocketTimeout}")
    private int socketTimeout;
    
    @Value("${${app.ambiente}"+".acumulados.urlConsulta}")
    private String urlConsulta;
       
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio Acumulados";
    
    private static final String ACUMULADOSSERVICECONSULTARDIARIOSBANCOI = "[==== INICIO AcumuladosDiariosBanco Acumulados Consultas - Service ====]";
	
	private static final String ACUMULADOSSERVICECONSULTARDIARIOSBANCOF = "[==== FIN AcumuladosDiariosBanco Acumulados Consultas - Service ====]";
	
	private static final String ACUMULADOSSERVICECONSULTARCOMPRAVENTAI = "[==== INICIO AcumuladosCompraVenta Acumulados Consultas - Service ====]";
	
	private static final String ACUMULADOSSERVICECONSULTARCOMPRAVENTAF = "[==== FIN AcumuladosCompraVenta Acumulados Consultas - Service ====]";
    
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
		wsrequest.setBody(acumuladoRequestJSON);
		wsrequest.setUrl(urlConsulta);	
		wsService.post(wsrequest);
		return null;
	}

	@Override
	public AcumuladoResponse consultarAcumuladosDiariosBanco(AcumuladoRequest acumuladoRequest) throws CustomException {
		LOGGER.info(ACUMULADOSSERVICECONSULTARDIARIOSBANCOI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String acumuladoRequestJSON;
		acumuladoRequestJSON = new Gson().toJson(acumuladoRequest);
		wsrequest.setBody(acumuladoRequestJSON);
		wsrequest.setUrl(urlConsulta);	
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(ACUMULADOSSERVICECONSULTARDIARIOSBANCOF);
				return respuesta2xxConsultarAcumuladosDiariosBanco(retorno);
			}else {
				LOGGER.error(respuesta4xxConsultarAcumuladosDiariosBanco(retorno));
				throw new CustomException(respuesta4xxConsultarAcumuladosDiariosBanco(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public AcumuladoResponse respuesta2xxConsultarAcumuladosDiariosBanco(WSResponse retorno) {
		 try {
			 return mapper.jsonToClass(retorno.getBody(), AcumuladoResponse.class);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;	
		}
	}
	
	public String respuesta4xxConsultarAcumuladosDiariosBanco(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado() .getDescripcion();
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	@Override
	public AcumuladoCompraVentaResponse consultarAcumuladosCompraVenta(AcumuladoRequest acumuladoRequest)
			throws CustomException {
		LOGGER.info(ACUMULADOSSERVICECONSULTARCOMPRAVENTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String acumuladoRequestJSON;
		acumuladoRequestJSON = new Gson().toJson(acumuladoRequest);
		wsrequest.setBody(acumuladoRequestJSON);
		wsrequest.setUrl(urlConsulta);	
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(ACUMULADOSSERVICECONSULTARCOMPRAVENTAF);
				return respuesta2xxconsultarConsultarAcumuladosCompraVenta(retorno);
	        }else {
	        	LOGGER.error(respuesta4xxConsultarAcumuladosCompraVenta(retorno));
				throw new CustomException(respuesta4xxConsultarAcumuladosCompraVenta(retorno));
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
		
	}
	
	public AcumuladoCompraVentaResponse respuesta2xxconsultarConsultarAcumuladosCompraVenta(WSResponse retorno) {
		try {
			return mapper.jsonToClass(retorno.getBody(), AcumuladoCompraVentaResponse.class);
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}
	
	public String  respuesta4xxConsultarAcumuladosCompraVenta(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
			
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

}
