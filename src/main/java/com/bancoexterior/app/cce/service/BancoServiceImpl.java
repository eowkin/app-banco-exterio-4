package com.bancoexterior.app.cce.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.cce.dto.BancoRequest;
import com.bancoexterior.app.cce.dto.BancoResponse;
import com.bancoexterior.app.cce.model.Banco;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;




@Service
public class BancoServiceImpl implements IBancoService{

	private static final Logger LOGGER = LogManager.getLogger(BancoServiceImpl.class);
	
	@Autowired
	private IWSService wsService;
    
    @Autowired 
	private Mapper mapper;
	
    @Value("${${app.ambiente}"+".ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${${app.ambiente}"+".SocketTimeout}")
    private int socketTimeout;
    
    @Value("${${app.ambiente}"+".banco.urlConsulta}")
    private String urlConsulta;
    
    @Value("${${app.ambiente}"+".banco.urlConsultaBuscarBanco}")
    private String urlConsultaBuscarBanco;
	
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio Bancos";
    
    private static final String BANCOSERVICELISTABANCOSI = "[==== INICIO Lista Bancos Consultas - Service ====]";
	
	private static final String BANCOSERVICELISTABANCOSF = "[==== FIN Lista Bancos Consultas - Service ====]";
	
	private static final String BANCOSERVICEBUSCARBANCOSI = "[==== INICIO Buscar Bancos Consultas - Service ====]";
	
	private static final String BANCOSERVICEBUSCARBANCOSF = "[==== FIN Buscar Bancos Consultas - Service ====]";
    
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }
    
    
    
    

    
	@Override
	public List<Banco> listaBancos(BancoRequest bancoRequest) throws CustomException {
		LOGGER.info(BANCOSERVICELISTABANCOSI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String bancoRequestJSON;
		bancoRequestJSON = new Gson().toJson(bancoRequest);
		
		wsrequest.setBody(bancoRequestJSON);
		//wsrequest.setUrl("https://172.19.148.51:8443/api/v1/bancos/cdinme/listadosbancos");
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(BANCOSERVICELISTABANCOSF);
	            return respuest2xxlistaBancos(retorno);
			}else {
				LOGGER.error(respuesta4xx(retorno));
				throw new CustomException(respuesta4xx(retorno));	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	public List<Banco> respuest2xxlistaBancos(WSResponse retorno) {
		try {
			BancoResponse bancoResponse = mapper.jsonToClass(retorno.getBody(), BancoResponse.class);
			if(bancoResponse.getResultado().getCodigo().equals("0000")){
	        	return bancoResponse.getLisBancos();
	        }else {
	        	return new ArrayList<>();
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return new ArrayList<>();
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

	public Banco respuest2xxBanco(WSResponse retorno) {
		try {
			BancoResponse bancoResponse = mapper.jsonToClass(retorno.getBody(), BancoResponse.class);
			if(bancoResponse.getResultado().getCodigo().equals("0000")){
	        	return bancoResponse.getDatosBanco();
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
			return null;
		}
        
	}




	@Override
	public Banco buscarBanco(BancoRequest bancoRequest) throws CustomException {
		LOGGER.info(BANCOSERVICEBUSCARBANCOSI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String bancoRequestJSON;
		bancoRequestJSON = new Gson().toJson(bancoRequest);
		wsrequest.setBody(bancoRequestJSON);
		//wsrequest.setUrl("http://172.19.148.7:9047/api/v1/bancos/cdinme/consultasbancos");
		wsrequest.setUrl(urlConsultaBuscarBanco);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				LOGGER.info(BANCOSERVICEBUSCARBANCOSF);
	            return respuest2xxBanco(retorno);
			}else {
				LOGGER.error(respuesta4xx(retorno));
				throw new CustomException(respuesta4xx(retorno));	
			}
		}else {
			LOGGER.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

}
