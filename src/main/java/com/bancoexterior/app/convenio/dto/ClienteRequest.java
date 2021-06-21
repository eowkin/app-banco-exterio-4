package com.bancoexterior.app.convenio.dto;

import java.io.Serializable;

import com.bancoexterior.app.convenio.model.ClientesPersonalizados;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class ClienteRequest implements Serializable{

	@JsonProperty("idSesion")
	private String idSesion;
	
	@JsonProperty("idUsuario")
	private String idUsuario;
	
	@JsonProperty("codUsuario")
	private String codUsuario;
	
	@JsonProperty("canal")
	private String canal;
	
	@JsonProperty("numeroPagina")
	private int numeroPagina;
	
	@JsonProperty("tamanoPagina")
	private int tamanoPagina;

	@JsonProperty("cliente")
	private ClientesPersonalizados cliente;
	
	
	
	public ClienteRequest() {
		super();
		this.cliente = new ClientesPersonalizados();
	}



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
