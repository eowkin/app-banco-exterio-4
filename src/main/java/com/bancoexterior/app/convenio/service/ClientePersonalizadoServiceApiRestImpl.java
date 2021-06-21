package com.bancoexterior.app.convenio.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bancoexterior.app.convenio.dto.ClienteDatosBasicoRequest;
import com.bancoexterior.app.convenio.dto.ClienteDatosBasicosResponse;
import com.bancoexterior.app.convenio.dto.ClienteRequest;
import com.bancoexterior.app.convenio.dto.ClienteResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.IWSService;
import com.bancoexterior.app.convenio.interfase.model.WSRequest;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.bancoexterior.app.convenio.model.DatosClientes;
import com.bancoexterior.app.convenio.response.Response;
import com.bancoexterior.app.convenio.response.Resultado;
import com.bancoexterior.app.util.Mapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientePersonalizadoServiceApiRestImpl implements IClientePersonalizadoServiceApiRest{

	
	@Autowired
	private IWSService wsService;
	
	@Autowired 
	private Mapper mapper;
	
	@Value("${des.ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${des.SocketTimeout}")
    private int socketTimeout;
    
    @Value("${des.clientesPersonalizados.urlConsulta}")
    private String urlConsulta;
    
    @Value("${des.clientesPersonalizados.urlActualizar}")
    private String urlActualizar;
    
    @Value("${des.datosbasicos.urlConsultaDatosBasicos}")
    private String urlConsultaDatosBasicos;
    
    
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio clientesPersonalizados";
	
    public WSRequest getWSRequest() {
    	WSRequest wsrequest = new WSRequest();
    	wsrequest.setConnectTimeout(connectTimeout);
		wsrequest.setContenType("application/json");
		wsrequest.setSocketTimeout(socketTimeout);
    	return wsrequest;
    }

	@Override
	public List<ClientesPersonalizados> listaClientesPersonalizados(ClienteRequest clienteRequest)
			throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/clientes/consultas");
	
		log.info("antes de llamarte WS en consultar listaClientesPersonalizados");
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				return respuesta2xxListaClientesPersonalizados(retorno);
			} else {
				throw new CustomException(respuesta4xxListaClientesPersonalizados(retorno));
			}
		} else {
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}

	public List<ClientesPersonalizados> respuesta2xxListaClientesPersonalizados(WSResponse retorno){
		try {
			ClienteResponse clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
			return clienteResponse.getListaClientes();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
		
	}
	
	public String respuesta4xxListaClientesPersonalizados(WSResponse retorno){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@Override
	public ClientesPersonalizados buscarClientesPersonalizados(ClienteRequest clienteRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/clientes/consultas");
		
		log.info("antes de llamarte WS en consultar buscarClientesPersonalizados");
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				return respuesta2xxbuscarClientesPersonalizados(retorno);
			} else {
				throw new CustomException(respuesta4xxListaClientesPersonalizados(retorno));
			}
		} else {
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}

	public ClientesPersonalizados respuesta2xxbuscarClientesPersonalizados(WSResponse retorno){
		try {
			ClienteResponse clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
			log.info(clienteResponse.getResultado().getCodigo());
			if(clienteResponse.getResultado().getCodigo().equals("0000")){
	        	log.info("Respusta codigo 0000 si existe el limite");
	        	return clienteResponse.getListaClientes().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	
	@Override
	public String actualizar(ClienteRequest clienteRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/clientes");
			
		log.info("antes de llamarte WS en actualizar");
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxActualizarCrear(retorno);
			}else {
				throw new CustomException(respuesta4xxActualizarCrear(retorno));
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
	
	public String respuesta4xxActualizarCrear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();		
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String crear(ClienteRequest clienteRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/clientes");
		log.info("antes de llamarte WS en crear");
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				return respuesta2xxActualizarCrear(retorno);
			}else {
				throw new CustomException(respuesta4xxActualizarCrear(retorno));
			}
		}else {
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public WSResponse consultarWs(ClienteRequest clienteRequest) {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/clientes/consultas");
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		return retorno;
	}

	@Override
	public DatosClientes buscarDatosBasicos(ClienteDatosBasicoRequest clienteDatosBasicoRequest)
			throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteDatosBasicoRequestJSON;
		clienteDatosBasicoRequestJSON = new Gson().toJson(clienteDatosBasicoRequest);
		wsrequest.setBody(clienteDatosBasicoRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/clientes/datosbasicos");
		log.info("antes de llamarte WS en buscarDatosBasicos ");
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				return respuesta2xxBuscarDatosBasicos(retorno);	
			} else {
				throw new CustomException(respuesta4xxActualizarCrear(retorno));
			}
		} else {
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}

	
	public DatosClientes respuesta2xxBuscarDatosBasicos(WSResponse retorno) {
		try {
			ClienteDatosBasicosResponse clienteDatosBasicosResponse  = mapper.jsonToClass(retorno.getBody(), ClienteDatosBasicosResponse.class);
			log.info(clienteDatosBasicosResponse.getResultado().getCodigo());
			if(clienteDatosBasicosResponse.getResultado().getCodigo().equals("0000")){
	        	log.info("Respusta codigo 0000 si existe el limite");
	        	return clienteDatosBasicosResponse.getDatosCliente();
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			e.printStackTrace();
			return new DatosClientes();
		}
		
	}
	
	
	@Override
	public ClienteResponse listaClientesPaginacion(ClienteRequest clienteRequest) throws CustomException {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl("https://172.19.148.51:8443/api/des/V1/divisas/clientes/consultas");
			
		log.info("antes de llamarte WS en listaClientesPaginacion");
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				return respuesta2xxListaClientesPaginacion(retorno);
			} else {
				throw new CustomException(respuesta4xxListaClientesPaginacion(retorno));
			}
		} else {
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}

	public ClienteResponse respuesta2xxListaClientesPaginacion(WSResponse retorno) {
		try {
			ClienteResponse clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
			log.info(clienteResponse.getResultado().getCodigo());
			if(clienteResponse.getResultado().getCodigo().equals("0000")){
	        	log.info("Respusta codigo 0000 si de clienteResponse");
	        	return clienteResponse;
	        }else {
	        	return null;
	        }
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public String respuesta4xxListaClientesPaginacion(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return  resultado.getDescripcion();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
