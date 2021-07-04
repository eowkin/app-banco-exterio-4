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
	
	@Value("${${app.ambiente}"+".ConnectTimeout}")
    private int connectTimeout;
    
    @Value("${${app.ambiente}"+".SocketTimeout}")
    private int socketTimeout;
    
    @Value("${${app.ambiente}"+".clientesPersonalizados.urlConsulta}")
    private String urlConsulta;
    
    @Value("${${app.ambiente}"+".clientesPersonalizados.urlActualizar}")
    private String urlActualizar;
    
    @Value("${${app.ambiente}"+".datosbasicos.urlConsultaDatosBasicos}")
    private String urlConsultaDatosBasicos;
    
    private static final String ERRORMICROCONEXION = "No hubo conexion con el micreoservicio clientesPersonalizados";
    
    private static final String CLIENTESPERSONALIZADOSSERVICELISTAI = "[==== INICIO Lista ClientesPersonalizados Consultas - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICELISTAF = "[==== FIN Lista ClientesPersonalizados Consultas - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICEBUSCARI = "[==== INICIO Buscar ClientesPersonalizados Consultas - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICEBUSCARF = "[==== FIN Buscar ClientesPersonalizados Consultas - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICEACTUALIZARI = "[==== INICIO Actualizar ClientesPersonalizados - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICEACTUALIZARF = "[==== FIN Actualizar ClientesPersonalizados - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICECREARI = "[==== INICIO Crear ClientesPersonalizados - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICECREARF = "[==== FIN Crear ClientesPersonalizados - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICEDATOSBASICOSI = "[==== INICIO BuscarDatosBasicos ClientesPersonalizados Consultas - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICEDATOSBASICOSF = "[==== FIN BuscarDatosBasicos ClientesPersonalizados Consultas - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICELISTAPAGINACIONI = "[==== INICIO Lista ClientesPersonalizados Consultas - Service ====]";
	
	private static final String CLIENTESPERSONALIZADOSSERVICELISTAPAGINACIONF = "[==== FIN Lista ClientesPersonalizados Consultas - Service ====]";
	
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
		log.info(CLIENTESPERSONALIZADOSSERVICELISTAI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				log.info(CLIENTESPERSONALIZADOSSERVICELISTAF);
				return respuesta2xxListaClientesPersonalizados(retorno);
			} else {
				log.error(respuesta4xxListaClientesPersonalizados(retorno));
				throw new CustomException(respuesta4xxListaClientesPersonalizados(retorno));
			}
		} else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}

	public List<ClientesPersonalizados> respuesta2xxListaClientesPersonalizados(WSResponse retorno){
		try {
			ClienteResponse clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
			return clienteResponse.getListaClientes();
		} catch (IOException e) {
			log.error(e.getMessage());
			return new ArrayList<>();
		}
		
	}
	
	public String respuesta4xxListaClientesPersonalizados(WSResponse retorno){
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return resultado.getDescripcion();
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}
	
	
	@Override
	public ClientesPersonalizados buscarClientesPersonalizados(ClienteRequest clienteRequest) throws CustomException {
		log.info(CLIENTESPERSONALIZADOSSERVICEBUSCARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlConsulta);
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				log.info(CLIENTESPERSONALIZADOSSERVICEBUSCARF);
				return respuesta2xxbuscarClientesPersonalizados(retorno);
			} else {
				log.error(respuesta4xxListaClientesPersonalizados(retorno));
				throw new CustomException(respuesta4xxListaClientesPersonalizados(retorno));
			}
		} else {
			log.info(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}

	public ClientesPersonalizados respuesta2xxbuscarClientesPersonalizados(WSResponse retorno){
		try {
			ClienteResponse clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
			if(clienteResponse.getResultado().getCodigo().equals("0000")){
	        	return clienteResponse.getListaClientes().get(0);
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
		
		
	}
	
	
	@Override
	public String actualizar(ClienteRequest clienteRequest) throws CustomException {
		log.info(CLIENTESPERSONALIZADOSSERVICEACTUALIZARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlActualizar);	
		retorno = wsService.put(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info(CLIENTESPERSONALIZADOSSERVICEACTUALIZARF);
				return respuesta2xxActualizarCrear(retorno);
			}else {
				log.error(respuesta4xxActualizarCrear(retorno));
				throw new CustomException(respuesta4xxActualizarCrear(retorno));
			}
		}else {
			log.info(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}
	
	
	public String respuesta2xxActualizarCrear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
		
	}
	
	public String respuesta4xxActualizarCrear(WSResponse retorno) {
		try {
			Response response = mapper.jsonToClass(retorno.getBody(), Response.class);
			return response.getResultado().getDescripcion();		
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	@Override
	public String crear(ClienteRequest clienteRequest) throws CustomException {
		log.info(CLIENTESPERSONALIZADOSSERVICECREARI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlActualizar);
		retorno = wsService.post(wsrequest);
		if(retorno.isExitoso()) {
			if(retorno.getStatus() == 200) {
				log.info(CLIENTESPERSONALIZADOSSERVICECREARF);
				return respuesta2xxActualizarCrear(retorno);
			}else {
				log.error(respuesta4xxActualizarCrear(retorno));
				throw new CustomException(respuesta4xxActualizarCrear(retorno));
			}
		}else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
		}
	}

	public WSResponse consultarWs(ClienteRequest clienteRequest) {
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlConsulta);
		log.info("antes de llamarte WS en consultar");
		retorno = wsService.post(wsrequest);
		return retorno;
	}

	@Override
	public DatosClientes buscarDatosBasicos(ClienteDatosBasicoRequest clienteDatosBasicoRequest)
			throws CustomException {
		log.info(CLIENTESPERSONALIZADOSSERVICEDATOSBASICOSI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteDatosBasicoRequestJSON;
		clienteDatosBasicoRequestJSON = new Gson().toJson(clienteDatosBasicoRequest);
		wsrequest.setBody(clienteDatosBasicoRequestJSON);
		wsrequest.setUrl(urlConsultaDatosBasicos);
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				log.info(CLIENTESPERSONALIZADOSSERVICEDATOSBASICOSF);
				return respuesta2xxBuscarDatosBasicos(retorno);	
			} else {
				log.error(respuesta4xxActualizarCrear(retorno));
				throw new CustomException(respuesta4xxActualizarCrear(retorno));
			}
		} else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}

	
	public DatosClientes respuesta2xxBuscarDatosBasicos(WSResponse retorno) {
		try {
			ClienteDatosBasicosResponse clienteDatosBasicosResponse  = mapper.jsonToClass(retorno.getBody(), ClienteDatosBasicosResponse.class);
			if(clienteDatosBasicosResponse.getResultado().getCodigo().equals("0000")){
	        	return clienteDatosBasicosResponse.getDatosCliente();
	        }else {
	        	return null;
	        }
		} catch (IOException e) {
			log.error(e.getMessage());
			return new DatosClientes();
		}
		
	}
	
	
	@Override
	public ClienteResponse listaClientesPaginacion(ClienteRequest clienteRequest) throws CustomException {
		log.info(CLIENTESPERSONALIZADOSSERVICELISTAPAGINACIONI);
		WSRequest wsrequest = getWSRequest();
		WSResponse retorno;
		String clienteRequestJSON;
		clienteRequestJSON = new Gson().toJson(clienteRequest);
		wsrequest.setBody(clienteRequestJSON);
		wsrequest.setUrl(urlConsulta);	
		retorno = wsService.post(wsrequest);
		if (retorno.isExitoso()) {
			if (retorno.getStatus() == 200) {
				log.info(CLIENTESPERSONALIZADOSSERVICELISTAPAGINACIONF);
				return respuesta2xxListaClientesPaginacion(retorno);
			} else {
				log.error(respuesta4xxListaClientesPaginacion(retorno));
				throw new CustomException(respuesta4xxListaClientesPaginacion(retorno));
			}
		} else {
			log.error(ERRORMICROCONEXION);
			throw new CustomException(ERRORMICROCONEXION);
			
		}
	}

	public ClienteResponse respuesta2xxListaClientesPaginacion(WSResponse retorno) {
		try {
			ClienteResponse clienteResponse = mapper.jsonToClass(retorno.getBody(), ClienteResponse.class);
			if(clienteResponse.getResultado().getCodigo().equals("0000")){
	        	return clienteResponse;
	        }else {
	        	return null;
	        }
			
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
		
	}
	
	public String respuesta4xxListaClientesPaginacion(WSResponse retorno) {
		try {
			Resultado resultado = mapper.jsonToClass(retorno.getBody(), Resultado.class);
			return  resultado.getDescripcion();
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}

}
