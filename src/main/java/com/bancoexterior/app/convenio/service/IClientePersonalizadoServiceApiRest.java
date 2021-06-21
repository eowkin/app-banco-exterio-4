package com.bancoexterior.app.convenio.service;

import java.util.List;

import com.bancoexterior.app.convenio.dto.ClienteDatosBasicoRequest;
import com.bancoexterior.app.convenio.dto.ClienteRequest;
import com.bancoexterior.app.convenio.dto.ClienteResponse;
import com.bancoexterior.app.convenio.exception.CustomException;
import com.bancoexterior.app.convenio.interfase.model.WSResponse;
import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.bancoexterior.app.convenio.model.DatosClientes;


public interface IClientePersonalizadoServiceApiRest {

	public WSResponse consultarWs(ClienteRequest clienteRequest);
	
	public List<ClientesPersonalizados> listaClientesPersonalizados(ClienteRequest clienteRequest) throws CustomException;
	
	public ClienteResponse listaClientesPaginacion(ClienteRequest clienteRequest) throws CustomException;
	
	public DatosClientes buscarDatosBasicos(ClienteDatosBasicoRequest clienteDatosBasicoRequest) throws CustomException;
	
	public ClientesPersonalizados buscarClientesPersonalizados(ClienteRequest clienteRequest) throws CustomException;
	
	public String actualizar(ClienteRequest clienteRequest) throws CustomException;
	
	public String crear(ClienteRequest clienteRequest) throws CustomException;
}
