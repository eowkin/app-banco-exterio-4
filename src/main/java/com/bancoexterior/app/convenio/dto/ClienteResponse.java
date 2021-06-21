package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.bancoexterior.app.convenio.model.DatosPaginacion;
import com.bancoexterior.app.convenio.response.Resultado;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClienteResponse implements Serializable{

	@JsonProperty("resultado")
	private Resultado resultado;
	
	@JsonProperty("datosPaginacion")
	private DatosPaginacion datosPaginacion;
	
	@JsonProperty("cliente")
	private List<ClientesPersonalizados> listaClientes;
	
	
	
	
	public ClienteResponse() {
		super();
		this.resultado = new Resultado();
		this.listaClientes = new ArrayList<>();
	}




	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
