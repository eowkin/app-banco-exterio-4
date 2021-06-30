package com.bancoexterior.app.cce.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BancoServiceImpl implements IBancoService{

	@Autowired
	private IWSService wsService;
    
    @Autowired 
	private Mapper mapper;
	
    @Value("${des.ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${des.SocketTimeout}")
    private int socketTimeout;
	
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio Bancos";
    
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }
    
    
    
    

    
	@Override
	public List<Banco> listaBancos(BancoRequest bancoRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String bancoRequestJSON;
		bancoRequestJSON = new Gson().toJson(bancoRequest);
		
		wsrequest.setBody(bancoRequestJSON);
		log.info("bancoRequestJSON: "+bancoRequestJSON);
		//
		//https://172.19.148.8:8443/api/v1/bancos/cdinme/listadosbancos
		log.info("urlConsulta: "+"https://172.19.148.51:8443/api/v1/bancos/cdinme/listadosbancos");
		wsrequest.setUrl("https://172.19.148.51:8443/api/v1/bancos/cdinme/listadosbancos");
		log.info("antes de llamarte WS en consultar listaBancos");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
	            return respuest2xxlistaBancos(retorno);
			}else {
				throw new CustomException(respuesta4xx(retorno));	
			}
		}else {
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
			e.printStackTrace();
			return new ArrayList<>();
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

	public Banco respuest2xxBanco(WSResponse retorno) {
		try {
			BancoResponse bancoResponse = mapper.jsonToClass(retorno.getBody(), BancoResponse.class);
			if(bancoResponse.getResultado().getCodigo().equals("0000")){
	        	return bancoResponse.getDatosBanco();
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
        
	}




	@Override
	public Banco buscarBanco(BancoRequest bancoRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String bancoRequestJSON;
		bancoRequestJSON = new Gson().toJson(bancoRequest);
		
		wsrequest.setBody(bancoRequestJSON);
		log.info("bancoRequestJSON: "+bancoRequestJSON);
		//
		//http://172.19.148.7:9047/api/v1/bancos/cdinme/consultasbancos
		//https://172.19.148.8:8443/api/v1/bancos/cdinme/listadosbancos
		log.info("urlConsulta: "+"http://172.19.148.7:9047/api/v1/bancos/cdinme/consultasbancos");
		//log.info("urlConsulta: "+"https://172.19.148.51:8443/api/v1/bancos/cdinme/consultasbancos");
		//wsrequest.setUrl("https://172.19.148.51:8443/api/v1/bancos/cdinme/consultasbancos");
		wsrequest.setUrl("http://172.19.148.7:9047/api/v1/bancos/cdinme/consultasbancos");
		log.info("antes de llamarte WS en buscarBanco");
		retorno = wsService.post(wsrequest);
		log.info("retorno: "+retorno);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
	            return respuest2xxBanco(retorno);
			}else {
				throw new CustomException(respuesta4xx(retorno));	
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

}
